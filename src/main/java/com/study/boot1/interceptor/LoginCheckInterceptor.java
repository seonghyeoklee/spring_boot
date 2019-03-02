package com.study.boot1.interceptor;

import com.google.gson.Gson;
import com.study.boot1.common.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	static Gson gson = new Gson();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		HttpSession session = request.getSession();

		Integer userIdx = (Integer)session.getAttribute(Constant.SESSION_KEY_LOGIN_USER_IDX);

		if(userIdx == null) {
//            if( request.getContentType().equals("application/json") ){ // json
			response.setCharacterEncoding("UTF-8");
			response.addHeader("Content-Type", "application/json;charset=UTF-8");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			Map<String, Object> result = new HashMap<>();
			result.put("succ", false);
			result.put("msg", HttpStatus.UNAUTHORIZED.toString());

			try{
				response.getWriter().print(gson.toJson(request));
			}catch(Exception e){
				e.printStackTrace();
			}
//            } else { // html
//                try{
//                    response.sendRedirect("");
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
			return false;
		}

		return super.preHandle(request, response, handler);
	}
}