package com.bzw.tars.comm

import com.qq.tars.protocol.tars.TarsOutputStream
import java.lang.reflect.Method

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

            var method: Method = t.javaClass.getMethod("writeTo");
            method(tarsOutputStream);
            return tarsOutputStream.toByteArray();
        } catch (e: Exception) {
            System.err.println("ToByteArray error");
            return null;
        }
    }

}