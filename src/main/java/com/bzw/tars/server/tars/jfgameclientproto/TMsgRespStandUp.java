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
public class TMsgRespStandUp {

	@TarsStructProperty(order = 0, isRequire = true)
	public int iResultID = 0;

	public int getIResultID() {
		return iResultID;
	}

	public void setIResultID(int iResultID) {
		this.iResultID = iResultID;
	}

	public TMsgRespStandUp() {
	}

	public TMsgRespStandUp(int iResultID) {
		this.iResultID = iResultID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(iResultID);
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
		if (!(obj instanceof TMsgRespStandUp)) {
			return false;
		}
		TMsgRespStandUp other = (TMsgRespStandUp) obj;
		return (
			TarsUtil.equals(iResultID, other.iResultID) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(iResultID, 0);
	}


	public void readFrom(TarsInputStream _is) {
		this.iResultID = _is.read(iResultID, 0, true);
	}

}
