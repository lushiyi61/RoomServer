package com.bzw.tars.server.jfgame.kotlin.timer

import java.util.concurrent.atomic.AtomicLong

/**
 * @创建者 zoujian
 * @创建时间 2018/7/24
 * @描述
 */


class TimerBase(
        var timestampOver: Long,             // 结束时间戳
        var timestampSuspend: Long,         // 挂起时间戳
        val gameID: Int,                      // 游戏ID
        val tableNo: String,                  // 游戏桌
        var state: Boolean                    // 激活状态 避免反复创建/释放该类
) {

    private var removeTimeout = 500; // 停用 500s 后强制释放

    /*
     * @description 检查是否超时
     * =====================================
     * @author zoujian
     * @date 2018/7/24 15:04
     * @param timestampCurrent 当前时间戳 System.currentTimeMillis() / 1000
     * @return
     */
    fun checkTimeout(timestampCurrent: Long): Boolean {
        // 停用 || 挂起状态
        if (state == false || timestampSuspend > 0) {
            return false;
        }

        if ((timestampCurrent - timestampOver) >= 0L) {
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
}