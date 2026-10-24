package com.tecsun.sisp.npm.util;

public class StaticBean {
    public static final String _T = "\t";

    public static final String _N = "\n";

    public static final String _R = "\r";

    /**
     * 通道过滤名称
     */
    public static enum JIEDIS_NAME {
        /**
         * 社保卡服务
         */
        cardservice {
            public String toString() {
                return "cardservice";
            }
        }
    }

    /**
     * 2013-08-17
     *
     */
    public static enum YES_OR_NO {
        yes {
            public String toString() {

                return "1";
            }
        },
        no {
            public String toString() {

                return "2";
            }
        };
    }

    /**
     * 2013-09-17
     * JDBC连接池代码
     *
     */
    public static enum DB_SOURCE_CODE {
        selfservicesource {
            public String toString() {

                return "selfservicesource";
            }
        },
        coresource {
            public String toString() {

                return "coresource";
            }
        },
        sbsource {
            public String toString() {

                return "sbsource";
            }
        },
        ahsimis_whcard {
            public String toString() {

                return "ahsimis_whcard";
            }
        };
    }

    /**
     * 2013-09-17
     * Parameter取值名称
     *
     */
    public static enum PARAMETER_CODE {
        jsonParam {
            public String toString() {

                return "jsonParam";
            }
        },
        rows {
            public String toString() {

                return "rows";
            }
        },
        page {
            public String toString() {

                return "page";
            }
        },
        order {
            public String toString() {

                return "order";
            }
        },
        sort {
            public String toString() {

                return "sort";
            }
        },
        total {
            public String toString() {

                return "total";
            }
        }
    }

    /**
     * 2013-09-17
     * Parameter取值名称
     *
     */
    public static enum CHAR_CODE {
        utf_8 {
            public String toString() {

                return "UTF-8";
            }
        },
        gbk {
            public String toString() {

                return "GBK";
            }
        },
        iso {
            public String toString() {

                return "ISO-8859-1";
            }
        },
        percent_transcoding {
            public String toString() {
                return "%";
            }
        },
        replace_transcoding {
            public String toString() {
                return "_U012_A21_0_";
            }
        };
    }

    /**
     * 2013-08-17
     *
     */
    public static enum ORG_TYPE {
        edu {// 教育局

            public String toString() {

                return "1";
            }
        },
        school {// 学校

            public String toString() {

                return "2";
            }
        },
        dep {// 部门

            public String toString() {

                return "3";
            }
        },
        goup {// 组

            public String toString() {

                return "4";
            }
        },
        comp {// 公司

            public String toString() {

                return "5";
            }
        },
        subsidiary {// 子公司

            public String toString() {

                return "6";
            }
        };
    }

    /**
     * 2014-06-17
     *
     */
    public static enum USER_STATUS {

        user_faka_status_zhengchang {// 正常

            public String toString() {

                return "1";
            }
        },
        user_status_import {// 已导入

            public String toString() {

                return "10";
            }
        },
        user_status_print {// 已打印

            public String toString() {

                return "11";
            }
        },
        user_status_issue {// 已发放

            public String toString() {

                return "12";
            }
        },
        user_status_retrieve {// 已受理

            public String toString() {

                return "13";
            }
        },
        user_status_pack {// 已打包

            public String toString() {

                return "14";
            }
        },
        user_status_receive {// 最终接收

            public String toString() {

                return "15";
            }
        },
        user_status_pass {// 审表通过

            public String toString() {

                return "16";
            }
        },
        user_status_scan {// 已扫描

            public String toString() {

                return "17";
            }
        },
        user_status_dataprocess {// 数据处理

            public String toString() {

                return "18";
            }
        },
        user_status_rewrite {// 处理回盘

            public String toString() {

                return "19";
            }
        },
        user_status_makecard {// 制卡中

            public String toString() {

                return "20";
            }
        },
        user_status_finished {// 成品卡

            public String toString() {

                return "21";
            }
        },
        user_status_bank_receive {// 银行领卡

            public String toString() {

                return "22";
            }
        },
        user_status_card_issued {// 已发卡

            public String toString() {

                return "23";
            }
        },
        user_status_zhika_pici {// 已加入批次

            public String toString() {

                return "50";
            }
        },
        user_status_bank_yes {// 开户成功

            public String toString() {

                return "51";
            }
        },
        user_status_bank_no {// 开户失败

            public String toString() {

                return "52";
            }
        },
        user_status_cardnumber {// 已分配卡号

            public String toString() {

                return "55";
            }
        },
        user_status_zhika_yes {// 工厂制卡成功

            public String toString() {

                return "60";
            }
        },
        user_status_zhika_no {// 工厂制卡失败

            public String toString() {

                return "61";
            }
        },
        user_faka_status_linshiguashi {// 临时挂失

            public String toString() {

                return "91";
            }
        },
        user_faka_status_zhengshiguashi {// 正式挂失

            public String toString() {

                return "92";
            }
        },
        user_faka_status_zhuxiao {// 注销

            public String toString() {

                return "93";
            }
        },
        user_faka_status_jihuo {// 激活

            public String toString() {

                return "94";
            }
        },
        user_faka_status_buka {// 补卡

            public String toString() {

                return "95";
            }
        },
        user_faka_status_dzx {// 待注销

            public String toString() {

                return "96";
            }
        },
        user_faka_status_huanka {// 换卡

            public String toString() {

                return "97";
            }
        },
        user_faka_status_huanhangxiaohu {// 换行销户

            public String toString() {

                return "98";
            }
        },
        user_status_deliver {// 上交

            public String toString() {

                return "141";
            }
        },
        user_status_transit {// 中转接收

            public String toString() {

                return "142";
            }
        },
        user_status_loss {// 遗失

            public String toString() {

                return "151";
            }
        },
        user_status_form_exception {// 异常表

            public String toString() {

                return "161";
            }
        },
        user_status_encrypt {// 加密失败

            public String toString() {

                return "181";
            }
        },
        user_status_update {// 已修改

            public String toString() {

                return "188";
            }
        },
        user_status_rewrite_fail {// 处理回盘失败

            public String toString() {

                return "191";
            }
        },
        user_status_card_exception {// 异常卡

            public String toString() {

                return "211";
            }
        };
    }

