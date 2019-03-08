package com.iflym.train.propertytranslate.config

import com.iflym.train.propertytranslate.util.HttpUrlUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * 文档相关的服务
 * 仅用于验证链接拼接场景
 * created at 2018-09-11
 *
 * @author flym
 */
@Component
class DocumentProperties {
    /** 文档(任意类型)的地址  */
    @Value("\${document.prefix}")
    lateinit var prefix: String

    /** 连接文档地址  */
    fun joinFullUrl(value: String): String {
        return HttpUrlUtils.joinUrl(prefix, value)
    }
}
