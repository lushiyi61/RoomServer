// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.server.tars.jfgameclientproto;

import com.qq.tars.protocol.util.*;
import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.*;
import com.qq.tars.protocol.tars.annotation.*;

@TarsStruct
public class TMsgRespEnterTable {

	@TarsStructProperty(order = 0, isRequire = true)
	public int iResultID = 0;
	@TarsStructProperty(order = 1, isRequire = false)
	public long lMasterID = 0L;
	@TarsStructProperty(order = 2, isRequire = false)
	public java.util.List<TPlayerInfo> vecPlayerInfo = null;

	public int getIResultID() {
		return iResultID;
	}

	public void setIResultID(int iResultID) {
		this.iResultID = iResultID;
	}

	public long getLMasterID() {
		return lMasterID;
	}

	public void setLMasterID(long lMasterID) {
		this.lMasterID = lMasterID;
	}

	public java.util.List<TPlayerInfo> getVecPlayerInfo() {
		return vecPlayerInfo;
	}

	public void setVecPlayerInfo(java.util.List<TPlayerInfo> vecPlayerInfo) {
		this.vecPlayerInfo = vecPlayerInfo;
	}

	public TMsgRespEnterTable() {
	}

	public TMsgRespEnterTable(int iResultID, long lMasterID, java.util.List<TPlayerInfo> vecPlayerInfo) {
		this.iResultID = iResultID;
		this.lMasterID = lMasterID;
		this.vecPlayerInfo = vecPlayerInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(iResultID);
		result = prime * result + TarsUtil.hashCode(lMasterID);
		result = prime * result + TarsUtil.hashCode(vecPlayerInfo);
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
		if (!(obj instanceof TMsgRespEnterTable)) {
			return false;
		}
		TMsgRespEnterTable other = (TMsgRespEnterTable) obj;
		return (
			TarsUtil.equals(iResultID, other.iResultID) &&
			TarsUtil.equals(lMasterID, other.lMasterID) &&
			TarsUtil.equals(vecPlayerInfo, other.vecPlayerInfo) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(iResultID, 0);
		_os.write(lMasterID, 1);
		if (null != vecPlayerInfo) {
			_os.write(vecPlayerInfo, 2);
		}
	}

	static java.util.List<TPlayerInfo> cache_vecPlayerInfo;
	static { 
		cache_vecPlayerInfo = new java.util.ArrayList<TPlayerInfo>();
		TPlayerInfo var_4 = new TPlayerInfo();
		cache_vecPlayerInfo.add(var_4);
	}

	public void readFrom(TarsInputStream _is) {
		this.iResultID = _is.read(iResultID, 0, true);
		this.lMasterID = _is.read(lMasterID, 1, false);
		this.vecPlayerInfo = (java.util.List<TPlayerInfo>) _is.read(cache_vecPlayerInfo, 2, false);
	}

}