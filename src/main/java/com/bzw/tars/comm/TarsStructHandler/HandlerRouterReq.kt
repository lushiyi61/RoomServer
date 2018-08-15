package com.bzw.tars.comm.TarsStructHandler

import com.bzw.tars.comm.TarsUtilsKt

/**
 * @创建者 zoujian
 * @创建时间 2018/8/13
 * @描述
 */
object HandlerRouterReq {

    public fun handlerReq(msgId: Short, msgData: ByteArray): Any? {
        val tarsClass = HandlerRouterMsg.getInstance().getTarsClassReq(msgId);
        if (tarsClass is Class<out Any>) {
            return TarsUtilsKt.toObject(msgData, tarsClass)!!;
        } else {
            System.err.println("handlerRouterReq: 消息未注册，msgId：" + msgId);
            return null;
        }
    }
}