package com.iflym.train.propertytranslate.translate.impl.tostring

import com.iflym.train.propertytranslate.domain.enums.DataPropertyDataType
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.tostring.ToStringContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2
import java.util.function.Function

/**
 * created at 2018-09-11
 *
 * @author flym
 */
class ToStringMapTransformer : MapTransformer<ToStringContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: ToStringContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val key = context.key!!
        val value = DataPropertyDataType.toStringIfSuitable(input[key])

        input[key] = value

        if(globalContext.dataSchemaParted) {
            inputSchema.changeProperty(key, Function { t -> t.changeDataType(t.dataType!!.changeToStringIfSuitable()) })
        }

        return Tuple2(input, inputSchema)
    }
}
