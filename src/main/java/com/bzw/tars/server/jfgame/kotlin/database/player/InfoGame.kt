package com.bzw.tars.server.jfgame.kotlin.database.player

import com.bzw.tars.server.jfgame.kotlin.common.Component

/**
 * @创建者 zoujian
 * @创建时间 2018/7/9
 * @描述
 */
data class Game(var roomID: Int,            // 房间ID
                var tableNo: String,          // 游戏桌编号
                var chairID: Int,             // 座位号（前端用）(从0开始，-1：表示未就坐)
//                var chairIdx: Int,           // 座次号（后端用）
                var state: Int)              // 游戏状态


class InfoGame(var game: Game) : Component("InfoGame") {

    override fun ToString(): String {
        return this.game.toString();
    }

}