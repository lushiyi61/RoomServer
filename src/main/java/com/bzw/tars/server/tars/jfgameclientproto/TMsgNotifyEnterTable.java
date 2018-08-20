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
public class TMsgNotifyEnterTable {

	@TarsStructProperty(order = 0, isRequire = false)
	public TPlayerInfo tPlayerInfo = null;

	public TPlayerInfo getTPlayerInfo() {
		return tPlayerInfo;
	}

	public void setTPlayerInfo(TPlayerInfo tPlayerInfo) {
		this.tPlayerInfo = tPlayerInfo;
	}

	public TMsgNotifyEnterTable() {
	}

	public TMsgNotifyEnterTable(TPlayerInfo tPlayerInfo) {
		this.tPlayerInfo = tPlayerInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(tPlayerInfo);
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
		if (!(obj instanceof TMsgNotifyEnterTable)) {
			return false;
		}
		TMsgNotifyEnterTable other = (TMsgNotifyEnterTable) obj;
		return (
			TarsUtil.equals(tPlayerInfo, other.tPlayerInfo) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		if (null != tPlayerInfo) {
			_os.write(tPlayerInfo, 0);
		}
	}

	static TPlayerInfo cache_tPlayerInfo;
	static { 
		cache_tPlayerInfo = new TPlayerInfo();
	}

	public void readFrom(TarsInputStream _is) {
		this.tPlayerInfo = (TPlayerInfo) _is.read(cache_tPlayerInfo, 0, false);
	}

}
