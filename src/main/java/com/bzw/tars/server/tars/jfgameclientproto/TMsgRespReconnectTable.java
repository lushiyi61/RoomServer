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
public class TMsgRespReconnectTable {

	@TarsStructProperty(order = 0, isRequire = true)
	public int iResultID = 0;
	@TarsStructProperty(order = 1, isRequire = true)
	public byte nChairNo = (byte)0;
	@TarsStructProperty(order = 2, isRequire = true)
	public byte nState = (byte)0;
	@TarsStructProperty(order = 3, isRequire = false)
	public byte[] vecGameInfo = null;

	public int getIResultID() {
		return iResultID;
	}

	public void setIResultID(int iResultID) {
		this.iResultID = iResultID;
	}

	public byte getNChairNo() {
		return nChairNo;
	}

	public void setNChairNo(byte nChairNo) {
		this.nChairNo = nChairNo;
	}

	public byte getNState() {
		return nState;
	}

	public void setNState(byte nState) {
		this.nState = nState;
	}

	public byte[] getVecGameInfo() {
		return vecGameInfo;
	}

	public void setVecGameInfo(byte[] vecGameInfo) {
		this.vecGameInfo = vecGameInfo;
	}

	public TMsgRespReconnectTable() {
	}

	public TMsgRespReconnectTable(int iResultID, byte nChairNo, byte nState, byte[] vecGameInfo) {
		this.iResultID = iResultID;
		this.nChairNo = nChairNo;
		this.nState = nState;
		this.vecGameInfo = vecGameInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(iResultID);
		result = prime * result + TarsUtil.hashCode(nChairNo);
		result = prime * result + TarsUtil.hashCode(nState);
		result = prime * result + TarsUtil.hashCode(vecGameInfo);
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
		if (!(obj instanceof TMsgRespReconnectTable)) {
			return false;
		}
		TMsgRespReconnectTable other = (TMsgRespReconnectTable) obj;
		return (
			TarsUtil.equals(iResultID, other.iResultID) &&
			TarsUtil.equals(nChairNo, other.nChairNo) &&
			TarsUtil.equals(nState, other.nState) &&
			TarsUtil.equals(vecGameInfo, other.vecGameInfo) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(iResultID, 0);
		_os.write(nChairNo, 1);
		_os.write(nState, 2);
		if (null != vecGameInfo) {
			_os.write(vecGameInfo, 3);
		}
	}

	static byte[] cache_vecGameInfo;
	static { 
		cache_vecGameInfo = new byte[1];
		byte var_5 = (byte)0;
		cache_vecGameInfo[0] = var_5;
	}

	public void readFrom(TarsInputStream _is) {
		this.iResultID = _is.read(iResultID, 0, true);
		this.nChairNo = _is.read(nChairNo, 1, true);
		this.nState = _is.read(nState, 2, true);
		this.vecGameInfo = (byte[]) _is.read(cache_vecGameInfo, 3, false);
	}

}
