// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.tarsgame;

import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.annotation.*;
import com.qq.tars.common.support.Holder;

@Servant
public interface IGameMessagePrx {
	/**
	 * 通往游戏的协议（单向）
	 * @return 0：正常，>0：请求其他指令（该值，消息数据为空）, <0：错误码
	 */
	public short doGameMessage(TReqMessage tReqMessage, @TarsHolder Holder<TRespMessage> tRespMessage);
	/**
	 * 通往游戏的协议（单向）
	 * @return 0：正常，>0：请求其他指令（该值，消息数据为空）, <0：错误码
	 */
	public short doGameMessage(TReqMessage tReqMessage, @TarsHolder Holder<TRespMessage> tRespMessage, @TarsContext java.util.Map<String, String> ctx);
	/**
	 * 通往游戏的协议（单向）
	 * @return 0：正常，>0：请求其他指令（该值，消息数据为空）, <0：错误码
	 */
	public void async_doGameMessage(@TarsCallback IGameMessagePrxCallback callback, TReqMessage tReqMessage);
	/**
	 * 通往游戏的协议（单向）
	 * @return 0：正常，>0：请求其他指令（该值，消息数据为空）, <0：错误码
	 */
	public void async_doGameMessage(@TarsCallback IGameMessagePrxCallback callback, TReqMessage tReqMessage, @TarsContext java.util.Map<String, String> ctx);
}
