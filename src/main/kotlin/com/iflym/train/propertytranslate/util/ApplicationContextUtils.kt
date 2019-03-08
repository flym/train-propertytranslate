package com.iflym.train.propertytranslate.util

import io.iflym.core.util.ExceptionUtils
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * 工具类,用于全局持有相应的引用信息
 * Created by flym on 5/24/2017.
 *
 * @author flym
 */
@Component
class ApplicationContextUtils : ApplicationContextAware, BeanFactoryPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    override fun postProcessBeanFactory(configurableListableBeanFactory: ConfigurableListableBeanFactory) {
        //do nothing
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ApplicationContextUtils.applicationContext = applicationContext
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        log.info("spring容器启动成功!")

        LOCKER.lock()
        try {
            isContextReady = true
            CONDITION.signalAll()
        } finally {
            LOCKER.unlock()
        }

    }

    companion object {
        private val LOCKER = ReentrantLock()
        private val CONDITION = LOCKER.newCondition()

        private val log = Loggers.loggerFor<ApplicationContextUtils>()

        @Volatile
        private var isContextReady: Boolean = false

        private lateinit var applicationContext: ApplicationContext

        /** 等待spring容器准备好,如果没有准备好,则阻塞当前线程  */
        private fun waitContextReady() {
            //快速判断,避免加锁
            if(isContextReady) {
                return
            }

            LOCKER.lock()
            try {
                while(!isContextReady) {
                    ExceptionUtils.doActionLogE(log, { CONDITION.await(3, TimeUnit.SECONDS) })
                    if(!isContextReady) {
                        log.debug("等待容器准备中...")
                    }
                }
            } finally {
                LOCKER.unlock()
            }
        }

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        fun <T> injectReadyBean(bean: T) {
            waitContextReady()
            val factory = applicationContext.autowireCapableBeanFactory
            factory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
            factory.initializeBean(bean, null)
        }


    }

}
