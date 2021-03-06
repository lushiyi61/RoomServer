// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.bzw.tars.server.tars.jfgameclientproto;

public enum E_RETCODE {

	E_COMMON_SUCCESS(0),
	E_PROTOCOL_ERROR(99),
	E_TABLE_ENTER_CUT_IN(100),
	E_TABLE_NOT_EXIST(101),
	E_PLAYER_NOT_EXIST(102),
	E_GAME_NOT_EXIST(103),
	E_TABLE_NO_ERROR(104),
	E_TABLE_IS_FULL(105),
	E_CHAIR_IS_FULL(106),
	E_SEAT_IS_TAKEN(107),
	E_PLAYER_NOT_SIT(108),
	E_PLAYER_IN_ROOM(109),
	E_NOT_ALLOW_DISMISS(110),
	E_TABLE_ENTER_ERROR(149),
	E_GAME_UNKNOWN_ERROR(150);

	private final int value;

	private E_RETCODE(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.name() + ":" + this.value;
	}

	public static E_RETCODE convert(int value) {
		for(E_RETCODE v : values()) {
			if(v.value() == value) {
				return v;
			}
		}
		return null;
	}
}
