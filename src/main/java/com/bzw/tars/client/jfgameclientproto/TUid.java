// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.jfgameclientproto;

import com.qq.tars.protocol.util.*;
import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.*;
import com.qq.tars.protocol.tars.annotation.*;

@TarsStruct
public class TUid {

	@TarsStructProperty(order = 0, isRequire = true)
	public long lUid = 0L;
	@TarsStructProperty(order = 1, isRequire = false)
	public String sToken = "";

	public long getLUid() {
		return lUid;
	}

	public void setLUid(long lUid) {
		this.lUid = lUid;
	}

	public String getSToken() {
		return sToken;
	}

	public void setSToken(String sToken) {
		this.sToken = sToken;
	}

	public TUid() {
	}

	public TUid(long lUid, String sToken) {
		this.lUid = lUid;
		this.sToken = sToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(lUid);
		result = prime * result + TarsUtil.hashCode(sToken);
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
		if (!(obj instanceof TUid)) {
			return false;
		}
		TUid other = (TUid) obj;
		return (
			TarsUtil.equals(lUid, other.lUid) &&
			TarsUtil.equals(sToken, other.sToken) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(lUid, 0);
		if (null != sToken) {
			_os.write(sToken, 1);
		}
	}


	public void readFrom(TarsInputStream _is) {
		this.lUid = _is.read(lUid, 0, true);
		this.sToken = _is.readString(1, false);
	}

}