// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.tarsgame;

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
	public TData stRespOneData = null;
	@TarsStructProperty(order = 1, isRequire = false)
	public TData stNotifyData = null;
	@TarsStructProperty(order = 2, isRequire = false)
	public java.util.List<TData> vecRespAllData = null;

	public TData getStRespOneData() {
		return stRespOneData;
	}

	public void setStRespOneData(TData stRespOneData) {
		this.stRespOneData = stRespOneData;
	}

	public TData getStNotifyData() {
		return stNotifyData;
	}

	public void setStNotifyData(TData stNotifyData) {
		this.stNotifyData = stNotifyData;
	}

	public java.util.List<TData> getVecRespAllData() {
		return vecRespAllData;
	}

	public void setVecRespAllData(java.util.List<TData> vecRespAllData) {
		this.vecRespAllData = vecRespAllData;
	}

	public TGameData() {
	}

	public TGameData(TData stRespOneData, TData stNotifyData, java.util.List<TData> vecRespAllData) {
		this.stRespOneData = stRespOneData;
		this.stNotifyData = stNotifyData;
		this.vecRespAllData = vecRespAllData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(stRespOneData);
		result = prime * result + TarsUtil.hashCode(stNotifyData);
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
			TarsUtil.equals(stRespOneData, other.stRespOneData) &&
			TarsUtil.equals(stNotifyData, other.stNotifyData) &&
			TarsUtil.equals(vecRespAllData, other.vecRespAllData) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		if (null != stRespOneData) {
			_os.write(stRespOneData, 0);
		}
		if (null != stNotifyData) {
			_os.write(stNotifyData, 1);
		}
		if (null != vecRespAllData) {
			_os.write(vecRespAllData, 2);
		}
	}

	static TData cache_stRespOneData;
	static { 
		cache_stRespOneData = new TData();
	}
	static TData cache_stNotifyData;
	static { 
		cache_stNotifyData = new TData();
	}
	static java.util.List<TData> cache_vecRespAllData;
	static { 
		cache_vecRespAllData = new java.util.ArrayList<TData>();
		TData var_7 = new TData();
		cache_vecRespAllData.add(var_7);
	}

	public void readFrom(TarsInputStream _is) {
		this.stRespOneData = (TData) _is.read(cache_stRespOneData, 0, false);
		this.stNotifyData = (TData) _is.read(cache_stNotifyData, 1, false);
		this.vecRespAllData = (java.util.List<TData>) _is.read(cache_vecRespAllData, 2, false);
	}

}
