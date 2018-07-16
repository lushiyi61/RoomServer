package com.bzw.tars.server.jfgame.kotlin.database.table

import com.bzw.tars.server.tars.jfgameclientproto.E_MSGTYPE

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述 桌子的基类
 */
abstract class TableBase(val tableNo: String, val gameID: Int, var roomNO: String) {
    var playerMin: Int = 2;         // 最小人数
    var playerMax: Int = 10;        // 最大人数

    var state: TableState = TableState.E_TABLE_INIT;           // 本桌状态
    var currentRound: Int = 0;                                  // 当前局数
    var canTalk: Boolean = false;                               // 聊天开关
    var playerDismiss: Boolean = false;                        // 玩家投票解散开关
    var totalRound: Int = -1;                                   // 总局数
    val createTime: Long = System.currentTimeMillis() / 1000;   // 创建时间


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
    fun initTable(
            playerMin: Int,
            playerMax: Int,
            state: TableState,
            canTalk: Boolean,
            playerDismiss: Boolean,
            totalRound: Int
    ) {
        this.playerMin = playerMin;
        this.playerMax = playerMax
        this.state = state;
        this.canTalk = canTalk;
        this.playerDismiss = playerDismiss;
        this.totalRound = totalRound;
    };

    /*
     * @description 给指定玩家发送消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:05
     */
    fun sendData(uid: Long, msgId: Short, msgData: ByteArray, msgtype: E_MSGTYPE = E_MSGTYPE.E_RESPONSE): Unit {
        if (msgtype == E_MSGTYPE.E_RESPONSE) {
            println(String.format("lUid:%s, send msg to Client, msgId:%s", uid, msgId));
        }


    };

    /*
     * @description 给本桌玩家广播消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:06
     */
    fun sendNotifyData(msgId: Short, msgData: ByteArray): Unit {
        println(String.format("tableNo:%s, send msg to Client, msgId:%s", tableNo, msgId));

    };
}