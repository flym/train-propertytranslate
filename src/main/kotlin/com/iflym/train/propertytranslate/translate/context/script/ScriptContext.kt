package com.iflym.train.propertytranslate.translate.context.script

import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext
import com.iflym.train.propertytranslate.util.Asserts
import com.iflym.train.propertytranslate.vo.DataPropertyVO

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class ScriptContext : KeyedTransformContext {
    /** 脚本  */
    lateinit var script: String

    /** 目标key属性信息  */
    lateinit var keyProperty: DataPropertyVO

    fun requiredKeyProperty(): DataPropertyVO {
        Asserts.notEmpty(this.keyProperty, "当前脚本上下文中并没有目标key属性。key:{},脚本:{}", this.key, this.script)

        return this.keyProperty
    }
}
