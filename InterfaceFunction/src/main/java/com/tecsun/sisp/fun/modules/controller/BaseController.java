package com.tecsun.sisp.fun.modules.controller;

import com.tecsun.sisp.fun.common.Constants;
import com.tecsun.sisp.fun.common.Result;

public abstract class BaseController {

    public Result result(String result,String message,Object obj){
        return new Result(result,message,obj);
     }
    
    public Result result(String result,String message,Object obj,long total){
        return new Result(result,message,obj,total);
     }
    
    public Result ok(String message,Object obj,long total){
        return result(Constants.RESULT_MESSAGE_SUCCESS , message , obj , total);
     }
    
    public Result ok(String message,Object obj){
        return result(Constants.RESULT_MESSAGE_SUCCESS , message , obj , 0);
     }
    
    public Result error(String message,Object obj){
        return result(Constants.RESULT_MESSAGE_ERROR , message , obj);
     }
    
    public static boolean isEmptyStr(Object str)
    {
        return str == null || str.toString().trim().length() < 1 ? true : false;
    }
}
