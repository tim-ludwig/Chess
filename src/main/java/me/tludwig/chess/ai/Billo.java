package me.tludwig.chess.ai;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.pieces.Piece;

public class Billo extends MinimaxAI {
	private static final long TIME = 4000;

	@Override
	public double evaluate(Board board) {
		double score = 0;

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Piece current = board.pieceAt(file, rank);

				if (current != null)
					score += current.value();
			}
		}

		return score;
	}
}

