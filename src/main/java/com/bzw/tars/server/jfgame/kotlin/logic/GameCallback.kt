package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientImpl
import com.bzw.tars.client.kotlin.game.GameBase
import com.bzw.tars.client.tars.jfgameclientproto.E_CLIENT_MSGID
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.client.tars.tarsgame.*
import com.bzw.tars.comm.TarsUtilsKt
import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng
import com.bzw.tars.server.jfgame.kotlin.timer.TimerMng

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
        System.err.println("IGameMessagePrxCallback:" + ex.toString());
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

            if (tRespMessage.nTimeout > 0) {
                TimerMng.getInstance().addTimer(this.tableNO, this.gameBase.gameID, tRespMessage.getNTimeout().toInt());
            } else if (tRespMessage.nTimeout < 0) {
                TimerMng.getInstance().removeTimer(this.tableNO);
            }
        } else {
//            System.err.println("TableGameInfo Callback error !!!");
        }
    }

    private fun doGameMsgNotify(tRespMessage: TRespMessage) {
        val infoPlayer = TableMng.getInstance().getInfoPlayer(this.tableNO);
        infoPlayer ?: return;

        for (gameData in tRespMessage.vecGameData){
            when (gameData.eMsgType) {
                EGameMsgType.E_RESPONE_DATA.value().toByte() -> this.doRespOne(gameData, infoPlayer.getPlayerDict());
                EGameMsgType.E_RESPALL_DATA.value().toByte() -> this.doRespAll(gameData, infoPlayer.getPlayerDict());
                EGameMsgType.E_NOTIFY_DATA.value().toByte() -> this.doNotify(gameData, infoPlayer.getPlayerDict());
                EGameMsgType.E_MIXTURE_DATA.value().toByte() -> this.doMixture(gameData, infoPlayer.getPlayerDict());
                EGameMsgType.E_NONE_DATA.value().toByte() -> null;
                else -> System.err.println("TableGameInfo msgType error !!!");
            }
        }
    }

    /*
     * @description 处理转发
     * =====================================
     * @author zoujian
     * @date 2018/7/25 15:33
     */

    private fun doRespOne(tGameData: TGameData, playerDict: MutableMap<Long, SharePlayerData>) {
        val tRespData = tGameData.tRespOneData;
        tRespData ?: return;
        for (player in playerDict.values) {
            if (player.chairIdx == this.chairIdx) {
                val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
                val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
                tRespPackage.vecMsgID.add(E_CLIENT_MSGID.E_GAME_ACTION.value().toShort());
                tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tRespData));
                tarsRouterPrx.doPush(player.uid, tRespPackage);
                break;
            }
        }
    }

    private fun doRespAll(tGameData: TGameData, playerDict: MutableMap<Long, SharePlayerData>) {
        val tListData = tGameData.getVecRespAllData();
        tListData ?: return;
        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
        for (player in playerDict.values) {
            if (player.chairIdx >= 0 && player.chairIdx < tListData.size) {
                val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
                tRespPackage.vecMsgID.add((0 - E_CLIENT_MSGID.E_GAME_ACTION.value()).toShort());
                tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tListData.get(player.chairIdx.toInt())));
                tarsRouterPrx.doPush(player.uid, tRespPackage);
            }
        }
    }

    private fun doNotify(tGameData: TGameData, playerDict: MutableMap<Long, SharePlayerData>) {
        val tNotifyData = tGameData.getTNotifyData();
        tNotifyData ?: return;
        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
        val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
        tRespPackage.vecMsgID.add((0 - E_CLIENT_MSGID.E_GAME_ACTION.value()).toShort());
        tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tNotifyData));
        for (player in playerDict.values) {
            tarsRouterPrx.doPush(player.uid, tRespPackage);
        }
    }

    private fun doMixture(tGameData: TGameData, playerDict: MutableMap<Long, SharePlayerData>) {
        val tNotifyData = tGameData.getTNotifyData();
        tNotifyData ?: return;
        val tRespData = tGameData.getTRespOneData();
        tRespData ?: return;

        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();

        val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
        val tNotifyPackage = TRespPackage(mutableListOf(), mutableListOf());

        tRespPackage.vecMsgID.add(E_CLIENT_MSGID.E_GAME_ACTION.value().toShort());
        tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tRespData));

        tNotifyPackage.vecMsgID.add((0 - E_CLIENT_MSGID.E_GAME_ACTION.value()).toShort());
        tNotifyPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tNotifyData));
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
        val tReqMessage = TReqRoomMsg()
        tReqMessage.nMsgID = msgID;
        tReqMessage.sTableNo = tableNO;

        when (msgID) {
            E_GAME_MSGID.GAMESTART.value().toShort() -> tReqMessage.vecData = this.handleGameStart(this.tableNO);
        }

        gameBase.iGameMsgPrx.async_doRoomMessage(this, tReqMessage);
    }


    private fun handleGameStart(tableNO: String): ByteArray? {
        TableMng.getInstance().initChairIdx(tableNO);
        val cTableChairIdxMng = TableMng.getInstance().getInfoChairIdxMng(this.tableNO);
        cTableChairIdxMng ?: return null;

        val tGamgStart = TGamgStart(cTableChairIdxMng.getChairIdxPlayerList());
        return TarsUtilsKt.toByteArray(tGamgStart);
    }
}