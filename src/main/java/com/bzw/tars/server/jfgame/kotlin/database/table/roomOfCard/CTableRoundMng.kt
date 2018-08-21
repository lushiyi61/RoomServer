package com.bzw.tars.server.jfgame.kotlin.database.table.roomOfCard

import com.bzw.tars.server.jfgame.kotlin.database.table.TableComponent

/**
 * @创建者 zoujian
 * @创建时间 2018/8/21
 * @描述 游戏回合管理
 */
class CTableRoundMng : TableComponent {
    constructor() : super("CTableRoundMng") {
    }

    override fun ToString(): String {
        var tmpStr = "CTableRoundMng:\n";
        return tmpStr;
    }
}