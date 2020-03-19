package com.lpreciado.java2048;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Hashtable;
import java.net.*;

public class GUI {

	int frameHeight = 394;
	int frameWidth = 328;
	int gameBoardSize = 296;
	int marginSize = 16;
	int highScore;
	
	Color backgroundColor = new Color(255, 255, 120);
	Hashtable<Integer, ImageIcon> numberTiles;
	Game game;
	GameBoard gb;
	JFrame frame;

	Font largeFeedbackFont = new Font("SansSerif", 0, 40);
	Font smallFeedbackFont = new Font("SansSerif", 0, 20);

	JLabel scoreLabel;
	JLabel highScoreLabel;
	
	public GUI() {
		game = new Game();
		this.frame = new MyFrame();
		frame.setFocusable(true);
		frame.setResizable(false);
		frame.addKeyListener(new MyFrame());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadNumberTiles();

		gb = new GameBoard();
		gb.setFocusable(true);
		// North Panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout());
		northPanel.setPreferredSize(new Dimension(frameWidth, 82));

		JLabel gameLabel = new JLabel("2048", SwingConstants.CENTER);
		gameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		scoreLabel = new JLabel("Score: " + game.getScore(), SwingConstants.CENTER);
		northPanel.add(scoreLabel);
		highScoreLabel = new JLabel("High Score: " + 0 , SwingConstants.CENTER);
		northPanel.add(highScoreLabel);
		northPanel.setBackground(backgroundColor);

		// West Panel
		JPanel westBuffer = new JPanel();
		westBuffer.setPreferredSize(new Dimension(marginSize, gameBoardSize));
		westBuffer.setBackground(backgroundColor);
		// East Panel
		JPanel eastBuffer = new JPanel();
		eastBuffer.setPreferredSize(new Dimension(marginSize, gameBoardSize));
		eastBuffer.setBackground(backgroundColor);
		// South Panel
		JPanel southBuffer = new JPanel();
		southBuffer.setPreferredSize(new Dimension(frameWidth, marginSize));
		southBuffer.setBackground(backgroundColor);

		// Add Panels to Frame
		frame.getContentPane().add(northPanel, BorderLayout.NORTH);
		frame.getContentPane().add(westBuffer, BorderLayout.WEST);
		frame.getContentPane().add(eastBuffer, BorderLayout.EAST);
		frame.getContentPane().add(southBuffer, BorderLayout.SOUTH);
		frame.getContentPane().add(gb, BorderLayout.CENTER);

		frame.getContentPane().setPreferredSize(new Dimension(frameWidth, frameHeight));
		frame.pack();
		frame.setVisible(true);
	}

	private void loadNumberTiles() {
		numberTiles = new Hashtable<Integer, ImageIcon>();
		ClassLoader cldr = this.getClass().getClassLoader();
		URL url0000 = cldr.getResource("images/tile00_64.png");
		URL url0002 = cldr.getResource("images/tile02_64.png");
		URL url0004 = cldr.getResource("images/tile04_64.png");
		URL url0008 = cldr.getResource("images/tile08_64.png");
		URL url0016 = cldr.getResource("images/tile16_64.png");
		URL url0032 = cldr.getResource("images/tile32_64.png");
		URL url0064 = cldr.getResource("images/tile64_64.png");
		URL url0128 = cldr.getResource("images/tile128_64.png");
		URL url0256 = cldr.getResource("images/tile256_64.png");
		URL url0512 = cldr.getResource("images/tile512_64.png");
		URL url1024 = cldr.getResource("images/tile1024_64.png");
		URL url2048 = cldr.getResource("images/tile2048_64.png");

		numberTiles.put(0, new ImageIcon(url0000));
		numberTiles.put(2, new ImageIcon(url0002));
		numberTiles.put(4, new ImageIcon(url0004));
		numberTiles.put(8, new ImageIcon(url0008));
		numberTiles.put(16, new ImageIcon(url0016));
		numberTiles.put(32, new ImageIcon(url0032));
		numberTiles.put(64, new ImageIcon(url0064));
		numberTiles.put(128, new ImageIcon(url0128));
		numberTiles.put(256, new ImageIcon(url0256));
		numberTiles.put(512, new ImageIcon(url0512));
		numberTiles.put(1024, new ImageIcon(url1024));
		numberTiles.put(2048, new ImageIcon(url2048));

	}

