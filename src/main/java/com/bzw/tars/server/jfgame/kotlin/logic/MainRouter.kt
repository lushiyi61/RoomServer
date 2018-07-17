package com.bzw.tars.server.jfgame.kotlin.logic

import com.bzw.tars.client.kotlin.ClientImpl
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage
import com.bzw.tars.server.jfgame.kotlin.database.game.GameMng
import com.bzw.tars.server.jfgame.kotlin.database.player.Game
import com.bzw.tars.server.jfgame.kotlin.database.player.InfoGame
import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng
import com.bzw.tars.server.jfgame.kotlin.database.table.TablePrivate
import com.bzw.tars.server.tars.jfgameclientproto.E_CLIENT_MSGID
import com.bzw.tars.server.tars.jfgameclientproto.E_RETCODE
import com.bzw.tars.server.tars.jfgameclientproto.TMsgReqEnterTable
import com.qq.tars.protocol.tars.TarsInputStream

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
        when (msgId as E_CLIENT_MSGID) {
            E_CLIENT_MSGID.E_TABLE_ENTER -> null;
            E_CLIENT_MSGID.E_TABLE_LEAVE -> null;
            E_CLIENT_MSGID.E_TABLE_SIT_DOWN -> null;
            E_CLIENT_MSGID.E_TABLE_STAND_UP -> null;

            E_CLIENT_MSGID.E_TABLE_RECONNECT -> null;
            E_CLIENT_MSGID.E_TABLE_PREPARE -> null;

            E_CLIENT_MSGID.E_TABLE_DISMISS -> null;
            E_CLIENT_MSGID.E_TABLE_VOTE_DISMISS -> null;

            E_CLIENT_MSGID.E_GAME_ACTION -> null;
            else -> System.err.println(String.format("MainRouter:onMessage,uid:%s,this error msgId:%s", uid, msgId));
        }

        if (E_RETCODE.E_COMMON_SUCCESS != res) {
            // 打包返回错误信息
            val tarsRouterPrx = ClientImpl.getInstance().getDoPushPrx();
            val tRespPackage = TRespPackage();
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
     * @description 玩家进入指定桌
     * =====================================
     * @author zoujian
     * @date 2018/7/17 15:53
     * @param
     * @return
     */
    private fun onEnterTable(uid: Long, msgData: ByteArray): E_RETCODE {
        // 解码
        val tMsgReqEnterTable: TMsgReqEnterTable = TMsgReqEnterTable()
        val tarsInputStream: TarsInputStream = TarsInputStream(msgData)
        tMsgReqEnterTable.readFrom(tarsInputStream)

        // 游戏桌不存在？同步请求服务端桌子信息
        if (TableMng.getInstance().getTable(tMsgReqEnterTable.getSTableNo()) == null) {
            // 同步请求服务端桌子信息

            // 创建游戏桌
//            val tablePrivate = TablePrivate();

        }

        // 游戏桌数据
        val tableBase = TableMng.getInstance().getTable(tMsgReqEnterTable.getSTableNo());
        tableBase ?: return E_RETCODE.E_TABLE_NOT_EXIST;

        // 玩家数据
        val playerBase = PlayerMng.getInstance().getPlayer(uid);
        playerBase ?: return E_RETCODE.E_PLAYER_NOT_EXIST;
        // 游戏数据
        val gameBase = GameMng.getInstance().getGame(tableBase.gameID);
        gameBase ?: return E_RETCODE.E_GAME_NOT_EXIST;


        // 游戏桌成功将该玩家加入
        val res = 0;


        if (0 == res) { // 加入成功
//            val infoGame = InfoGame(Game(tableBase.gameID, tableBase.roomNO,));

        } else {

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
    private fun onGameMessage(): Unit {

    }
}