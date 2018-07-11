package com.bzw.tars.client.kotlin

import com.bzw.tars.client.jfgame.TarsRouterPrx
import com.qq.tars.client.Communicator
import com.qq.tars.client.CommunicatorConfig
import com.qq.tars.client.CommunicatorFactory

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述
 */
class ClientImpl private constructor() {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = ClientImpl();
    }


    /////////////////////////////////////////////////
    fun loadConfig() {
        val cfg = CommunicatorConfig()
        this._Communicator = CommunicatorFactory.getInstance().getCommunicator(cfg)
    };

    fun getDoPushPrx(): TarsRouterPrx {
        if (this._TarsRouterPrx == null) {
            this._Communicator.stringToProxy(TarsRouterPrx::class.java, this._TarsRouterObj);
        }
        return this._TarsRouterPrx;
    };

    lateinit private var _Communicator: Communicator;
    lateinit private var _TarsRouterObj: String;
    lateinit private var _TarsRouterPrx: TarsRouterPrx;
}