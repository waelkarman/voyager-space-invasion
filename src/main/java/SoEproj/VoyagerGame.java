/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class VoyagerGame extends JFrame {

    private Image iconWindows;

    public VoyagerGame() {
        initUI();
    }                                               
    
    private void initUI() {
        
        add(new Board());
        
        setResizable(false);
        pack();
        setTitle("Voyager On The Edge Of The Solar System");
        loadWindowsIcon(); 
        setIconImage(iconWindows);

        setLocationRelativeTo(null); // centra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
 
    private void loadWindowsIcon() {
        ImageIcon ii = new ImageIcon(".\\src\\main\\java\\SoEproj\\Resource\\ico.png");
        iconWindows = ii.getImage();        
    }

      
       public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            VoyagerGame ex = new VoyagerGame();
            ex.setVisible(true);
        });
    }
}
