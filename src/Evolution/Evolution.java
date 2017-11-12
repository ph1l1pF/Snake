package Evolution;

import Evolution.CrossOver.ChromosomeCrossOver;
import Evolution.CrossOver.ICrossOver;
import Evolution.Mutation.IMutation;
import Evolution.Mutation.MutationImpl;
import Game.NeuralNetworkPlayer;
import Game.Snake;
import NeuralNetwork.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;


public class Evolution {

    /**
     * The probability that some individual will mutate.
     */
    private static final double MUTATION_PROBABILITY = 0.1;
    private static Evolution instance;
    /**
     * The probability that a connection will be changed during mutation
     */
    private final double PROBABILITY_CONNECTION_AFFECTED = 0.1;
    private final int NUM_HIDDEN_NEURONS = 3;
    int numGeneration = 0;
    private ICrossOver crossOver = new ChromosomeCrossOver();
    private IMutation mutation = new MutationImpl();
    private int currentGenerationNumber = -1;
    private volatile boolean alreadyDone = false;
    private Snake snakeGame;

    private Evolution() {
    }

    public static synchronized Evolution getInstance() {
        if (instance == null) {
            instance = new Evolution();
        }
        return instance;
    }

    public ICrossOver getCrossOver() {
        return crossOver;
    }

    public int getCurrentGenerationNumber() {
        return currentGenerationNumber;
    }

    public void processEvolution(List<NeuralNetworkPlayer> lstPlayers) {

        if (lstPlayers == null) {
            lstPlayers = createNeuralNetworkPlayers(30);
        }
        if (snakeGame != null) {
            snakeGame.setVisible(false);
            snakeGame.dispatchEvent(new WindowEvent(snakeGame, WindowEvent.WINDOW_CLOSING));
            // snakeGame.getContentPane().removeAll();
            numGeneration++;
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

        synchronized (this) {
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
        for (int i = 0; i < newListPlayers.size() * 0.9; i++) {
            int upperBound = newListPlayers.size() - 1;
            int lowerBound = (int) ((newListPlayers.size() - 1) * 0.8);
            NeuralNetworkPlayer parent1 = newListPlayers.get(new Random().nextInt(upperBound - lowerBound) + lowerBound);
            NeuralNetworkPlayer parent2 = newListPlayers.get(new Random().nextInt(upperBound - lowerBound) + lowerBound);

            System.out.println(fitness(parent1) + " und " + fitness(parent2));

            NeuralNetwork child = crossOver.crossOver(parent1.getNetwork(), parent2.getNetwork());
            newPopulation.add(child);
        }

        // fill rest with random exemplars
        while (newPopulation.size() < newListPlayers.size()) {
            newPopulation.add(crossOver.crossOver(newListPlayers.get(new Random().nextInt(newListPlayers.size() - 1)).getNetwork(),
                    newListPlayers.get(new Random().nextInt(newListPlayers.size() - 1)).getNetwork()));
        }

        for (int i = 0; i < lstPlayers.size(); i++) {
            lstPlayers.get(i).setNetwork(newPopulation.get(i));
        }

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
        for (NeuralNetworkPlayer player : lstPlayers) {
            mutation.mutate(player.getNetwork());
        }
    }

}
