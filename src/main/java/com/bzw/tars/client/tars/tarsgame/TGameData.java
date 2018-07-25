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
 * 游戏返回数据
 * =======================
 * 根据EGameMsgType取下列数据，详细见枚举说明
 */
@TarsStruct
public class TGameData {

	@TarsStructProperty(order = 0, isRequire = false)
	public TData tRespOneData = null;
	@TarsStructProperty(order = 1, isRequire = false)
	public TData tNotifyData = null;
	@TarsStructProperty(order = 2, isRequire = false)
	public java.util.List<TData> vecRespAllData = null;

	public TData getTRespOneData() {
		return tRespOneData;
	}

	public void setTRespOneData(TData tRespOneData) {
		this.tRespOneData = tRespOneData;
	}

	public TData getTNotifyData() {
		return tNotifyData;
	}

	public void setTNotifyData(TData tNotifyData) {
		this.tNotifyData = tNotifyData;
	}

	public java.util.List<TData> getVecRespAllData() {
		return vecRespAllData;
	}

	public void setVecRespAllData(java.util.List<TData> vecRespAllData) {
		this.vecRespAllData = vecRespAllData;
	}

	public TGameData() {
	}

	public TGameData(TData tRespOneData, TData tNotifyData, java.util.List<TData> vecRespAllData) {
		this.tRespOneData = tRespOneData;
		this.tNotifyData = tNotifyData;
		this.vecRespAllData = vecRespAllData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(tRespOneData);
		result = prime * result + TarsUtil.hashCode(tNotifyData);
		result = prime * result + TarsUtil.hashCode(vecRespAllData);
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
		if (!(obj instanceof TGameData)) {
			return false;
		}
		TGameData other = (TGameData) obj;
		return (
			TarsUtil.equals(tRespOneData, other.tRespOneData) &&
			TarsUtil.equals(tNotifyData, other.tNotifyData) &&
			TarsUtil.equals(vecRespAllData, other.vecRespAllData) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		if (null != tRespOneData) {
			_os.write(tRespOneData, 0);
		}
		if (null != tNotifyData) {
			_os.write(tNotifyData, 1);
		}
		if (null != vecRespAllData) {
			_os.write(vecRespAllData, 2);
		}
	}

	static TData cache_tRespOneData;
	static { 
		cache_tRespOneData = new TData();
	}
	static TData cache_tNotifyData;
	static { 
		cache_tNotifyData = new TData();
	}
	static java.util.List<TData> cache_vecRespAllData;
	static { 
		cache_vecRespAllData = new java.util.ArrayList<TData>();
		TData var_12 = new TData();
		cache_vecRespAllData.add(var_12);
	}

	public void readFrom(TarsInputStream _is) {
		this.tRespOneData = (TData) _is.read(cache_tRespOneData, 0, false);
		this.tNotifyData = (TData) _is.read(cache_tNotifyData, 1, false);
		this.vecRespAllData = (java.util.List<TData>) _is.read(cache_vecRespAllData, 2, false);
	}

}
