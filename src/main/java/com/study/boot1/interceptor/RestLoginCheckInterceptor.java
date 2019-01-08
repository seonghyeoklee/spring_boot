package com.study.boot1.interceptor;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.boot1.service.SignService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class RestLoginCheckInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		HttpSession session = request.getSession();

		Integer userIdx = (Integer) session.getAttribute(SignService.SIGN_IN_USER_IDX_KEY);

		if(userIdx == null) {

			response.setCharacterEncoding("UTF-8");
			response.addHeader("Content-Type", "application/json;charset=UTF-8");
			try{
				PrintWriter printWriter = response.getWriter();
				Map<String,Object> result = new LinkedTreeMap<>();
				result.put("succ", false);
				result.put("msg", "NEED LOGIN");

				printWriter.print(new Gson().toJson(result));
			}catch(Exception e){
				e.printStackTrace();
			}

			return false;
		}

		return super.preHandle(request, response, handler);
	}

	/*
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}
	*/
}
