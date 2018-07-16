package com.bzw.tars.server.jfgame.kotlin.database.table

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
        if (this.m_tableDict.size > dataMax) {
            return -1;
        }
        if (this.m_tableDict.containsKey(tableNo)) {
            return -2;
        }
        this.m_tableDict.put(tableNo, tableBase);

        return 0;
    };

    fun getTable(tableNo: String): TableBase? {
        return this.m_tableDict.get(tableNo);
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
        this.m_tableDict.remove(tableNo);
    }

    /*
     * @description 定时清理无效桌数据
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:30
     */
    fun removeTable() {
//        this.m_tableDict.filterValues {  }
    }

    // 桌子字典
    private val m_tableDict = mutableMapOf<String, TableBase>();
}