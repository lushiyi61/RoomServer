package com.bzw.tars.server.jfgame.kotlin.database.player

import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData

/**
 * @创建者 zoujian
 * @创建时间 2018/7/9
 * @描述
 */

class CPlayerGameInfo(val dataGame: SharePlayerData) : PlayerComponent("CPlayerGameInfo") {

    override fun ToString(): String {
        return this.dataGame.toString();
    }

}