package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientImpl
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.comm.TarsUtilsKt
import com.bzw.tars.server.jfgame.kotlin.database.game.GameMng
import com.bzw.tars.server.jfgame.kotlin.database.player.Game
import com.bzw.tars.server.jfgame.kotlin.database.player.InfoGame
import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TablePrivate
import com.bzw.tars.server.tars.jfgameclientproto.*
import com.qq.tars.protocol.tars.TarsInputStream
import com.qq.tars.protocol.tars.TarsOutputStream

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
            E_CLIENT_MSGID.E_TABLE_PREPARE.value().toShort() -> null;

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
     * @return
     */
    fun onOffLine(): Unit {

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

        // 游戏桌不存在？同步请求服务端桌子信息
        if (TableMng.getInstance().getTable(tMsgReqEnterTable.getSTableNo()) == null) {
            this.doAddTable(tMsgReqEnterTable);
        }

        // 取游戏桌数据
        val tableBase = TableMng.getInstance().getTable(tMsgReqEnterTable.getSTableNo());
        tableBase ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 游戏桌成功将该玩家加入
        val res = tableBase.doEnterTable(uid, tMsgReqEnterTable.nChairNo);
        if (E_RETCODE.E_COMMON_SUCCESS == res) { // 加入成功
            val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();


            val tMsgRespEnterTable = TMsgRespEnterTable(11111, PlayerMng.getInstance().getTarsPlayerInfo(uid), mutableListOf());
            val tMsgNotifyEnterTable = TMsgNotifyEnterTable();
            for (v in tableBase.playerDict.values) {
                val tPlayerInfo = PlayerMng.getInstance().getTarsPlayerInfo(v.uid);
                tPlayerInfo ?: continue;
                if (v.uid == uid) { // 本人
                    tMsgNotifyEnterTable.tPlayerInfo = tPlayerInfo;
                } else {
                    tMsgRespEnterTable.vecPlayerInfo.add(tPlayerInfo);
                }
            }

            for (v in tableBase.playerDict.values) {
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
        }

        return res;
    }

    /*
     * @description 处理游戏消息
     * =====================================
     * @author zoujian
     * @date 2018/7/16 15:14
     * @param
     * @return Unit
     */
    private fun onGameMessage(): Unit {

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
        // 玩家数据
        val game: Game? = PlayerMng.getInstance().getInfoGame(uid);
        game ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        // 游戏桌数据
        val tableBase = TableMng.getInstance().getTable(game.tableNo);
        tableBase ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 玩家准备

        // 广播玩家准备消息

        // 检查是否可以开桌
        if (tableBase.canStartGame()) {

        }

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