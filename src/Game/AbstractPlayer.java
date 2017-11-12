package Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public abstract class AbstractPlayer {

    private static int idCounter = 0;
    private Color color;
    private List<JPanel> snake;
    private JPanel food;
    private Snake.Direction direction;
    private Set<Point> setPointsUsedBySnake;
    private boolean hasLost;
    private int id;

    public void setNumMoves(int numMoves) {
        this.numMoves = numMoves;
    }

    public int getNumMoves() {

        return numMoves;
    }

    private int numMoves = 0;

    public AbstractPlayer(Color color, JPanel food, List<JPanel> snake, Set<Point> setPointsUsedBySnake) {
        this.color = color;
        this.snake = snake;
        this.food = food;
        this.setPointsUsedBySnake = setPointsUsedBySnake;
        hasLost = false;
        direction = Snake.Direction.getRandomDirection();
        id = idCounter++;
    }

    public int getId() {
        return id;
    }

    public Snake.Direction getDirection() {
        return direction;
    }

    public void setDirection(Snake.Direction direction) {
        this.direction = direction;
    }

    public JPanel getFood() {
        return food;
    }

    public void setFood(JPanel food) {
        this.food = food;
    }

    public Color getColor() {
        return color;
    }

    public List<JPanel> getSnake() {
        return snake;
    }

    public boolean hasLost() {
        return hasLost;
    }

    public void setHasLost(boolean value) {
        hasLost = value;
    }

    public abstract void makeMove();

    public Set<Point> getSetPointsUsedBySnake() {
        return setPointsUsedBySnake;
    }
}
