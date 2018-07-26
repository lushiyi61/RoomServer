package com.bzw.tars.server.jfgame.kotlin.database.table.comm

import com.bzw.tars.server.jfgame.kotlin.database.share.SharePlayerData
import com.bzw.tars.server.jfgame.kotlin.database.table.TableComponent

/**
 * @创建者 zoujian
 * @创建时间 2018/7/26
 * @描述
 */
class InfoChair : TableComponent {
    constructor(chairNum: Byte) : super("InfoChair") {
        this.chairNum = chairNum;
    }

    private val chairNum: Byte;      // 最大玩家数
    private val chairDict = mutableMapOf<Byte, SharePlayerData>();  // key (座位号)

    /*
     * @description 获取当前桌所有座位上的玩家
     * =====================================
     * @author zoujian
     * @date 2018/7/26 14:06
     * @param
     * @return
     */
    fun getAllPlayer(): MutableMap<Byte, SharePlayerData> {
        // 这里是否应该深拷贝一份
        return this.chairDict;
    }

    /*
     * @description 指定玩家坐下
     * =====================================
     * @author zoujian
     * @date 2018/7/26 14:01
     * @param
     * @return
     */
    fun addPlayer(sharePlayerData: SharePlayerData) {
        this.chairDict.put(sharePlayerData.chairNo, sharePlayerData);
    }

    /*
     * @description 指定玩家离开
     * =====================================
     * @author zoujian
     * @date 2018/7/26 14:09
     * @param
     * @return
     */
    fun removePlayer(sharePlayerData: SharePlayerData) {
        this.chairDict.remove(sharePlayerData.chairNo);
        sharePlayerData.chairNo = -1;
    }

    /*
     * @description 检查座位是否坐满
     * =====================================
     * @author zoujian
     * @date 2018/7/26 10:58
     * @param
     * @return
     */
    fun checkChairNum(): Boolean {
        if (this.chairDict.size >= this.chairNum) {
            return true;
        }
        return false;
    }

    /*
     * @description 选座,返回座位号
     * =====================================
     * @author zoujian
     * @date 2018/7/26 11:00
     * @param
     * @return -1：当前座位有人，其他：chairNo
     */
    fun chooseTheSeat(chairNo: Byte): Byte {
        if ((chairNo > 0) && (chairNo <= this.chairNum)) { // 手动选座
            if (this.chairDict.containsKey(chairNo)) {
                return -1;
            } else {
                return chairNo;
            }
        }

        for (i in 1..this.chairNum) {
            this.chairDict.get(i.toByte()) ?: return i.toByte();
        }
        return -1;
    }

    override fun ToString(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}