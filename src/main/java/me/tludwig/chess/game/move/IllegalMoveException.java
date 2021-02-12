package me.tludwig.chess.game.move;

import java.io.Serial;

public class IllegalMoveException extends IllegalStateException {
	@Serial
	private static final long serialVersionUID = -4506135362253107257L;

	public IllegalMoveException() {
		super();
	}

	public IllegalMoveException(String string) {
		super(string);
	}
}
