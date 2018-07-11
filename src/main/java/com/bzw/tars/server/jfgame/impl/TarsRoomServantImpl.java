package com.bzw.tars.server.jfgame.impl;

import com.bzw.tars.server.jfgame.kotlin.database.AppData;
import com.bzw.tars.server.jfgame.kotlin.impl.Test;
import com.bzw.tars.server.tars.jfgame.TClientParam;
import com.bzw.tars.server.tars.jfgame.TUserBaseInfoExt;
import com.bzw.tars.server.tars.jfgame.TarsRoomServant;
import com.bzw.tars.server.tars.jfgameclientproto.TPackage;
import com.qq.tars.protocol.tars.TarsInputStream;

public class TarsRoomServantImpl implements TarsRoomServant {
    @Override
    public String hello() {
        Test test = new Test();
        return String.format("hello ,%s, time=%s", test.hello(), System.currentTimeMillis());
    }

    @Override
    public int onRequest(long lUin, byte[] sMsgPack, TClientParam stClientParam, TUserBaseInfoExt stUerBaseInfo) {
        System.out.println("Recv msg..., playerID:" + lUin + ",IP:" + stClientParam.sAddr);

        int ret = 0;
        // 解码
        TPackage tPackage = new TPackage();
        TarsInputStream tarsInputStream = new TarsInputStream(sMsgPack);
        tPackage.readFrom(tarsInputStream);

        // 循环处理指令
        for (int i = 0; i < tPackage.vecMsgHead.size(); i++) {

            AppData appData = AppData.Companion.getInstance();

            // 玩家 IP 消息头 消息包


        }

        return ret;
    }

    @Override
    public int onOffLine(long lUin) {
        return 0;
    }
}
