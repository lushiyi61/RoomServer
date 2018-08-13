package com.bzw.tars.comm

import com.bzw.tars.client.tars.tarsgame.TReqClientMsg
import com.bzw.tars.server.tars.jfgameclientproto.*

/**
 * @创建者 zoujian
 * @创建时间 2018/8/13
 * @描述 静态类，储存协议解析对象
 */
class MsgHandler {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MsgHandler();
    }

    private constructor() {
        this.initReqRouterMap();
        this.initRespRouterMap();
    }

    // Router 协议对应结构体
    private val msgReqRouterMap: MutableMap<Short, Any> = mutableMapOf();
    private val msgRespRouterMap: MutableMap<Short, Any> = mutableMapOf();


    private fun initReqRouterMap() {
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_ENTER.value().toShort(), TMsgReqEnterTable::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_LEAVE.value().toShort(), TMsgCommPlaceholder::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_SIT_DOWN.value().toShort(), TMsgReqSitDown::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_STAND_UP.value().toShort(), TMsgCommPlaceholder::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_RECONNECT.value().toShort(), TMsgCommPlaceholder::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_PREPARE.value().toShort(), TMsgCommPlaceholder::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_START_BY_MASTER.value().toShort(), TMsgCommPlaceholder::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_DISMISS.value().toShort(), TMsgCommPlaceholder::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_VOTE_DISMISS.value().toShort(), TMsgCommPlaceholder::class);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_GAME_ACTION.value().toShort(), TReqClientMsg::class);
    }

    private fun initRespRouterMap() {

    }

}