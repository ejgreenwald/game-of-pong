/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author student1
 */
class GamePanel extends JPanel {

    private final Ellipse2D ball;
    private final Rectangle2D paddle;
    private final int BALL_SIZE = 20;
    private final int PADDLE_WIDTH = 15;
    private final int PADDLE_LENGTH = 100;
    private int dx = 5;
    private int dy = 5;
    private int paddleDy = 5;
    private int score = 0;
    private final Timer timer;
    private boolean gameOver = false;
    private HighValue<Score> highScores;
    private int highScoresSize = 10;
    private String highScoreFile = "high_scores.bin";

    GamePanel(int width, int height) throws IOException, InterruptedException {
        paddle = new Rectangle2D.Float(width - 65, height / 2, PADDLE_WIDTH, PADDLE_LENGTH);
        ball = new Ellipse2D.Float(0, 0, BALL_SIZE, BALL_SIZE);
        setBackground(Color.BLACK);
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ball.setFrame(ball.getX() + dx, ball.getY() + dy, BALL_SIZE, BALL_SIZE);
                //paddle.translate(0, paddleDy);
                if (ball.getMinY() < 0 || ball.getMaxY() > getHeight()) {
                    dy = -dy;
                }
                if (ball.getMinX() < 0) {
                    dx = -dx;
                }
                if (ball.getBounds2D().intersects(paddle)) {
                    dx = -(Math.abs(dx));
                    score++;
                }
                if (ball.getMaxX() > getWidth()) {
                    gameOver = true;
                }
                repaint();
            }
        });
        //Thread.sleep(100);
        timer.start();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() == ' ') {
                    if (timer.isRunning()) {
                        pause();
                    } else {
                        resume();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (paddle.getMinY() < 0) {
                    paddle.setRect(paddle.getX(), 0, PADDLE_WIDTH, PADDLE_LENGTH);
                }
                if (paddle.getMaxY() > getHeight()) {
                    paddle.setRect(paddle.getX(), getHeight() - PADDLE_LENGTH, PADDLE_WIDTH, PADDLE_LENGTH);
                } else {
                    paddle.setRect(paddle.getX(), paddle.getY() + paddleDy * e.getWheelRotation(), PADDLE_WIDTH, PADDLE_LENGTH);
                }
            }
        });
    }

    private void pause() {
        timer.stop();
    }

    private void resume() {
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        setFocusable(true);
        this.requestFocus();
        g.setColor(Color.WHITE);
        g.fillOval((int) ball.getX(), (int) ball.getY(), BALL_SIZE, BALL_SIZE);
        g.fillRect((int) paddle.getX(), (int) paddle.getY(), PADDLE_WIDTH, PADDLE_LENGTH);
        g.setFont(new Font("Serif", Font.BOLD, 24));
        g.drawString(score + "", getWidth() / 2, 20);
        if (gameOver) {
            try {
                gameOver(g);
            } catch (IOException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void gameOver(Graphics g) throws IOException, ClassNotFoundException {
        timer.stop();
        g.setFont(new Font("Serif", Font.BOLD, 45));
        g.drawString("GAME OVER", getWidth() / 3, getHeight() / 2);
        tryAddHighScore();
        JOptionPane.showMessageDialog(this, displayScores(highScores), "High Score", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);

    }

    private void tryAddHighScore() throws ClassNotFoundException, IOException, HeadlessException {
        File hsf = new File(highScoreFile);
        if (!hsf.exists()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(hsf))) {
                oos.writeObject(new HighValue<Score>(highScoresSize));
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(hsf))) {
            highScores = (HighValue<Score>) ois.readObject();
            addScore();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(hsf))) {
            oos.writeObject(highScores);
        }
    }

    private void addScore() throws HeadlessException {
        if (highScores.wouldMakeIt(new Score("test", score))) {
            String name;
            Score s;
            do{
                name = JOptionPane.showInputDialog(this, "You got a high score!\nEnter your name:");
                s = new Score(name.trim(), score);
            }while(name.trim().equals(""));
            highScores.add(s);
        }
    }

    void restoreGame() throws FileNotFoundException {
        try (final Scanner sc = new Scanner(new File("pong.txt"))) {
            score = sc.nextInt();
            ball.setFrame(sc.nextDouble(), sc.nextDouble(), BALL_SIZE, BALL_SIZE);
            paddle.setFrame(sc.nextDouble(), sc.nextDouble(), PADDLE_WIDTH, PADDLE_LENGTH);
            dx = sc.nextInt();
            dy = sc.nextInt();
        }
    }

    void saveGame() throws FileNotFoundException {
        String newLine = System.getProperty("line.separator");
        try (final PrintWriter pw = new PrintWriter("pong.txt")) {
            pw.printf("%d%s", score, newLine);
            pw.printf("%f%n", ball.getX());
            pw.printf("%f%n", ball.getY());
            pw.printf("%f%n", paddle.getX());
            pw.printf("%f%n", paddle.getY());
            pw.printf("%d%n", dx);
            pw.printf("%d%n", dy);
        }
    }

    private String displayScores(HighValue<Score> highScores) {
        StringBuilder sb = new StringBuilder();
        for (Score score : highScores) {
            sb.append(score.toString() + "\n");
        }
        return sb.toString();
    }

}
