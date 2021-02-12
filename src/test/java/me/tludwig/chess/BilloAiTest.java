package me.tludwig.chess;

import me.tludwig.chess.ai.Billo;
import me.tludwig.chess.ai.ChessAI;
import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.game.pieces.Piece;
import me.tludwig.chess.graphics.GameFrame;

import java.util.Scanner;

import static me.tludwig.chess.game.pieces.Alliance.BLACK;
import static me.tludwig.chess.game.pieces.Alliance.WHITE;
import static me.tludwig.chess.game.pieces.PieceType.*;

public class BilloAiTest {
	public static void main(String[] args) {
		ChessAI ai = new Billo();

		Piece[][] board = new Piece[8][8];


		board[0][0] = new Piece(KING, WHITE);
		board[4][0] = new Piece(ROOK, WHITE);

		board[6][7] = new Piece(PAWN, BLACK);
		board[6][6] = new Piece(PAWN, BLACK);
//		board[4][3] = new Piece(ROOK, BLACK);
		board[4][4] = new Piece(ROOK, BLACK);
		board[7][7] = new Piece(KING, BLACK);
		Board b = new Board(WHITE, board);
		GameFrame f = new GameFrame(b);

		System.out.println(b);

		Scanner sc = new Scanner(System.in);

		sc.next();

		while (!b.ended()) {
			long time = System.currentTimeMillis();
			AbstractMove predicted = ai.predictMove(b);
			time = System.currentTimeMillis() - time;

			System.out.println("Predicted: " + predicted);
			System.out.println("Time: " + time + "ms");

			b.apply(predicted);

			System.out.println(b);
			f.repaint();
		}

		System.out.println(b.moveHist);

		sc.close();
	}
}
