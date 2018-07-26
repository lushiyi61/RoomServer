package com.bzw.tars.server.jfgame.kotlin.database.player

/*
 * @description 物理信息
 * =====================================
 * @author zoujian
 * @date 2018/7/9 14:29
 * @param IP：登录IP
 * @param Address：登录地址
 * @return
 */
data class DataPhysical(var clientIP: String,
                        var address: String,
                        val loginTime: Long);


class InfoPhysical(var dataPhysical: DataPhysical) : PlayerComponent("InfoPhysical") {

    override fun ToString(): String {
        return this.dataPhysical.toString();
    }

}