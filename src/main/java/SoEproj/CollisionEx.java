/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class CollisionEx extends JFrame {

    private Image iconWindows;

    public CollisionEx() {
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
            CollisionEx ex = new CollisionEx();
            ex.setVisible(true);
        });
    }
}
