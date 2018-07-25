package com.bzw.tars.server.jfgame.kotlin.database.player;

/*
 * @description 会员信息
 * =====================================
 * @author zoujian
 * @date 2018/7/9 14:07
 * @param  level：vip等级
 * @param  varidity：有效期
 */
data class Vip(var level: Int,
               var varidity: Int);


class InfoVip(var vip: Vip) : PlayerComponent("InfoVip") {

    override fun ToString(): String {
        return this.vip.toString();
    }

}