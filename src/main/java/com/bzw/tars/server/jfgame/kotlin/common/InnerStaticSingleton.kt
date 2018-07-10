package com.bzw.tars.server.jfgame.kotlin.common

/**
 * @创建者 zoujian
 * @创建时间 2018/7/10
 * @描述 Kotlin 5种单例模式之内部类式
 */
class InnerStaticSingleton private constructor() {
    companion object {
        fun getInstance() = Holder.INSTANCE

    }

    private object Holder {
        val INSTANCE = InnerStaticSingleton()
    }
}