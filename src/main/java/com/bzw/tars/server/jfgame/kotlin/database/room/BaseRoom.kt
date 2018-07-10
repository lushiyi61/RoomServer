package com.bzw.tars.server.jfgame.kotlin.database.room

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述 房间的基类
 */
abstract class BaseRoom {
    abstract val roomID: String;            // 房间ID
    abstract val roomName: String;          // 房间昵称
    abstract val playerMin: Int;            // 最小人数
    abstract val playerMax: Int;            // 最大人数
    abstract val tableNum: Int;             // 桌子数
    abstract val baseScore: Int;             // 底分


    abstract val createTime: Long;          // 创建时间
    abstract val dismissTime: Long;          // 解散时间点

    abstract var canDismiss: Boolean;        // 允许释放该内存
    abstract var lastTime: Long;             // 最后操作时间点


}