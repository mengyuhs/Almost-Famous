package com.liema.game.base.listener;

import com.liema.common.global.ConfigManager;
import com.liema.common.rpc.ThriftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @author Noseparte
 * @date 2019/8/21 12:17
 * @Description
 */
public class ApplicationEventListener implements ApplicationListener {

    public final static Logger LOGGER = LogManager.getLogger("GameCore");

    private ThriftClient thriftClient;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            LOGGER.debug("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) {
            LOGGER.debug("初始化完成");
            LOGGER.debug("初始GameData策划数据");
            String path = ApplicationEventListener.class.getResource("/gamedata").getFile();
            ConfigManager.loadGameData(path);
        } else if (event instanceof ContextRefreshedEvent) {
            LOGGER.debug("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {
            LOGGER.debug("应用已启动完成");
        } else if (event instanceof ContextStartedEvent) {
            LOGGER.debug("应用启动，需要在代码动态添加监听器才可捕获");
        } else if (event instanceof ContextStoppedEvent) {
            LOGGER.debug("应用停止");
        } else if (event instanceof ContextClosedEvent) {
            ApplicationContext applicationContext = ((ContextClosedEvent) event).getApplicationContext();
            thriftClient = applicationContext.getBean(ThriftClient.class);
            thriftClient.close();
            LOGGER.debug("应用关闭");
        }
    }

}