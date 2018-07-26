package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientImpl
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.comm.TarsUtilsKt
import com.bzw.tars.client.kotlin.game.GameMng
import com.bzw.tars.server.jfgame.kotlin.database.player.InfoGame
import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng
import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TablePrivate
import com.bzw.tars.server.jfgame.kotlin.database.table.TableState
import com.bzw.tars.server.jfgame.kotlin.database.table.comm.InfoPlayer
import com.bzw.tars.server.tars.jfgameclientproto.*

/**
 * @创建者 zoujian
 * @创建时间 2018/7/16
 * @描述 处理来着Router的消息类
 */
class MainRouter {

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

        var res: E_RETCODE = E_RETCODE.E_TABLE_ENTER_ERROR;
        when (msgId) {
            E_CLIENT_MSGID.E_TABLE_ENTER.value().toShort() -> res = this.onEnterTable(uid, msgId, msgData);
            E_CLIENT_MSGID.E_TABLE_LEAVE.value().toShort() -> null;
            E_CLIENT_MSGID.E_TABLE_SIT_DOWN.value().toShort() -> null;
            E_CLIENT_MSGID.E_TABLE_STAND_UP.value().toShort() -> null;

            E_CLIENT_MSGID.E_TABLE_RECONNECT.value().toShort() -> null;
            E_CLIENT_MSGID.E_TABLE_PREPARE.value().toShort() -> res = this.onPlayerPrepare(uid);
            E_CLIENT_MSGID.E_START_BY_MASTER.value().toShort() -> null;

            E_CLIENT_MSGID.E_TABLE_DISMISS.value().toShort() -> null;
            E_CLIENT_MSGID.E_TABLE_VOTE_DISMISS.value().toShort() -> null;

            E_CLIENT_MSGID.E_GAME_ACTION.value().toShort() -> null;
            else -> System.err.println(String.format("MainRouter:onMessage,uid:%s,this error msgId:%s", uid, msgId));
        }

        if (E_RETCODE.E_COMMON_SUCCESS != res) {
            // 打包返回错误信息
            val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
            val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
            val tMsgRespErrorCode = TMsgRespErrorCode(res.value().toShort());
            tRespPackage.vecMsgID.add(E_CLIENT_MSGID.E_MSGID_ERROR.value().toShort());
            tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tMsgRespErrorCode));
            tarsRouterPrx.doPush(uid, tRespPackage);
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
        // 查找玩家信息
        val game = PlayerMng.getInstance().getInfoGame(uid);
        game ?: return true;

        // 查找玩家table信息
        val tableBase = TableMng.getInstance().getTable(game.tableNo);
        tableBase ?: return true;

        //查找座位信息
        val tablePlayer = tableBase.playerDict.get(uid);
        tablePlayer ?: return true;

        // 查找游戏开始信息
        // 游戏未开始（广播玩家离开，清除玩家数据，清除游戏桌数据）
        // 游戏已经开始（广播玩家离线）
        var msgData: ByteArray;
        var msgID: Short;
        if (tableBase.state == TableState.E_TABLE_INIT) {
            msgID = (0 - E_CLIENT_MSGID.E_TABLE_LEAVE.value()).toShort();
            msgData = TarsUtilsKt.toByteArray(TMsgNotifyLeaveTable(tablePlayer.chairNo))!!;

            tableBase.doLeaveTable(uid);
        } else {
            msgID = (0 - E_CLIENT_MSGID.E_PLAYER_DISCONNECT.value()).toShort();
            msgData = TarsUtilsKt.toByteArray(TMsgNotifyDisconnect(tablePlayer.chairNo))!!;
        }

        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
        val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
        tRespPackage.vecMsgID.add(msgID);
        tRespPackage.vecMsgData.add(msgData);
        for (v in tableBase.playerDict.values) {
            if (v.uid != uid) { // 非本人
                tarsRouterPrx.doPush(v.uid, tRespPackage);
            }
        }

        return false;
    }


    /*
     * @description 指令处理！
     * =====================================
     */

    /*
     * @description 玩家进入指定桌
     * =====================================
     * @author zoujian
     * @date 2018/7/17 15:53
     * @param
     * @return
     */
    private fun onEnterTable(uid: Long, msgId: Short, msgData: ByteArray): E_RETCODE {
        // 解码
        val tMsgReqEnterTable: TMsgReqEnterTable = TarsUtilsKt.toObject(msgData, TMsgReqEnterTable::class.java)!!;

        // 获取玩家数据
        val playerBase = PlayerMng.getInstance().getPlayer(uid);
        playerBase ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        // 检查玩家信息
        val infoGame = PlayerMng.getInstance().getInfoGame(uid);
        if (infoGame != null && infoGame.flag) {
            return E_RETCODE.E_PLAYER_IN_ROOM;
        }

        // 游戏桌不存在？同步请求服务端桌子信息
        if (TableMng.getInstance().getTable(tMsgReqEnterTable.getSTableNo()) == null) {
            this.doAddTable(tMsgReqEnterTable);
        }

        // 取游戏桌玩家数据
        val infoPlayer = TableMng.getInstance().getInfoPlayer(tMsgReqEnterTable.getSTableNo());
        infoPlayer ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 是否可以进入该桌
        if (infoPlayer.checkPlayerNum()) {
            return E_RETCODE.E_TABLE_IS_FULL;
        }

        // 新建一块共享内存
        val sharePlayerData = SharePlayerData(uid, tMsgReqEnterTable.getSTableNo());
        playerBase.addPlayerBase(InfoGame(sharePlayerData));
        infoPlayer.addPlayer(sharePlayerData);

        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();

        val tMsgRespEnterTable = TMsgRespEnterTable(11111, mutableListOf());
        val tMsgNotifyEnterTable = TMsgNotifyEnterTable();
        for (v in infoPlayer.getPlayerDict().values) {
            val tPlayerInfo = PlayerMng.getInstance().getTarsPlayerInfo(v.uid);
            tPlayerInfo ?: continue;
            if (v.uid == uid) { // 本人
                tMsgNotifyEnterTable.tPlayerInfo = tPlayerInfo;
            }
            tMsgRespEnterTable.vecPlayerInfo.add(tPlayerInfo);
        }

        for (v in infoPlayer.getPlayerDict().values) {
            val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
            if (v.uid == uid) { // 本人
                tRespPackage.vecMsgID.add(msgId);
                tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tMsgRespEnterTable));

            } else {
                tRespPackage.vecMsgID.add((0 - msgId).toShort());
                tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(tMsgNotifyEnterTable));
            }

            tarsRouterPrx.doPush(v.uid, tRespPackage);
        }


        return E_RETCODE.E_COMMON_SUCCESS;
    }

