package com.bzw.tars.server.jfgame.impl;

import com.bzw.tars.server.jfgame.kotlin.database.AppData;
import com.bzw.tars.server.jfgame.kotlin.database.player.InfoPhysical;
import com.bzw.tars.server.jfgame.kotlin.database.player.Physical;
import com.bzw.tars.server.jfgame.kotlin.database.player.Player;
import com.bzw.tars.server.jfgame.kotlin.database.player.PlayerMng;
import com.bzw.tars.server.jfgame.kotlin.impl.Test;
import com.bzw.tars.server.jfgame.kotlin.logic.MainRouter;
import com.bzw.tars.server.tars.jfgame.TClientParam;
import com.bzw.tars.server.tars.jfgame.TUserBaseInfoExt;
import com.bzw.tars.server.tars.jfgame.TarsRoomServant;
import com.bzw.tars.server.tars.jfgameclientproto.TPackage;
import com.qq.tars.protocol.tars.TarsInputStream;

public class TarsRoomServantImpl implements TarsRoomServant {
    private MainRouter mainRouter = new MainRouter();

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
            // 玩家数据不存在
            if (PlayerMng.Companion.getInstance().getPlayer(lUin) == null) {
                Player player = new Player();
                InfoPhysical infoPhysical = new InfoPhysical(new Physical(stClientParam.sAddr, "", System.currentTimeMillis() / 1000));
                player.Add(infoPhysical);
                PlayerMng.Companion.getInstance().addPlayer(lUin, player);
            }
            // 处理玩家消息
            this.mainRouter.onMessage(lUin, tPackage.vecMsgHead.get(i).nMsgID, tPackage.vecMsgData.get(i));

        }

        return ret;
    }

    @Override
    public int onOffLine(long lUin) {
        return 0;
    }
}
