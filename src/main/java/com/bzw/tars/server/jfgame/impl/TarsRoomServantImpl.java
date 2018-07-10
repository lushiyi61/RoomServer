package com.bzw.tars.server.jfgame.impl;

import com.bzw.tars.server.jfgame.kotlin.impl.Test;
import com.bzw.tars.server.tars.jfgame.TClientParam;
import com.bzw.tars.server.tars.jfgame.TUserBaseInfoExt;
import com.bzw.tars.server.tars.jfgame.TarsRoomServant;

public class TarsRoomServantImpl implements TarsRoomServant {
    @Override
    public String hello() {
        Test test = new Test();
        return String.format("hello ,%s, time=%s", test.hello(),  System.currentTimeMillis());
    }

    @Override
    public int onRequest(long lUin, byte[] sMsgPack, String sCurServantAddr, TClientParam stClientParam, TUserBaseInfoExt stUerBaseInfo) {




        return 0;
    }

    @Override
    public int onOffLine(long lUin) {
        return 0;
    }
}
