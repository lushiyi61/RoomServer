package com.bzw.tars.server.jfgame.kotlin.database.player;

/*
 * @description 会员信息
 * =====================================
 * @author zoujian
 * @date 2018/7/9 14:07
 * @param  level：vip等级
 * @param  varidity：有效期
 */
data class PlayerVipInfo(var level: Int,
                         var varidity: Int);


class CPlayerVipInfo(var playerVipInfo: PlayerVipInfo) : PlayerComponent("CPlayerVipInfo") {

    override fun ToString(): String {
        return this.playerVipInfo.toString();
    }

}