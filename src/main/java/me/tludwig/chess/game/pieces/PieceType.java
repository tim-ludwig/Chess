package me.tludwig.chess.game.pieces;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.Position;
import me.tludwig.chess.game.Step;

import java.util.Arrays;
import java.util.stream.Stream;

public enum PieceType {
	KING("K", 0, Step.KING, 1),
	QUEEN("Q", 9, Step.QUEEN),
	ROOK("R", 5, Step.ROOK),
	BISHOP("B", 3, Step.BISHOP),
	KNIGHT("N", 3, Step.KNIGHT, 1),
	PAWN("", 1) {
		@Override
		public Stream<Position> possibleStart(Board board, Position position, Alliance alliance, boolean capture) {
			if (capture) return super.possibleStart(board, position, alliance, true);

			return board.line(position, alliance.other().pawnStep(), position.rank() == (alliance == Alliance.WHITE ? 3 : 4) ? 2 : 1).filter(pos -> {
				Piece target = board.pieceAt(pos);

				return target != null && target.type == this && target.alliance == alliance;
			});
		}

		@Override
		public Stream<Position> possibleMoves(Board board, Position pos, Alliance alliance) {
			return board.lineMove(pos, alliance.pawnStep(), pos.rank() == alliance.pawnRank() ? 2 : 1);
		}

		@Override
		public Stream<Position> possibleCaptures(Board board, Position pos, Alliance alliance) {
			Stream.Builder<Position> sb = Stream.builder();

			possibleCaptures(board, pos, alliance, alliance.pawnCapture, 1).forEach(sb::add);

			if (pos.offset(alliance.pawnCapture[0]).equals(board.getEnPassant()) || pos.offset(alliance.pawnCapture[1]).equals(board.getEnPassant())) {
				sb.add(board.getEnPassant());
			}

			return sb.build();
		}
	};

	public final String symbol;
	public final int value, maxStepNum;
	public final Step[] stepPattern;

	@SuppressWarnings("SameParameterValue")
	PieceType(String symbol, int value) {
		this(symbol, value, null);
	}

	PieceType(String symbol, int value, Step[] stepPattern) {
		this(symbol, value, stepPattern, 7);
	}

	PieceType(String symbol, int value, Step[] stepPattern, int maxStepNum) {
		this.symbol = symbol;
		this.value = value;
		this.stepPattern = stepPattern;
		this.maxStepNum = maxStepNum;
	}

	public char unicodeFigurine(Alliance c) {
		return (char) ((c == Alliance.BLACK ? '♚' : '♔') + ordinal());
	}

	public static PieceType[] promotableTo() {
		return new PieceType[]{KNIGHT, BISHOP, ROOK, QUEEN};
	}

	public static PieceType fromLetter(String letter) {
		for (PieceType type : values()) {
			if (type.symbol.equals(letter)) {
				return type;
			}
		}

		return null;
	}

	public Stream<Position> possibleStart(Board board, Position position, Alliance alliance, boolean capture) {
		return possibleCaptures(board, position, alliance.other()).filter(pos -> board.pieceAt(pos).type == this);
	}

	public Stream<Position> possibleMoves(Board board, Position pos, Alliance alliance, boolean isCapture) {
		return isCapture ? possibleCaptures(board, pos, alliance) : possibleMoves(board, pos, alliance);
	}

	public Stream<Position> possibleMoves(Board board, Position pos, Alliance alliance) {
		return possibleMoves(board, pos, stepPattern, maxStepNum);
	}

	public Stream<Position> possibleCaptures(Board board, Position pos, Alliance alliance) {
		return possibleCaptures(board, pos, alliance, stepPattern, maxStepNum);
	}

	protected static Stream<Position> possibleMoves(Board board, Position pos, Step[] steps, int maxStepNum) {
		return Arrays.stream(steps).flatMap(step -> board.lineMove(pos, step, maxStepNum));
	}

	protected static Stream<Position> possibleCaptures(Board board, Position pos, Alliance alliance, Step[] steps, int maxStepNum) {
		return Arrays.stream(steps).flatMap(step -> board.lineCapture(pos, step, maxStepNum, alliance));
	}
}