//    private fun onLeaveTable(uid: Long): E_RETCODE {
//
//    }

    /*
     * @description 处理游戏消息
     * =====================================
     * @author zoujian
     * @date 2018/7/16 15:14
     * @param
     * @return Unit
     */
    private fun onGameMessage(uid: Long, msgId: Short, msgData: ByteArray): E_RETCODE {
        // 取玩家数据
        val game = PlayerMng.getInstance().getInfoGame(uid);
        game ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        // 取游戏桌数据
        val tableBase = TableMng.getInstance().getTable(game.tableNo);
        tableBase ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 取游戏数据
        val gameBase = GameMng.getInstance().getGame(tableBase.gameID);
        gameBase ?: return E_RETCODE.E_GAME_NOT_EXIST;

        // 异步执行游戏请求

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
    private fun onPlayerPrepare(uid: Long): E_RETCODE {
        // 取当前玩家游戏数据
        val dataGame: SharePlayerData? = PlayerMng.getInstance().getInfoGame(uid);
        dataGame ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        // 玩家准备
        dataGame.state = 0;

        // 取游戏桌玩家数据
        val infoPlayer = TableMng.getInstance().getInfoPlayer(dataGame.tableNo);
        infoPlayer ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 广播玩家准备消息
        val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
        for (v in infoPlayer.getPlayerDict().values) {
            val tRespPackage = TRespPackage(mutableListOf(), mutableListOf());
            if (v.uid == uid) {  // 本人
                tRespPackage.vecMsgID.add(E_CLIENT_MSGID.E_TABLE_PREPARE.value().toShort());
                tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(TMsgCommPlaceholder()));
                tarsRouterPrx.doPush(v.uid, tRespPackage);
            } else {
                tRespPackage.vecMsgID.add((0 - E_CLIENT_MSGID.E_TABLE_PREPARE.value()).toShort());
                tRespPackage.vecMsgData.add(TarsUtilsKt.toByteArray(TMsgNotifyPrepare(dataGame.chairNo)));
                tarsRouterPrx.doPush(v.uid, tRespPackage);
            }
        }


        // 检查是否可以开桌
//        if (tableBase.canStartGame()) {
//
//        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }


    /////////////////////////////////////////////内部函数//////////////////////////////////////////////////////
    private fun doAddTable(tMsgReqEnterTable: TMsgReqEnterTable) {
        // 同步请求服务端桌子信息

        // 创建游戏桌
        var tableBase: TableBase? = null;
        when (tMsgReqEnterTable.nRoomType) {
            E_ROOM_TYPE.E_ROOM_CARD.value().toByte() -> {
                tableBase = TablePrivate(tMsgReqEnterTable.getSTableNo(), 111111, "111111");
            };
            else -> System.err.println("onEnterTable::nRoomType errr,nRoomType is " + tMsgReqEnterTable.nRoomType.toString());
        }


        if (tableBase != null) {
            TableMng.getInstance().addTable(tableBase.tableNo, tableBase);
        }
    }
}