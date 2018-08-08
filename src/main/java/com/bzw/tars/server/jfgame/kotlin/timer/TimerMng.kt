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
        println("==========test======== timestampCurrent"+ System.currentTimeMillis() / 1000 );
        println("==========test======== timestampOver   "+ timestampOver.toString());
        println("==========test======== timeout         "+ timeout.toString());
        var timerBase = this.m_timerDict.get(tableNo);
        if (timerBase == null) {
            timerBase = TimerBase(timestampOver, 0L, gameID, tableNo, true);
        } else {
            timerBase.timestampOver = timestampOver;
            timerBase.timestampSuspend = 0L;
            timerBase.state = true;
        }

        this.m_timerDict.put(timerBase.tableNo, timerBase);
    }

    /*
     * @description 释放指定桌定时器
     * =====================================
     * @author zoujian
     * @date 2018/7/24 15:12
     */
    fun removeTimer(tableNo: String) {
        this.m_timerDict.remove(tableNo);
    }

    /*
     * @description 中止该定时器
     * =====================================
     * @author zoujian
     * @date 2018/7/24 11:47
     * @param
     * @return
     */
    fun suspendTimer(tableNo: String) {
        val timestampSuspend = System.currentTimeMillis() / 1000;

        val timerBase = this.m_timerDict.get(tableNo);
        timerBase ?: return;
        timerBase.timestampSuspend = timestampSuspend;
    }

    /*
     * @description 恢复该定时器
     * =====================================
     * @author zoujian
     * @date 2018/7/24 11:47
     * @param
     * @return
     */
    fun recoverTimer(tableNo: String) {
        val timerBase = this.m_timerDict.get(tableNo);
        timerBase ?: return;

        val timestampOver = System.currentTimeMillis() / 1000 + (timerBase.timestampOver - timerBase.timestampSuspend);
        timerBase.timestampOver = timestampOver;
        timerBase.timestampSuspend = 0L;
    }

    fun getTimerDict(): MutableMap<String, TimerBase> {
        return this.m_timerDict;
    }

    // key:TableNo
    private val m_timerDict = mutableMapOf<String, TimerBase>();
}