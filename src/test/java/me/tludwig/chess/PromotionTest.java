package me.tludwig.chess;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.game.pieces.Piece;
import me.tludwig.chess.graphics.GameFrame;

import java.util.Scanner;

import static me.tludwig.chess.game.pieces.Alliance.BLACK;
import static me.tludwig.chess.game.pieces.Alliance.WHITE;
import static me.tludwig.chess.game.pieces.PieceType.*;

public class PromotionTest {
	public static void main(String[] args) {
		Piece[][] board = new Piece[8][8];

		board[6][0] = new Piece(PAWN, WHITE);
		board[7][1] = new Piece(KNIGHT, BLACK);
		board[0][0] = new Piece(KING, WHITE);
		board[7][7] = new Piece(KING, BLACK);
		GameFrame frame = new GameFrame(new Board(WHITE, board));

		Scanner sc = new Scanner(System.in);
		AbstractMove m;

		while (true) {
			System.out.println(frame.board);
			if (!sc.hasNext()) {
				break;
			}

			m = AbstractMove.fromSAN(sc.next(), frame.board);

			if (!m.isLegal(frame.board)) {
				System.out.println("Illegal move");
				continue;
			}

			frame.board.apply(m);
			frame.repaint();
		}

		sc.close();
	}
}
