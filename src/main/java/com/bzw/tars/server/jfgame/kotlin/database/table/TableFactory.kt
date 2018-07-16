package com.bzw.tars.server.jfgame.kotlin.database.table

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述
 */

class TableFactory {

    fun create(tableName: String, tableNo: String, gameID: Int, roomNO: String): TableBase? {
        if (tableName.length != 0) {
            when (tableName) {
                "TablePrivate" -> return TablePrivate(tableNo, gameID, roomNO);
            }
        }


        return null;
    }
}