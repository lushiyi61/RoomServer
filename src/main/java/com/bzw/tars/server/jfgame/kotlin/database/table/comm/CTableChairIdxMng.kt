package com.bzw.tars.server.jfgame.kotlin.database.table.comm

import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.jfgame.kotlin.database.table.TableComponent

/**
 * @创建者 zoujian
 * @创建时间 2018/7/26
 * @描述
 */
class CTableChairIdxMng : TableComponent("CTableChairIdxMng") {
    private val chairIdxList = mutableListOf<Long>();

    /*
     * @description 游戏开始时调用
     * =====================================
     * @author zoujian
     * @date 2018/7/26 17:18
     * @param
     * @return
     */
    fun initChairIdx(chairNum: Byte, chairNoDict: MutableMap<Byte, SharePlayerData>) {
        this.chairIdxList.clear();
        for (i in 1..chairNum) {
            val sharePlayerData = chairNoDict.get(i.toByte()) ?: continue;
            if (sharePlayerData.state.equals(0)){
                sharePlayerData.chairIdx = this.chairIdxList.size.toByte();
                this.chairIdxList.add(sharePlayerData.uid);
            }
        }
    }

    /*
     * @description 游戏中，游戏数据返回，分发时调用
     * =====================================
     * @author zoujian
     * @date 2018/7/26 17:18
     * @param
     * @return
     */
    fun getChairIdxList(): MutableList<Long> {
        return this.chairIdxList;
    }

    override fun ToString(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}