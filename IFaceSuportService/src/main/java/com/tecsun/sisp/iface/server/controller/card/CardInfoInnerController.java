package com.tecsun.sisp.iface.server.controller.card;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.CardInfoBean;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.card.CardAllInfo;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.outerface.card.CardInfoBus;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: CardInfoInnerController
 * Description:  提供给内部其他子系统调用，已存在的类会拦截所有请求并进行验证
 * CreateTime： 2015年06月07日 12时:28分
 */
@Controller
@RequestMapping(value = "/ifaceInner/cardInfo")
public class CardInfoInnerController extends BaseController {

    /**
     * SISP_IFACE_CARD_002 卡基础数据（包含个人照片） 社保卡号和身份证号码至少有一项不为空
     */
    @RequestMapping(value = "/getCardAllInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getCardAllInfo(@RequestBody CardInfoBean bean)throws Exception{
        String cardno = bean.getCardNo();
        String cernum = bean.getId();
        String result = "";
        String message = "";
        if ((cardno != null && !cardno.equals("")) || (cernum != null && !cernum.equals(""))) {
            CardInfoBus bus = new CardInfoBus();
            CardAllInfo info = bus.getCardAllInfo(cardno, cernum);
            if (info != null && info.getErr().equals("OK")) {
                info.setSex(DictionaryUtil.getDictName(Constants.SEX_GROUP, info.getSex()));
                info.setEthnic(DictionaryUtil.getDictName(Constants.PARAM_NATION_GROUP, info.getEthnic()));
                info.setPersonstatus(DictionaryUtil.getDictName(Constants.PERATATUS_GROUP, info.getPersonstatus()));
                info.setRegisttype(DictionaryUtil.getDictName(Constants.REGNATURE_GROUP, info.getRegisttype()));
                result = Constants.RESULT_MESSAGE_SUCCESS;
                message = "卡基础数据获取成功";
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = info.getErr();
            }
            return this.result(result, message, info);
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "社保卡号和身份证号码至少有一项不为空";
            return this.result(result, message, null);
        }

    }
}
