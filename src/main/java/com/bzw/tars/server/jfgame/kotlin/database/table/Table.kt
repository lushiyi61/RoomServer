package com.bzw.tars.server.jfgame.kotlin.database.table

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述 游戏桌的基类（基本实现）
 */

data class Table(
        val tableID: Int,        // 游戏桌编号
        val gameID: Int,         // 游戏ID
        val roomID: String,      // 房间ID
        val playerMin: Int,      // 最小人数
        val playerMax: Int,      // 最大人数


        var state: Int,          // 本桌状态
        val canTalk: Boolean = false,    // 聊天开关
        val create: Long = System.currentTimeMillis() / 1000); // 创建时间


class InfoTable(var table: Table) {

}