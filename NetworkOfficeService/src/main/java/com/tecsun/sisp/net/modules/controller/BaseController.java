package com.tecsun.sisp.net.modules.controller;

import com.tecsun.sisp.net.common.Constants;
import com.tecsun.sisp.net.common.JsonResult;
import com.tecsun.sisp.net.common.Result;
import com.tecsun.sisp.net.common.vo.faceverify.ResultVerify;

public abstract class BaseController {

	public Result result(String result, String message) {
        return result(result, message, null);
    }

    public Result result(String result, String message, Object obj) {
        return new Result(result, message, obj);
    }
    
    public ResultVerify resultVerify(String result, String message) {
        return resultVerify(result, message, null);
    }

    public ResultVerify resultVerify(String result, String message, Object obj) {
        return new ResultVerify(result, message, obj);
    }
    
    public ResultVerify resultVerify(String result, String message, Object obj , long total) {
        return new ResultVerify(result, message, obj , total);
    }
    
    public ResultVerify ok(long total, String message, Object obj) {
        return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, message, obj , total);
    }

    public ResultVerify ok(String message, Object obj) {
        return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, message, obj , 0);
    }

    public ResultVerify error(String message, Object obj) {
        return resultVerify(Constants.RESULT_MESSAGE_ERROR, message, obj);
    }
    
    //添加
    /**
     * 渲染失败数据
     *
     * @return result
     */
    protected JsonResult renderError() {
        JsonResult result = new JsonResult();
        result.setSuccess(false);
        result.setStatus("500");
        return result;
    }

    /**
     * 渲染失败数据（带消息）
     *
     * @param msg 需要返回的消息
     * @return result
     */
    protected JsonResult renderError(String msg) {
        JsonResult result = renderError();
        result.setMsg(msg);
        return result;
    }

    /**
     * 渲染成功数据
     *
     * @return result
     */
    protected JsonResult renderSuccess() {
        JsonResult result = new JsonResult();
        result.setSuccess(true);
        result.setStatus("200");
        return result;
    }

    /**
     * 渲染成功数据（带信息）
     *
     * @param msg 需要返回的信息
     * @return result
     */
    protected JsonResult renderSuccess(String msg) {
        JsonResult result = renderSuccess();
        result.setMsg(msg);
        return result;
    }

    /**
     * 渲染成功数据（带数据）
     *
     * @param obj 需要返回的对象
     * @return result
     */
    protected JsonResult renderSuccess(Object obj) {
        JsonResult result = renderSuccess();
        result.setObj(obj);
        return result;
    }
    
    protected JsonResult renderError(String status,String msg,String exception) {
    	JsonResult result = new JsonResult();
    	result.setSuccess(false);
    	result.setStatus(status);
    	result.setMsg(msg);
    	result.setException(exception);
    	return result;
    }
    
    protected JsonResult renderError(String status,String msg,Object obj) {
    	JsonResult result = new JsonResult();
    	result.setSuccess(false);
    	result.setStatus(status);
    	result.setMsg(msg);
    	result.setObj(obj);
    	return result;
    }
    
    protected JsonResult renderError(String status,String msg) {
    	JsonResult result = new JsonResult();
    	result.setSuccess(false);
    	result.setStatus(status);
    	result.setMsg(msg);
    	return result;
    }
}
