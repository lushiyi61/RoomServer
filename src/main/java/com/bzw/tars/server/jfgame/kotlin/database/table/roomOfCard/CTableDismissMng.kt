package com.bzw.tars.server.jfgame.kotlin.database.table.roomOfCard

import com.bzw.tars.server.jfgame.kotlin.database.table.TableComponent

/**
 * @创建者 zoujian
 * @创建时间 2018/8/15
 * @描述
 */
class CTableDismissMng : TableComponent {
    constructor() : super("CTableDismissMng") {
    }

    private var playerNum: Byte = 100;        // 总人数
    private var isDismiss: Boolean = false; // 是否处于解散中
    private var supportNum: Byte = 0;
    private var voteList: MutableList<Long> = mutableListOf();

    public fun initDismissMng(playerNum: Byte) {
        this.playerNum = playerNum;
    }

    /*
     * @description 参与投票
     * =====================================
     * @author zoujian
     * @date 2018/8/15 20:13
     * @param
     * @return 投票是否成功
     */
    public fun doVote(uid: Long, support: Boolean): Boolean {
        if (!isDismiss) return false; // 未发起投票
        if (this.voteList.contains(uid)) return false;   // 已经投过票了

        this.voteList.add(uid);
        if (support) this.supportNum++

        return true;
    }

    /*
     * @description 发起投票
     * =====================================
     * @author zoujian
     * @date 2018/8/15 20:19
     * @param
     * @return 成功或失败
     */
    public fun initiateVote(uid: Long): Boolean {
        if (isDismiss) return false else isDismiss = true;
        supportNum = 0;
        voteList.clear();
        this.doVote(uid, true)

        return this.isDismiss;
    }

    /*
     * @description 检查投票是否结束
     * =====================================
     * @author zoujian
     * @date 2018/8/15 20:25
     * @param
     * @return
     */
    public fun checkDismissFinish(): Boolean {
        if (voteList.size == playerNum.toInt()) return true;
        if (supportNum * 2 > playerNum) return true;
        return false
    }

    /*
     * @description 获取投票结果
     * =====================================
     * @author zoujian
     * @date 2018/8/15 20:30
     * @param
     * @return 解散 or 不解散
     */
    public fun getDismissResult(): Boolean {
        return if (supportNum * 2 > playerNum) true else false;
    }

    /*
     * @description 取消投票（重置投票）
     * =====================================
     * @author zoujian
     * @date 2018/8/16 10:19
     * @param
     * @return
     */
    public fun cancelVote() {
        isDismiss = false; // 是否处于解散中
        supportNum = 0;
        voteList.clear();
    }

    override fun ToString(): String {
        var tmpStr = "CTableDismissMng:\n";
        return tmpStr;
    }
}