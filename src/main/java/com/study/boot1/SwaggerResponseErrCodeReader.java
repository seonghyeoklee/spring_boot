package com.study.boot1;

import com.google.common.base.Optional;
import com.study.boot1.common.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.*;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class SwaggerResponseErrCodeReader implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {
        Optional<SwaggerResponseErrorCodes> swaggerResponseErrorCodesOptional = context.findAnnotation(SwaggerResponseErrorCodes.class);
        Set<ResponseMessage> responseMessageSet = getResponseMsgSetWhenErrorCodes(swaggerResponseErrorCodesOptional);

        if(!responseMessageSet.isEmpty())
            context.operationBuilder().responseMessages(responseMessageSet);
    }

    private Set<ResponseMessage> getResponseMsgSetWhenErrorCodes(Optional<SwaggerResponseErrorCodes> swaggerResponseErrorOptional) {
        Set<ResponseMessage> responseMessageSet = new HashSet<>();

        if(swaggerResponseErrorOptional.isPresent()) {
            SwaggerResponseErrorCodes swaggerResponseErrorCodes = swaggerResponseErrorOptional.get();

            Map<Integer, List<ErrorCode>> map = new HashMap<>();

            for(ErrorCode errCode : swaggerResponseErrorCodes.value()) {
                Integer statusCode = errCode.getHttpStatus().value();
                map.computeIfAbsent(statusCode, ArrayList::new).add(errCode);
            }

            for(Integer statusCode : map.keySet()) {
                responseMessageSet.add( createResponseMessage(statusCode, map.get(statusCode)) );
            }
        }

        return responseMessageSet;
    }

    private ResponseMessage createResponseMessage(int statusCode, List<ErrorCode> errCodeList) {
        String message = getStatusErrorDescription(errCodeList);

        return createResponseMessage(statusCode, message);
    }

    private String getStatusErrorDescription(List<ErrorCode> errCodeList) {
        errCodeList.sort(Comparator.comparingInt(ErrorCode::getCode));

        String description = errCodeList.stream()
                .map(item -> String.format("(%d) %s", item.getCode(), item.getMsg()))
                .reduce("", (identity, item) -> identity + (identity.isEmpty() ? "" : "\n") + item);

        return description;
    }

    private ResponseMessage createResponseMessage(int statusCode, String message) {
        return new ResponseMessage(statusCode, message, null, new HashMap<>(), new ArrayList<>());
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

}
