package Game;


import Evolution.*;
import NeuralNetwork.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class Snake extends JFrame {

    public static final int SNAKE_PART_SIZE = 15;
    private static final Dimension FRAME_SIZE = new Dimension(SNAKE_PART_SIZE * 80, SNAKE_PART_SIZE * 50);
    private static final int FOOD_SIZE = SNAKE_PART_SIZE;
    private int clockPeriod = 30;
    private List<NeuralNetworkPlayer> lstPlayers = new ArrayList<>();
    private final Timer timer = new Timer();
    private boolean gameOver = false;

    public Snake() {
        initializeFrame();

    }

    public void startGame(List<NeuralNetworkPlayer> lstPlayers) {
        getContentPane().removeAll();
        this.lstPlayers = lstPlayers;
        for (AbstractPlayer player : this.lstPlayers) {
            player.setHasLost(false);
            player.setNumMoves(0);
            player.getSnake().clear();
        }

        for (AbstractPlayer player : lstPlayers) {
            placeFood(player);
            createNewSnakePart(true, player);
        }
    }

    private void initializeFrame() {
        setSize(FRAME_SIZE);

        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                for (AbstractPlayer player : lstPlayers) {
                    if (player.getSnake().size() == 0) {
                        return;
                    }
                }
                for (AbstractPlayer player : lstPlayers) {
                    player.makeMove();
                    moveSnake(player);
                    checkForCollision(player);
                    checkIfFoodIsHit(player);
                }

            }

        }, 0, clockPeriod);

        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }

    private void checkForCollision(AbstractPlayer player) {
        List<JPanel> snake = player.getSnake();
        for (int i = 2; i < snake.size(); i++) {
            if (snake.get(i).getX() == snake.get(0).getX() && snake.get(i).getY() == snake.get(0).getY()) {
                player.setHasLost(true);
            }
        }
        boolean oneSurvivor = false;
        for (AbstractPlayer p : lstPlayers) {
            if (!p.hasLost()) {
                oneSurvivor = true;
            }
        }
        gameOver = !oneSurvivor;
        if (gameOver) {
            timer.cancel();
            printStats();
            Evolution.getInstance().createNewGeneration(lstPlayers);
        }
    }

    private void printStats() {
        Object[] stats = updateTitle();
        System.out.println("Generation " + stats[3] + "\n");
        System.out.println("Min " + stats[0]);
        System.out.println("Max " + stats[1]);
        System.out.println("Avg " + stats[2]);


        int maxLength = 0;
        NeuralNetwork bestNetwork = null;
        for (AbstractPlayer player : lstPlayers) {
            if (player.getSnake().size() > maxLength) {
                maxLength = player.getSnake().size();
                bestNetwork = ((NeuralNetworkPlayer) player).getNetwork();
            }
        }
        System.out.println("Best Network:\n");
        System.out.println(bestNetwork);
        System.out.println("-------------------" + "\n\n");
        Object[] arr = updateTitle();
        String string = "";
        for (Object o : arr) {
            string += String.valueOf(o) + ";";
        }

        List<String> lines = new ArrayList<>();
        lines.add(string);
        new StatsWriter().writeStats(lines);
    }

    private void checkIfFoodIsHit(AbstractPlayer player) {
        List<JPanel> snake = player.getSnake();
        JPanel food = player.getFood();
        int headX = snake.get(0).getX();
        int headY = snake.get(0).getY();

        if (headX == food.getX() && headY == food.getY()) {
            createNewSnakePart(false, player);
            placeFood(player);
        }

    }

    public Point randomPoint() {
        Random random = new Random();
        int xs = (int) FRAME_SIZE.getWidth() / FOOD_SIZE - 1;
        int ys = (int) FRAME_SIZE.getHeight() / FOOD_SIZE - 1;
        int x = random.nextInt(xs) * FOOD_SIZE;
        int y = random.nextInt(ys) * FOOD_SIZE;
        return new Point(x, y);
    }

    public Object[] updateTitle() {
        int min = Integer.MAX_VALUE;
        int max = 0;
        int numAlive = 0;
        double avg = 0;
        for (AbstractPlayer player : lstPlayers) {
            int size = player.getSnake().size();
            if (size > max) {
                max = size;
            }
            if (size < min) {
                min = size;
            }
            avg += size;
            if (!player.hasLost()) {
                numAlive++;
            }
        }
        avg /= lstPlayers.size();
        avg = Math.floor(avg);
        int generationNumber = Evolution.getInstance().getCurrentGenerationNumber();
        setTitle("Generation: " + generationNumber + "\t" + "Snakes alive: " + numAlive + "\t" + "Min: "
                + min + "\t" + "Max: " + max + "\t" + "Avg: " + (int) avg);

        return new Object[]{min, max, avg, generationNumber};
    }

    private void placeFood(AbstractPlayer player) {
        Point point;
        do {
            point = randomPoint();
        }
        while (player.getSetPointsUsedBySnake().contains(point));

        final JPanel food = player.getFood();

        food.setBackground(player.getColor());
        food.setBounds(point.x, point.y, FOOD_SIZE, FOOD_SIZE);
        getContentPane().add(food);
    }

    private synchronized void createNewSnakePart(boolean firstPart, AbstractPlayer player) {
        JPanel newTail = new JPanel();
        newTail.setBackground(player.getColor());
        newTail.setSize(SNAKE_PART_SIZE, SNAKE_PART_SIZE);

        List<JPanel> snake = player.getSnake();

        Point positionsTail = new Point();
        if (snake.size() > 0) {
            positionsTail = new Point((int) snake.get(snake.size() - 1).getBounds().getX(), (int) snake.get(snake.size() - 1).getBounds().getY());
        }

        Point positionNewTail = new Point();

        Direction direction = player.getDirection();
        if (firstPart) {
            Point randomPoint = randomPoint();
            positionNewTail = new Point(randomPoint.x, randomPoint.y);
            newTail.setBackground(player.getColor());
        } else if (direction == Direction.RIGHT) {
            positionNewTail = new Point(positionsTail.x - SNAKE_PART_SIZE, positionsTail.y);
        } else if (direction == Direction.LEFT) {
            positionNewTail = new Point(positionsTail.x + SNAKE_PART_SIZE, positionsTail.y);
        } else if (direction == Direction.DOWN) {
            positionNewTail = new Point(positionsTail.x, positionsTail.y - SNAKE_PART_SIZE);
        } else if (direction == Direction.UP) {
            positionNewTail = new Point(positionsTail.x, positionsTail.y + SNAKE_PART_SIZE);
        }

        newTail.setBounds(positionNewTail.x, positionNewTail.y, newTail.getWidth(), newTail.getHeight());
        getContentPane().add(newTail);
        snake.add(newTail);

        updateTitle();
    }

    private synchronized void moveSnake(AbstractPlayer player) {

        if (player.hasLost()) {
            return;
        }

        if (player.getSnake().size() <= 5 && player.getNumMoves() > 1000) {
            player.setHasLost(true);
        }

        player.setNumMoves(player.getNumMoves() + 1);

        final List<JPanel> snake = player.getSnake();

        final JPanel snakeHead = snake.get(0);
        Point newPositionHead = Direction.getPointAfterMoving(player.getDirection(), snakeHead.getLocation());


        Point oldPos = new Point(snakeHead.getX(), snakeHead.getY());

        final Point finalNewPosHead = new Point(newPositionHead.x, newPositionHead.y);
        snakeHead.setBounds(finalNewPosHead.x, finalNewPosHead.y, snakeHead.getWidth(), snakeHead.getHeight());

        for (int i = 1; i < snake.size(); i++) {
            int oldX = snake.get(i).getX();
            int oldY = snake.get(i).getY();

            snake.get(i).setLocation(oldPos.x, oldPos.y);
            oldPos.x = oldX;
            oldPos.y = oldY;
        }

        if (newPositionHead.x >= FRAME_SIZE.width || newPositionHead.x < 0 || newPositionHead.y < 0 || newPositionHead.y >= FRAME_SIZE.height) {
            player.setHasLost(true);
        }
    }

    public enum Direction {
        DOWN, UP, RIGHT, LEFT;

        public static Direction getRelativeLeft(Direction d) {
            switch (d) {
                case UP:
                    return LEFT;
                case LEFT:
                    return DOWN;
                case DOWN:
                    return RIGHT;
                case RIGHT:
                    return UP;
            }
            return null;
        }

        public static Direction getRelativeRight(Direction d) {
            switch (d) {
                case UP:
                    return RIGHT;
                case RIGHT:
                    return DOWN;
                case DOWN:
                    return LEFT;
                case LEFT:
                    return UP;
            }
            return null;
        }


        public static Point getPointAfterMoving(Direction d, Point p) {
            Point newP = new Point(p.x, p.y);
            switch (d) {
                case LEFT:
                    newP.x -= Snake.SNAKE_PART_SIZE;
                    break;
                case RIGHT:
                    newP.x += Snake.SNAKE_PART_SIZE;
                    break;
                case UP:
                    newP.y -= Snake.SNAKE_PART_SIZE;
                    break;
                case DOWN:
                    newP.y += Snake.SNAKE_PART_SIZE;
                    break;
            }
            return newP;
        }

        public static Direction getRandomDirection() {
            switch (new Random().nextInt(3)) {
                case 0:
                    return LEFT;
                case 1:
                    return RIGHT;
                case 2:
                    return UP;
                case 3:
                    return DOWN;
            }
            return null;
        }
    }

}
