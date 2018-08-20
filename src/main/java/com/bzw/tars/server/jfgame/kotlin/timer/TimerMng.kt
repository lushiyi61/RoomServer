package com.bzw.tars.server.jfgame.kotlin.timer

/**
 * @创建者 zoujian
 * @创建时间 2018/7/24
 * @描述
 */
class TimerMng private constructor(val dataMax: Int = 10000) {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = TimerMng();
    }

    /*
     * @description 添加/更新一个定时器
     * =====================================
     * @author zoujian
     * @date 2018/7/24 11:23
     * @param tableNo:游戏桌号
     * @param tableNo:游戏桌号
     * @param timeout:超时时间（单位秒）
     * @return
     */
    fun addTimer(tableNo: String, gameID: Int, timeout: Int) {
        val timestampOver = System.currentTimeMillis() / 1000 + timeout.toLong();
        var timerBase = this.timerDict.get(tableNo);
        if (timerBase == null) {
            timerBase = TimerBase(timestampOver, 0L, gameID, tableNo, true);
            this.timerDict.put(timerBase.tableNo, timerBase);
        } else {
            timerBase.updateTimeBase(timestampOver);
        }
    }

    fun getTimer(tableNo: String): TimerBase? {
        return this.timerDict.get(tableNo);
    }

    /*
     * @description 释放指定桌定时器
     * =====================================
     * @author zoujian
     * @date 2018/7/24 15:12
     */
    fun removeTimer(tableNo: String) {
        this.timerDict.remove(tableNo);
    }

    /*
     * @description 中止该定时器
     * =====================================
     * @author zoujian
     * @date 2018/7/24 11:47
     * @param
     * @return
     */
    fun hangupTimer(tableNo: String) {
        val timerBase = this.timerDict.get(tableNo);
        timerBase ?: return;
        timerBase.setState(false);
    }

    fun getTimerDict(): MutableMap<String, TimerBase> {
        return this.timerDict;
    }

    // key:TableNo
    private val timerDict = mutableMapOf<String, TimerBase>();
}