package com.bzw.tars.server.jfgame.kotlin.database.share

/**
 * @创建者 zoujian
 * @创建时间 2018/7/26
 * @描述
 */
data class SharePlayerData(val uid: Long,
                           val tableNo: String,         // 所在游戏桌
                           var chairNo: Byte = -1,      // 所在座位号
                           var chairIdx: Byte = -1,     // 所对应的游戏座位序号
                           var state: Byte = 0,        // 玩家状态
                           var flag: Boolean = true)    // 该内存数据的有效性，使用前检查