import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.client.HttpClientUtil;
import com.tecsun.sisp.iface.client.IFaceHttpsClient;
import com.tecsun.sisp.iface.common.vo.CardBean;
import com.tecsun.sisp.iface.common.vo.CardInfoBean;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;
import com.tecsun.sisp.iface.common.vo.card.CardBaseInfo;
import com.tecsun.sisp.iface.common.vo.card.TsjbxxVO;
import com.tecsun.sisp.iface.server.controller.card.CardInfoController;
import com.tecsun.sisp.iface.server.controller.so.SecQueryController;
import com.tecsun.sisp.iface.server.model.service.SecQueryServiceImpl;
import com.tecsun.sisp.iface.server.util.CommUtil;
import org.junit.Test;
import  com.tecsun.sisp.iface.server.controller.BaseController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mervin on 2015/12/1.
 */
public class DBTest extends  BaseController{
    @Test
    public void getCardStatus(){
        CardBean bean=new CardBean ();
        bean.setId("110101199001010015");
        //bean.setCardNo("H18227960");
        bean.setName("伍慧玲");
        try {
         // String url="http://192.168.7.131:8080/sisp/iface/cardInfo/getCardStatus?tokenId=aab";
          String url="http://127.0.0.1:81/sisp/iface/cardInfo/getCardStatus?tokenId=aab";
            String data = HttpClientUtil.getData(81,url, bean, true);
            IFaceHttpsClient.isVerify=false;
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void checklogin(){
        CardBean bean=new CardBean ();
        bean.setId("110101199001010015");
        bean.setCardNo("H18227960");
       // bean.setName("伍慧玲");
        try {
            // String url="http://192.168.7.131:8080/sisp/iface/cardInfo/getCardStatus?tokenId=aab";
            String url="http://127.0.0.1:81/sisp/iface/rest/login?tokenId=aab";
            String data = HttpClientUtil.getData(81,url, bean, true);
            IFaceHttpsClient.isVerify=false;
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getPersonBasicInfo(){
        CardBean bean=new CardBean ();
        bean.setId("110101199001010015");
     //   bean.setCardid("H18227960");

        try {
            String url="http://127.0.0.1:81/sisp/iface/rest/getPersonBasicInfo?tokenId=aab";
            String data = HttpClientUtil.getData(81,url, bean, true);
            IFaceHttpsClient.isVerify=false;
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
   public static void main(String[] args) throws  Exception{
               SecQueryController caed=new SecQueryController();
               219.154.45.61
               
               
               
               
               

//       CardBaseInfo bean=new CardBaseInfo();
//       bean.setIdcard("110101199001010015");
//       bean.setName("伍慧玲");
//       bean.setCardid("H18227960");
//       bean.setBankaccount(CommUtil.generateValue("123456"));
//       bean.setStatus("0");
//       bean.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//       bean.setLogintime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//       ss.insertPersonBasicInfo(bean);
//       SecQueryServiceImpl ss=new SecQueryServiceImpl();
//      // CardBean bean=new CardBean();
//       TsjbxxVO bean=new TsjbxxVO();
//       bean.setGrbh("110101199001010015");
//       TsjbxxVO aa= ss.getTsjbxxVO(bean);
//       System.out.println(aa.toString());




//       String pass="fsfddfdsasdf123456";
//       System.out.print(pass.substring(pass.length()-6,pass.length()));

//        CardInfoController caed=new CardInfoController();
//        CardBean bean=new CardBean();
//        bean.setId("110101199001010015");
//        bean.setCardNo("H18227960");
//        try {
//            caed.getCardAllInfo(bean);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        SecQueryController sc=new SecQueryController();
////        SecQueryBean bean=new SecQueryBean ();
////        bean.setIdcard("110101199001010015");
////        bean.setCardid("H18227960");
////        sc.getPersonBasicInfo(bean);
}
//public static void main(String[] args) {
//    JsonObject json = new JsonObject();
//    json.addProperty("name","莫定洪");
//    json.addProperty("id","110101199001010015");//转为json时需要
//    String wsResult = (String)getWebClient("http://192.168.7.131:8080/ifacesuportservice/iface/cardInfo/getCardStatus",json.toString(),String.class);
//    System.out.println(wsResult);//url本机：tomcat端口/地址
//}



}
