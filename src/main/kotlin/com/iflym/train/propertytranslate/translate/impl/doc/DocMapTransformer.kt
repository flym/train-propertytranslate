package com.iflym.train.propertytranslate.translate.impl.doc

import com.iflym.train.propertytranslate.config.DocumentProperties
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.doc.DocContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.impl.ValueTransformer
import com.iflym.train.propertytranslate.util.HttpUrlUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2
import io.iflym.core.util.ObjectUtils
import org.springframework.beans.factory.annotation.Autowired
import java.util.function.Function

/**
 * created at 2018-09-11
 *
 * @author flym
 */
class DocMapTransformer : MapTransformer<DocContext>, ValueTransformer {
    @Autowired
    private lateinit var documentProperties: DocumentProperties

    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: DocContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val key = context.key!!
        val value = translateValue(input[key])

        input[key] = value

        if (globalContext.dataSchemaParted) {
            inputSchema.changeProperty(key, Function{ t -> t.changeDataType(t.dataType!!.changeToValueStringIfSuitable()) })
        }

        return Tuple2(input, inputSchema)
    }

    override fun translateSingleValue(value: Any): Any {
        return if (ObjectUtils.isEmpty(value)) {
            ""
        } else HttpUrlUtils.buildAhref(documentProperties.joinFullUrl(value.toString()), "下载查看")
    }
}
