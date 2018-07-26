package com.bzw.tars.server.jfgame.kotlin.database.table.comm

import com.bzw.tars.server.jfgame.kotlin.database.table.TableComponent

/**
 * @创建者 zoujian
 * @创建时间 2018/7/26
 * @描述
 */

data class DataGame(var state: Byte);

class CTableGameInfo(dataGame: DataGame) : TableComponent("CTableGameInfo") {

    override fun ToString(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}