package com.iflym.train.propertytranslate.translate.impl

import com.iflym.train.propertytranslate.domain.enums.DataPropertyDataType
import com.iflym.train.propertytranslate.util.Asserts
import io.iflym.core.util.ListUtils

/**
 * created at 2018-09-11
 *
 * @author flym
 */
interface ValueTransformer {
    /**
     * 转换值
     *
     * @param value 入参
     * @return 转换后的结果值
     */
    fun translateValue(value: Any?): Any? {
        if(value == null) {
            return translateNullValue()
        }

        Asserts.assertTrue(!DataPropertyDataType.isObject(value), "转换的数据值不能为对象类型:{}", value)

        return if(DataPropertyDataType.isList(value)) {
            translateListValue(value as List<*>)
        } else translateSingleValue(value)
    }

    /**
     * 转换null值，即针对null值进行转换
     * 默认同样返回null
     *
     * @return 转换后的值
     */
    fun translateNullValue(): Any? {
        return null
    }

    /**
     * 转换普通值， 非集合
     *
     * @param value 单个非集合的参数值
     * @return 转换后的结果值
     */
    fun translateSingleValue(value: Any): Any

    /**
     * 转换集合值
     * 其默认实现为对集合内的每一个对象单独转换，然后重新填入集合中,即针对集合，开发者只需要默认的 {[.translateSingleValue]} 即可
     *
     * @param value 入参，则值为一个List集合
     * @return 转换后的值
     */
    fun translateListValue(value: List<*>): Any {
        return ListUtils.map(value, { this.translateSingleValue(it!!) })
    }
}
