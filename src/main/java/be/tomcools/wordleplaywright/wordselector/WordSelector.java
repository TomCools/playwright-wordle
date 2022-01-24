package be.tomcools.wordleplaywright.wordselector;

import be.tomcools.wordleplaywright.dictionary.Dictionary;
import be.tomcools.wordleplaywright.state.LetterState;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WordSelector {
    private Dictionary dictionary;

    public WordSelector(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String determineNextWord(LetterState letterState) {
        final List<String> validWords = dictionary.wordList()
                .stream()
                .filter(letterState::isPossibility)
                .collect(Collectors.toList());

        final String selectedWord = optimize(validWords);

        validWords.remove(selectedWord);

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
