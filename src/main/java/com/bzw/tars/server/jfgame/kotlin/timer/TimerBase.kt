package com.bzw.tars.server.jfgame.kotlin.timer

import java.util.concurrent.atomic.AtomicLong

/**
 * @创建者 zoujian
 * @创建时间 2018/7/24
 * @描述
 */


class TimerBase(
        private var timestampOver: Long,             // 结束时间戳
        private var timestampSuspend: Long,         // 挂起时间戳
        val gameID: Int,                      // 游戏ID
        val tableNo: String,                  // 游戏桌
        private var state: Boolean                    // 激活状态 避免反复创建/释放该类
) {

    private val removeTimeout = 5000L; // 停用 5000s 后强制释放
    private val dismissTimeout = 500L; // 挂起 500s 超时时间

    /*
     * @description 刷新游戏定时器
     * =====================================
     * @author zoujian
     * @date 2018/8/15 15:05
     * @param
     * @return
     */
    fun updateTimeBase(timestampOver: Long) {
        this.timestampOver = timestampOver;
        this.timestampSuspend = 0L;
        this.state = true;
    }

    /*
     * @description 挂起该定时器（申请解散中）
     * =====================================
     * @author zoujian
     * @date 2018/8/15 15:10
     * @param
     * @return
     */
    fun suspendTimeBase(timestampSuspend: Long = System.currentTimeMillis() / 1000) {
        this.timestampSuspend = timestampSuspend;
    }

    /*
     * @description 恢复定时器
     * =====================================
     * @author zoujian
     * @date 2018/8/15 15:49
     * @param
     * @return
     */
    fun recoverTimeBase(timestampCurr: Long = System.currentTimeMillis() / 1000) {
        this.timestampOver += timestampCurr - this.timestampSuspend;
        this.timestampSuspend = 0L;
    }

    /*
     * @description 检查游戏是否超时
     * =====================================
     * @author zoujian
     * @date 2018/7/24 15:04
     * @param timestampCurrent 当前时间戳 System.currentTimeMillis() / 1000
     * @return
     */
    fun checkGameTimeout(timestampCurrent: Long): Boolean {
        // 停用 || 挂起状态
        if (state == false || timestampSuspend > 0L) {
            return false;
        }

        if ((timestampCurrent - timestampOver) >= 0L) {
            return true;
        }
        return false;
    }

    /*
     * @description 检查Room解散是否超时
     * =====================================
     * @author zoujian
     * @date 2018/8/15 14:55
     * @param
     * @return
     */
    fun checkRoomTimeout(timestampCurrent: Long): Boolean {
        // 停用 || 挂起状态
        if (timestampSuspend == 0L) {
            return false;
        }

        if ((timestampCurrent - timestampSuspend) >= dismissTimeout) {
            return true;
        }
        return false;
    }

    /*
     * @description 检查是否可以强制释放
     * =====================================
     * @author zoujian
     * @date 2018/7/24 15:13
     */
    fun checkCanRemove(): Boolean {
        val currentTime = System.currentTimeMillis() / 1000;
        if ((state == false)
                && (currentTime - timestampOver >= removeTimeout)) {
            return true;
        }
        return false;
    }

    fun getState(): Boolean {
        return this.state;
    }

    fun setState(state: Boolean) {
        this.state = state;
    }
}