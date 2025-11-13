package game;

import java.io.Serial;
import java.io.Serializable;


public class ScoreInfo implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final int score;


    public ScoreInfo(String name, int score) {
        this.playerName = name;
        this.score = score;
    }


    public String getPlayerName() {
        return this.playerName;
    }


    public int getScore() {
        return this.score;
    }
}