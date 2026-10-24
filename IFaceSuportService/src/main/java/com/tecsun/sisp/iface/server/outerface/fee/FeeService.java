package com.tecsun.sisp.iface.server.outerface.fee;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 此类用于地税系统的请求交易
 * Created by Ivan on 15-6-19.
 */
public class FeeService {
    public static final Logger logger = Logger.getLogger(FeeService.class);
    private String ip = "";
    private int port;
    private int timeOut = 20; //单位 : 分钟
    private final String date = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

    ChannelFactory factory = null;
    ClientBootstrap bootstrap = null;
    HandlerResult result = new HandlerResult();

    public FeeService(String ip, int port, int timeOut) {
        this.ip = ip;
        this.port = port;
        this.timeOut = timeOut;
    }
    /**
     * 向银行发起请求，并等待响应结果
     *
     * @param reqBs 字节数组
     * @return
     */
    public HandlerResult sendReq(final byte[] reqBs) {
        factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());
        bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(new FeeMsgHandler(reqBs, result));
            }
        });
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.connect(new InetSocketAddress(ip, port));
        // 等待响应结果
        long useTime = 0;
        long step = 100;// 时间步长 毫秒
        while (useTime < timeOut * 60 * 1000) {
            if (!result.processOver) { // 尚未处理结束，则继续等待
                useTime += step;
                try {
                    Thread.sleep(step);
                } catch (InterruptedException e) {
                    logger.error(" InterruptedException ", e);
                }
            } else {// 已经处理结束
                try {
                    if (factory != null) factory.releaseExternalResources();
                    if (bootstrap != null) bootstrap.shutdown();
                } catch (Exception e) {
                    logger.error(" bootstrap.shutdown error ", e);
                }
                return result;
            }
        }
        try {
            if (factory != null) factory.releaseExternalResources();
            if (bootstrap != null) bootstrap.shutdown();
        } catch (Exception e) {
            logger.error(" bootstrap.shutdown error ", e);
        }
        // 如果没有在 while 循环里成功返回，则算是超时
        result.result = 300;
        result.message = "交易超时";
        return result;
    }

    /**
     * 缴费查询
     * @param param
     * @return
     */
    public HandlerResult sendQuryInfo(String param) {
        String req = "9021  " + param+"  " + date +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";
        return this.sendReq(byteUtil(req));
    }
    /**
     * 缴费申报
     * @param param
     * @return
     */
    public HandlerResult payApply(String param) {
    	String req = "8022  "+"11111111111111111111" + param+"  " +"11111111111111111111"+ date +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";
        return this.sendReq(byteUtil(req));
    }
    /**
     * 缴费申报撤销
     * @param param
     * @return
     */
    public HandlerResult revokeApply(String param) {
    	String req = "8023  "+"11111111111111111111" + param+"  " +"11111111111111111111"+ date +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";
        return this.sendReq(byteUtil(req));
    }
    /**
     * 用户欠费查询
     * @param param
     * @return
     */
    public HandlerResult debtsQuery(String param) {
    	String req = "9007  "+"11111111111111111111" + param+"  " +"11111111111111111111"+ date +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";
        return this.sendReq(byteUtil(req));
    }
    /**
     * 用户欠费缴费
     * @param param
     * @return
     */
    public HandlerResult debtsPay(String param) {
    	String req = "8008  "+"11111111111111111111" + param+"  " +"11111111111111111111"+ date +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";
        return this.sendReq(byteUtil(req));
    }
    /**
     * 差错调整
     * @param param
     * @return
     */
    public HandlerResult errorAdjust(String param) {
    	String req = "8010  "+"11111111111111111111" + param+"  " +"11111111111111111111"+ date +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";
        return this.sendReq(byteUtil(req));
    }
    /**
     * 取消缴费
     * @param param
     * @return
     */
    public HandlerResult cancelPay(String param) {
    	String req = "8020  "+"11111111111111111111" + param+"  " +"11111111111111111111"+ date +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";
        return this.sendReq(byteUtil(req));
    }

    
    public byte[] byteUtil(String req){
    	 byte[] allBs = null;
    	try {
            byte[] bodyBs = req.getBytes("GBK");
            allBs = new byte[4 + bodyBs.length];
            int len = req.length();
            byte[] src = new byte[4];
            allBs[0] = (byte) ((len >> 24) & 0xFF);
            allBs[1] = (byte) ((len >> 16) & 0xFF);
            allBs[2] = (byte) ((len >> 8) & 0xFF);
            allBs[3] = (byte) (len & 0xFF);
            System.arraycopy(bodyBs, 0, allBs, 4, bodyBs.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    	return allBs;
    }
    
    public static void main(String[] args) {
//        HandlerResult re = new RCCBankService("localhost", 1990, 2).sendReq4Hex("76");
//        HandlerResult re = new RCCBankService("172.31.16.21", 31001, 20).registration(821, "91200059", "402077153990001");
//        System.out.println(re.valueMap);
//        System.out.println(re.bodyBase64Str);
        String bodyStr = "交易超时aa99";
        try {
            byte[] bs = bodyStr.getBytes("GBK");
            System.out.println(new String(bs, "GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String req = "9021  " + "110101199001010015  " + "20150624142323" +
                "11111111111111111111" + "22222222222222222222" + "33333333333333333333" + "44444444444444444444" + "55555555555555555555";

        FeeService service = new FeeService("192.168.0.11", 10003, 10);

        try {
            byte[] bodyBs = req.getBytes("GBK");
            byte[] allBs = new byte[4 + bodyBs.length];
            int len = req.length();
            byte[] src = new byte[4];
            allBs[0] = (byte) ((len >> 24) & 0xFF);
            allBs[1] = (byte) ((len >> 16) & 0xFF);
            allBs[2] = (byte) ((len >> 8) & 0xFF);
            allBs[3] = (byte) (len & 0xFF);
            System.arraycopy(bodyBs, 0, allBs, 4, bodyBs.length);
            service.sendReq(allBs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
}
