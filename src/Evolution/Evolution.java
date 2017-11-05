package Evolution;

import Game.NeuralNetworkPlayer;
import Game.Snake;
import NeuralNetwork.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class Evolution {

    private static Evolution instance;

    /**
     * The probability that some individual will mutate.
     */
    private static final double MUTATION_PROBABILITY = 0.1;


    /**
     * The probability that a connection will be changed during mutation
     */
    private static final double PROBABILITY_CONNECTION_AFFECTED = 0.3;


    private static final int NUM_HIDDEN_NEURONS = 3;

    public int getCurrentGenerationNumber() {
        return currentGenerationNumber;
    }

    private int currentGenerationNumber = -1;

    private volatile boolean alreadyDone = false;

    int numGames = 0;
    private Snake snakeGame;


    public static synchronized Evolution getInstance() {
        if (instance == null) {
            instance = new Evolution();
        }
        return instance;
    }

    private Evolution() {
    }


    public void processEvolution(List<NeuralNetworkPlayer> lstPlayers) {

        if (lstPlayers == null) {
            lstPlayers = createNeuralNetworkPlayers(30);
        }
        if(snakeGame!=null){
            snakeGame.setVisible(false);
           // snakeGame.getContentPane().removeAll();
            numGames++;
        }
        currentGenerationNumber++;
        snakeGame = new Snake();
        snakeGame.startGame(lstPlayers);
    }

    public void createNewGeneration(List<NeuralNetworkPlayer> lstPlayers) {
        synchronized (this) {
            if (alreadyDone) {
                return;
            } else {
                alreadyDone = true;
            }
        }
        select(lstPlayers);
        mutate(lstPlayers);
        processEvolution(lstPlayers);

        synchronized (this){
            alreadyDone = false;
        }
    }

    private List<NeuralNetworkPlayer> createNeuralNetworkPlayers(int size) {
        List<NeuralNetworkPlayer> lstPlayers = new ArrayList<>(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            Color color = Color.getHSBColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
            List<JPanel> lstPanels = new ArrayList<>();
            JPanel food = new JPanel();
            NeuralNetworkPlayer player = new NeuralNetworkPlayer(color, food, lstPanels, generateRandomFullmeshNeuralNetwork(), new HashSet<>());
            lstPlayers.add(player);
        }
        return lstPlayers;
    }

    private void select(List<NeuralNetworkPlayer> lstPlayers) {

        List<NeuralNetworkPlayer> newListPlayers = new ArrayList<>();
        newListPlayers.addAll(lstPlayers);

        newListPlayers.sort(Comparator.comparing(this::fitness));

        List<NeuralNetwork> newPopulation = new ArrayList<>();

        // 80% of new population must consist of best exemplars by crossing all of them
        for(int i = 0; i < newListPlayers.size() * 0.9; i++){
            int upperBound = newListPlayers.size()-1;
            int lowerBound = (int)((newListPlayers.size()-1) * 0.8);
            NeuralNetworkPlayer parent1 = newListPlayers.get(new Random().nextInt(upperBound-lowerBound)+lowerBound);
            NeuralNetworkPlayer parent2 = newListPlayers.get(new Random().nextInt(upperBound-lowerBound)+lowerBound);

            NeuralNetwork child = crossOver(parent1.getNetwork(),parent2.getNetwork());
            newPopulation.add(child);
        }

        // fill rest with random exemplars
        while (newPopulation.size()<newListPlayers.size()) {
            newPopulation.add(crossOver(newListPlayers.get(new Random().nextInt(newListPlayers.size()-1)).getNetwork(),
                    newListPlayers.get(new Random().nextInt(newListPlayers.size()-1)).getNetwork()));
        }

        for (int i = 0; i < lstPlayers.size(); i++) {
            lstPlayers.get(i).setNetwork(newPopulation.get(i));
        }

    }

    public NeuralNetwork crossOver(NeuralNetwork net1, NeuralNetwork net2) {
        NeuralNetwork childNet = generateRandomFullmeshNeuralNetwork();
        for (int i = 1; i < net1.getNeurons().length; i++) {
            for (int k = 1; k < net1.getNeurons()[i].length; k++) {
                for (int m = 0; m < net1.getNeurons()[i][k].getIngoingConnections().size(); m++) {
                    double w1 = net1.getNeurons()[i][k].getIngoingConnections().get(m).getWeight();
                    double w2 = net2.getNeurons()[i][k].getIngoingConnections().get(m).getWeight();
                    childNet.getNeurons()[i][k].getIngoingConnections().get(m).setWeight((w1 + w2) / 2.0);
                }
            }
        }
        for (int i = 0; i < net1.getNeurons().length; i++) {
            for (int k = 0; k < net1.getNeurons()[i].length; k++) {
                childNet.getNeurons()[i][k].setBias((net1.getNeurons()[i][k].getBias() + net2.getNeurons()[i][k].getBias()) / 2);
            }
        }
        return childNet;
    }

    private Integer fitness(NeuralNetworkPlayer x) {
        return x.getSnake().size();
    }


    private Neuron[][] generateNeurons() {
        Neuron in1 = new InputNeuron("left obstacle");
        Neuron in2 = new InputNeuron("right obstacle");
        Neuron in3 = new InputNeuron("front obstacle");
        Neuron in4 = new InputNeuron("food");

        Neuron[] hiddenNeurons = new Neuron[NUM_HIDDEN_NEURONS];
        for (int i = 0; i < hiddenNeurons.length; i++) {
            hiddenNeurons[i] = new HiddenNeuron("hidden " + i);
        }

        Neuron outputNeuron = new OutputNeuron("output");

        return new Neuron[][]{{in1, in2, in3, in4}, hiddenNeurons, {outputNeuron}};

    }

    public NeuralNetwork generateRandomFullmeshNeuralNetwork() {

        NeuralNetwork net = new NeuralNetwork(generateNeurons());
        net.generateFullmesh();
        return net;
    }


    private void mutate(List<NeuralNetworkPlayer> lstPlayers) {
        Random random = new Random();
        for (NeuralNetworkPlayer player : lstPlayers) {
            if (random.nextDouble() <= MUTATION_PROBABILITY) {
                for (Connection connection : player.getNetwork().getConnections()) {
                    if (random.nextDouble() <= PROBABILITY_CONNECTION_AFFECTED) {
                        double manipulationValue = random.nextDouble();
                        if (random.nextBoolean()) {
                            manipulationValue /= 1;
                        }
                        connection.setWeight(connection.getWeight() * manipulationValue);
                    }
                }
            }
        }


    }

}
