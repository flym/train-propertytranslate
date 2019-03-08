package com.iflym.train.propertytranslate.translate.util

/**
 * created at 2018-09-03
 *
 * @author flym
 */
object KeyedContextUtils {
    /** 将一个嵌套的key转换为实际的多层key值  */
    fun resolveNest(key: String): Array<String> {
        return com.iflym.train.propertytranslate.util.StringUtils.splitByDot(key)
    }
}
