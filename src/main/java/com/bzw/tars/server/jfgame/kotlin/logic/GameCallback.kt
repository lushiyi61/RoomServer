package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientImpl
import com.bzw.tars.client.kotlin.game.GameBase
import com.bzw.tars.client.tars.jfgameclientproto.E_CLIENT_MSGID
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.client.tars.tarsgame.*
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng

/**
 * @创建者 zoujian
 * @创建时间 2018/7/25
 * @描述
 */
class GameCallback : IGameMessagePrxCallback {
    constructor(tableNO: String, gameBase: GameBase, chairIdx: Byte = -1) : super() {
        this.tableNO = tableNO;
        this.gameBase = gameBase;
        this.chairIdx = chairIdx;
    }

    val gameBase: GameBase;
    val tableNO: String;
    val chairIdx: Byte;

    override fun callback_expired() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun callback_exception(ex: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun callback_doRoomMessage(ret: Short, tRespMessage: TRespMessage?) {
        this.doCallback(ret.toInt(), tRespMessage);
    }

    override fun callback_doClientMessage(ret: Short, tRespMessage: TRespMessage?) {
        // chairIdx >= 0
        this.doCallback(ret.toInt(), tRespMessage);
    }

    private fun doCallback(ret: Int, tRespMessage: TRespMessage?) {
        if (ret >= 0) {
            this.doNewGameMsg(ret.toShort());

            tRespMessage ?: return;
            doGameMsgNotify(tRespMessage);
        } else {
//            System.err.println("Game Callback error !!!");
        }
    }

    private fun doGameMsgNotify(tRespMessage: TRespMessage) {
        val tablePlayerDict = TableMng.getInstance().getTablePlayer(this.tableNO);
        tablePlayerDict ?: return;

        when (tRespMessage.eMsgType) {
            EGameMsgType.E_RESPONE_DATA.value() -> null;
            EGameMsgType.E_RESPALL_DATA.value() -> null;
            EGameMsgType.E_NOTIFY_DATA.value() -> null;
            EGameMsgType.E_MIXTURE_DATA.value() -> null;
            else -> System.err.println("Game msgType error !!!");
        }
    }

    /*
     * @description 处理转发
     * =====================================
     * @author zoujian
     * @date 2018/7/25 15:33
     */

    private fun doRespOne(tRespMessage: TRespMessage, playerDict: MutableMap<Long, TableBase.TablePlayer>) {
        val tRespData = tRespMessage.getTGameData().tRespOneData;
        tRespData ?: return;
        for (player in playerDict.values) {
            if (player.chairIdx == this.chairIdx) {
                val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
                val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
                tRespPackage.vecMsgID.add(E_CLIENT_MSGID.E_GAME_ACTION.value().toShort());
                tRespPackage.vecMsgData.add(tRespData.getVecData());
                tarsRouterPrx.doPush(player.uid, tRespPackage);
                break;
            }
        }
    }

    private fun doRespAll(tRespMessage: TRespMessage, playerDict: MutableMap<Long, TableBase.TablePlayer>) {
        val tListData = tRespMessage.getTGameData().getVecRespAllData();
        tListData ?: return;
        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
        for (player in playerDict.values) {
            if (player.chairIdx >= 0 && player.chairIdx < tListData.size) {
                val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
                tRespPackage.vecMsgID.add((0 - E_CLIENT_MSGID.E_GAME_ACTION.value()).toShort());
                tRespPackage.vecMsgData.add(tListData.get(player.chairIdx.toInt()).getVecData());
                tarsRouterPrx.doPush(player.uid, tRespPackage);
            }
        }
    }

    private fun doNotify(tRespMessage: TRespMessage, playerDict: MutableMap<Long, TableBase.TablePlayer>) {
        val tNotifyData = tRespMessage.getTGameData().getTNotifyData();
        tNotifyData ?: return;
        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
        val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
        tRespPackage.vecMsgID.add((0 - E_CLIENT_MSGID.E_GAME_ACTION.value()).toShort());
        tRespPackage.vecMsgData.add(tNotifyData.getVecData());
        for (player in playerDict.values) {
            tarsRouterPrx.doPush(player.uid, tRespPackage);
        }
    }

    private fun doMixture(tRespMessage: TRespMessage, playerDict: MutableMap<Long, TableBase.TablePlayer>) {
        val tNotifyData = tRespMessage.getTGameData().getTNotifyData();
        tNotifyData ?: return;
        val tRespData = tRespMessage.getTGameData().getTRespOneData();
        tRespData ?: return;

        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();

        val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
        val tNotifyPackage = TRespPackage(mutableListOf(), mutableListOf());

        tRespPackage.vecMsgID.add(E_CLIENT_MSGID.E_GAME_ACTION.value().toShort());
        tRespPackage.vecMsgData.add(tRespData.getVecData());

        tNotifyPackage.vecMsgID.add((0 - E_CLIENT_MSGID.E_GAME_ACTION.value()).toShort());
        tNotifyPackage.vecMsgData.add(tNotifyData.getVecData());
        for (player in playerDict.values) {
            if (player.chairIdx == this.chairIdx) {
                tarsRouterPrx.doPush(player.uid, tRespPackage);
            } else {
                tarsRouterPrx.doPush(player.uid, tNotifyPackage);
            }
        }
    }

    /*
     * @description 新的请求
     * =====================================
     * @author zoujian
     * @date 2018/7/25 14:10
     * @param
     * @return
     */
    private fun doNewGameMsg(msgID: Short) {
        val tReqMessage = TReqMessage()
        tReqMessage.nMsgID = msgID;
        tReqMessage.sTableNo = tableNO;
        gameBase.iGameMsgPrx.async_doRoomMessage(GameCallback(tableNO, gameBase), tReqMessage);
    }
}