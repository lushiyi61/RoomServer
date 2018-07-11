package com.bzw.tars.server.jfgame.kotlin.database.table

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述
 */
abstract class TableMng {

    abstract fun onMessage();
    abstract fun sendData();
    abstract fun sendNotifyData();

}