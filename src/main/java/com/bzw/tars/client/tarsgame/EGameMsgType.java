// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.tarsgame;

public enum EGameMsgType {

	ENOTIFYDATA(0),
	ERESPONEDATA(1),
	ERESPALLDATA(2),
	EMIXTUREDATA(3);

	private final int value;

	private EGameMsgType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.name() + ":" + this.value;
	}

	public static EGameMsgType convert(int value) {
		for(EGameMsgType v : values()) {
			if(v.value() == value) {
				return v;
			}
		}
		return null;
	}
}
