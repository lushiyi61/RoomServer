package com.bzw.tars.server.jfgame.kotlin.database.player;

import org.junit.Test;

/**
 * @创建者 zoujian
 * @创建时间 2018/7/9
 * @描述
 */
public class TablePlayerTest {
    @Test
    public void test() {
        PlayerBase playerBase = new PlayerBase();
        CPlayerPhysicalInfo CPlayerPhysicalInfo = new CPlayerPhysicalInfo(new PlayerPhysicalInfo("192.168.1.1", "深圳", System.currentTimeMillis() / 1000));
        CPlayerPersonalInfo CPlayerPersonalInfo = new CPlayerPersonalInfo(new PlayerPersonalInfo(1111, (byte)1, "nizou", "http", "1234"));

        playerBase.addPlayerBase(CPlayerPersonalInfo);
        playerBase.addPlayerBase(CPlayerPhysicalInfo);
        playerBase.addPlayerBase(CPlayerPhysicalInfo);
        CPlayerPhysicalInfo = new CPlayerPhysicalInfo(new PlayerPhysicalInfo("192.168.1.1", "湖南", System.currentTimeMillis() / 1000));
        playerBase.addPlayerBase(CPlayerPhysicalInfo);
        String str = playerBase.ToString();
        System.out.println(str);


        CPlayerPhysicalInfo tmp = (CPlayerPhysicalInfo) playerBase.getPlayerBase("CPlayerPhysicalInfo");
        System.out.println(tmp.getPlayerPhysicalInfo().getAddress());
    }
}
