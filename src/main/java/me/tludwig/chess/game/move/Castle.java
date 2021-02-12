package me.tludwig.chess.game.move;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.Position;
import me.tludwig.chess.game.pieces.Alliance;

public class Castle extends AbstractMove {
	private final Position kingFrom, kingTo, rookFrom, rookTo;
	private final boolean kingside;

	public Castle(Alliance alliance, boolean kingside) {
		super(alliance);
		this.kingside = kingside;

		int backRank = alliance.backRank();

		kingFrom = new Position(4, backRank);
		rookFrom = new Position(kingside ? 7 : 0, backRank);
		kingTo = new Position(kingside ? 6 : 2, backRank);
		rookTo = new Position(kingside ? 5 : 3, backRank);
	}

	@Override
	protected boolean check(Board board) {
		if (!board.canCastle(kingside, allianceToMove)) {
			return false;
		}
		if (board.pieceAt(rookFrom) == null) {
			return false;
		}
		if (board.pieceAt(kingFrom) == null) {
			return false;
		}
		if (board.isAttacked(rookTo, allianceToMove)) {
			return false;
		}
		if (board.isAttacked(kingFrom, allianceToMove)) {
			return false;
		}

		int backRank = allianceToMove.backRank();

		if (kingside) {
			if (board.pieceAt(5, backRank) != null) {
				return false;
			}
			if (board.pieceAt(6, backRank) != null) {
				return false;
			}
		} else {
			if (board.pieceAt(1, backRank) != null) {
				return false;
			}
			if (board.pieceAt(2, backRank) != null) {
				return false;
			}
			if (board.pieceAt(3, backRank) != null) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Board apply(Board board) {
		board.move(rookFrom, rookTo);
		board.move(kingFrom, kingTo);

		return board.nextMove();
	}
}
