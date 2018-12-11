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
public class ScoreEntry implements Comparable<ScoreEntry>,Serializable{
    private final String nome;
    private final int punteggio;

    public ScoreEntry(String nome, int punteggio) {
        System.out.println("Sono in ScoreEntry");
        this.nome = nome;
        this.punteggio = punteggio;
    }
    
    public String getNome() {
        return nome;
    }

    public int getPunteggio() {
        return punteggio;
    }

    @Override
    public String toString() {
        return "Player: "+nome+", Score: "+punteggio;
    }


    @Override
    public int compareTo(ScoreEntry t) {
        if(this.punteggio == t.punteggio){
            return 0;
        }
        else if (this.punteggio < t.punteggio)
            return -1;
        else
            return 1;
        
    
    }
    
    
    
    
    
}
