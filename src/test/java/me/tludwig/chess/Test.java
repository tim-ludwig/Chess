package me.tludwig.chess;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.game.pieces.Piece;
import me.tludwig.chess.game.pieces.PieceType;
import me.tludwig.chess.graphics.GameFrame;

import java.util.Scanner;

import static me.tludwig.chess.game.pieces.Alliance.BLACK;
import static me.tludwig.chess.game.pieces.Alliance.WHITE;

public class Test {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		AbstractMove m;

		Piece[][] board = new Piece[8][8];

		board[4][4] = new Piece(PieceType.BISHOP, BLACK);
		board[1][1] = new Piece(PieceType.PAWN, WHITE);
		board[7][7] = new Piece(PieceType.KING, BLACK);
		board[0][0] = new Piece(PieceType.KING, WHITE);
		GameFrame frame = new GameFrame(new Board());

		while (!frame.board.ended()) {
			frame.repaint();
			System.out.println(frame.board);

			if (!sc.hasNext()) {
				break;
			}

			m = AbstractMove.fromLAN(sc.next(), frame.board.toMove());

			if (!m.isLegal(frame.board)) {
				System.err.println("Illegal move");

				continue;
			}

			frame.board.apply(m);
		}

		if (frame.board.isInCheck(frame.board.toMove())) {
			System.out.println("Mate");
		} else {
			System.out.println("Stalemate");
		}

		frame.repaint();

		sc.close();
	}
}
