package com.bzw.tars.comm.Router

import com.bzw.tars.comm.TarsUtilsKt

/**
 * @创建者 zoujian
 * @创建时间 2018/8/13
 * @描述
 */
object HandlerReq {

    public fun handlerReq(msgId: Short, msgData: ByteArray): Any? {
        val tarsClass = HandlerMsg.getInstance().getTarsClassReq(msgId);
        if (tarsClass != null) {
            return TarsUtilsKt.toObject(msgData, tarsClass.javaClass)!!;
        } else {
            System.err.println("handlerRouterReq: 消息未注册，msgId：" + msgId);
            return null;
        }
    }
}