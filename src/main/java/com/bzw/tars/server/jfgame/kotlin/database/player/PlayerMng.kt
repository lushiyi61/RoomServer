package com.bzw.tars.server.jfgame.kotlin.database.player

import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.tars.jfgameclientproto.TPlayerInfo

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

    fun removePlayer(uid: Long) {
        this.m_OnlinePlayerDict.remove(uid);
    }

    fun getPlayer(uid: Long): PlayerBase? {
        return this.m_OnlinePlayerDict.get(uid);
    }

    private val m_OnlinePlayerDict = mutableMapOf<Long, PlayerBase>();      // 在线玩家数据字典
//    private val m_OfflinePlayerDict = mutableMapOf<Long, TablePlayer>();     // 离线玩家数据字典


    //////////////////////////////////////////////////////////////////////////////////

    fun getInfoGame(uid: Long): SharePlayerData? {
        val playerBase = this.m_OnlinePlayerDict.get(uid);
        playerBase ?: return null;
        val infoGame = playerBase.getPlayerBase("CPlayerGameInfo");
        infoGame ?: return null;

        return (infoGame as CPlayerGameInfo).dataGame;
    }

    fun getInfoPersonal(uid: Long): PlayerPersonalInfo? {
        val playerBase = this.m_OnlinePlayerDict.get(uid);
        playerBase ?: return null;
        val infoPersonal = playerBase.getPlayerBase("CPlayerPersonalInfo");
        infoPersonal ?: return null;

        return (infoPersonal as CPlayerPersonalInfo).playerPersonalInfo;
    }

    fun getInfoPhysical(uid: Long): PlayerPhysicalInfo? {
        val playerBase = this.m_OnlinePlayerDict.get(uid);
        playerBase ?: return null;
        val infoPhysical = playerBase.getPlayerBase("CPlayerPhysicalInfo");
        infoPhysical ?: return null;

        return (infoPhysical as CPlayerPhysicalInfo).playerPhysicalInfo;
    }

    fun getTarsPlayerInfo(uid: Long): TPlayerInfo? {
        val personal = this.getInfoPersonal(uid);
        personal ?: return null;
        val game = this.getInfoGame(uid);
        game ?: return null;
        val playerPhysicalInfo: PlayerPhysicalInfo? = this.getInfoPhysical(uid);
        playerPhysicalInfo ?: return null;

        val tPlayerInfo = TPlayerInfo(uid, game.state, game.chairNo,
                personal.nickName, personal.portraitNo, personal.portraitPath, personal.sex, playerPhysicalInfo.clientIP);

        return tPlayerInfo;
    }
}