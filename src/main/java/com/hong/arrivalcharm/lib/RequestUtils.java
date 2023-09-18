package com.hong.arrivalcharm.lib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RequestUtils {
	/**
	 * 매번 Method로 Request, Response를 넘기는건 비효율적임에 따라 Spring 전역에서 사용 가능한 HttpServletRequest를 Utils로 불러온다.
	 *
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public HttpServletResponse getHttpServletResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
}