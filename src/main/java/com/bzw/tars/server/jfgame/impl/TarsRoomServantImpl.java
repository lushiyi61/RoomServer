package com.bzw.tars.server.jfgame.impl;

import com.bzw.tars.server.jfgame.kotlin.database.player.*;
import com.bzw.tars.server.jfgame.kotlin.logic.MainRouter;
import com.bzw.tars.server.tars.jfgame.TClientParam;
import com.bzw.tars.server.tars.jfgame.TUserBaseInfoExt;
import com.bzw.tars.server.tars.jfgame.TarsRoomServant;
import com.bzw.tars.server.tars.jfgameclientproto.TReqPackage;
import com.qq.tars.protocol.tars.TarsInputStream;

public class TarsRoomServantImpl implements TarsRoomServant {
    private MainRouter mainRouter = new MainRouter();

    @Override
    public String hello() {
        return String.format("hello , time=%s", System.currentTimeMillis());
    }

    @Override
    public int onRequest(long lUin, byte[] sMsgPack, TClientParam stClientParam, TUserBaseInfoExt stUerBaseInfo) {
        System.out.println("Recv msg..., playerID:" + lUin + ",IP:" + stClientParam.sAddr);

        int ret = 0;
        // 解码
        TReqPackage tReqPackage = new TReqPackage();
        TarsInputStream tarsInputStream = new TarsInputStream(sMsgPack);
        tReqPackage.readFrom(tarsInputStream);

        // 循环处理指令
        for (int i = 0; i < tReqPackage.vecMsgID.size(); i++) {
            // 玩家数据不存在
            if (PlayerMng.Companion.getInstance().getPlayer(lUin) == null) {
                PlayerBase playerBase = new PlayerBase();
                // 增加玩家物理信息
                CPlayerPhysicalInfo cPlayerPhysicalInfo = new CPlayerPhysicalInfo(new PlayerPhysicalInfo(stClientParam.sAddr, "", System.currentTimeMillis() / 1000));
                // 增加玩家基本信息
                CPlayerPersonalInfo cPlayerPersonalInfo = new CPlayerPersonalInfo(new PlayerPersonalInfo(lUin, (byte) 0, "SB" + lUin, "0", ""));

                playerBase.addPlayerBase(cPlayerPhysicalInfo);
                playerBase.addPlayerBase(cPlayerPersonalInfo);
                PlayerMng.Companion.getInstance().addPlayer(lUin, playerBase);
            }
            // 处理玩家消息
            this.mainRouter.onMessage(lUin, tReqPackage.vecMsgID.get(i), tReqPackage.vecMsgData.get(i));

        }

        return ret;
    }

    @Override
    public int onOffLine(long lUin) {
        System.out.println("Recv onOffLine, playerID:" + lUin);
        if(this.mainRouter.onOffLine(lUin))
        {
            PlayerMng.Companion.getInstance().removePlayer(lUin);
        }
        return 0;
    }
}
