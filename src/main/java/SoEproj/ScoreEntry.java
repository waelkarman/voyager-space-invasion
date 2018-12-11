package SoEproj;


import java.io.Serializable;


public class ScoreEntry implements Comparable<ScoreEntry>,Serializable{
    private final String nome;
    private final int punteggio;

    public ScoreEntry(String nome, int punteggio) {
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
        return this.punteggio - t.punteggio;
    }
    
}
