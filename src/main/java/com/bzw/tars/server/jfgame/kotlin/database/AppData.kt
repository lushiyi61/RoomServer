package com.bzw.tars.server.jfgame.kotlin.database

import com.bzw.tars.server.jfgame.kotlin.database.room.Room

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述 Kotlin 5种单例模式之内部类式
 * 管理房间/游戏桌
 *
 * @param dataMax 允许同时存在的最大Room数量
 */


class AppData private constructor(val dataMax: Int = 1000) {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = AppData();
    }

    private val roomDict = mutableMapOf<String, Room>();

    fun Set(roomID: String, roomData: Room): Boolean {
        if (this.roomDict.size > dataMax) {
            return false;
        }
        this.roomDict.put(roomID, roomData);
        return true;
    }

    fun Get(roomID: String): Room? {
        return this.roomDict.get(roomID);
    }

    /*
     * @description 定时调用，清理数据
     * =====================================
     * @author zoujian
     * @date 2018/7/10 17:54
     */
//    fun ReleaseMemory(){
//        this.roomDict.forEach<String, Room>();
//    }
}