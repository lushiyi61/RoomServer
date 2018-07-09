package com.bzw.tars.server.jfgame.impl;

import com.bzw.tars.server.jfgame.TarsRoomServant;
import com.bzw.tars.server.jfgame.kotlin.impl.Test;

public class TarsRoomServantImpl implements TarsRoomServant {
    @Override
    public String hello() {
        Test test = new Test();
        return String.format("hello ,%s, time=%s", test.hello(),  System.currentTimeMillis());
    }
}
