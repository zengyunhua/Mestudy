package com.tecsun.sisp.iface.server.util;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.TransBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: ThreadPoolTask
 * Description:线程池执行的任务
 * CreateTime： 2015年07月31日 15时:34分
 */
public class ThreadPoolTask implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolTask.class);

    //保存任务所需要的数据
    private TransBean transBean;
    //是否可以添加数据到业务分析子系统中
    private boolean isInsert=true;

    public ThreadPoolTask(TransBean transBean) {
        this.transBean = transBean;
    }

    /**
     * 添加数据到业务分析子系统，需要对数据进行校验，所有操作完成后，对构造参数赋值为null，以便下一次实例化
     */
    public void run() {
        try {
            if(StringUtils.isEmpty(DictionaryUtil.getDictName(Constants.CHANNELCODE, transBean.getChannelcode()))){
                isInsert=false;
                throw new Exception("调用接口:"+transBean.getBusinesscode()+",渠道类型不存在："+transBean.getChannelcode());
            }
            if(isInsert){
                transBean.setTransid(CommUtil.getUUID());
                transBean.setStarttime(CommUtil.getNowDateLongStr());
                transBean.setEndtime(CommUtil.getNowDateLongStr());
                transBean.setTransmsg("1");
                transBean.setStatus("1");
                transBean.setResult("1");
                String jsonData= JsonMapper.buildNormalMapper().toJson(transBean);
                DictionaryUtil.postClientRequest(jsonData, DictionaryUtil.getHost(Constants.TRANSANALYSIS_URI));
            }
            transBean=null;
        } catch (Exception e) {
            logger.error("添加操作记录到业务分析子系统出错：" + e.getMessage());
        }
    }
}
