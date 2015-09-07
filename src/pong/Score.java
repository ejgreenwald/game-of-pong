//yosef shalom lejtman
package pong;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable {

    private String name;
    private int score;

    public Score(String name, int score) {
        if (name.equals("")) {
            throw new IllegalArgumentException("Name can't be blank");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score can't be less than zero");
        }
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(Score that) {
        return this.getScore() - that.getScore();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("%s%50d", name, score);
    }
    
}
