package com.bzw.tars.server.jfgame.kotlin.database.table

import com.bzw.tars.server.jfgame.kotlin.database.table.comm.CTableChairNoMng
import com.bzw.tars.server.jfgame.kotlin.database.table.comm.CTablePlayerMng

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述 单例模式
 * 管理所有的桌子
 *
 *
 */
class TableMng private constructor(val dataMax: Int = 10000) {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = TableMng();
    }

    // 桌子字典
    private val tableDict = mutableMapOf<String, TableBase>();
    //////////////////////////////////////////////////////////////////////////


    /*
     * @description 增加一个新的桌子
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:25
     * @param
     * @return 0:添加成功
     */
    fun addTable(tableNo: String, tableBase: TableBase): Int {
        if (this.tableDict.size > dataMax) {
            return -1;
        }
        if (this.tableDict.containsKey(tableNo)) {
            return -2;
        }
        this.tableDict.put(tableNo, tableBase);

        return 0;
    };

    /*
     * @description 获取游戏桌
     * =====================================
     * @author zoujian
     * @date 2018/7/25 15:35
     * @param
     * @return
     */
    fun getTable(tableNo: String): TableBase? {
        return this.tableDict.get(tableNo);
    }

    /*
     * @description 强制删除指定桌 数据
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:28
     * @param
     * @return
     */
    fun removeTable(tableNo: String) {
        this.tableDict.remove(tableNo);
    }

    /*
     * @description 定时清理无效桌数据
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:30
     */
    fun removeTable() {
//        this.tableDict.filterValues {  }
    }

    /*
     * @description 获取指定桌，玩家管理类
     * =====================================
     * @author zoujian
     * @date 2018/7/26 14:49
     * @param
     * @return
     */
    fun getInfoPlayer(tableNo: String): CTablePlayerMng? {
        val tableBase = this.getTable(tableNo);
        tableBase ?: return null;
        val infoPlayer = tableBase.getTableBase("CTablePlayerMng");
        infoPlayer ?: return null;

        return infoPlayer as CTablePlayerMng;
    }

    /*
     * @description 获取指定桌，座位管理类
     * =====================================
     * @author zoujian
     * @date 2018/7/26 14:49
     * @param
     * @return
     */
    fun getInfoChair(tableNo: String): CTableChairNoMng? {
        val tableBase = this.getTable(tableNo);
        tableBase ?: return null;
        val infoChair = tableBase.getTableBase("CTableChairNoMng");
        infoChair ?: return null;

        return infoChair as CTableChairNoMng;
    }


//    fun getTablePlayerDict(tableNo: String): MutableMap<Long, SharePlayerData>? {
//        val tableBase = this.tableDict.get(tableNo);
//        tableBase ?: return null;
//        val infoPlayer = tableBase.getTableBase("CTablePlayerMng");
//        infoPlayer ?: return null;
//
//        return (infoPlayer as CTablePlayerMng).getPlayerDict();
//    }


//    /*
//     * @description 获取本桌玩家数据
//     * =====================================
//     * @author zoujian
//     * @date 2018/7/25 16:56
//     * @param
//     * @return
//     */
//    fun getTablePlayer(tableNo: String): MutableMap<Long, TableBase.TablePlayer>? {
//        val tableBase = this.getTable(tableNo);
//        tableBase ?: return null;
//        return tableBase.playerDict;
//    }
}