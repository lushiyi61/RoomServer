package com.bzw.tars.server.jfgame.impl;

import com.bzw.tars.server.jfgame.TarsRoomServant;

public class TarsRoomServantImpl implements TarsRoomServant {
    @Override
    public String hello() {
        return String.format("hello kotlin, time=%s", System.currentTimeMillis());
    }
}
