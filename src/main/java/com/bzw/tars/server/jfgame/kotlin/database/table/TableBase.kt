package com.bzw.tars.server.jfgame.kotlin.database.table

import com.bzw.tars.server.tars.jfgameclientproto.E_MSGTYPE

/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述
 */
abstract class TableBase(val tableNo: String) {


    /*
     * @description 当前桌是否可以释放
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:42
     * @param
     * @return
     */
    abstract fun canRemove(): Boolean;

    /*
     * @description 是否可以开始游戏
     * =====================================
     * @author zoujian
     * @date 2018/7/11 16:19
     * @param
     * @return
     */
    abstract fun canStartGame(): Boolean;


    /*
     * @description 给指定玩家发送消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:05
     */
    fun sendData(uid: Long, msgId: Short, msgData: ByteArray, msgtype: E_MSGTYPE = E_MSGTYPE.E_RESPONSE): Unit {
        if (msgtype == E_MSGTYPE.E_RESPONSE) {
            println(String.format("lUid:%s, send msg to Client, msgId:%s", uid, msgId));
        }



    };

    /*
     * @description 给本桌玩家广播消息
     * =====================================
     * @author zoujian
     * @date 2018/7/11 15:06
     */
    fun sendNotifyData(msgId: Short, msgData: ByteArray): Unit {
        println(String.format("tableNo:%s, send msg to Client, msgId:%s", tableNo, msgId));

    };
}