package com.bzw.tars.server.jfgame.kotlin.database.game

import com.bzw.tars.client.tars.tarsgame.IGameMessagePrx
import com.qq.tars.client.CommunicatorConfig
import com.qq.tars.client.CommunicatorFactory

/**
 * @创建者 zoujian
 * @创建时间 2018/7/17
 * @描述
 */
class GameBase {
    var iGameMsgPrx: IGameMessagePrx;
    var tarsGameObj: String;
    var gameID: Int;

    constructor(gameID: Int, tarsGameObj: String) {
        val cfg = CommunicatorConfig()
        val Communicator = CommunicatorFactory.getInstance().getCommunicator(cfg)
        this.iGameMsgPrx = Communicator.stringToProxy(IGameMessagePrx::class.java, tarsGameObj);
        this.gameID = gameID;
        this.tarsGameObj = tarsGameObj;
    }

    fun ToString(): String {
        return String.format("<< GameID:%s,tarsGameObj:%s>>", this.gameID, this.tarsGameObj);
    }
}
