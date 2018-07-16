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
public class TMsgReqStandUp {

	@TarsStructProperty(order = 0, isRequire = false)
	public byte placeholder = 0;

	public byte getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(byte placeholder) {
		this.placeholder = placeholder;
	}

	public TMsgReqStandUp() {
	}

	public TMsgReqStandUp(byte placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(placeholder);
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
		if (!(obj instanceof TMsgReqStandUp)) {
			return false;
		}
		TMsgReqStandUp other = (TMsgReqStandUp) obj;
		return (
			TarsUtil.equals(placeholder, other.placeholder) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(placeholder, 0);
	}


	public void readFrom(TarsInputStream _is) {
		this.placeholder = _is.read(placeholder, 0, false);
	}

}
