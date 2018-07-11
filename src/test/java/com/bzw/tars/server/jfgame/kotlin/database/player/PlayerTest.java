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
    public void test(){
        Player player = new Player();
        InfoPhysical infoPhysical = new InfoPhysical(new Physical( "192.168.1.1","深圳"));
        InfoPersonal infoPersonal = new InfoPersonal(new Personal(1111,1,"nizou","http","1234"));

        player.Add(infoPersonal);
        player.Add(infoPhysical);
        player.Add(infoPhysical);
        infoPhysical = new InfoPhysical(new Physical( "192.168.1.1","湖南"));
        player.Add(infoPhysical);
        String str  = player.ToString();
        System.out.println(str);


        InfoPhysical tmp = (InfoPhysical)player.Get("InfoPhysical");
        System.out.println(tmp.getPhysical().getAddress());
    }
}
