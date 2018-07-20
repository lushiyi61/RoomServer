package com.bzw.tars.server.jfgame.kotlin.database.table

import com.bzw.tars.server.jfgame.kotlin.database.player.Game
import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng
import com.bzw.tars.server.tars.jfgameclientproto.E_RETCODE

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述 桌子的基类
 */
abstract class TableBase(val tableNo: String, val gameID: Int, var roomNO: String) {
    /////////////////////// 玩家管理 ///////////////////////////////
    // 玩家ID，座位号，座次号，准备状态
    data class TablePlayer(var uid: Long, var chairNo: Byte, var chairIdx: Byte, var state: Boolean) {}

    val playerDict = mutableMapOf<Long, TablePlayer>();  // key (Long:玩家ID)
    val chairDict = mutableMapOf<Byte, Long>();     // key (Byte:座位号)
    val chairList = mutableListOf<Long>();          // key (座次 -> 玩家)
    ////////////////////////////////////////////////////////////////

    var playerMin: Byte = 2;         // 最小人数
    var playerMax: Byte = 5;         // 最大人数
    var chairNun: Byte = 5;          // 座位数

    var state: TableState = TableState.E_TABLE_INIT;           // 本桌状态
    var currentRound: Int = 0;                                  // 当前局数
    var autoSit: Boolean = true;                                // 自动坐下开关
    var canLook: Boolean = false;                               // 允许旁观
    var canTalk: Boolean = false;                               // 聊天开关
    var playerDismiss: Boolean = false;                        // 玩家投票解散开关
    var totalRound: Int = -1;                                   // 总局数
    var gameMsgID: Short = 0;                                   // 当前游戏动作指令（游戏开始后赋值）
    var ipLimitation: Byte = 0;                                 // 0：不限制，1：限制第一位，2：限制前两位，3：限制前三位，4，限制全四位
    val createTime: Long = System.currentTimeMillis() / 1000;   // 创建时间

//    protected fun enterTable(uid: Long, ipAddr: String = "1.1.1.1"): E_RETCODE {
//        if (TableState.E_TABLE_INIT != state) {
//            return E_RETCODE.E_TABLE_ENTER_CUT_IN;
//        }
//
//
//        return E_RETCODE.E_COMMON_SUCCESS;
//    }


    /*
     * @description 当前桌是否可以释放
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:42
     * @param
     * @return
     */
    abstract fun canRemove(): Boolean;

    /*
     * @description 是否可以开始游戏
     * =====================================
     * @author zoujian
     * @date 2018/7/11 16:19
     * @param
     * @return
     */
    abstract fun canStartGame(): Boolean;

    /*
     * @description 初始化本桌基本数据
     * =====================================
     * @author zoujian
     * @date 2018/7/16 11:25
     * @param
     * @return
     */
    protected fun initTable(
            playerMin: Byte,
            playerMax: Byte,
            state: TableState,
            canLook: Boolean,
            canTalk: Boolean,
            playerDismiss: Boolean,
            ipLimitation: Byte,
            totalRound: Int
    ) {
        this.playerMin = playerMin;
        this.playerMax = playerMax
        this.state = state;
        this.canLook = canLook;
        this.canTalk = canTalk;
        this.playerDismiss = playerDismiss;
        this.ipLimitation = ipLimitation;
        this.totalRound = totalRound;
    };

    /*
     * @description 给指定玩家发送消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:05
     */
    protected fun sendData(uid: Long, msgId: Short, msgData: ByteArray): Unit {
        println(String.format("lUid:%s, send msg to Client, msgId:%s", uid, msgId));


    };

    /*
     * @description 给本桌玩家广播消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:06
     */
    protected fun sendNotifyData(msgId: Short, msgData: ByteArray): Unit {
        println(String.format("tableNo:%s, send msg to Client, msgId:%s", tableNo, msgId));

    };


    /*
     * @description 玩家进入本桌
     * =====================================
     * @author zoujian
     * @date 2018/7/19 14:12
     * @param
     * @return
     */
    fun doEnterTable(uid: Long, chairNo: Byte): E_RETCODE {
        if (this.playerDict.size >= this.playerMax) {
            return E_RETCODE.E_TABLE_IS_FULL;
        }

        this.playerDict.put(uid, TableBase.TablePlayer(uid, -1, -1, false));
        val game: Game? = PlayerMng.getInstance().getInfoGame(uid)
        game ?: return E_RETCODE.E_PLAYER_NOT_EXIST;
        game.roomNO = this.roomNO;

        if (!this.canLook && chairNo > 0) {
            return this.doSitDownTable(uid, chairNo);
        }

        return E_RETCODE.E_COMMON_SUCCESS;
    }


    fun doSitDownTable(uid: Long, chairNo: Byte): E_RETCODE {
        val tablePlayer = this.playerDict.get(uid);
        tablePlayer ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        val game: Game? = PlayerMng.getInstance().getInfoGame(uid)
        game ?: return E_RETCODE.E_PLAYER_NOT_EXIST;

        if (this.chairDict.size >= this.chairNun) {
            return E_RETCODE.E_CHAIR_IS_FULL
        };

        var tmpChairNo = chairNo;
        if (tmpChairNo > 0) {  // 选桌
            this.chairDict.get(tmpChairNo) ?: return E_RETCODE.E_SEAT_IS_TAKEN;
        } else {            // 自动坐桌
            tmpChairNo = this.doAutoSit();
        }

        this.chairDict.put(tmpChairNo, uid);
        tablePlayer.chairNo = tmpChairNo;
        game.chairNo = tmpChairNo;

        return E_RETCODE.E_COMMON_SUCCESS;
    }

    fun doGameStart() {

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * @description 清理指定玩家数据
     * =====================================
     * @author zoujian
     * @date 2018/7/19 16:11
     * @param
     * @return
     */
    private fun doCleanPlayerInfo(uid: Long) {
        val player = this.playerDict.get(uid);
        player ?: return;

        this.chairDict.remove(player.chairNo);
        this.playerDict.remove(uid);
    }

    /*
     * @description 自动入座(需保证座位未满)
     * =====================================
     * @author zoujian
     * @date 2018/7/19 16:20
     * @param 返回分配的座位号
     * @return
     */
    private fun doAutoSit(): Byte {
        for (i in 1..this.chairNun) {
            this.chairDict.get(i.toByte()) ?: return i.toByte();
        }
        return this.chairNun;
    }
}