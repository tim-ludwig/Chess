package me.tludwig.chess.game.pieces;

import me.tludwig.chess.game.Step;

public enum Alliance {
	BLACK(new Step[]{Step.UP_LEFT, Step.UP_RIGHT}),
	WHITE(new Step[]{Step.DOWN_LEFT, Step.DOWN_RIGHT});

	public final Step[] pawnCapture;

	Alliance(Step[] pawnCapture) {
		this.pawnCapture = pawnCapture;
	}

	public Alliance other() {
		return this == WHITE ? BLACK : WHITE;
	}

	public int pawnRank() {
		return this == WHITE ? 1 : 6;
	}

	public int backRank() {
		return this == WHITE ? 0 : 7;
	}

	public Step pawnStep() {
		return this == WHITE ? Step.DOWN : Step.UP;
	}
}
