package me.tludwig.chess.ai;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.game.pieces.Alliance;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MinimaxAI extends ChessAI {
	private static final int MAX_DEPTH = 4;
	private static final Function<BoardMovePair, List<BoardMovePair>> TREE_EXPANSION = pair -> pair.board.allPossibleMoves().map(move -> new BoardMovePair(move, pair.board.copy().apply(move))).collect(Collectors.toList());

	@Override
	public AbstractMove predictMove(Board board) {
		Minimax<BoardMovePair> minimax = new Minimax<>(pair -> pair.board.ended() ? evaluateLeaf(pair.board) : evaluate(pair.board), TREE_EXPANSION);

		return minimax.minimax(new BoardMovePair(null, board), MAX_DEPTH, board.toMove() == Alliance.WHITE).element.move;
	}

	public static class BoardMovePair {
		final AbstractMove move;
		final Board board;

		public BoardMovePair(AbstractMove move, Board board) {
			this.move = move;
			this.board = board;
		}
	}
}
