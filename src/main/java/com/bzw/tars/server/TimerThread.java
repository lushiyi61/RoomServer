package com.bzw.tars.server;

/**
 * @创建者 zoujian
 * @创建时间 2018/7/24
 * @描述
 */

import com.bzw.tars.client.kotlin.ClientImpl;
import com.bzw.tars.client.kotlin.game.GameBase;
import com.bzw.tars.client.kotlin.game.GameMng;
import com.bzw.tars.client.tars.jfgame.TarsRouterPrx;
import com.bzw.tars.client.tars.jfgameclientproto.E_CLIENT_MSGID;
import com.bzw.tars.client.tars.jfgameclientproto.TRespPackage;
import com.bzw.tars.client.tars.tarsgame.IGameMessagePrx;
import com.bzw.tars.server.jfgame.kotlin.database.table.TableBase;
import com.bzw.tars.server.jfgame.kotlin.database.table.TableMng;
import com.bzw.tars.server.jfgame.kotlin.timer.TimerBase;
import com.bzw.tars.server.jfgame.kotlin.timer.TimerMng;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.*;

class TimerThread extends Thread {
    private int bufferTime = 2; // 释放的检查间隔 单位：秒
    private int sleepTime = 500; // 释放的检查间隔 单位：毫秒

    private long checkTime = System.currentTimeMillis() / 1000;

    @Override
    public void run() {
        System.out.println(String.format("================游戏超时线程->GameID:%s================"));
        try {
            while (true) {
                sleep(sleepTime);
                long nowTime = System.currentTimeMillis() / 1000;
                if (checkTime == nowTime) {
                    continue;
                }
                checkTime = nowTime;
                // 每秒检查一次
                Map<String, TimerBase> timerDict = TimerMng.Companion.getInstance().getTimerDict();

                for (TimerBase timerBase : timerDict.values()) {
                    if (timerBase.checkTimeout(checkTime + bufferTime)) {
                        // 停用当前定时器
                        timerBase.setState(false);
                        // 异步发送超时信号
                        GameBase gameBase = GameMng.Companion.getInstance().getGame(timerBase.getGameID());
                        if (gameBase != null) {
//                        iGameMessagePrx.async_doGameMessage();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * @description 处理超时结果，推送给客户端
     * =====================================
     * @author zoujian
     * @date 2018/7/24 19:10
     */
    private void doTimeoutNotifyClient() {
        TarsRouterPrx tarsRouterPrx = ClientImpl.Companion.getInstance().getDoPushPrx();
        TRespPackage tRespPackage = new TRespPackage(new ArrayList(), new ArrayList());
        tRespPackage.vecMsgID.add((short) (0 - E_CLIENT_MSGID.E_GAME_ACTION.value()));
//        tRespPackage.vecMsgData.add(msgData);

        TableBase tableBase = TableMng.Companion.getInstance().getTable("");
        if (tableBase == null) {
            return;
        }
        for (TableBase.TablePlayer playerInfo : tableBase.getPlayerDict().values()) {
            tarsRouterPrx.doPush(playerInfo.getUid(), tRespPackage);
        }
    }
}