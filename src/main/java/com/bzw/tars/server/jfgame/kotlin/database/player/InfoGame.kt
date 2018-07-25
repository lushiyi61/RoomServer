package com.bzw.tars.server.jfgame.kotlin.database.player

/**
 * @创建者 zoujian
 * @创建时间 2018/7/9
 * @描述
 */
data class Game(var roomNO: String,          // 房间编号
                var tableNo: String,          // 游戏桌编号
                var chairNo: Byte,             // 座位号（前端用）(从0开始，-1：表示未就坐)
                var state: Byte)              // 游戏准备状态


class InfoGame(var game: Game) : PlayerComponent("InfoGame") {

    override fun ToString(): String {
        return this.game.toString();
    }

}