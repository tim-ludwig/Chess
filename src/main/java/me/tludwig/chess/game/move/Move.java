package me.tludwig.chess.game.move;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.Position;
import me.tludwig.chess.game.pieces.Alliance;
import me.tludwig.chess.game.pieces.Piece;
import me.tludwig.chess.game.pieces.PieceType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static me.tludwig.chess.game.pieces.PieceType.PAWN;

public class Move extends AbstractMove {
	private static final Pattern MOVE_PATTERN = Pattern.compile("(?<from>[a-h][1-8])(?<capture>x)?(?<to>[a-h][1-8])(=(?<promote>[NBRQ]))?");
	private static final Pattern SAN_PATTERN = Pattern.compile("(?<type>[NBRQK]?)(?<fromFile>[a-h])?(?<fromRank>[1-8])?(?<capture>x)?(?<to>[a-h][1-8])(=(?<promote>[NBRQ]))?");

	private final Position from, to;
	private final boolean isCapture;
	private final PieceType promoteTo;

	public Move(Alliance allianceToMove, Position from, Position to) {
		this(allianceToMove, from, to, false, null);
	}

	public Move(Alliance allianceToMove, Position from, Position to, boolean isCapture) {
		this(allianceToMove, from, to, isCapture, null);
	}

	public Move(Alliance allianceToMove, Position from, Position to, PieceType promoteTo) {
		this(allianceToMove, from, to, false, promoteTo);
	}

	public Move(Alliance allianceToMove, Position from, Position to, boolean isCapture, PieceType promoteTo) {
		super(allianceToMove);

		this.from = from;
		this.to = to;
		this.isCapture = isCapture;
		this.promoteTo = promoteTo;
	}

	public static Move fromPositions(Board b, Position from, Position to) {
		return null; // TODO Implement
	}

	public static Move fromSAN(String move, Board board) {
		Matcher matcher = SAN_PATTERN.matcher(move);

		if (!matcher.matches()) {
			return null;
		}

		PieceType type = PieceType.fromLetter(matcher.group("type"));
		PieceType promote = PieceType.fromLetter(matcher.group("promote"));
		boolean capture = matcher.group("capture") != null;
		Position to = Position.fromString(matcher.group("to"));

		String rankStr = matcher.group("fromRank");
		String fileStr = matcher.group("fromFile");

		int rank = -1, file = -1;

		if (rankStr != null) rank = Position.rankFromChar(rankStr.charAt(0));
		if (fileStr != null) file = Position.fileFromChar(fileStr.charAt(0));

		List<Position> startCandidates = type.possibleStart(board, to, board.toMove(), capture).collect(Collectors.toList());
		Position from = null;

		if (fileStr == null && rankStr == null) {
			if (startCandidates.size() == 1) {
				from = startCandidates.get(0);
			}
		} else if (fileStr == null) {
			for (Position candidate : startCandidates) {
				if (candidate.rank() == rank) {
					from = candidate;
					break;
				}
			}
		} else if (rankStr == null) {
			for (Position candidate : startCandidates) {
				if (candidate.file() == file) {
					from = candidate;
					break;
				}
			}
		} else {
			from = new Position(file, rank);
		}

		if (from == null) {
			return null;
		}

		return new Move(board.toMove(), from, to, capture, promote);
	}

	public static Move fromLAN(String move, Alliance allianceToMove) {
		Matcher matcher = MOVE_PATTERN.matcher(move);

		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}

		PieceType type = PieceType.fromLetter(matcher.group("type"));
		PieceType promote = PieceType.fromLetter(matcher.group("promote"));
		boolean capture = matcher.group("capture") != null;
		Position from = Position.fromString(matcher.group("from"));
		Position to = Position.fromString(matcher.group("to"));

		return new Move(allianceToMove, from, to, capture, promote);
	}

	@Override
	protected boolean check(Board board) {
		Piece toMove = board.pieceAt(from);

		if (toMove == null) {
			return false;
		}
		if (toMove.alliance != allianceToMove) {
			return false;
		}

		if (toMove.type.possibleMoves(board, from, allianceToMove, isCapture).noneMatch(to::equals)) {
			return false;
		}

		if (promoteTo != null) {
			if (toMove.type != PieceType.PAWN) {
				return false;
			}
			if (to.rank() != allianceToMove.other().backRank()) {
				return false;
			}
			if (promoteTo == PieceType.KING || promoteTo == PieceType.PAWN) {
				return false;
			}
		} else if (toMove.type == PieceType.PAWN && to.rank() == allianceToMove.other().backRank()) {
			return false;
		}

		if (isCapture) {
			Piece toCapture;

			if (to.equals(board.getEnPassant()) && toMove.type == PAWN) {
				Position epPawn = to.offset(allianceToMove.other().pawnStep());
				toCapture = board.pieceAt(epPawn);
			} else {
				toCapture = board.pieceAt(to);
			}

			if (toCapture == null) {
				return false;
			}
			if (toCapture.type == PieceType.KING) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "" + from + (isCapture ? "x" : "") + to + (promoteTo != null ? "=" + promoteTo.symbol : "");
	}

	@Override
	public Board apply(Board board) {
		board.move(from, to);
		if (promoteTo != null) {
			board.put(to, promoteTo, allianceToMove);
		}

		return board.nextMove();
	}
}
