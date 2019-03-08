package com.iflym.train.propertytranslate.translate

import com.iflym.train.propertytranslate.translate.enums.TransformerResultType
import com.iflym.train.propertytranslate.vo.DataSchemaVO

/**
 * created at 2018-08-31
 *
 * @author flym
 */
interface TransformerInvocation : kotlin.Cloneable {
    /** 初始化相应信息  */
    fun init()

    /** 执行处理，并最终产生相应的调用结果  */
    fun proceed()

    /**
     * 此转换器返回转换的结果的类型
     *
     * @return 具体结果类型
     */
    fun resultType(): TransformerResultType {
        return TransformerResultType.MAP
    }

    /**
     * 返回当前转换的结果
     *
     * @return 结果值
     */
    fun result(): Any?

    /**
     * 返回当前转换的结果的数据结构，如果在转换时不处理结构，则返回null
     *
     * @return 数据结构, 可能为null
     */
    fun resultSchema(): DataSchemaVO?
}
