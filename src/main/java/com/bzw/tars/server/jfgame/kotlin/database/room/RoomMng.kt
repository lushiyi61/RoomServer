package com.bzw.tars.server.jfgame.kotlin.database.room

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述 房间管理类
 */
class RoomMng private constructor(val dataMax: Int = 1000) {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = RoomMng();
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
}