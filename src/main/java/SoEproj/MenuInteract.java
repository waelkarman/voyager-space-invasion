package SoEproj;



import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuInteract implements MouseListener{

    Board contesto;
    JLabel tasto;

    public MenuInteract(Board board,JLabel entry) {
        tasto = entry;
        contesto = board;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        
        contesto.initGame();
        contesto.destroyMenu();
        contesto.gameLaunch();
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