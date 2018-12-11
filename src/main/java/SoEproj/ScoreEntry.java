package SoEproj;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ScoreEntry implements Comparable<ScoreEntry>, Serializable {
    private final String name;
    private final int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
    
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDate.now().format(formatter);
        return String.format("%15s%10d%12s", name, score, date);
        //return "Player: " + name + ",\tScore: " + score;
    }

    @Override
    public int compareTo(ScoreEntry t) {
        return this.score - t.score;
    }
    
}
