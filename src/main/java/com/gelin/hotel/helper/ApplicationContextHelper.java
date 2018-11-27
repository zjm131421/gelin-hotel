package com.gelin.hotel.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * The type Application context helper.
 * <p>
 * 通过get方式获取对象
 *
 * @author houya
 */
@Component
@Lazy(false)
public class ApplicationContextHelper implements ApplicationContextAware {
    /**
     * ctx
     */
    private static ApplicationContext ctx;

    @Override
    public synchronized void setApplicationContext(ApplicationContext appContext)
            throws BeansException {
        ctx = appContext;

    }

    /**
     * Gets application context.
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
}
