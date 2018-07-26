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
        InfoPhysical infoPhysical = new InfoPhysical(new DataPhysical("192.168.1.1", "深圳", System.currentTimeMillis() / 1000));
        InfoPersonal infoPersonal = new InfoPersonal(new DataPersonal(1111, (byte)1, "nizou", "http", "1234"));

        playerBase.addPlayerBase(infoPersonal);
        playerBase.addPlayerBase(infoPhysical);
        playerBase.addPlayerBase(infoPhysical);
        infoPhysical = new InfoPhysical(new DataPhysical("192.168.1.1", "湖南", System.currentTimeMillis() / 1000));
        playerBase.addPlayerBase(infoPhysical);
        String str = playerBase.ToString();
        System.out.println(str);


        InfoPhysical tmp = (InfoPhysical) playerBase.getPlayerBase("InfoPhysical");
        System.out.println(tmp.getDataPhysical().getAddress());
    }
}
