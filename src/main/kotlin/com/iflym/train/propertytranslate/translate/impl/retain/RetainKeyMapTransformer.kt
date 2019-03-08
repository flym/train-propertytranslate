package com.iflym.train.propertytranslate.translate.impl.retain

import com.google.common.collect.Maps
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.retain.RetainKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class RetainKeyMapTransformer : MapTransformer<RetainKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: RetainKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val key = context.key!!

        val map = Maps.newHashMap<String, Any>()
        map[key] = input[key]

        if(globalContext.dataSchemaParted) {
            val tuple2 = inputSchema.findSchemaOrProperty(key)
            inputSchema.clear()
            inputSchema.addSchemaOrProperty(tuple2.t1, tuple2.t2)
        }

        return Tuple2(map, inputSchema)
    }
}
