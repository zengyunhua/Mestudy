package com.tecsun.sisp.iface.common.util;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.DocumentException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class InvokeUtil {

    /**
     * 调用接口  
     * @param method
     * @param param
     * @return
     */
    public static Map<String,String> invoke(String method,String[] param){
        String re = null;
       Map<String,String> map = new HashMap<String,String>();
        try {
            Call call=(Call)(new Service()).createCall();
            call.setTargetEndpointAddress(new URL(Config.getInstance().get("biometrics.endpoint")));
            call.setOperationName(new QName(Config.getInstance().get("biometrics.namespace"), method));
            call.setTimeout(30000);
            re = (String)call.invoke(param);
            re = "<root>" + re + "</root>";
            map  = Dom4JUtil.readXmlToMap(re);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }
}
