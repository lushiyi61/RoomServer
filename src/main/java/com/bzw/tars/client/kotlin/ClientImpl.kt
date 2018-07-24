package com.bzw.tars.client.kotlin


import com.bzw.tars.client.tars.jfgame.TarsRouterPrx
import com.bzw.tars.client.kotlin.game.GameBase
import com.bzw.tars.client.kotlin.game.GameMng
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
        println(String.format("==========loadConfig=========="));
        val cfg = CommunicatorConfig()
        this._Communicator = CommunicatorFactory.getInstance().getCommunicator(cfg)
        this._TarsRouterObj = "JFGame.RouterServer2.PushObj@tcp -h 192.168.0.242 -p 28892 -t 60000 -e 0";
        this._TarsRouterPrx = this._Communicator.stringToProxy(TarsRouterPrx::class.java, this._TarsRouterObj);


        // 游戏初始化
//        GameMng.getInstance().addGame(111111, GameBase(111111,""))
    };

    fun getDoPushPrx(): TarsRouterPrx {
        return this._TarsRouterPrx;
    };

    lateinit private var _Communicator: Communicator;
    lateinit private var _TarsRouterObj: String;
    lateinit private var _TarsRouterPrx: TarsRouterPrx;
}