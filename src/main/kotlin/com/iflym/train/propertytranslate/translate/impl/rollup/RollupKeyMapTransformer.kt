package com.iflym.train.propertytranslate.translate.impl.rollup

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.rollup.RollupKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.util.Asserts
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2
import io.iflym.core.util.ObjectUtils

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class RollupKeyMapTransformer : MapTransformer<RollupKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: RollupKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val key = context.key!!

        val v = input[key]
        if(ObjectUtils.isEmpty(v)) {
            return Tuple2(input, inputSchema)
        }

        Asserts.assertTrue(v is Map<*, *>, "指定的对象并不是map类型.key:{}, 实际类型:{}", key, v!!.javaClass)

        @Suppress("UNCHECKED_CAST")
        input.putAll(v as Map<String, Any?>)

        if(context.clear) {
            input.remove(key)
        }

        if(globalContext.dataSchemaParted) {
            val schema = inputSchema.findSchema(key)!!

            //先清除，避免后面的覆盖之后，再次清除
            if(context.clear) {
                inputSchema.removeSchema(schema)
            }

            schema.findSchemaSetOrEmpty().forEach({ inputSchema.addSchema(it) })
            schema.findPropertySetOrEmpty().forEach({ inputSchema.addProperty(it) })
        }

        return Tuple2(input, inputSchema)
    }
}
