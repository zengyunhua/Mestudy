package com.tecsun.sisp.iface.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

/**
 * ClassName: StartupListener
 * Description: 初始化应用程序
 * 1.初始化redis连接池
 * CreateTime： 2015年06月07日 13时:50分
 */
public class StartupListener extends ContextLoader implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(StartupListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DataBase.init();
        ThreadPoolUtil.init();
//        try {
//            DBConnection.initConn();
//        } catch (SQLException e) {
//            logger.error("初始化程序失败：无法初始化数据库连接:database sisp");
//        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
