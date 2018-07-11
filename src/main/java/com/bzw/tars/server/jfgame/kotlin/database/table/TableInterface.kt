package com.bzw.tars.server.jfgame.kotlin.database.table

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述
 */
interface TableInterface {

    /*
     * @description 处理指定玩家消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:04
     */
    fun onMessage(): Unit;

    /*
     * @description 给指定玩家发送消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:05
     */
    fun sendData(): Unit;

    /*
     * @description 给本桌玩家广播消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:06
     */
    fun sendNotifyData(): Unit;

    /*
     * @description 超时
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:07
     */
    fun onTimeOut(): Unit;

    val tableNo: String;        // 游戏桌编号
    val gameID: Int;            // 游戏ID
    val roomNo: String;         // 房间编号
    val playerMin: Int;         // 最小人数
    val playerMax: Int;         // 最大人数

    val canChat: Boolean;       // 聊天开关
    val canDismiss: Boolean;    // 投票解散开关
    val totalRound: Int;        // 总局数
    val createTime: Long;       // 创建时间

    var state: Int;             // 本桌状态
    var currentRound: Int;      // 当前局数
}