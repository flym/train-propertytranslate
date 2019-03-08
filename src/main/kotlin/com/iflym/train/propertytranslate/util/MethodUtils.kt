package com.iflym.train.propertytranslate.util

import com.google.common.collect.Maps
import io.iflym.core.tuple.Tuple1Holder
import io.iflym.core.util.ExceptionUtils
import io.iflym.core.util.ExceptionUtils.doFunRethrowE
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodHandles.Lookup.*
import java.lang.reflect.Constructor
import java.lang.reflect.Method


object MethodUtils {
    private val SPECIAL_METHOD_HANDLE_MAP = Maps.newHashMap<Method, MethodHandle>()

    private val LOOKUP_PRIVATE: Constructor<MethodHandles.Lookup>


    init {
        val constructorHolder: Tuple1Holder<Constructor<MethodHandles.Lookup>> = Tuple1Holder()
        ExceptionUtils.doActionRethrowE({
            val constructor = MethodHandles.Lookup::class.java.getDeclaredConstructor(Class::class.java, Int::class.java)
            constructor.isAccessible = true
            //noinspection unchecked
            constructorHolder.t1 = constructor
        })

        LOOKUP_PRIVATE = constructorHolder.t1
    }


    /**
     * 修正原方法不能调用protected的问题
     * @see io.iflym.core.util.MethodUtils.invokeSpecial
     *
     * */
    @Suppress("UNCHECKED_CAST")
    fun <T> invokeSpecial(method: Method, instance: Any, vararg params: Any): T {
        val declaringClass = method.declaringClass
        return doFunRethrowE {
            var handle: MethodHandle? = SPECIAL_METHOD_HANDLE_MAP[method]
            if(handle == null) {
                handle = LOOKUP_PRIVATE.newInstance(declaringClass, PRIVATE or PROTECTED or PACKAGE or PUBLIC)
                        .unreflectSpecial(method, declaringClass)
                SPECIAL_METHOD_HANDLE_MAP[method] = handle
            }

            return@doFunRethrowE handle!!.bindTo(instance).invokeWithArguments(*params)
        } as T
    }
}