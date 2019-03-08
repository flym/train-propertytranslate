package com.iflym.train.propertytranslate.test

import com.iflym.train.propertytranslate.BaseTest
import com.iflym.train.propertytranslate.TransformDefineConstant
import com.iflym.train.propertytranslate.domain.TransformDefine
import com.iflym.train.propertytranslate.mapper.TransformDefineMapper
import com.iflym.train.propertytranslate.service.PropertyTransformService
import com.iflym.train.propertytranslate.util.JsonUtils
import io.iflym.mybatis.util.DbUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.FileCopyUtils
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.io.InputStreamReader

/**
 * created at 2019-03-04
 *
 * @author flym
 */

class PropertyTransformServiceTest : BaseTest() {
    @Autowired
    lateinit var propertyTransformService: PropertyTransformService

    @Autowired
    lateinit var transformDefineMapper: TransformDefineMapper

    @BeforeMethod
    fun init() {
        DbUtils.createTable(jdbcTemplate, null, TransformDefineConstant.TABLE_NAME, TransformDefineConstant.TABLE_SQL)
    }

    @Suppress("UNCHECKED_CAST")
    private fun inputData(): MutableMap<String, Any?> {
        val resource = applicationContext!!.getResource("classpath:propertytransformservice/order.json")
        val json = FileCopyUtils.copyToString(InputStreamReader(resource.inputStream, Charsets.UTF_8))

        return JsonUtils.parse(json, MutableMap::class.java) as MutableMap<String, Any?>
    }

    private fun saveTransformDefine() {
        val orderTransformDefine = TransformDefine(groupKey = groupKey, key = orderDefineKey,
                schemaXml = readString("order-schema.xml"),
                transformerXml = readString("orderInfo-transformer.xml"),
                ftlContent = readString("orderInfo.ftl"))

        transformDefineMapper.save(orderTransformDefine)

        val suborderTransformDefine = TransformDefine(groupKey = groupKey, key = subOrderDefineKey,
                transformerXml = readString("suborders-transformer.xml"),
                groupFtlContent = readString("orders.ftl"))
        transformDefineMapper.save(suborderTransformDefine)
    }

    @Test
    fun testTransformAndPrint() {
        saveTransformDefine()

        val str = propertyTransformService.transformAndPrint(groupKey, orderDefineKey, inputData())
        val expect = readString("expect/orderInfo.html")

        expectStr(str, expect)
    }

    @Test
    fun testTransformGroupAndPrint() {
        saveTransformDefine()
        val str = propertyTransformService.transformGroupAndPrint(groupKey, inputData())

        val expect = readString("expect/orders.html")
        expectStr(str, expect)
    }

    companion object {
        const val groupKey = "test"
        const val orderDefineKey = "orderInfo"
        const val subOrderDefineKey = "subOrders"

        fun readString(classpathPath: String): String {
            val stream = PropertyTransformServiceTest::class.java.getResourceAsStream("/propertytransformservice/$classpathPath")
            return FileCopyUtils.copyToString(stream.reader(Charsets.UTF_8))
        }

        fun expectStr(str: String, expect: String) {
            //去掉所有空格
            val noSpaceStr = str.replace("\\s+".toRegex(), "")
            val noSpaceExpect = expect.replace("\\s+".toRegex(), "")

            Assert.assertEquals(noSpaceStr, noSpaceExpect)
        }
    }
}
