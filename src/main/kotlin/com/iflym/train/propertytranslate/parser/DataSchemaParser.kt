package com.iflym.train.propertytranslate.parser

import com.google.common.collect.Maps
import com.iflym.train.propertytranslate.domain.enums.DataPropertyDataType
import com.iflym.train.propertytranslate.util.Loggers
import com.iflym.train.propertytranslate.vo.DataPropertyVO
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

/**
 * 用于解析相应的xml结构，生成相应的schemaVO体系
 * created at 2018-09-03
 *
 * @author flym
 */
class DataSchemaParser : DefaultHandler() {

    private var root: DataSchemaVO? = null

    private var current: DataSchemaVO? = null

    private val startConsumerMap = Maps.newHashMap<String, (Attributes) -> Unit>()
    private val endConsumerMap = Maps.newHashMap<String, () -> Unit>()

    init {
        startConsumerMap[DataSchemaVO.ELEMENT_NAME_SCHEMA] = this::startSchema
        startConsumerMap[DataPropertyVO.ELEMENT_NAME_PROPERTY] = this::startProperty

        endConsumerMap[DataSchemaVO.ELEMENT_NAME_SCHEMA] = this::endSchema
        endConsumerMap[DataPropertyVO.ELEMENT_NAME_PROPERTY] = this::endProperty
    }

    private fun startSchema(attributes: Attributes) {
        val schema = DataSchemaVO()
        schema.name = attributes.getValue(DataSchemaVO.QNAME_NAME)
        schema.title = attributes.getValue(DataSchemaVO.QNAME_TITLE)

        schema.parentSchema = current

        current?.addSchema(schema)

        if(root == null) {
            root = schema
        }

        current = schema
    }

    private fun endSchema() {
        current = current!!.parentSchema
    }

    private fun startProperty(attributes: Attributes) {
        val propety = DataPropertyVO()
        propety.name = attributes.getValue(DataPropertyVO.QNAME_NAME)
        propety.title = attributes.getValue(DataPropertyVO.QNAME_TITLE)
        propety.dataType = DataPropertyDataType.valueOf(attributes.getValue(DataPropertyVO.QNAME_DATA_TYPE))
        val orderStr = attributes.getValue("order")
        propety.order = orderStr?.toInt() ?: Int.MAX_VALUE

        current!!.addProperty(propety)
    }

    private fun endProperty() {
        //nothing to do
    }

    fun resolvedSchema(): DataSchemaVO? {
        return root
    }

    @Throws(SAXException::class)
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        super.startElement(uri, localName, qName, attributes)

        (startConsumerMap[qName] ?: { log.warn("收到不认识的开始节点:{}", qName) })(attributes)
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)

        (endConsumerMap[qName] ?: { log.warn("收到不认识的结束节点:{}", qName) })()
    }

    companion object {
        private val log = Loggers.loggerFor<DataSchemaParser>()
    }
}
