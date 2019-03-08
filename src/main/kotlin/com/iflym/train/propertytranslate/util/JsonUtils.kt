package com.iflym.train.propertytranslate.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.iflym.core.util.ExceptionUtils
import io.iflym.mybatis.domain.field.json.jackson.JsonedModule
import java.text.SimpleDateFormat
import java.util.*

/**
 * 统一化json工具
 * Created by flym on 2017/9/11.
 *
 * @author flym
 */
object JsonUtils {
    private val objectMapper = ObjectMapper()

    init {
        objectMapper.findAndRegisterModules()

        //空类序列化忽略
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        //未知属性忽略
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        /*允许字段名不带引号 " {a:1} "*/
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
        /*允许用单引号 " {'a':'1'} "*/
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
        //允许单个元素匹配到数组上
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)

        //仅输出有值的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)

        //设置默认的时区以及格式
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
        objectMapper.dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        //注册Jsoned类型支持
        JsonedModule.registerJackson(objectMapper)
    }

    fun <T> toJsonStr(obj: T): String {
        return ExceptionUtils.doFunRethrowE({ objectMapper.writeValueAsString(obj) })
    }

    fun <T> parse(jsonStr: String, clazz: Class<T>): T {
        return ExceptionUtils.doFunRethrowE({ objectMapper.readValue(jsonStr, clazz) })
    }
}
