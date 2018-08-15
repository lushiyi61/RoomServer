package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientPrxMng
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.comm.TarsUtilsKt
import com.bzw.tars.client.tars.tarsgame.*
import com.bzw.tars.comm.TarsStructHandler.HandlerRouterReq
import com.bzw.tars.comm.TarsStructHandler.HandlerRouterResp
import com.bzw.tars.server.jfgame.kotlin.database.player.CPlayerGameInfo
import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng
import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TableState
import com.bzw.tars.server.jfgame.kotlin.database.table.comm.*
import com.bzw.tars.server.tars.jfgameclientproto.*
import kotlin.reflect.KFunction

/**
 * @创建者 zoujian
 * @创建时间 2018/7/16
 * @描述 处理来着Router的消息类
 */
class MainRouter {

    private val msgReqRouterMap: MutableMap<Short, KFunction<out E_RETCODE>> = mutableMapOf()

    constructor() {
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_ENTER.value().toShort(), this::onEnterTable);
//        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_LEAVE.value().toShort(), this);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_SIT_DOWN.value().toShort(), this::onSitDown);
//        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_STAND_UP.value().toShort(), this);
//        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_RECONNECT.value().toShort(), this);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_PREPARE.value().toShort(), this::onPlayerPrepare);
//        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_START_BY_MASTER.value().toShort(), this);
//        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_DISMISS.value().toShort(), this);
//        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_VOTE_DISMISS.value().toShort(), this);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_GAME_ACTION.value().toShort(), this::onGameMessage);


    }

    /*
     * @description 处理来自Router的玩家消息
     * =====================================
     * @author zoujian
     * @date 2018/7/16 13:45
     * @param uid:玩家ID
     * @param msgId:消息ID
     * @param msgData:消息数据
     * @return
     */
    fun onMessage(uid: Long, msgId: Short, msgData: ByteArray): Unit {
        println(String.format("MainRouter:onMessage,uid:%s,msgId:%s", uid, msgId));
        var res: E_RETCODE = E_RETCODE.E_PROTOCOL_ERROR;

        val msgStruct = HandlerRouterReq.handlerReq(msgId, msgData);
        if (msgStruct != null) {
            val callFun = this.msgReqRouterMap.get(msgId);
            if (callFun is KFunction) {
                res = callFun.call(uid, msgId, msgStruct);
            }
        }

        if (E_RETCODE.E_COMMON_SUCCESS != res) {
            // 打包返回错误信息
            val tMsgRespErrorCode = TMsgRespErrorCode(res.value().toShort());
            val tRespPackage = HandlerRouterResp.handlerResp(res.value().toShort(), tMsgRespErrorCode);
            val clientPrx = ClientPrxMng.getInstance().getRouterPrx();
            clientPrx.doPush(uid, tRespPackage);
        }
    }

    /*
     * @description 处理来自Router的玩家断线消息
     * =====================================
     * @author zoujian
     * @date 2018/7/16 13:46
     * @param
     * @return 是否应该删除玩家信息
     */
    fun onOffLine(uid: Long): Boolean {
        var res: Boolean = true;
        // 查找玩家信息
        val sharePlayerData = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return res;

        // 查找玩家table信息
        val tableBase = TableMng.getInstance().getTable(sharePlayerData.tableNo);
        tableBase ?: return res;

        // 取游戏桌玩家数据(准备广播)
        val cTablePlayerMng = TableMng.getInstance().getInfoPlayer(sharePlayerData.tableNo);
        cTablePlayerMng ?: return res;

        // 查找游戏开始信息
        // 游戏未开始（广播玩家离开，清除玩家数据，清除游戏桌数据）
        // 游戏已经开始（广播玩家离线）
        var msgData: ByteArray;
        var msgID: Short;
        if (tableBase.state == TableState.E_TABLE_INIT ||
                sharePlayerData.chairNo <= 0) {
            msgID = (0 - E_CLIENT_MSGID.E_TABLE_LEAVE.value()).toShort();
            msgData = TarsUtilsKt.toByteArray(TMsgNotifyLeaveTable(sharePlayerData.chairNo))!!;

            cTablePlayerMng.removePlayer(uid);
            // 清理座位
            if (sharePlayerData.chairNo > 0) {
                val cTableChairNoMng = TableMng.getInstance().getInfoChairNoMng(sharePlayerData.tableNo);
                if (cTableChairNoMng != null) {
                    cTableChairNoMng.removePlayer(sharePlayerData);
                }
            }
        } else {
            msgID = (0 - E_CLIENT_MSGID.E_PLAYER_DISCONNECT.value()).toShort();
            msgData = TarsUtilsKt.toByteArray(TMsgNotifyDisconnect(sharePlayerData.chairNo))!!;
            res = false;
        }


        val tarsRouterPrx = ClientPrxMng.getInstance().getRouterPrx();
        val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
        tRespPackage.vecMsgID.add(msgID);
        tRespPackage.vecMsgData.add(msgData);
        for (v in cTablePlayerMng.getPlayerDict().values) {
            if (v.uid != uid) { // 非本人
                tarsRouterPrx.doPush(v.uid, tRespPackage);
            }
        }

        return res;
    }


    //=====================================


    /*
     * @description 玩家进入指定桌
     * =====================================
     * @author zoujian
     * @date 2018/7/17 15:53
     * @param
     * @return
     */
    fun onEnterTable(uid: Long, msgId: Short, msgStruct: TMsgReqEnterTable): E_RETCODE {
        // 获取玩家数据
        val playerBase = PlayerMng.getInstance().getPlayer(uid);
        playerBase ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        // 检查玩家信息
        val playerData = PlayerMng.getInstance().getInfoGame(uid);
        if (playerData != null) {
            return E_RETCODE.E_PLAYER_IN_ROOM;
        }

        // 游戏桌不存在？同步请求服务端桌子信息
        if (TableMng.getInstance().getTable(msgStruct.getSTableNo()) == null) {
            this.doCreateTable(msgStruct);
        }

        // 取游戏桌玩家数据
        val cTablePlayerMng = TableMng.getInstance().getInfoPlayer(msgStruct.getSTableNo());
        cTablePlayerMng ?: return E_RETCODE.E_TABLE_NOT_EXIST;
        val cTableChairNoMng = TableMng.getInstance().getInfoChairNoMng(msgStruct.getSTableNo());
        cTableChairNoMng ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 是否可以进入该桌
        if (cTablePlayerMng.checkPlayerNum()) {
            return E_RETCODE.E_TABLE_IS_FULL;
        }

        // 新建一块共享内存
        val sharePlayerData = SharePlayerData(uid, msgStruct.getSTableNo());
        playerBase.addPlayerBase(CPlayerGameInfo(sharePlayerData));
        cTablePlayerMng.addPlayer(sharePlayerData);

        // 是否自动坐下
        if (!cTableChairNoMng.checkChairNum()) {
            // 获取座位号
            val chairNo = cTableChairNoMng.chooseTheSeat(msgStruct.nChairNo);
            sharePlayerData.chairNo = chairNo;
            cTableChairNoMng.addPlayer(sharePlayerData);
        }

        // 准备转发
        val tMsgRespEnterTable = TMsgRespEnterTable(11111, mutableListOf());
        val tMsgNotifyEnterTable = TMsgNotifyEnterTable();
        for (v in cTablePlayerMng.getPlayerDict().values) {
            val tPlayerInfo = PlayerMng.getInstance().getTarsPlayerInfo(v.uid);
            tPlayerInfo ?: continue;
            if (v.uid == uid) { // 本人
                tMsgNotifyEnterTable.tPlayerInfo = tPlayerInfo;
            }
            tMsgRespEnterTable.vecPlayerInfo.add(tPlayerInfo);
        }

        val clientPrx = ClientPrxMng.getInstance().getRouterPrx();
        for (v in cTablePlayerMng.getPlayerDict().values) {
            var tRespPackage = TRespPackage();
            if (v.uid == uid) { // 本人
                tRespPackage = HandlerRouterResp.handlerResp(msgId, tMsgRespEnterTable);
            } else {
                tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), tMsgNotifyEnterTable);
            }
            clientPrx.doPush(v.uid, tRespPackage);
        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }


    fun onSitDown(uid: Long, msgId: Short, msgData: ByteArray): E_RETCODE {


        return E_RETCODE.E_COMMON_SUCCESS;
    }

    /*
     * @description 处理游戏消息
     * =====================================
     * @author zoujian
     * @date 2018/7/16 15:14
     * @param
     * @return Unit
     */
    fun onGameMessage(uid: Long, msgId: Short, msgStruct: TReqClientMsg): E_RETCODE {
        // 取玩家数据
        val sharePlayerData = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        // 取游戏桌数据
        val tableBase = TableMng.getInstance().getTable(sharePlayerData.tableNo);
        tableBase ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 异步执行游戏请求
        val gamePrx = ClientPrxMng.getInstance().getClientPrx(tableBase.gameID.toString());
        if (gamePrx is IGameMessagePrx) {
            val tReqRoomTranspondMsg = TReqRoomTranspondMsg(msgStruct.nMsgID, sharePlayerData.tableNo, sharePlayerData.chairIdx.toShort(), msgStruct.vecData);
            gamePrx.async_doClientMessage(GameCallback(sharePlayerData.tableNo, tableBase.gameID), tReqRoomTranspondMsg);
        }
        return E_RETCODE.E_COMMON_SUCCESS;
    }


    /*
     * @description 玩家请求准备
     * =====================================
     * @author zoujian
     * @date 2018/7/19 14:41
     * @param
     * @return
     */
    fun onPlayerPrepare(uid: Long, msgId: Short, msgStruct: TMsgCommPlaceholder): E_RETCODE {
        // 取当前玩家游戏数据
        val sharePlayerData: SharePlayerData? = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        if (sharePlayerData.chairNo <= 0) {
            return E_RETCODE.E_PLAYER_NOT_SIT;
        }

        // 玩家准备
        sharePlayerData.state = E_PLAYER_STATE.E_PLAYER_PREPARE.value().toByte();

        // 取游戏桌玩家数据
        val infoPlayer = TableMng.getInstance().getInfoPlayer(sharePlayerData.tableNo);
        infoPlayer ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 广播玩家准备消息
        val clientPrx = ClientPrxMng.getInstance().getRouterPrx();
        for (playerData in infoPlayer.getPlayerDict().values) {
            var tRespPackage = TRespPackage();
            if (playerData.uid == uid) {  // 本人
                tRespPackage = HandlerRouterResp.handlerResp(msgId, TMsgCommPlaceholder());
            } else {
                tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), TMsgNotifyPrepare(sharePlayerData.chairNo));
            }
            clientPrx.doPush(playerData.uid, tRespPackage);
        }

