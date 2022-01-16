package be.tomcools.wordleplaywright.dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileDictionary implements Dictionary {
    // This reads the words from a file.
    public List<String> wordList() {
        List<String> words = new ArrayList<>();
        Path path = Path.of("words");
        try (var lines = Files.lines(path)) {
            words = lines
                    .filter(line -> line.length() == 5)
                    .map(String::toLowerCase)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return words;
    }
}
