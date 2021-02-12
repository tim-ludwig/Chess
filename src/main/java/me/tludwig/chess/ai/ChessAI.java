package me.tludwig.chess.ai;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.game.pieces.Alliance;

public abstract class ChessAI {
	public abstract AbstractMove predictMove(Board board);

	public abstract double evaluate(Board board);

	public final double evaluateLeaf(Board board) {
		assert board.ended();

		if (!board.isInCheck()) { // Stalemate
			return 0;
		}

		if (board.toMove() == Alliance.WHITE) { // Checkmate for Black
			return Double.NEGATIVE_INFINITY;
		}

		return Double.POSITIVE_INFINITY; // Checkmate for White
	}
}
