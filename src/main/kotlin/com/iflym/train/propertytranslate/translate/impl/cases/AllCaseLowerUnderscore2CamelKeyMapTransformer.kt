package com.iflym.train.propertytranslate.translate.impl.cases

import com.google.common.collect.Maps
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.cases.AllCaseLowerUnderscore2CamelKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class AllCaseLowerUnderscore2CamelKeyMapTransformer : MapTransformer<AllCaseLowerUnderscore2CamelKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: AllCaseLowerUnderscore2CamelKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val clear = context.clear
        val newMap = if(clear) Maps.newHashMap() else Maps.newHashMap(input)

        input.forEach { k, v ->
            val newName = com.iflym.train.propertytranslate.util.StringUtils.lowerUnderscore2Camel(k)
            newMap[newName] = v

            if(globalContext.dataSchemaParted && newName != k) {
                inputSchema.addOrChangeSchemaOrPropertyName(k, newName, clear)
            }
        }

        return Tuple2(newMap, inputSchema)
    }
}
