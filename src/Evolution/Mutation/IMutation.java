package Evolution.Mutation;

import NeuralNetwork.NeuralNetwork;

public interface IMutation {

    NeuralNetwork mutate(NeuralNetwork network);

}
