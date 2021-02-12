package me.tludwig.chess;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.pieces.Alliance;
import me.tludwig.chess.game.pieces.Piece;
import me.tludwig.chess.game.pieces.PieceType;
import me.tludwig.chess.graphics.GameFrame;

public class AttackersTest {
	public static void main(String[] args) {
		Piece[][] board = new Piece[8][8];

		board[4][4] = new Piece(PieceType.BISHOP, Alliance.BLACK);
//		board[1][1] = new Piece(PieceType.PAWN, Alliance.WHITE);
		board[7][7] = new Piece(PieceType.KING, Alliance.BLACK);
		board[0][0] = new Piece(PieceType.KING, Alliance.WHITE);
		Board b = new Board(Alliance.WHITE, board);

		System.out.println(b);
		GameFrame f = new GameFrame(b);
		System.out.println(b.allPossibleMoves().count());
	}
}
