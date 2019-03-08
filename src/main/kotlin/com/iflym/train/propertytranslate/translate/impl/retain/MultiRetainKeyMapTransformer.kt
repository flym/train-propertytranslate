package com.iflym.train.propertytranslate.translate.impl.retain

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.retain.MultiRetainKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataPropertyVO
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiRetainKeyMapTransformer : MapTransformer<MultiRetainKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: MultiRetainKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val map = Maps.newHashMap<String, Any>()


        context.retainList.stream()
                .map { it.key }
                .forEach { map[it] = input[it] }

        if(globalContext.dataSchemaParted) {
            val schemaOrPropertyList = Lists.newArrayList<Tuple2<DataSchemaVO?, DataPropertyVO?>>()
            context.retainList.stream()
                    .map { it.key!! }
                    .forEach { t -> schemaOrPropertyList.add(inputSchema.findSchemaOrProperty(t)) }

            inputSchema.clear()
            schemaOrPropertyList.forEach { t -> inputSchema.addSchemaOrProperty(t.t1, t.t2) }
        }

        return Tuple2(map, inputSchema)
    }
}
