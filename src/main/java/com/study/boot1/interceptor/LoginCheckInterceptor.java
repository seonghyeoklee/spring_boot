package com.study.boot1.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.boot1.common.Constant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		HttpSession session = request.getSession();

		Integer userIdx = (Integer)session.getAttribute(Constant.SESSION_KEY_LOGIN_USER_IDX);

		if(userIdx == null){
			Cookie[] cookies = request.getCookies();
			String token = "";

			if(cookies != null){
				for(Cookie cookie : cookies){
					if(cookie.getName().equals("Autosign")){
						token = cookie.getValue();
						break;
					}
				}
			}

			if(token != null){
				//token으로 userIdx 조회
				//session에 userIdx 저장
				//클라이언트 쿠키 삭제
				//update 사용시간
				//새로운 쿠키 발급
			}
		}

		return super.preHandle(request, response, handler);
	}
}
