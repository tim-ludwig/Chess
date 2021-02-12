package me.tludwig.chess.game.move;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.Position;
import me.tludwig.chess.game.pieces.Alliance;

import java.util.function.UnaryOperator;

public abstract class AbstractMove implements UnaryOperator<Board> {
	public final Alliance allianceToMove;

	public AbstractMove(Alliance allianceToMove) {
		this.allianceToMove = allianceToMove;
	}

	public static AbstractMove fromPositions(Board b, Position from, Position to) {
		return Move.fromPositions(b, from, to);
	}

	public static AbstractMove fromSAN(String move, Board b) {
		if ("0-0".equals(move)) {
			return new Castle(b.toMove(), true);
		}
		if ("0-0-0".equals(move)) {
			return new Castle(b.toMove(), false);
		}

		return Move.fromSAN(move, b);
	}

	public static AbstractMove fromLAN(String move, Alliance allianceToMove) {
		if ("0-0".equals(move)) {
			return new Castle(allianceToMove, true);
		}
		if ("0-0-0".equals(move)) {
			return new Castle(allianceToMove, false);
		}

		return Move.fromLAN(move, allianceToMove);
	}

	public final boolean isLegal(Board board) {
		if (board.toMove() != allianceToMove) {
			return false;
		}

		if (!check(board)) {
			return false;
		}

		if (apply(board.copy()).isInCheck(allianceToMove)) {
			return false;
		}

		return true;
	}

	protected abstract boolean check(Board board);

	@Override
	public abstract Board apply(Board board);
}
