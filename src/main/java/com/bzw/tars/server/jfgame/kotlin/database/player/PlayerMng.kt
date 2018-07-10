package com.bzw.tars.server.jfgame.kotlin.database.player

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述 玩家管理类
 *
 * @param dataMax：最大玩家数
 */
class PlayerMng private constructor(val dataMax: Int = 10000) {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = PlayerMng();
    }

    private val m_OnlinePlayerDict = mutableMapOf<Long, Player>();
    private val m_OfflinePlayerDict = mutableMapOf<Long, Player>();
}