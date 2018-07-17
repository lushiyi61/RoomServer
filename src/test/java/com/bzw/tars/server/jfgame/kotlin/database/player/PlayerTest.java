package com.bzw.tars.server.jfgame.kotlin.database.player;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @创建者 zoujian
 * @创建时间 2018/7/9
 * @描述
 */
public class PlayerTest {
    @Test
    public void test() {
        PlayerBase playerBase = new PlayerBase();
        InfoPhysical infoPhysical = new InfoPhysical(new Physical("192.168.1.1", "深圳", System.currentTimeMillis() / 1000));
        InfoPersonal infoPersonal = new InfoPersonal(new Personal(1111, 1, "nizou", "http", "1234"));

        playerBase.Add(infoPersonal);
        playerBase.Add(infoPhysical);
        playerBase.Add(infoPhysical);
        infoPhysical = new InfoPhysical(new Physical("192.168.1.1", "湖南", System.currentTimeMillis() / 1000));
        playerBase.Add(infoPhysical);
        String str = playerBase.ToString();
        System.out.println(str);


        InfoPhysical tmp = (InfoPhysical) playerBase.Get("InfoPhysical");
        System.out.println(tmp.getPhysical().getAddress());
    }
}
