package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientPrxMng
import com.bzw.tars.client.tars.tarsgame.*
import com.bzw.tars.comm.TarsUtilsKt
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng

/**
 * @创建者 zoujian
 * @创建时间 2018/8/20
 * @描述
 */
object MainGame {

    public fun doRoomReqGameMsg(msgID: Short, tableNo: String) {
        val tableBase = TableMng.getInstance().getTable(tableNo);
        tableBase ?: return;

        val tReqMessage = TReqRoomMsg()
        tReqMessage.nMsgID = msgID;
        tReqMessage.sTableNo = tableNo;

        when (msgID) {
            E_GAME_MSGID.GAMECREATE.value().toShort() -> tReqMessage.vecData = this.handleGameCreate(tableNo);
            E_GAME_MSGID.GAMESTART.value().toShort() -> tReqMessage.vecData = this.handleGameStart(tableNo);
//            E_GAME_MSGID.GAMETIMEOUT.value().toShort()
//            E_GAME_MSGID.GAMEFINISH.value().toShort()
//            E_GAME_MSGID.GAMEDISMISS.value().toShort()
        }

        val gamePrx = ClientPrxMng.getInstance().getClientPrx(tableBase.gameID.toString());
        if (gamePrx is IGameMessagePrx) {
            gamePrx.async_doRoomMessage(GameCallback(tableNo, tableBase.gameID), tReqMessage);
        }
    }

    private fun handleGameStart(tableNo: String): ByteArray? {
        val tableBase = TableMng.getInstance().getTable(tableNo);
        tableBase ?: return null
        tableBase.doStartGame();

        val cTableChairIdxMng = TableMng.getInstance().getInfoChairIdxMng(tableNo);
        cTableChairIdxMng ?: return null;

        val tGamgStart = TGamgStart(cTableChairIdxMng.getChairIdxPlayerList());
        return TarsUtilsKt.toByteArray(tGamgStart);
    }

    private fun handleGameCreate(tableNo: String): ByteArray? {
        val tableBase = TableMng.getInstance().getTable(tableNo);
        tableBase ?: return null


        val tGameCreate = TGameCreate(1, "");
        return TarsUtilsKt.toByteArray(tGameCreate);
    }
}
