package be.tomcools.wordleplaywright.wordselector;

import be.tomcools.wordleplaywright.state.LetterState;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WordSelector {
    private List<String> words;

    public WordSelector(List<String> words) {
        this.words = words;
    }

    public String determineNextWord(LetterState letterState) {
        final List<String> validWords = words.stream()
                .filter(letterState::isPossibility)
                .collect(Collectors.toList());

        final String selectedWord = optimize(validWords);

        validWords.remove(selectedWord);

        // Set the word list to only the remaining valid words.
        // Next loops of letterState will resolve it in this case.
        this.words = validWords;

        return selectedWord;
    }

    // This could ofcourse be improved, ex. prioritizing the most frequent letters of the English language.
    private String optimize(List<String> validWords) {
        Collections.shuffle(validWords);
        final Optional<WordAndDiffCharacters> maxDiffCharacters = validWords.stream()
                .map(word -> new WordAndDiffCharacters(word, word.chars().distinct().count()))
                .max(Comparator.comparingLong(WordAndDiffCharacters::uniqueCharacters));
        return maxDiffCharacters.get().word();
    }



}
