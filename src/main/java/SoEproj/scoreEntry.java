/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoEproj;


import java.io.Serializable;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wael Karman
 */
public class scoreEntry implements Comparable<scoreEntry>,Serializable{
    private final String nome;
    private final int punteggio;

    public scoreEntry(String nome, int punteggio) {
        this.nome = nome;
        this.punteggio = punteggio;
    }
    
    public synchronized String getAutore() {
        return nome;
    }

    public synchronized int getTitolo() {
        return punteggio;
    }

    @Override
    public String toString() {
        return "Player: "+nome+", Score: "+punteggio;
    }


    @Override
    public int compareTo(scoreEntry t) {
        if(this.punteggio == t.punteggio){
            return 0;
        }
        else if (this.punteggio < t.punteggio)
            return -1;
        else
            return 1;
        
    
    }
    
    
    
    
    
}
