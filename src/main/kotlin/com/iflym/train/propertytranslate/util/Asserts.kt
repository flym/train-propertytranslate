package com.iflym.train.propertytranslate.util

import io.iflym.core.util.AssertUtils
import io.iflym.core.util.ObjectUtils

/**
 * 断言类, 用于快速判断条件是否满足
 *
 * @author flym
 */
object Asserts {
    //---------------------------- 附加工具方法 start ------------------------------//

    /** 验证数据非空，此验证失败异常不需要告警  */
    fun notEmpty(instance: Any, messageTemplate: String, vararg params: Any?) {
        assertTrue(!ObjectUtils.isEmpty(instance), messageTemplate, *params)
    }

    //---------------------------- 附加工具方法 end ------------------------------//

    /** 业务断言, 如果失败则throw相应的业务异常，并带有相应的错误码  */
    fun assertTrue(bool: Boolean, messageTemplate: String, vararg params: Any?) {
        AssertUtils.assertTrue(bool, RuntimeException::class.java, messageTemplate, params)
    }

}
