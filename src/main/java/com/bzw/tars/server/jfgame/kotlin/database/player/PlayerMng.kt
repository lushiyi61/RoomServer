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

    fun addPlayer(uid: Long, player: PlayerBase): Int {
        if (this.m_OnlinePlayerDict.size > dataMax) {
            return -1;
        }
        if (this.m_OnlinePlayerDict.containsKey(uid)) {
            return -2;
        }
        this.m_OnlinePlayerDict.put(uid, player);

        return 0;
    };

    fun getPlayer(uid: Long): PlayerBase? {
        return this.m_OnlinePlayerDict.get(uid);
    }

    private val m_OnlinePlayerDict = mutableMapOf<Long, PlayerBase>();      // 在线玩家数据字典
//    private val m_OfflinePlayerDict = mutableMapOf<Long, Player>();     // 离线玩家数据字典
}