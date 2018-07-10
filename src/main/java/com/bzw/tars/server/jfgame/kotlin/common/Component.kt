package com.bzw.tars.server.jfgame.kotlin.common

/**
 * @创建者 zoujian
 * @创建时间 2018/7/9
 * @描述 组件设计模式——抽象类
 */
abstract class Component(val name: String) {
    abstract fun ToString(): String;
}