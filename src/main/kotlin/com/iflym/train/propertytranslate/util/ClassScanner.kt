/* Created by flym at 2014/6/25 */
package com.iflym.train.propertytranslate.util

import com.google.common.collect.Lists
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.springframework.util.Assert
import java.lang.reflect.Modifier
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * 用于实现项目内的类信息扫描和处理
 *
 * @author flym
 */
class ClassScanner(packageNames: Array<String>) {

    private val reflections: Reflections

    init {
        Assert.notEmpty(packageNames, "请至少指定一个包名")

        //仅处理java类,以.class结尾的文件
        val predicateList = Stream.of(*packageNames)
                .map { t -> t.replace(".", "/") }
                .map { t -> com.google.common.base.Predicate<String> { it!!.startsWith(t) && it.endsWith(".class") } }
                .collect(Collectors.toList())

        val urlList = Stream.of(*packageNames)
                .map({ ClasspathHelper.forPackage(it) })
                .flatMap({ it.stream() })
                .collect(Collectors.toList<Any>())

        //用于扫描类注解
        val typeScanner = TypeAnnotationsScanner()
        //用于子类信息处理
        val subTypeScanner = SubTypesScanner()
        //用于扫描方法注解
        val methodScanner = MethodAnnotationsScanner()
        //用于扫描字段注解
        val fieldScanner = FieldAnnotationsScanner()

        val config = ConfigurationBuilder.build(urlList, predicateList, typeScanner, subTypeScanner, fieldScanner, methodScanner)
        config.setExpandSuperTypes(false)

        this.reflections = Reflections(config)
    }

    /** 找到所有继承指定类或实现指定接口的公共非接口子类  */
    fun <T> scanSubClass(superClassOrInterface: Class<T>): List<Class<out T>> {
        val classSet = reflections.getSubTypesOf(superClassOrInterface)
        return filterPublicClass(classSet)
    }

    companion object {
        val defaultInstance = ClassScanner(System.getProperty("classScanner.package", "com.iflym.train").split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())

        private fun <T> filterPublicClass(classSet: Set<Class<out T>>): List<Class<out T>> {
            val classList = Lists.newArrayList<Class<out T>>()
            for(clazz in classSet) {
                //不能为抽象类，不能为接口，只能为公共类
                if(clazz.isInterface) {
                    continue
                }
                val modifier = clazz.modifiers
                if(!Modifier.isPublic(modifier)) {
                    continue
                }
                if(Modifier.isAbstract(modifier)) {
                    continue
                }

                classList.add(clazz)
            }
            return classList
        }
    }
}
