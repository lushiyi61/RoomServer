// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.jfgame;

import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.annotation.*;
import com.qq.tars.common.support.Holder;

@Servant
public interface TarsRoomPrx {

	public String hello();

	public String hello(@TarsContext java.util.Map<String, String> ctx);

	public void async_hello(@TarsCallback TarsRoomPrxCallback callback);

	public void async_hello(@TarsCallback TarsRoomPrxCallback callback, @TarsContext java.util.Map<String, String> ctx);

	public int onRequest(long lUin, String sMsgPack, String sCurServantAddr, TClientParam stClientParam, UserBaseInfoExt stUerBaseInfo);

	public int onRequest(long lUin, String sMsgPack, String sCurServantAddr, TClientParam stClientParam, UserBaseInfoExt stUerBaseInfo, @TarsContext java.util.Map<String, String> ctx);

	public void async_onRequest(@TarsCallback TarsRoomPrxCallback callback, long lUin, String sMsgPack, String sCurServantAddr, TClientParam stClientParam, UserBaseInfoExt stUerBaseInfo);

	public void async_onRequest(@TarsCallback TarsRoomPrxCallback callback, long lUin, String sMsgPack, String sCurServantAddr, TClientParam stClientParam, UserBaseInfoExt stUerBaseInfo, @TarsContext java.util.Map<String, String> ctx);

	public int onOffLine(long lUin);

	public int onOffLine(long lUin, @TarsContext java.util.Map<String, String> ctx);

	public void async_onOffLine(@TarsCallback TarsRoomPrxCallback callback, long lUin);

	public void async_onOffLine(@TarsCallback TarsRoomPrxCallback callback, long lUin, @TarsContext java.util.Map<String, String> ctx);
}
