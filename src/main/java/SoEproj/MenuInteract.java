package SoEproj;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuInteract implements MouseListener{

    private Board board;
    private JLabel command;

    public MenuInteract(Board board, JLabel command) {
        this.command = command;
        this.board = board;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        
        board.initGame();
        board.destroyMenu();
        board.gameLaunch();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

	}


   




}