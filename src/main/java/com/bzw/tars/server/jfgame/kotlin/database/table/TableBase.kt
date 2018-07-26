package com.bzw.tars.server.jfgame.kotlin.database.table

import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng
import com.bzw.tars.server.tars.jfgameclientproto.E_RETCODE

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述 桌子类
 * 采用组件模式，组装table
 *
 */
class TableBase(val tableNo: String, val gameID: Int, var roomNO: String) : TableComponent("TableBase") {
    private val tableInfo = mutableMapOf<String, TableComponent>();

    fun addTableBase(c: TableComponent) {
        this.tableInfo.put(c.name, c);
    }

    fun getTableBase(name: String): TableComponent? {
        return this.tableInfo.get(name);
    }

    override fun ToString(): String {
        var tmpStr: String = String.format("TableInfo-> type:%d\n", this.tableType);
        for (v in this.tableInfo.values) {
            tmpStr += v.ToString();
            tmpStr += "\n";
        }
        return tmpStr;
    }

    private val tableType: Byte = 0;                            // 类型
    var state: TableState = TableState.E_TABLE_INIT;           // 本桌状态
    /////////////////////////对外功能接口////////////////////////////


    /////////////////////// 玩家管理 ///////////////////////////////
    // 玩家ID，座位号，座次号，准备状态
//    data class TablePlayer(var uid: Long, var chairNo: Byte, var chairIdx: Byte, var state: Boolean) {}
//
//    val playerDict = mutableMapOf<Long, TablePlayer>();  // key (Long:玩家ID)
//    val chairDict = mutableMapOf<Byte, Long>();     // key (Byte:座位号)
    val chairList = mutableListOf<Long>();          // key (座次 -> 玩家)
    ////////////////////////游戏桌属性/////////////////////////////////

    var playerMin: Byte = 2;         // 最小人数
    var playerMax: Byte = 5;         // 最大人数
//    var chairNun: Byte = 5;          // 座位数
//

    var currentRound: Int = 0;                                  // 当前局数
    var autoSit: Boolean = true;                                // 自动坐下开关
    var canLook: Boolean = false;                               // 允许旁观
    var canTalk: Boolean = false;                               // 聊天开关
    var playerDismiss: Boolean = false;                        // 玩家投票解散开关
    var totalRound: Int = -1;                                   // 总局数
    var gameMsgID: Short = 0;                                   // 当前游戏动作指令（游戏开始后赋值）
    var ipLimitation: Byte = 0;                                 // 0：不限制，1：限制第一位，2：限制前两位，3：限制前三位，4，限制全四位
    val createTime: Long = System.currentTimeMillis() / 1000;   // 创建时间



    fun doLeaveTable(uid: Long) {
//        val tablePlayer = this.playerDict.remove(uid);
//        tablePlayer ?: return;
//        this.chairDict.remove(tablePlayer.chairNo);
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
//        val player = this.playerDict.get(uid);
//        player ?: return;
//
//        this.chairDict.remove(player.chairNo);
//        this.playerDict.remove(uid);
    }
}