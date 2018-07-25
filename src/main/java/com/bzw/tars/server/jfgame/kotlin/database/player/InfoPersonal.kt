package com.bzw.tars.server.jfgame.kotlin.database.player

/*
 * @description 个人资料
 * =====================================
 * @author zoujian
 * @date 2018/7/9 14:19
 * @param lPlayID：玩家ID
 * @param iSex：性别
 * @param sNickName：昵称
 * @param sPortrait：头像
 * @return
 */
data class Personal(var playID: Long,
                    var sex: Byte,
                    var nickName: String,
                    var portraitNo: String,
                    var portraitPath: String
);



class InfoPersonal(var personal: Personal) : PlayerComponent("InfoPersonal") {

    override fun ToString(): String {
        return this.personal.toString();
    }

}



