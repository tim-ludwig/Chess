package me.tludwig.chess.game;

import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.game.move.Castle;
import me.tludwig.chess.game.move.Move;
import me.tludwig.chess.game.pieces.Alliance;
import me.tludwig.chess.game.pieces.Piece;
import me.tludwig.chess.game.pieces.PieceType;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

import static me.tludwig.chess.game.pieces.Alliance.BLACK;
import static me.tludwig.chess.game.pieces.Alliance.WHITE;
import static me.tludwig.chess.game.pieces.PieceType.*;

public class Board {
	private static final Color DARK_SQUARE = Color.decode("#B58863");
	private static final Color LIGHT_SQUARE = Color.decode("#F0D9B5");
	private static final Color MARKED = new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), 0x4b);
	private final Piece[][] board = new Piece[8][8];
	private Alliance allianceToMove;
	private final Map<Alliance, List<PieceType>> capturedLists = new HashMap<>();
	private final List<Position> marked = new LinkedList<>();
	public final List<AbstractMove> moveHist;

	private final State state;

	public Board() {
		this(WHITE, new State(), new LinkedList<>());

		initPosition(WHITE, board);
		initPosition(BLACK, board);
	}

	private static void initPosition(Alliance c, Piece[][] board) {
		for (int file = 0; file < 8; file++) {
			board[c.pawnRank()][file] = new Piece(PAWN, c);
		}

		board[c.backRank()][0] = new Piece(ROOK, c);
		board[c.backRank()][1] = new Piece(KNIGHT, c);
		board[c.backRank()][2] = new Piece(BISHOP, c);
		board[c.backRank()][3] = new Piece(QUEEN, c);
		board[c.backRank()][4] = new Piece(KING, c);
		board[c.backRank()][5] = new Piece(BISHOP, c);
		board[c.backRank()][6] = new Piece(KNIGHT, c);
		board[c.backRank()][7] = new Piece(ROOK, c);
	}

	private Board(Board board) {
		this(board.allianceToMove, board.state, board.moveHist);

		for (int rank = 0; rank < 8; rank++) {
			System.arraycopy(board.board[rank], 0, this.board[rank], 0, 8);
		}
	}

	private Board(Alliance toMove, State state, List<AbstractMove> moveHist) {
		allianceToMove = toMove;
		this.state = state.copy();
		this.moveHist = new LinkedList<>();
		this.moveHist.addAll(moveHist);
	}

	public Board(Alliance toMove, Piece[][] board) {
		this(toMove, new State(), new LinkedList<>());

		for (int rank = 0; rank < 8; rank++) {
			System.arraycopy(board[rank], 0, this.board[rank], 0, 8);
		}
	}

	public Board copy() {
		return new Board(this);
	}

	public Alliance toMove() {
		return allianceToMove;
	}

	public Board nextMove() {
		allianceToMove = allianceToMove.other();

		return this;
	}

	public Piece pieceAt(int file, int rank) {
		return board[rank][file];
	}

	public Piece pieceAt(Position pos) {
		return pieceAt(pos.file(), pos.rank());
	}

	public boolean canCastle(boolean kingside, Alliance alliance) {
		return kingside ? canCastleKingside(alliance) : canCastleQueenside(alliance);
	}

	public boolean canCastleKingside(Alliance alliance) {
		return alliance == WHITE ? state.whiteKingCastle : state.blackKingCastle;
	}

	public boolean canCastleQueenside(Alliance alliance) {
		return alliance == WHITE ? state.whiteQueenCastle : state.blackQueenCastle;
	}

	public Position getEnPassant() {
		return state.enPassant;
	}

	public Stream<Position> getAttackers(Position pos, Alliance attacked) {
		return Arrays.stream(PieceType.values()).flatMap(type -> type.possibleCaptures(this, pos, attacked).filter(attPos -> {
			if (pieceAt(attPos) == null) {
				return false;
			} // edge-case related to e.p. TODO: Investigate

			return pieceAt(attPos).type == type;
		}));
	}

	public boolean isAttacked(Position pos, Alliance attacked) {
		return getAttackers(pos, attacked).findAny().isPresent();
	}

	public boolean isInCheck() {
		return isInCheck(allianceToMove);
	}

	public boolean isInCheck(Alliance toCheck) {
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Piece p = pieceAt(file, rank);

				if (p == null) {
					continue;
				}
				if (p.type == KING && p.alliance == toCheck) {
					return isAttacked(new Position(file, rank), toCheck);
				}
			}
		}

		assert false : "no king?";
		throw new IllegalStateException("no king?");
	}

	public boolean ended() {
		return allPossibleMoves().findAny().isEmpty();
	}

	public boolean isCheckMate() {
		return ended() && isInCheck();
	}

	public boolean isStaleMate() {
		return ended() && !isInCheck();
	}

	public Stream<AbstractMove> allPossibleMoves() {
		Stream.Builder<Stream<AbstractMove>> moveStreams = Stream.builder();

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Position pos = new Position(file, rank);
				Piece p = pieceAt(pos);

				if (p == null) {
					continue;
				}
				if (p.alliance != allianceToMove) {
					continue;
				}

				if (p.type == PAWN) {
					moveStreams.add(PAWN.possibleMoves(this, pos, allianceToMove).flatMap(to -> {
						if (to.rank() != allianceToMove.other().backRank()) {
							return Stream.of(new Move(allianceToMove, pos, to));
						}

						return Stream.of(PieceType.promotableTo()).map(promoteTo -> new Move(allianceToMove, pos, to, promoteTo));
					}));
					moveStreams.add(PAWN.possibleCaptures(this, pos, allianceToMove).flatMap(to -> {
						if (to.rank() != allianceToMove.other().backRank()) {
							return Stream.of(new Move(allianceToMove, pos, to, true));
						}

						return Stream.of(PieceType.promotableTo()).map(promoteTo -> new Move(allianceToMove, pos, to, true, promoteTo));
					}));
				} else {
					moveStreams.add(p.type.possibleMoves(this, pos, allianceToMove).map(to -> new Move(allianceToMove, pos, to)));
					moveStreams.add(p.type.possibleCaptures(this, pos, allianceToMove).map(to -> new Move(allianceToMove, pos, to, true)));
				}
			}
		}

		// add Castles
		moveStreams.add(Stream.of(new Castle(allianceToMove, true), new Castle(allianceToMove, false)));

		return moveStreams.build().flatMap(s -> s).filter(move -> move.isLegal(this));
	}

	public Stream<Position> line(Position position, Step step, int maxSteps) {
		return Stream.iterate(position.offset(step), Position::isInBounds, pos -> pos.offset(step))
				.limit(maxSteps);
	}

	public Stream<Position> lineMove(Position position, Step step, int maxSteps) {
		return line(position, step, maxSteps).takeWhile(pos -> pieceAt(pos) == null);
	}

	public Stream<Position> lineCapture(Position position, Step step, int maxSteps, Alliance alliance) {
		//@formatter:off
		return line(position, step, maxSteps)
				.dropWhile(pos -> pieceAt(pos) == null)
				.limit(1)
				.filter(pos -> pieceAt(pos).alliance != alliance);
		//@formatter:on
	}

	public void put(Position pos, PieceType type, Alliance alliance) {
		marked.add(pos);
		board[pos.rank()][pos.file()] = new Piece(type, alliance);
	}

	public void move(Position from, Position to) {
		Piece moving, captured;
		marked.add(from);
		marked.add(to);

		moving = pieceAt(from);

		if (to.equals(state.enPassant) && moving.type == PAWN) {
			Position epPawn = to.offset(allianceToMove.other().pawnStep());
			captured = pieceAt(epPawn);
			board[epPawn.rank()][epPawn.file()] = null;
		} else {
			captured = pieceAt(to);
		}

		if (captured != null) {
			capturedLists.computeIfAbsent(allianceToMove, __ -> new LinkedList<>()).add(captured.type);
		}

		board[to.rank()][to.file()] = moving;
		board[from.rank()][from.file()] = null;

		state.update(moving, from, to);
	}

	public Board apply(AbstractMove m) {
		moveHist.add(m);
		marked.clear();

		return m.apply(this);
	}

	@Override
	public String toString() {
		return toString(WHITE);
	}

	public String toString(Alliance perspective) {
		StringBuilder sb = new StringBuilder();
		String headline = "   " + String.join(" ", "a", "b", "c", "d", "e", "f", "g", "h");
		sb.append(headline);
		sb.append("\n ┌").append("─".repeat(8 * 2 + 1)).append("┐\n");

		for (int i = 0; i < 8; i++) {
			int rank = perspective == WHITE ? 7 - i : i;
			sb.append(rank + 1);
			sb.append("│ ");

			for (int file = 0; file < 8; file++) {
				Piece p = board[rank][file];

				if (p == null) {
					sb.append(" ");
				} else {
					sb.append(p.unicodeFigurine());
				}

				sb.append(" ");
			}

			sb.append("│");
			sb.append(rank + 1);
			sb.append("\n");
		}

		sb.append(" └").append("─".repeat(8 * 2 + 1)).append("┘\n");
		sb.append(headline);
		return sb.toString();
	}

	public void draw(Graphics g) {
		draw(g, WHITE);
	}

	public void draw(Graphics g, Alliance perspective) {
		g.setColor(DARK_SQUARE);
		g.fillRect(0, 0, 360, 360);

		int drawX, drawY;

		for (int y = 0; y < 8; y++) {
			int rank = perspective == WHITE ? 7 - y : y;
			drawY = 45 * y;

			for (int x = 0; x < 8; x++) {
				drawX = 45 * x;
				if ((x + y) % 2 == 0) {
					g.setColor(LIGHT_SQUARE);
					g.fillRect(drawX, drawY, 45, 45);
				}

				if (marked.contains(new Position(x, rank))) {
					g.setColor(MARKED);
					g.fillRect(drawX, drawY, 45, 45);
				}

				Piece p = board[rank][x];
				if (p != null) {
					p.draw(g, drawX, drawY);
				}
			}
		}
	}

	private static class State {
		public boolean blackKingCastle = true;
		public boolean whiteKingCastle = true;
		public boolean blackQueenCastle = true;
		public boolean whiteQueenCastle = true;
		public Position enPassant = null;

		public State() {
		}

		private State(boolean blackKingCastle, boolean whiteKingCastle, boolean blackQueenCastle, boolean whiteQueenCastle, Position enPassant) {
			this.blackKingCastle = blackKingCastle;
			this.whiteKingCastle = whiteKingCastle;
			this.blackQueenCastle = blackQueenCastle;
			this.whiteQueenCastle = whiteQueenCastle;
			this.enPassant = enPassant;
		}

		public void update(Piece moving, Position from, Position to) {
			switch (moving.type) {
				case PAWN:
					if (Math.abs(to.rank() - from.rank()) == 2) {
						enPassant = from.offset(moving.alliance.pawnStep());
					} else {
						enPassant = null;
					}
					break;

				case KING:
					if (moving.alliance == WHITE) {
						whiteKingCastle = false;
						whiteQueenCastle = false;
					} else {
						blackKingCastle = false;
						blackQueenCastle = false;
					}
					break;

				case ROOK:
					if (from.file() == 0) {
						if (moving.alliance == WHITE) {
							whiteQueenCastle = false;
						} else {
							blackQueenCastle = false;
						}
					} else if (from.file() == 7) {
						if (moving.alliance == WHITE) {
							whiteKingCastle = false;
						} else {
							blackKingCastle = false;
						}
					}
					break;

				default:
					break;
			}
		}

		public State copy() {
			return new State(blackKingCastle, whiteKingCastle, blackQueenCastle, whiteQueenCastle, enPassant);
		}
	}
}
