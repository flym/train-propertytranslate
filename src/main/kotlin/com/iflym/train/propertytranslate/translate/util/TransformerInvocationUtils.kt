package com.iflym.train.propertytranslate.translate.util

import com.google.common.collect.Lists
import com.iflym.train.propertytranslate.translate.TransformerInvocation
import com.iflym.train.propertytranslate.translate.enums.TransformerResultType

/**
 * created at 2018-08-31
 *
 * @author flym
 */
object TransformerInvocationUtils {

    @Suppress("UNCHECKED_CAST")
    fun  addResult(list: MutableList<Any>, invocation: TransformerInvocation) {
        val result = invocation.result()
        when (invocation.resultType()) {
            TransformerResultType.MAP -> list.add(result!!)
            TransformerResultType.LIST -> {
                list.addAll(result as List<Any>)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getResultAsMap(invocation: TransformerInvocation): Map<String, Any>? {
        val result = invocation.result()
        return when (invocation.resultType()) {
            TransformerResultType.MAP -> result as Map<String, Any>
            TransformerResultType.LIST -> (result as List<Map<String, Any>>)[0]
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getResultAsList(invocation: TransformerInvocation): List<Map<String, Any>> {
        val result = invocation.result()
        return when (invocation.resultType()) {
            TransformerResultType.MAP -> Lists.newArrayList<Any>(result) as List<Map<String, Any>>
            TransformerResultType.LIST -> result as List<Map<String, Any>>
        }
    }
}
