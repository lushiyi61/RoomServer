package com.bzw.tars.server.jfgame.kotlin.database.table

import com.bzw.tars.server.jfgame.kotlin.database.table.comm.CTableChairIdxMng
import com.bzw.tars.server.jfgame.kotlin.database.table.comm.CTableChairNoMng
import com.bzw.tars.server.jfgame.kotlin.database.table.roomOfCard.CTableDismissMng

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述 桌子类
 * 采用组件模式，组装table
 *
 */
class TableBase(val tableNo: String, val gameID: Int, var roomNO: String) : TableComponent("TableBase") {
    private val tableDict = mutableMapOf<String, TableComponent>();

    fun addTableBase(tableComponent: TableComponent) {
        this.tableDict.put(tableComponent.name, tableComponent);
    }

    fun getTableBase(name: String): TableComponent? {
        return this.tableDict.get(name);
    }

    override fun ToString(): String {
        var tmpStr: String = String.format("TableInfo-> type:%d\n", this.tableType);
        for (tableComponent in this.tableDict.values) {
            tmpStr += tableComponent.ToString();
            tmpStr += "\n";
        }
        return tmpStr;
    }

    private val tableType: Byte = 0;                                    // 本桌类型
    private var state: TableState = TableState.E_TABLE_INIT;           // 本桌状态
    /////////////////////////对外功能接口////////////////////////////
    /*
     * @description 玩家是否可以离开
     * =====================================
     * @author zoujian
     * @date 2018/8/15 16:56
     * @param
     * @return
     */
    fun canLeaveTable(): Boolean {
        //

        if (this.state == TableState.E_TABLE_INIT) {
            return true;
        }

        return false
    }

    /*
     * @description 是否可以开桌
     * =====================================
     * @author zoujian
     * @date 2018/7/27 10:43
     * @return
     */
    fun canStartGame(): Boolean {
        // 开始条件
        val playerNum = 2;

        // 准备人数
        val tableComponent = this.getTableBase("CTableChairNoMng");
        if (tableComponent is CTableChairNoMng) {
            if (tableComponent.getPrepareNum() >= playerNum) {
                return true;
            }
        }

        return false;
    }

    fun needCreateGame(): Boolean {
        return if (this.state == TableState.E_TABLE_INIT) true else false
    }

    /*
     * @description 开始游戏
     * =====================================
     * @author zoujian
     * @date 2018/7/27 10:44
     * @param
     * @return
     */
    fun doStartGame(): Boolean {
        this.state = TableState.E_TABLE_PLAYING;

        // 初始化玩家
        val cTableChairIdxMng = this.getTableBase("CTableChairIdxMng");
        val cTableChairNoMng = this.getTableBase("CTableChairNoMng");
        if (cTableChairIdxMng !is CTableChairIdxMng || cTableChairNoMng !is CTableChairNoMng) {
            return false;
        }

        cTableChairIdxMng.initChairIdx(cTableChairNoMng.getChairNum(), cTableChairNoMng.getChairNoDict());

        // 私人场，刷新投票解散人数
        val cTableDismissMng = this.getTableBase("CTableDismissMng");
        if (cTableDismissMng is CTableDismissMng) cTableDismissMng.initDismissMng(cTableChairIdxMng.getChairIdxPlayerNum());


        return true
    }

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