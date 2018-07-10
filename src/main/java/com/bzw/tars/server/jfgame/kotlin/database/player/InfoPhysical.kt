package com.bzw.tars.server.jfgame.kotlin.database.player

import com.bzw.tars.server.jfgame.kotlin.common.Component

/*
 * @description 物理信息
 * =====================================
 * @author zoujian
 * @date 2018/7/9 14:29
 * @param IP：登录IP
 * @param Address：登录地址
 * @return
 */
data class Physical(var clientIP: String,
                    var address: String);


class InfoPhysical(var physical: Physical) : Component("InfoPhysical") {

    override fun ToString(): String {
        return this.physical.toString();
    }

}