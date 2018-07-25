package com.bzw.tars.server.jfgame.kotlin.database.player

/*
 * @description 玩家信息接口
 * =====================================
 * @author zoujian
 * @date 2018/7/9 14:08
 * @param
 * @return
 */
open class PlayerBase : PlayerComponent("PlayerBase") {
    private val playerInfo = mutableMapOf<String, PlayerComponent>();

    fun addPlayerBase(c: PlayerComponent) {
        this.playerInfo.put(c.name, c);
    }

    fun getPlayerBase(name: String): PlayerComponent? {
        return this.playerInfo.get(name);
    }

    override fun ToString(): String {
        var tmpStr: String = "PlayerInfo：\n";
        for (v in this.playerInfo.values) {
            tmpStr += v.ToString();
            tmpStr += "\n";
        }
        return tmpStr;
    }

}


//
///*
// * @description 战绩信息
// * =====================================
// * @author zoujian
// * @date 2018/7/9 14:02
// * @param iGameId：游戏ID
// */
//data class RecordInfo(var iGameId: Int);
