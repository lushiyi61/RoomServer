package com.bzw.tars.server.jfgame.kotlin.database.game

/**
 * @创建者 zoujian
 * @创建时间 2018/7/17
 * @描述 游戏管理类
 */
class GameMng private constructor() {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = GameMng();
    }

    fun addGame(gameID: Int, gameBase: GameBase): Int {
        if (this.m_gameDict.containsKey(gameID)) {
            return -2;
        }
        this.m_gameDict.put(gameID, gameBase);

        return 0;
    }

    fun getGame(gameID: Int): GameBase? {
        return this.m_gameDict.get(gameID);
    }

    private val m_gameDict = mutableMapOf<Int, GameBase>();
}