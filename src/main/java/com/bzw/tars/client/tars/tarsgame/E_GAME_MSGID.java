// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.client.tars.tarsgame;

public enum E_GAME_MSGID {

	GAMECREATE(11),
	GAMESTART(12),
	GAMETIMEOUT(13),
	GAMEFINISH(14),
	GAMEOVER(15),
	GAMEDISMISS(16),
	GAMERESULT(17),
	GAMEACTION(18);

	private final int value;

	private E_GAME_MSGID(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.name() + ":" + this.value;
	}

	public static E_GAME_MSGID convert(int value) {
		for(E_GAME_MSGID v : values()) {
			if(v.value() == value) {
				return v;
			}
		}
		return null;
	}
}
