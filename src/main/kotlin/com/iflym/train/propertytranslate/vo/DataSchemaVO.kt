package com.iflym.train.propertytranslate.vo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.common.base.Charsets
import com.google.common.collect.Lists
import com.iflym.train.propertytranslate.domain.enums.SchemaRalation
import com.iflym.train.propertytranslate.parser.DataSchemaParser
import com.iflym.train.propertytranslate.util.Asserts
import com.iflym.train.propertytranslate.util.SaxUtils
import io.iflym.core.tuple.Tuple2
import io.iflym.core.util.CloneUtils
import io.iflym.core.util.ExceptionUtils
import java.io.ByteArrayInputStream
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.collections.HashSet

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class DataSchemaVO : Cloneable {
    /**
     * 当前这一层的惟一名,一般以相应的property name来表示
     * 如果是顶级，则为 ROOT
     */
    var name: String? = null

    /**
     * 中文标题
     * 如果为顶级，则为 ROOT
     */
    var title: String? = null

    /** 父级  */
    @JsonIgnore
    var parentSchema: DataSchemaVO? = null

    /** 父级的关系  */
    var parentRelation: SchemaRalation? = null

    /** 子类的分类集  */
    private var schemaSet: MutableSet<DataSchemaVO>? = null

    /** 当前层的属性集(不包括引用类型)  */
    private var propertySet: MutableSet<DataPropertyVO>? = null

    fun fetchOrderedPropertySet(): List<DataPropertyVO> {
        val list = Lists.newArrayList<DataPropertyVO>(fetchValidPropertySet())
        list.sort()
        return list
    }

    private fun fetchValidPropertySet(): MutableSet<DataPropertyVO> {
        if(propertySet == null) {
            propertySet = mutableSetOf()
        }

        return propertySet!!
    }

    private fun fetchValidSchemaSet(): MutableSet<DataSchemaVO> {
        if(schemaSet == null) {
            schemaSet = mutableSetOf()
        }

        return schemaSet!!
    }

    fun findSchemaSetOrEmpty(): Set<DataSchemaVO> {
        return Optional.ofNullable(schemaSet).orElse(Collections.emptySet())
    }

    fun findPropertySetOrEmpty(): Set<DataPropertyVO> {
        return Optional.ofNullable(propertySet).orElse(Collections.emptySet())
    }

    fun addProperty(property: DataPropertyVO) {
        Asserts.notEmpty(property, "不能添加空属性")

        //因为dataProperty的eq按name来比较，因此避免添加不成功，强行先删除旧的，再添加，以覆盖式处理
        val set = fetchValidPropertySet()
        set -= property
        set += property
    }

    fun addSchema(schema: DataSchemaVO) {
        Asserts.notEmpty(schema, "不能添加空集")
        schema.parentSchema = this

        //覆盖式处理
        val set = fetchValidSchemaSet()
        set.remove(schema)
        set.add(schema)
    }

    fun addSchemaOrProperty(schema: DataSchemaVO?, property: DataPropertyVO?) {
        schema?.let { addSchema(it) }
        property?.let { addProperty(it) }
    }

    fun removeProperty(property: DataPropertyVO) {
        fetchValidPropertySet().remove(property)
    }

    fun removeSchema(schema: DataSchemaVO) {
        fetchValidSchemaSet().remove(schema)
    }

    fun changeProperty(oldProperty: DataPropertyVO, newProperty: DataPropertyVO) {
        if(oldProperty === newProperty) {
            return
        }

        val list = fetchValidPropertySet()
        list.remove(oldProperty)
        list.add(newProperty)
    }

    fun changeProperty(name: String, propertyFun: Function<DataPropertyVO, DataPropertyVO>) {
        val oldProperty = findProperty(name)
        if(oldProperty != null) {
            val newProperty = propertyFun.apply(oldProperty)
            changeProperty(oldProperty, newProperty)
        }
    }

    fun changeSchema(oldSchema: DataSchemaVO, newSchema: DataSchemaVO) {
        if(oldSchema === newSchema) {
            return
        }
        val list = fetchValidSchemaSet()
        list.remove(oldSchema)
        list.add(newSchema)
    }

    fun findProperty(name: String): DataPropertyVO? {
        return if(propertySet == null) {
            null
        } else propertySet!!.stream().filter({ t -> Objects.equals(t.name, name) }).findFirst().orElse(null)

    }

    fun findSchema(name: String): DataSchemaVO? {
        return if(schemaSet == null) {
            null
        } else schemaSet!!.stream().filter({ t -> Objects.equals(t.name, name) }).findFirst().orElse(null)

    }

    fun changeName(newName: String): DataSchemaVO {
        val value = CloneUtils.clone(this)
        value.name = newName

        return value
    }

    fun findSchemaOrProperty(name: String): Tuple2<DataSchemaVO?, DataPropertyVO?> {
        val property = this.findProperty(name)
        val schema = this.findSchema(name)

        Asserts.assertTrue(property != null || schema != null, "根据属性名没有找到子属性或集合.{}", name)

        return Tuple2(schema, property)
    }

    fun addOrChangeSchemaOrPropertyName(oldName: String, newName: String, change: Boolean) {
        val tuple2 = findSchemaOrProperty(oldName)
        val property = tuple2.t2
        val schema = tuple2.t1

        if(property != null) {
            val newProperty = property.changeName(newName)
            if(change) {
                this.changeProperty(property, newProperty)
            } else {
                this.addProperty(newProperty)
            }
        }
        if(schema != null) {
            val newSchema = schema.changeName(newName)
            if(change) {
                this.changeSchema(schema, newSchema)
            } else {
                this.addSchema(newSchema)
            }
        }
    }

    fun removeSchemaOrProperty(name: String) {
        val tuple2 = findSchemaOrProperty(name)
        Optional.ofNullable(tuple2.t1).ifPresent({ this.removeSchema(it) })
        Optional.ofNullable(tuple2.t2).ifPresent({ this.removeProperty(it) })
    }

    fun clear() {
        Optional.ofNullable(schemaSet).ifPresent { it.clear() }
        Optional.ofNullable(propertySet).ifPresent { it.clear() }
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public override fun clone(): DataSchemaVO {
        val value = super.clone() as DataSchemaVO
        return ExceptionUtils.doFunRethrowE({
            if(propertySet != null) {
                value.propertySet = HashSet(propertySet)
            }

            if(schemaSet != null) {
                val valueSchemaList = schemaSet!!.stream().map({ it.clone() }).collect(Collectors.toSet())
                valueSchemaList.forEach { t -> t.parentSchema = value }
                value.schemaSet = valueSchemaList
            }

            return@doFunRethrowE value
        })
    }

    companion object {
        const val ELEMENT_NAME_SCHEMA = "schema"

        const val QNAME_NAME = "name"
        const val QNAME_TITLE = "title"

        fun build(xmlConfig: String): DataSchemaVO {
            return ExceptionUtils.doFunRethrowE({
                val sax = SaxUtils.notValidFactory()

                val parser = DataSchemaParser()
                ByteArrayInputStream(xmlConfig.toByteArray(Charsets.UTF_8)).let { sax.newSAXParser().parse(it, parser) }

                return@doFunRethrowE parser.resolvedSchema()!!
            })
        }
    }
}
