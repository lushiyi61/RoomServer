package com.bzw.tars.server.jfgame.kotlin.database.table

/**
 * @创建者 zoujian
 * @创建时间 2018/7/16
 * @描述
 */
enum class TableState {
    E_TABLE_INIT,       // 初始化
    E_TABLE_PLAYING,    // 游戏中
    E_TABLE_HANGUP,     // 游戏挂起（投票解散中）
}