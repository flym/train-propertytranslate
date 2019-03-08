package com.iflym.train.propertytranslate.util

import org.slf4j.LoggerFactory


object Loggers {
    inline fun <reified T : Any> loggerFor() = LoggerFactory.getLogger(T::class.java)!!

}