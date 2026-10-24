package com.tecsun.sisp.net.modules.controller.common;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tecsun.sisp.net.common.ResponseCode;
import com.tecsun.sisp.net.common.Result;
import com.tecsun.sisp.net.exception.ServiceException;

/**
 * 统一异常处理器
 * 必须放在springmvc 扫描的controller包下,否在会返回错误页面,不会返回json
 */
@ControllerAdvice
@ResponseBody
public class CommonExceptionHandler {

    private static Logger logger = LogManager.getLogger(CommonExceptionHandler.class.getName());

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Result EmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        Result resultMessage = new Result(ResponseCode.BAD_REQUEST,"在该表中找不到数据，请检查传入数据是否有误");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.debug(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(BindException.class)
    public Result BeanPropertyBindingResult(BindException ex) {
        Result resultMessage = new Result(ResponseCode.BUSINESS,"数据绑定错误，请检查传入格式");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"请求方式错误");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        
        return resultMessage;
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public Result UnsupportedEncodingException(UnsupportedEncodingException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"编码转换异常，操作失败");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(JsonProcessingException.class)
    public Result JsonProcessingException(JsonProcessingException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"操作失败(发生转换错误)");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result MaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"文件上传失败,文件大小超过上传限制");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Result FileNotFoundException(FileNotFoundException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"文件创建失败,操作失败");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.debug(ex.getMessage());
        
        return resultMessage;
    }

    @ExceptionHandler(ServiceException.class)
    public Result RuntimeException(ServiceException ex) {
        Result resultMessage = new Result(ex.getCode(),ex.getMessage());
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.debug(ex.getMessage());
        return resultMessage;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"系统错误");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        
        return resultMessage;
    }
    
    @ExceptionHandler(MultipartException.class)
    public Result handleException(MultipartException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"不是上传请求");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        
        return resultMessage;
    }

    @ExceptionHandler(ClassCastException.class)
    public Result ClassCastException(ClassCastException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"类型转换异常:"+ex.getMessage());
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        
        return resultMessage;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result MissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Result resultMessage = new Result(ResponseCode.BAD_REQUEST,"缺少" + ex.getParameterName()+"参数");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(NullPointerException.class)
    public Result NullPointerException(NullPointerException ex) {
        Result resultMessage = new Result(ResponseCode.BAD_REQUEST,"空指针异常");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        
        return resultMessage;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result MethodArgumentTypeMismatchException(MethodArgumentNotValidException ex) {
        Result resultMessage = new Result(ResponseCode.BAD_REQUEST,"模型转换异常,请检查传入数据类型");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result DataIntegrityViolationException(DataIntegrityViolationException ex) {
        Result resultMessage = new Result(ResponseCode.BUSINESS,"操作失败，已经存在该数据,或者某个字段长度过长");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        
        return resultMessage;
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public Result DataIntegrityViolationException(DataAccessResourceFailureException ex) {
        Result resultMessage = new Result(ResponseCode.INTERNAL_SREVER_ERROR,"服务器网络链接异常");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }

    @ExceptionHandler(ParseException.class)
    public Result ParseException(DataAccessResourceFailureException ex) {
        Result resultMessage = new Result(ResponseCode.BAD_REQUEST,"传入时间格式错误");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        
        return resultMessage;
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public Result ServletRequestBindingException(ServletRequestBindingException ex) {
        Result resultMessage = new Result(ResponseCode.BAD_REQUEST,"数据转换异常");
        StringWriter stringWriter = new StringWriter();  
        ex.printStackTrace(new PrintWriter(stringWriter,true));  
        logger.error(ex.getMessage()+"\r\n"+"错误信息："+stringWriter.toString());
        return resultMessage;
    }
    
    

}
