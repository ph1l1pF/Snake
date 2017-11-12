package Evolution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class StatsWriter {

    private static boolean firstWrite = true;

    public void writeStats(List<String> lines) {
        try {
            Path path = Paths.get("stats.txt");
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            if (firstWrite) {
                List<String> headLine = new ArrayList<>();
                headLine.add("min, max, avg, gen");
                Files.write(path, headLine);
                firstWrite = false;
            }
            Files.write(path, lines, StandardOpenOption.APPEND);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
