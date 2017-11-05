package Game;

import NeuralNetwork.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class NeuralNetworkPlayer extends AbstractPlayer {

    public NeuralNetwork getNetwork() {
        return network;
    }

    private NeuralNetwork network;

    public NeuralNetworkPlayer(Color color, JPanel food, List<JPanel> snake, NeuralNetwork network, Set<Point> setPointsUsedBySnake) {
        super(color, food, snake, setPointsUsedBySnake);
        this.network = network;
    }

    @Override
    public void makeMove() {
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


            if (new Point(snakePart.getX(), snakePart.getY()).equals(posFront)) {
                obstacleFront = -1;
            }

            if (new Point(snakePart.getX(), snakePart.getY()).equals(posLeft)) {
                obstacleLeft = -1;
            } else if (new Point(getFood().getX(), getFood().getY()).equals(new Point(snakePart.getX(), snakePart.getY()))) {
                obstacleFront = -1;
            }


            if (new Point(snakePart.getX(), snakePart.getY()).equals(posRight)) {
                obstacleRight = -1;
            }

        }

        // check where food is...
        if (new Point(getFood().getX(), getFood().getY()).equals(posFront)) {
            obstacleFront = 1;
        }
        if (new Point(getFood().getX(), getFood().getY()).equals(posLeft)) {
            obstacleLeft = 1;
        }
        if (new Point(getFood().getX(), getFood().getY()).equals(posRight)) {
            obstacleRight = 1;
        }

        // food distance difference
        Point pLeft = Snake.Direction.getPointAfterMoving(Snake.Direction.getRelativeLeft(currentDirection), posHead);
        Point pRight = Snake.Direction.getPointAfterMoving(Snake.Direction.getRelativeRight(currentDirection), posHead);
        Point pStraight = Snake.Direction.getPointAfterMoving(currentDirection, posHead);

        double[] outputs = new double[3];

        // left
        outputs[0] = network.computeOutputs(obstacleLeft, obstacleRight, obstacleFront,
                distanceDifferenceToFood(posHead, pLeft, new Point(getFood().getX(), getFood().getY())))[0];

        // right
        outputs[1] = network.computeOutputs(obstacleLeft, obstacleRight, obstacleFront,
                distanceDifferenceToFood(posHead, pRight, new Point(getFood().getX(), getFood().getY())))[0];

        // straight
        outputs[2] = network.computeOutputs(obstacleLeft, obstacleRight, obstacleFront,
                distanceDifferenceToFood(posHead, pStraight, new Point(getFood().getX(), getFood().getY())))[0];

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
    }

    private int distanceDifferenceToFood(Point oldPos, Point newPos, Point posFood) {
        int diffOld = manhattanDistance(oldPos, posFood);
        int diffNew = manhattanDistance(newPos, posFood);
        return diffOld - diffNew;
    }

    private int manhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }
}
