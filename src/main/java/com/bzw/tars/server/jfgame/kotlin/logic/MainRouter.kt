package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientPrxMng
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.client.tars.tarsgame.*
import com.bzw.tars.comm.TarsStructHandler.HandlerRouterReq
import com.bzw.tars.comm.TarsStructHandler.HandlerRouterResp
import com.bzw.tars.server.jfgame.kotlin.database.player.CPlayerGameInfo
import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng
import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.jfgame.kotlin.database.table.roomOfCard.CTableDismissMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TableType
import com.bzw.tars.server.jfgame.kotlin.database.table.comm.*
import com.bzw.tars.server.jfgame.kotlin.timer.TimerMng
import com.bzw.tars.server.tars.jfgameclientproto.*
import kotlin.reflect.KFunction

/**
 * @创建者 zoujian
 * @创建时间 2018/7/16
 * @描述 处理来着Router的消息类
 */
class MainRouter {
    // 消息注册Map
    private val msgReqRouterMap: MutableMap<Short, KFunction<out E_RETCODE>> = mutableMapOf()

    /*
     * @description 初始化时，进行消息注册
     * =====================================
     * @author zoujian
     * @date 2018/8/15 11:47
     */
    public constructor() {
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_ENTER.value().toShort(), this::onEnterTable);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_LEAVE.value().toShort(), this::onTableLeave);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_SIT_DOWN.value().toShort(), this::onSitDown);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_STAND_UP.value().toShort(), this::onTableStandUp);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_PLAYER_RECONNECT.value().toShort(), this::onTableReconnect);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_PREPARE.value().toShort(), this::onPlayerPrepare);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_START_BY_MASTER.value().toShort(), this::onStartByMaster);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_DISMISS.value().toShort(), this::onTableDismiss);
        this.msgReqRouterMap.put(E_CLIENT_MSGID.E_TABLE_VOTE_DISMISS.value().toShort(), this::onTableVoteDismiss);

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
    public fun onMessage(uid: Long, msgId: Short, msgData: ByteArray): Unit {
        println(String.format("MainRouter:onMessage,uid:%s,msgId:%s", uid, msgId));
        var res: E_RETCODE = E_RETCODE.E_PROTOCOL_ERROR;

        // 协议解析 及处理
        val msgStruct = HandlerRouterReq.handlerReq(msgId, msgData);
        if (msgStruct != null) {
            val callFun = this.msgReqRouterMap.get(msgId);
            if (callFun is KFunction) {
                res = callFun.call(uid, msgId, msgStruct);
            }
        }

        // 出错 返回错误码
        if (E_RETCODE.E_COMMON_SUCCESS != res) {
            val tMsgRespErrorCode = TMsgRespErrorCode(res.value().toShort());
            val tRespPackage = HandlerRouterResp.handlerResp(E_CLIENT_MSGID.E_MSGID_ERROR.value().toShort(), tMsgRespErrorCode);
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
        var tRespPackage = TRespPackage();
        if (tableBase.canLeaveTable() || sharePlayerData.chairNo <= 0) {
            tRespPackage = HandlerRouterResp.handlerResp((0 - E_CLIENT_MSGID.E_TABLE_LEAVE.value()).toShort(), TMsgNotifyWho(sharePlayerData.chairNo));

            cTablePlayerMng.removePlayer(uid);
            // 清理座位
            if (sharePlayerData.chairNo > 0) {
                val cTableChairNoMng = TableMng.getInstance().getInfoChairNoMng(sharePlayerData.tableNo);
                if (cTableChairNoMng != null) {
                    cTableChairNoMng.removePlayer(sharePlayerData);
                }
            }
        } else {
            tRespPackage = HandlerRouterResp.handlerResp((0 - E_CLIENT_MSGID.E_PLAYER_DISCONNECT.value()).toShort(), TMsgNotifyWho(sharePlayerData.chairNo));
            res = false;
        }

        val clientPrx = ClientPrxMng.getInstance().getRouterPrx();
        for (playerData in cTablePlayerMng.getPlayerDict().values) {
            if (playerData.uid != uid) { // 非本人
                clientPrx.doPush(playerData.uid, tRespPackage);
            }
        }

        return res;
    }


    //===================================================================================


    /*
     * @description 玩家进入指定桌
     * =====================================
     * @author zoujian
     * @date 2018/7/17 15:53
     * @param
     * @return E_RETCODE
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
            if (!cTableChairNoMng.addPlayer(chairNo, sharePlayerData)) {
                return E_RETCODE.E_SEAT_IS_TAKEN;
            }
        }

        // 准备转发
        val tMsgRespEnterTable = TMsgRespEnterTable(11111, mutableListOf());
        val tMsgNotifyEnterTable = TMsgNotifyEnterTable();
        for (playerData1 in cTablePlayerMng.getPlayerDict().values) {
            val tPlayerInfo = PlayerMng.getInstance().getTarsPlayerInfo(playerData1.uid);
            tPlayerInfo ?: continue;
            if (playerData1.uid == uid) { // 本人
                tMsgNotifyEnterTable.tPlayerInfo = tPlayerInfo;
            }
            tMsgRespEnterTable.vecPlayerInfo.add(tPlayerInfo);
        }

        val clientPrx = ClientPrxMng.getInstance().getRouterPrx();
        for (playerData1 in cTablePlayerMng.getPlayerDict().values) {
            var tRespPackage = TRespPackage();
            if (playerData1.uid == uid) { // 本人
                tRespPackage = HandlerRouterResp.handlerResp(msgId, tMsgRespEnterTable);
            } else {
                tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), tMsgNotifyEnterTable);
            }
            clientPrx.doPush(playerData1.uid, tRespPackage);
        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }

    /*
     * @description 玩家请求离开
     * =====================================
     * @author zoujian
     * @date 2018/8/15 17:58
     * @param
     * @return
     */
    fun onTableLeave(uid: Long, msgId: Short, msgStruct: TMsgCommPlaceholder): E_RETCODE {
        // 获取玩家数据
        val playerData = PlayerMng.getInstance().getInfoGame(uid);
        playerData ?: return E_RETCODE.E_PLAYER_IN_ROOM;

        // 是否可以离开

        // 离开

        return E_RETCODE.E_COMMON_SUCCESS;
    }

    fun onSitDown(uid: Long, msgId: Short, msgStruct: TMsgReqSitDown): E_RETCODE {
        val sharePlayerData = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return E_RETCODE.E_PLAYER_NOT_EXIST;


        val cTablePlayerMng = TableMng.getInstance().getInfoPlayer(sharePlayerData.tableNo);
        cTablePlayerMng ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 取游戏桌座位数据
        val cTableChairNoMng = TableMng.getInstance().getInfoChairNoMng(sharePlayerData.tableNo);
        cTableChairNoMng ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        if (!cTableChairNoMng.checkChairNum()) {
            // 获取座位号
            val chairNo = cTableChairNoMng.chooseTheSeat(msgStruct.nChairNo);
            if (!cTableChairNoMng.addPlayer(chairNo, sharePlayerData)) {
                return E_RETCODE.E_SEAT_IS_TAKEN;
            }
        }

        val clientPrx = ClientPrxMng.getInstance().getRouterPrx();
        for (playerData1 in cTablePlayerMng.getPlayerDict().values) {
            var tRespPackage = TRespPackage();
            if (playerData1.uid == uid) { // 本人
                tRespPackage = HandlerRouterResp.handlerResp(msgId, TMsgRespSitDown(sharePlayerData.chairNo));
            } else {
                tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), TMsgNotifySitDown(sharePlayerData.uid, sharePlayerData.chairNo));
            }
            clientPrx.doPush(playerData1.uid, tRespPackage);
        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }

    fun onTableStandUp(uid: Long, msgId: Short, msgStruct: TMsgCommPlaceholder): E_RETCODE {
        // 获取玩家数据
        val playerData = PlayerMng.getInstance().getInfoGame(uid);
        playerData ?: return E_RETCODE.E_PLAYER_IN_ROOM;



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
                tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), TMsgNotifyWho(sharePlayerData.chairNo));
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


    fun onStartByMaster(uid: Long, msgId: Short, msgStruct: TMsgCommPlaceholder): E_RETCODE {
        // 获取玩家数据
        val sharePlayerData = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return E_RETCODE.E_PLAYER_IN_ROOM;

        // 玩家是否是房主


        // 检查是否可以开桌
        val tableBase = TableMng.getInstance().getTable(sharePlayerData.tableNo);
        if (tableBase != null && tableBase.canStartGame()) {
            MainGame.doRoomReqGameMsg(E_GAME_MSGID.GAMECREATE.value().toShort(), tableBase.tableNo)
        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }

    fun onTableDismiss(uid: Long, msgId: Short, msgStruct: TMsgCommPlaceholder): E_RETCODE {
        // 获取玩家数据
        val sharePlayerData = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return E_RETCODE.E_PLAYER_IN_ROOM;

        val cTableDismissMng = TableMng.getInstance().getInfoDismissMng(sharePlayerData.tableNo);
        cTableDismissMng ?: return E_RETCODE.E_NOT_ALLOW_DISMISS;

        if (cTableDismissMng.initiateVote(uid)) {
            val timerBase = TimerMng.getInstance().getTimer(sharePlayerData.tableNo);
            timerBase ?: return E_RETCODE.E_NOT_ALLOW_DISMISS;

            timerBase.suspendTimeBase();


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
                    tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), TMsgNotifyWho(sharePlayerData.chairNo));
                }
                clientPrx.doPush(playerData.uid, tRespPackage);
            }
        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }

    fun onTableVoteDismiss(uid: Long, msgId: Short, msgStruct: TMsgReqVoteDismiss): E_RETCODE {
        // 获取玩家数据
        val sharePlayerData = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return E_RETCODE.E_PLAYER_IN_ROOM;

        val cTableDismissMng = TableMng.getInstance().getInfoDismissMng(sharePlayerData.tableNo);
        cTableDismissMng ?: return E_RETCODE.E_NOT_ALLOW_DISMISS;

        if (cTableDismissMng.doVote(uid, msgStruct.support)) {
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
                    tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), TMsgNotifyVoteDismiss(msgStruct.support, sharePlayerData.chairNo));
                }
                clientPrx.doPush(playerData.uid, tRespPackage);
            }

            if (cTableDismissMng.checkDismissFinish()){
                val dismissResult = cTableDismissMng.getDismissResult()
                for (playerData in infoPlayer.getPlayerDict().values) {
                    var tRespPackage = TRespPackage();
                    tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), TMsgNotifyDismissResult(dismissResult, 111));
                    clientPrx.doPush(playerData.uid, tRespPackage);
                }

                val timerBase = TimerMng.getInstance().getTimer(sharePlayerData.tableNo);
                if (dismissResult && timerBase != null){
                    timerBase.recoverTimeBase();
                }
            }
        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }

    fun onTableReconnect(uid: Long, msgId: Short, msgStruct: TMsgCommPlaceholder): E_RETCODE {
        // 获取玩家数据
        val sharePlayerData = PlayerMng.getInstance().getInfoGame(uid);
        sharePlayerData ?: return E_RETCODE.E_PLAYER_IN_ROOM;

        val cTablePlayerMng = TableMng.getInstance().getInfoPlayer(sharePlayerData.tableNo);
        cTablePlayerMng ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        val tableBase = TableMng.getInstance().getTable(sharePlayerData.tableNo);
        tableBase ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 组装数据
        val tMsgNotifyWho = TMsgNotifyWho(sharePlayerData.chairNo);
        val tMsgRespEnterTable = TMsgRespEnterTable(11111, mutableListOf());
        for (playerData1 in cTablePlayerMng.getPlayerDict().values) {
            val tPlayerInfo = PlayerMng.getInstance().getTarsPlayerInfo(playerData1.uid);
            tPlayerInfo ?: continue;
            tMsgRespEnterTable.vecPlayerInfo.add(tPlayerInfo);
        }

        // 发数据
        val clientPrx = ClientPrxMng.getInstance().getRouterPrx();
        for (playerData1 in cTablePlayerMng.getPlayerDict().values) {
            var tRespPackage: TRespPackage;
            if (playerData1.uid == uid) { // 本人
                tRespPackage = HandlerRouterResp.handlerResp(msgId, tMsgRespEnterTable);
            } else {
                tRespPackage = HandlerRouterResp.handlerResp((0 - msgId).toShort(), tMsgNotifyWho);
            }
            clientPrx.doPush(playerData1.uid, tRespPackage);
        }



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


    /////////////////////////////////////////////内部函数//////////////////////////////////////////////////////
    private fun doCreateTable(tMsgReqEnterTable: TMsgReqEnterTable) {
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
            TableType.E_ROOM_CARD.ordinal.toByte() -> {
                tableBase.addTableBase(CTableDismissMng());
//                tableBase = TablePrivate(tMsgReqEnterTable.getSTableNo(), 111111, "111111");
            };
            else -> System.err.println("onEnterTable::nRoomType errr,nRoomType is " + tMsgReqEnterTable.nRoomType.toString());
        }



        TableMng.getInstance().addTable(tableBase.tableNo, tableBase);

    }


    private fun doCanLeaveTable(uid: Long) {

    }
}