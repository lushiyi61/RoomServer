// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.tars.tarsgame;

import com.qq.tars.protocol.util.*;
import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.*;
import com.qq.tars.protocol.tars.annotation.*;

/**
 * 返回数据
 * nTimeout 0：无动作 >0：更新该桌定时器 <0：销毁该桌定时器
 */
@TarsStruct
public class TRespMessage {

	@TarsStructProperty(order = 0, isRequire = true)
	public short nTimeout = 0;
	@TarsStructProperty(order = 1, isRequire = true)
	public java.util.List<TGameData> vecGameData = null;

	public short getNTimeout() {
		return nTimeout;
	}

	public void setNTimeout(short nTimeout) {
		this.nTimeout = nTimeout;
	}

	public java.util.List<TGameData> getVecGameData() {
		return vecGameData;
	}

	public void setVecGameData(java.util.List<TGameData> vecGameData) {
		this.vecGameData = vecGameData;
	}

	public TRespMessage() {
	}

	public TRespMessage(short nTimeout, java.util.List<TGameData> vecGameData) {
		this.nTimeout = nTimeout;
		this.vecGameData = vecGameData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(nTimeout);
		result = prime * result + TarsUtil.hashCode(vecGameData);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TRespMessage)) {
			return false;
		}
		TRespMessage other = (TRespMessage) obj;
		return (
			TarsUtil.equals(nTimeout, other.nTimeout) &&
			TarsUtil.equals(vecGameData, other.vecGameData) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(nTimeout, 0);
		_os.write(vecGameData, 1);
	}

	static java.util.List<TGameData> cache_vecGameData;
	static { 
		cache_vecGameData = new java.util.ArrayList<TGameData>();
		TGameData var_15 = new TGameData();
		cache_vecGameData.add(var_15);
	}

	public void readFrom(TarsInputStream _is) {
		this.nTimeout = _is.read(nTimeout, 0, true);
		this.vecGameData = (java.util.List<TGameData>) _is.read(cache_vecGameData, 1, true);
	}

}
