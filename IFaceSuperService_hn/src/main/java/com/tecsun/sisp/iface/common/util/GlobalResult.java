package com.tecsun.sisp.iface.common.util;

import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.Result;

import java.io.IOException;

/**全局消息处理类，系统逻辑
 * ClassName: GlobalResult
 * Description:
 * CreateTime： 2015年06月12日 16时:57分
 */
public class GlobalResult {

    public static String error_301(String message){
        return globalResult("IFAC-EERR-301",message==null?"IFAC-EERR-301:连接超时，无法获取tokenId,需重新登录":message,null);
    }

    public static String error_302(String message){
        return globalResult("IFAC-EERR-302",message==null?"IFAC-EERR-302:redis服务器读取数据出错":message,null);
    }

    public static String error_303(String message){
        return globalResult("IFAC-EERR-303",message==null?"IFAC-EERR-303:拦截非法访问德生宝":message,null);
    }

    private static String globalResult(String code,String message,Object obj){
        Result result = new Result(code,message, obj);
        try {
            return JsonMapper.buildNormalMapper().toJson(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
