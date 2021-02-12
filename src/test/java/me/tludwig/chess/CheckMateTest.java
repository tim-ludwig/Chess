package me.tludwig.chess;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.pieces.Piece;
import me.tludwig.chess.game.pieces.PieceType;

import static me.tludwig.chess.game.pieces.Alliance.BLACK;
import static me.tludwig.chess.game.pieces.Alliance.WHITE;
import static me.tludwig.chess.game.pieces.PieceType.KING;
import static me.tludwig.chess.game.pieces.PieceType.ROOK;

public class CheckMateTest {
	public static void main(String[] args) {
		Piece[][] board = new Piece[8][8];

//		board[7][7] = new Piece(PieceType.BISHOP, BLACK);
		board[7][1] = new Piece(ROOK, BLACK);
		board[1][7] = new Piece(ROOK, BLACK);
		board[2][2] = new Piece(PieceType.PAWN, WHITE);
		board[0][0] = new Piece(KING, WHITE);

		Board b = new Board(WHITE, board);
		System.out.println(b);
		System.out.println("isInCheck: " + b.isInCheck());
		System.out.println("isCheckMate: " + b.isCheckMate());
		System.out.println("isStaleMate: " + b.isStaleMate());
	}
}
