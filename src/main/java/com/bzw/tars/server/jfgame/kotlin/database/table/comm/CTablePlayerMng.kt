package com.bzw.tars.server.jfgame.kotlin.database.table.comm

import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.jfgame.kotlin.database.table.TableComponent

/**
 * @创建者 zoujian
 * @创建时间 2018/7/25
 * @描述
 */

class CTablePlayerMng : TableComponent {
    constructor(playerMax: Byte) : super("CTablePlayerMng") {
        this.playerMax = playerMax;
    }

    private val playerMax: Byte;      // 最大玩家数
    private val playerDict = mutableMapOf<Long, SharePlayerData>();  // key (Long:玩家ID)

    /*
     * @description 检查玩家人数是否达到上限
     * =====================================
     * @author zoujian
     * @date 2018/7/25 20:01
     * @return
     */
    fun checkPlayerNum(): Boolean {
        if (this.playerDict.size >= this.playerMax) {
            return true;
        }
        return false;
    }

    fun addPlayer(sharePlayerData: SharePlayerData) {
        this.playerDict.put(sharePlayerData.uid, sharePlayerData);
    }

    fun removePlayer(uid: Long) {
        this.playerDict.remove(uid);
    }

    fun getPlayerDict(): MutableMap<Long, SharePlayerData> {
        return this.playerDict;
    }

    override fun ToString(): String {
        var tmpStr = "CTablePlayerMng:\n";
        for (sharePlayerData in this.playerDict.values) {
            tmpStr += sharePlayerData.toString();
            tmpStr += "\n";
        }
        return tmpStr;
    }
}