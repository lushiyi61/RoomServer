// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.jfgameclientproto;

public enum E_CLIENT_MSGID {

	E_TABLE_ENTER(1001),
	E_TABLE_LEAVE(1002),
	E_TABLE_RECONNECT(1003),
	E_TABLE_PREPARE(1004),
	E_TABLE_DISMISS(1005),
	E_TABLE_VOTE_DISMISS(1006),
	E_TABLE_CHAT_TEXT(1007),
	E_TABLE_CHAT_AUDIO(1008),
	E_TABLE_KICK_OUT(1009),
	E_GAME_ACTION(9999);

	private final int value;

	private E_CLIENT_MSGID(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.name() + ":" + this.value;
	}

	public static E_CLIENT_MSGID convert(int value) {
		for(E_CLIENT_MSGID v : values()) {
			if(v.value() == value) {
				return v;
			}
		}
		return null;
	}
}
