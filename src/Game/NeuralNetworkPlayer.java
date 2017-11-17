package Game;

import Evolution.Evolution;
import NeuralNetwork.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class NeuralNetworkPlayer extends AbstractPlayer {

    private final int valueFoodAvaible = 1;
    private final int valueObstacleInWay = -1;

    public NeuralNetwork getNetwork() {
        return network;
    }

    private NeuralNetwork network;

    public NeuralNetworkPlayer(Color color, JPanel food, List<JPanel> snake, NeuralNetwork network, Set<Point> setPointsUsedBySnake) {
        super(color, food, snake, setPointsUsedBySnake);
        this.network = network;
    }

    @Override
    public synchronized void makeMove() {
        Point posHead = getSnake().get(0).getLocation();
        int obstacleLeft = 0, obstacleRight = 0, obstacleFront = 0;
        Snake.Direction currentDirection = getDirection();

        Point posFront = Snake.Direction.getPointAfterMoving(currentDirection, posHead);

        Snake.Direction relativeLeftDir = Snake.Direction.getRelativeLeft(currentDirection);
        Point posLeft = Snake.Direction.getPointAfterMoving(relativeLeftDir, posHead);

        Snake.Direction relativeRightDir = Snake.Direction.getRelativeRight(currentDirection);
        Point posRight = Snake.Direction.getPointAfterMoving(relativeRightDir, posHead);


        // check for other parts of snake...
        for (int i = 2; i < getSnake().size(); i++) {
            JPanel snakePart = getSnake().get(i);
            Point posOtherSnakePart = new Point(snakePart.getX(), snakePart.getY());

            if (posOtherSnakePart.equals(posFront)) {
                obstacleFront = valueObstacleInWay;
            } else if (posOtherSnakePart.equals(posLeft)) {
                obstacleLeft = valueObstacleInWay;
            } else if (posOtherSnakePart.equals(posRight)) {
                obstacleRight = valueObstacleInWay;
            }

        }

        // check where food is...
        Point posFood = new Point(getFood().getX(), getFood().getY());
        if (posFood.equals(posFront)) {
            obstacleFront = valueFoodAvaible;
        }
        if (posFood.equals(posLeft)) {
            obstacleLeft = valueFoodAvaible;
        }
        if (posFood.equals(posRight)) {
            obstacleRight = valueFoodAvaible;
        }


        double[] outputs = new double[3];

        double inverseSnakeLength = -1 * getSnake().size();
        // left
        int distDiff = distanceDifferenceToFood(posHead, posLeft, posFood);
        outputs[0] = network.computeOutputs(obstacleLeft, obstacleRight, obstacleFront,
                distDiff, inverseSnakeLength)[0];

        printInput(obstacleLeft, obstacleRight, obstacleFront,
                distDiff, inverseSnakeLength, "left");
        printOutput(outputs[0]);

        // right
        distDiff = distanceDifferenceToFood(posHead, posRight, posFood);
        outputs[1] = network.computeOutputs(obstacleLeft, obstacleRight, obstacleFront,
                distDiff, inverseSnakeLength)[0];
        printInput(obstacleLeft, obstacleRight, obstacleFront,
                distDiff, inverseSnakeLength, "right");
        printOutput(outputs[0]);

        // straight
        distDiff = distanceDifferenceToFood(posHead, posFront, posFood);
        outputs[2] = network.computeOutputs(obstacleLeft, obstacleRight, obstacleFront,
                distDiff, inverseSnakeLength)[0];
        printInput(obstacleLeft, obstacleRight, obstacleFront,
                distDiff, inverseSnakeLength, "front");
        printOutput(outputs[0]);

        int iMax = 0;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > outputs[iMax]) {
                iMax = i;
            }
        }

        switch (iMax) {
            case 0:
                // turn relative left
                currentDirection = Snake.Direction.getRelativeLeft(currentDirection);
                break;
            case 1:
                // turn relative right
                currentDirection = Snake.Direction.getRelativeRight(currentDirection);
                break;
            case 2:
                // go straight, nothing to change
                break;
        }
        setDirection(currentDirection);

        //test
        if (getSnake().size() > 30 && Evolution.getInstance().getCurrentGenerationNumber() > 2) {
            if (obstacleLeft == -1 && iMax == 0) {
                System.out.println("hindernis links " + obstacleLeft);
                System.out.println("hindernis rechts " + obstacleRight);
                System.out.println("hindernis vorn " + obstacleFront);

                System.out.println("output links " + outputs[0]);
                System.out.println("output rechts " + outputs[1]);
                System.out.println("output gerade " + outputs[2]);
            }
        }
    }

    private void printOutput(double output) {
        System.out.println("output=" + output);
    }

    private void printInput(int obstacleLeft, int obstacleRight, int obstacleFront, int distDiff, double inverseSnakeLength, String dir) {

        System.out.println("inputs for " + dir + "\n: obstacleLeft=" + obstacleLeft + ", obstacleRight=" + obstacleRight + ", obstacleFront=" + obstacleFront
                + ", distance Difference=" + distDiff + ", inverseSnakeLength=" + inverseSnakeLength);
    }

    private int distanceDifferenceToFood(Point oldPos, Point newPos, Point posFood) {
        int diffOld = manhattanDistance(oldPos, posFood);
        int diffNew = manhattanDistance(newPos, posFood);
        int distDiff = (diffOld - diffNew) / Snake.SNAKE_PART_SIZE;
        return distDiff;
    }

    private int manhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }
}
