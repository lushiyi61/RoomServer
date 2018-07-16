package com.bzw.tars.server.jfgame.kotlin.database.table

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述
 */
class TablePrivate(tableNo: String, gameID: Int, roomNO: String) : TableBase(tableNo, gameID, roomNO) {
    override fun canRemove(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canStartGame(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}