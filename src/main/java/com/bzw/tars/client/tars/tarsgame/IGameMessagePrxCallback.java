// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.tars.tarsgame;

import com.qq.tars.rpc.protocol.tars.support.TarsAbstractCallback;

public abstract class IGameMessagePrxCallback extends TarsAbstractCallback {

	public abstract void callback_doRoomMessage(short ret, TRespMessage tRespMessage);

	public abstract void callback_doClientMessage(short ret, TRespMessage tRespMessage);

}
