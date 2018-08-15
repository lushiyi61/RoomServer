package com.bzw.tars.comm.TarsStructHandler

import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.comm.TarsUtilsKt

/**
 * @创建者 zoujian
 * @创建时间 2018/8/15
 * @描述
 */
object HandlerRouterResp {
    public fun <T : Any> handlerResp(msgId: Short, msgStruct: T): TRespPackage {
        val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
        tRespPackage.vecMsgID.add(msgId);
        tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(msgStruct));

        return tRespPackage
    }
}