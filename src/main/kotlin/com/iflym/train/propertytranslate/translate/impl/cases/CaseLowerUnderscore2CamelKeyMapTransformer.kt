package com.iflym.train.propertytranslate.translate.impl.cases

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.cases.CaseLowerUnderscore2CamelKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class CaseLowerUnderscore2CamelKeyMapTransformer : MapTransformer<CaseLowerUnderscore2CamelKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: CaseLowerUnderscore2CamelKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val key = context.key!!
        val newKey = com.iflym.train.propertytranslate.util.StringUtils.lowerUnderscore2Camel(key)

        input[newKey] = input[key]

        if(context.clear) {
            input.remove(key)
        }

        if(globalContext.dataSchemaParted && key != newKey) {
            inputSchema.addOrChangeSchemaOrPropertyName(key, newKey, context.clear)
        }

        return Tuple2(input, inputSchema)
    }
}
