package pong;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PongFrame extends JFrame {

    private final JButton saveButton = new JButton("Save Game");
    private final JButton restoreButton = new JButton("Restore Game");
    private final int width = 500, height = 500;


    public PongFrame() throws IOException, InterruptedException {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(saveButton, BorderLayout.NORTH);
        add(restoreButton, BorderLayout.SOUTH);

        final GamePanel gamePanel = new GamePanel(width, height);
        add(gamePanel);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    gamePanel.saveGame();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(PongFrame.this,
                            ex.getMessage(), "Save File Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    gamePanel.restoreGame();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(PongFrame.this,
                            ex.getMessage(), "Restore Game File Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

}

