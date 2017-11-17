package Evolution;

import Evolution.CrossOver.ChromosomeCrossOver;
import Evolution.CrossOver.ICrossOver;
import Evolution.Mutation.IMutation;
import Evolution.Mutation.MutationImpl;
import Game.NeuralNetworkPlayer;
import Game.Snake;
import NeuralNetwork.*;
import NeuralNetwork.Neuron.HiddenNeuron;
import NeuralNetwork.Neuron.InputNeuron;
import NeuralNetwork.Neuron.Neuron;
import NeuralNetwork.Neuron.OutputNeuron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;


public class Evolution {


    private static Evolution instance;
    private final int NUM_HIDDEN_NEURONS = 1;
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
            SwingUtilities.invokeLater(() -> {
                snakeGame.setVisible(false);
                snakeGame.dispatchEvent(new WindowEvent(snakeGame, WindowEvent.WINDOW_CLOSING));
            });
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

    private synchronized void select(List<NeuralNetworkPlayer> lstPlayers) {

        List<NeuralNetworkPlayer> newListPlayers = new ArrayList<>();
        newListPlayers.addAll(lstPlayers);

        newListPlayers.sort(Comparator.comparing(this::fitness));

        List<NeuralNetwork> newPopulation = new ArrayList<>();

        // 80% of new population must consist of best exemplars by crossing all of them
        for (int i = 0; i < newListPlayers.size() * 0.5; i++) {
            int upperBound = newListPlayers.size() - 1;
            int lowerBound = (int) ((newListPlayers.size() - 1) * 0.8);

            int rand1 = new Random().nextInt(upperBound - lowerBound) + lowerBound;
            int rand2 = new Random().nextInt(upperBound - lowerBound) + lowerBound;

            NeuralNetworkPlayer parent1 = newListPlayers.get(rand1);
            NeuralNetworkPlayer parent2 = newListPlayers.get(rand2);


            if (parent1.getNetwork().equalConnections(parent2.getNetwork()) || parent1.equals(parent2)) {
                i--;
                continue;
            }
            //todo sometimes nets are equal
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
        Neuron in5 = new InputNeuron("length");

        Neuron[] hiddenNeurons = new Neuron[NUM_HIDDEN_NEURONS];
        for (int i = 0; i < hiddenNeurons.length; i++) {
            hiddenNeurons[i] = new HiddenNeuron("hidden " + i);
        }

        Neuron outputNeuron = new OutputNeuron("output");

        return new Neuron[][]{{in1, in2, in3, in4, in5}, hiddenNeurons, {outputNeuron}};

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