	class GameBoard extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(new Color(20, 20, 20));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			int[][] board = game.getGameBoard();
			for (int y = 1; y < 5; y++) {
				for (int x = 1; x < 5; x++) {
					int X = (8 * x) + (64 * (x - 1));
					int Y = (8 * y) + (64 * (y - 1));

					int thisNumber = board[y - 1][x - 1];

					if (numberTiles.containsKey(thisNumber)) {
						ImageIcon thisTile = numberTiles.get(thisNumber);
						thisTile.paintIcon(this, g, X, Y);
					}

				}
			}
		}

	}

	class WinBoard extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(new Color(20, 20, 20));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setFont(largeFeedbackFont);
			g.setColor(new Color(0, 200, 0));
			g.drawString("You Win!", 20, 50);
			g.setFont(smallFeedbackFont);
			g.setColor(new Color(0, 0, 0));
			g.drawString("Press ENTER to Play Again", 20, 80);

		}
	}

	class LoseBoard extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(new Color(20, 20, 20));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setFont(largeFeedbackFont);
			g.setColor(new Color(200, 0, 0));
			g.drawString("You Lose", 20, 50);
			g.setFont(smallFeedbackFont);
			g.setColor(new Color(255, 255, 255));
			g.drawString("Press ENTER to try Again", 20, 80);
		}
	}

	class MyFrame extends JFrame implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if (game.getGameState() == GameState.CONTINUE) {
				switch (key) {
				case KeyEvent.VK_UP:

					game.combineGridVertical(true);
					game.SpawnNumber();
					game.checkState();
					updateScore();
					gb.repaint();

					break;
				case KeyEvent.VK_DOWN:

					game.combineGridVertical(false);
					game.SpawnNumber();
					game.checkState();
					updateScore();
					gb.repaint();

					break;
				case KeyEvent.VK_LEFT:

					game.combineGrid(true);
					game.SpawnNumber();
					game.checkState();
					updateScore();
					gb.repaint();

					break;
				case KeyEvent.VK_RIGHT:

					game.combineGrid(false);
					game.SpawnNumber();
					game.checkState();
					updateScore();
					gb.repaint();

					break;
				}
				GameState gs = game.getGameState();
				if (gs == GameState.LOSE) {
					frame.getContentPane().remove(gb);
					frame.getContentPane().add(new LoseBoard(), BorderLayout.CENTER);
					updateScore();
					frame.getContentPane().invalidate();
					frame.getContentPane().validate();
				} else if (gs == GameState.WIN) {
					frame.getContentPane().remove(gb);
					frame.getContentPane().add(new WinBoard(), BorderLayout.CENTER);
					updateScore();
					frame.getContentPane().invalidate();
					frame.getContentPane().validate();
				}
			} else {
				if(key == KeyEvent.VK_ENTER) {
					game = new Game();
					frame.getContentPane().remove(((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER));
					frame.getContentPane().add(gb);
					gb.repaint();
					frame.getContentPane().invalidate();
					frame.getContentPane().validate();
				}
			}

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}
	
	public void updateScore() {
		scoreLabel.setText("<html>Score: <br>" + game.getScore() + "</html>");
		if(game.getScore() > this.highScore) {
			this.highScore = game.getScore();
			updateHighScore();
		}
	}
	
	public void updateHighScore() {
		highScoreLabel.setText("<html>High Score: <br>" + this.highScore + "</html>");
	}

}
