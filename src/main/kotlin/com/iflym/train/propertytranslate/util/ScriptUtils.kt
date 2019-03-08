/* Created by flym at 15-12-12 */
package com.iflym.train.propertytranslate.util

import com.google.common.base.Throwables
import com.google.common.cache.CacheBuilder
import io.iflym.core.tuple.Tuple3
import io.iflym.core.util.ObjectUtils
import org.mvelx.MVEL
import org.mvelx.ParserContext
import java.io.Serializable

/**
 * 脚本引擎工具类，用于评估执行相应的脚本
 *
 * @author flym
 */
object ScriptUtils {
    private val EXPRESSION_CACHE = CacheBuilder.newBuilder().maximumSize(100000).weakValues().build<Tuple3<String, Boolean, Boolean>, Serializable>()

    /**
     * 评估一段脚本，并返回相应的结构信息
     *
     * @param expression   要评估的表达式，如a+b
     * @param paramContext 当前传入的参数，表达式中通过this引用此参数，获取属性时可省略this,如this.a简化为a
     * @param varMap       相应的变量，可以在表达式中使用
     * @param nullSafe     是否是null安全,即在过程中如果出现null属性或属性值为null,则保证表达式提前返回null,而不是throw异常
     */
    fun <T, P> eval(expression: String, paramContext: P, varMap: MutableMap<*, Any?>?, nullSafe: Boolean): T {
        val varMapNull = ObjectUtils.isEmpty(varMap)
        try {
            val statement = EXPRESSION_CACHE.get(Tuple3(expression, varMapNull, nullSafe)) {
                val parserContext = ParserContext()
                //nullSafe增加相应的标记
                if(nullSafe) {
                    parserContext.parserConfiguration.isNullSafe = true
                }

                return@get MVEL.compileExpression(expression, parserContext)
            }

            //默认使用变量map，以支持存储临时变量
            val currentVarMap: MutableMap<*, *> = varMap ?: mutableMapOf<Any, Any?>()

            @Suppress("UNCHECKED_CAST")
            return MVEL.executeExpression(statement, paramContext, currentVarMap) as T
        } catch(e: Exception) {
            Throwables.throwIfUnchecked(e)
            throw RuntimeException("评估mvel脚本失败:" + e.message, e)
        }

    }
}
