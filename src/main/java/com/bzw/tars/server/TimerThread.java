package com.bzw.tars.server;

/**
 * @创建者 zoujian
 * @创建时间 2018/7/24
 * @描述
 */

import com.bzw.tars.client.kotlin.ClientPrxMng;
import com.bzw.tars.client.tars.tarsgame.E_GAME_MSGID;
import com.bzw.tars.client.tars.tarsgame.IGameMessagePrx;
import com.bzw.tars.client.tars.tarsgame.TReqRoomMsg;
import com.bzw.tars.server.jfgame.kotlin.logic.GameCallback;
import com.bzw.tars.server.jfgame.kotlin.timer.TimerBase;
import com.bzw.tars.server.jfgame.kotlin.timer.TimerMng;

import java.util.Map;

class TimerThread extends Thread {
    private int bufferTime = 2; // 释放的检查间隔 单位：秒
    private int sleepTime = 500; // 释放的检查间隔 单位：毫秒

    private long checkTime = System.currentTimeMillis() / 1000;

    @Override
    public void run() {
        System.out.println(String.format("================游戏超时线程==================="));
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
                    // 游戏超时检查
                    if (timerBase.checkGameTimeout(checkTime + bufferTime)) {
                        // 停用当前定时器
                        timerBase.setState(false);
                        // 异步发送超时信号
                        IGameMessagePrx gamePrx = (IGameMessagePrx)ClientPrxMng.Companion.getInstance().getClientPrx(String.valueOf(timerBase.getGameID()));
                        if (gamePrx != null) {
                            TReqRoomMsg tReqMessage = new TReqRoomMsg();
                            tReqMessage.nMsgID = (short) E_GAME_MSGID.GAMETIMEOUT.value();
                            tReqMessage.sTableNo = timerBase.getTableNo();
                            gamePrx.async_doRoomMessage(new GameCallback(timerBase.getTableNo(), timerBase.getGameID(), (byte) -1), tReqMessage);
                        }
                    }

                    // 投票解散超时检查
                    if (timerBase.checkRoomTimeout(checkTime)){
                        // 恢复定时器
                        timerBase.recoverTimeBase(checkTime);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}