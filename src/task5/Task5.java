package task5;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class Task5 {
    public static void main(String[] args) {
        Task5 task5 = new Task5();
        task5.start();
    }

    private void start() {
        List<String> results = createFolderProcessor().join();
        countWordsAndWriteToFile(results);
    }

    private FolderProcessor createFolderProcessor() {
        ForkJoinPool pool = new ForkJoinPool();
        FolderProcessor folder = new FolderProcessor(Constants.researchingFolder, Constants.searchingExtension);
        pool.execute(folder);
        pool.shutdown();
        return folder;
    }

    private void countWordsAndWriteToFile(List<String> paths) {
        ArrayList<Integer> counts = new ArrayList<>();
        paths.forEach(path -> {
            File file = new File(path);
            int amountOfLetter  = countWordsInFile(file);
            counts.add(amountOfLetter);
        });
        writeToFile(paths, counts);
    }

    private int countWordsInFile(File file) {
        String requiredLetter = Constants.requiredStartingLetter;
        ArrayList<String> words = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {

            stream.map(line -> line.split("\\W"))
                    .forEach(wordsArray -> Arrays.stream(wordsArray)
                            .filter(word -> word.startsWith(requiredLetter))
                            .forEach(words::add));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return words.size();
    }

    private void writeToFile(List<String> paths, ArrayList<Integer> count) {
        try {
            Path path = Paths.get(Constants.storageFile);
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            for (int i = 0; i < paths.size(); i++) {
                writer.append(paths.get(i)).append(" - ").append(String.valueOf(count.get(i))).append(" required letter usage");
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
