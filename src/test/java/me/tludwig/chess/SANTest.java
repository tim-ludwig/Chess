package me.tludwig.chess;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.graphics.GameFrame;

import java.util.Scanner;

public class SANTest {
	public static void main(String[] args) {
		GameFrame frame = new GameFrame(new Board());

		Scanner sc = new Scanner(System.in);
		AbstractMove m;

		while (!frame.board.ended()) {
//			System.out.println(frame.board);
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

		System.out.println(frame.board.toMove().other() + " won!");

		sc.close();
	}
}