    /**
     * 2014-06-17
     *
     */
    public static enum SHEBAO_STATUS {
        shebao_status_update_password {// 修改密码

            public String toString() {

                return "0";
            }
        },
        shebao_status_zhengchang {// 正常

            public String toString() {

                return "1";
            }
        },
        shebao_status_buka {// 补卡

            public String toString() {

                return "70";
            }
        },
        shebao_status_huanka {// 换卡

            public String toString() {

                return "80";
            }
        },
        shebao_status_jihuo {// 激活

            public String toString() {

                return "10";
            }
        },
        shebao_status_jiegua {// 解挂

            public String toString() {

                return "11";
            }
        },
        shebao_status_linshiguashi {// 临时挂失

            public String toString() {

                return "20";
            }
        },
        shebao_status_dzx {// 待注销

            public String toString() {

                return "30";
            }
        },
        shebao_status_zhuxiao {// 注销

            public String toString() {

                return "40";
            }
        },
        shebao_status_zhengshiguashi {// 正式挂失

            public String toString() {

                return "50";
            }
        },
        shebao_status_huanhangxiaohu {// 换行销户

            public String toString() {

                return "60";
            }
        };
    }

    /**
     * 2014-06-17
     *
     */
    public static enum USER_LOG_TYPE {
        user_log_type_import {// 导入

            public String toString() {

                return "1";
            }
        },
        user_log_type_account {// 开户

            public String toString() {

                return "2";
            }
        },
        user_log_type_print {// 打印

            public String toString() {

                return "3";
            }
        },
        user_log_type_issue {// 发放

            public String toString() {

                return "4";
            }
        },
        user_log_type_retrieve {// 回收

            public String toString() {

                return "5";
            }
        },
        user_log_type_pack {// 打包

            public String toString() {

                return "6";
            }
        },
        user_log_type_receive {// 最终接收

            public String toString() {

                return "7";
            }
        },
        user_log_type_verify {// 审表

            public String toString() {

                return "8";
            }
        },
        user_log_type_exception {// 异常表9

            public String toString() {

                return "9";
            }
        },
        user_log_type_scan {// 扫描登记

            public String toString() {

                return "10";
            }
        },
        user_log_type_process_add {// 新建数据处理

            public String toString() {

                return "11";
            }
        },
        user_log_type_process_encrypt {// 数据加密

            public String toString() {

                return "12";
            }
        },
        user_log_type_process_delete {// 删除数据处理

            public String toString() {

                return "13";
            }
        },
        user_log_type_process_rewrite {// 数据回盘

            public String toString() {

                return "14";
            }
        },
        user_log_type_card_batch {// 创建制卡批次

            public String toString() {

                return "15";
            }
        },
        user_log_type_card_number {// 分配卡号

            public String toString() {

                return "16";
            }
        },
        user_log_type_card_rewrite {// 制卡回盘

            public String toString() {

                return "17";
            }
        },
        user_log_type_card_finished {// 成品卡回盘

            public String toString() {

                return "18";
            }
        },
        user_log_type_bank_receive {// 网点领卡

            public String toString() {

                return "19";
            }
        },
        user_log_type_card_personal_issue {//

            public String toString() {

                return "20";
            }
        },
        user_log_shebao_jihuo {// 社保卡激活

            public String toString() {

                return "31";
            }
        }/*,
        user_log_type_account
        {// 

            public String toString()
            {

                return "21";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "22";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "23";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "24";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "31";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "32";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "33";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "34";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "35";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "36";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "37";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "41";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "42";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "43";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "44";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "45";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "46";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "47";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "51";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "52";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "53";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "54";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "55";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "56";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "57";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "58";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "59";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "60";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "61";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        },
        user_log_type_account
        {// 

            public String toString()
            {

                return "";
            }
        }*/;

    }

}
