package me.tludwig.chess.graphics;

import me.tludwig.chess.game.Board;
import me.tludwig.chess.game.Position;
import me.tludwig.chess.game.move.AbstractMove;
import me.tludwig.chess.game.pieces.Alliance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serial;

public class GameFrame extends JFrame {
	@Serial
	private static final long serialVersionUID = 36041611548791359L;
	private static final int TILE_SIZE = 45;
	public final Board board;
	private Point cursor;
	private Position selected;

	public GameFrame(Board board) {
		super("Schach");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setPreferredSize(new Dimension(8 * TILE_SIZE, 8 * TILE_SIZE));
		pack();

		JPanel boardPanel = new ChessPanel();
		ChessMouseListener mouseListener = new ChessMouseListener();

		boardPanel.addMouseMotionListener(mouseListener);
		boardPanel.addMouseListener(mouseListener);

		getContentPane().add(boardPanel);

		this.board = board;
	}

	private final class ChessPanel extends JPanel {
		@Serial
		private static final long serialVersionUID = -7505544147734350956L;

		@Override
		public void paintComponent(Graphics g) {
			board.draw(g);

			if (selected == null) {
				return;
			}

			g.setColor(Color.CYAN);
			g.drawRect(selected.file() * TILE_SIZE, (7 - selected.rank()) * TILE_SIZE, TILE_SIZE, TILE_SIZE);

			if (cursor != null) {
				board.pieceAt(selected).draw(g, (int) cursor.getX() - 22, (int) cursor.getY() - 22);
			}
		}
	}

	private final class ChessMouseListener implements MouseMotionListener, MouseListener {
		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			cursor = e.getPoint();
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Position to = Position.fromPoint(e.getPoint(), TILE_SIZE, Alliance.WHITE);

			if (!to.equals(selected)) {
				AbstractMove m = AbstractMove.fromPositions(board, selected, to);

				if (m != null && m.isLegal(board)) {
					board.apply(m);
				}
			} else if (cursor == null) {
				selected = null;
			}

			cursor = null;
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			selected = Position.fromPoint(e.getPoint(), TILE_SIZE, Alliance.WHITE);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
