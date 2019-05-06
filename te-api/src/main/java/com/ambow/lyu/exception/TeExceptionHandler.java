package com.ambow.lyu.exception;

import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestControllerAdvice
public class TeExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(TeException.class)
	public Response handleLteException(TeException e){
		Response r = new Response();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Response handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return Response.error("数据库中已存在该记录");
	}

	@ExceptionHandler(Exception.class)
	public Response handleException(Exception e){
		logger.error(e.getMessage(), e);
		return Response.error();
	}
}
