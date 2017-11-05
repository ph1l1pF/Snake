package Evolution.CrossOver;

import Evolution.Evolution;
import NeuralNetwork.NeuralNetwork;

public interface ICrossOver {

    NeuralNetwork crossOver(NeuralNetwork parent1, NeuralNetwork parent2);
}
