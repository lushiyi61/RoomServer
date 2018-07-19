package com.bzw.tars.comm

import com.qq.tars.protocol.tars.TarsInputStream
import com.qq.tars.protocol.tars.TarsOutputStream

/**
 * @创建者 zoujian
 * @创建时间 2018/7/18
 * @描述
 */
object TarsUtilsKt {

    fun <T : Any> toByteArray(t: T): ByteArray? {
        try {
            val tarsOutputStream = TarsOutputStream();
            tarsOutputStream.setServerEncoding("UTF-8");

            t.javaClass.getMethod("writeTo", *arrayOf<Class<*>>(TarsOutputStream::class.java)).invoke(t, tarsOutputStream);
            return tarsOutputStream.toByteArray();
        } catch (e: Exception) {
            System.err.println("ToByteArray error");
            return null;
        }
    }

    fun <T : Any> toObject(byteArray: ByteArray, beanClass: Class<T>): T? {

        try {
            val tarsInputStream = TarsInputStream(byteArray);
            tarsInputStream.setServerEncoding("UTF-8");

            val t = beanClass.newInstance();
            beanClass.getMethod("readFrom", *arrayOf<Class<*>>(TarsInputStream::class.java)).invoke(t, tarsInputStream);
            return t;
        } catch (e: Exception) {
            System.err.println("ToObject error");
            return null;
        }
    }

}