//        // 检查是否可以开桌
//        val tableBase = TableMng.getInstance().getTable(sharePlayerData.tableNo);
//        if (tableBase != null && tableBase.canStartGame()) {
//            this.doCreateGame(tableBase);
//        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }


    /////////////////////////////////////////////内部函数//////////////////////////////////////////////////////

    fun doCreateTable(tMsgReqEnterTable: TMsgReqEnterTable) {
        // 同步请求服务端桌子信息

        // 创建游戏桌
        var tableBase: TableBase = TableBase(tMsgReqEnterTable.getSTableNo(), 111111, "111111");

        val cTableChairNoMng = CTableChairNoMng(6);
        val cTableChairIdxMng = CTableChairIdxMng();
        val cTableGameInfo = CTableGameInfo(TableGameInfo(1));
        val cTablePlayerMng = CTablePlayerMng(6);

        tableBase.addTableBase(cTableChairNoMng);
        tableBase.addTableBase(cTableChairIdxMng);
        tableBase.addTableBase(cTableGameInfo);
        tableBase.addTableBase(cTablePlayerMng);
        when (tMsgReqEnterTable.nRoomType) {
            E_ROOM_TYPE.E_ROOM_CARD.value().toByte() -> {
//                tableBase = TablePrivate(tMsgReqEnterTable.getSTableNo(), 111111, "111111");
            };
            else -> System.err.println("onEnterTable::nRoomType errr,nRoomType is " + tMsgReqEnterTable.nRoomType.toString());
        }



        TableMng.getInstance().addTable(tableBase.tableNo, tableBase);

    }


    fun doCreateGame(tableBase: TableBase) {
        // 请求创建游戏
        val gamePrx = ClientPrxMng.getInstance().getClientPrx(tableBase.gameID.toString());
        if (gamePrx is IGameMessagePrx) {
            val tGameCreate = TGameCreate(1, "");

            val tReqRoomMsg = TReqRoomMsg()
            tReqRoomMsg.nMsgID = E_GAME_MSGID.GAMECREATE.value().toShort()
            tReqRoomMsg.sTableNo = tableBase.tableNo;
            tReqRoomMsg.vecData = TarsUtilsKt.toByteArray(tGameCreate);
            gamePrx.async_doRoomMessage(GameCallback(tableBase.tableNo, tableBase.gameID, (-1).toByte()), tReqRoomMsg)
        }
    }
}