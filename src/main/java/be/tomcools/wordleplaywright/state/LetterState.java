package be.tomcools.wordleplaywright.state;

import be.tomcools.wordleplaywright.wordselector.MatchedLetters;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LetterState {
    private final List<MatchedLetters> correctChars;
    private final List<MatchedLetters> presentChars;
    private final Set<Character> absentCharacters;

    public LetterState(List<MatchedLetters> correctChars, List<MatchedLetters> presentChars, Set<Character> absent) {
        this.correctChars = correctChars;
        this.presentChars = presentChars;
        this.absentCharacters = absent;
    }

    public boolean isPossibility(String tryWord) {
        char[] lettersInTryWord = tryWord.toCharArray();
        for (int i = 0; i < 5; i++) {
            int index = i;
            Optional<MatchedLetters> letterForPosition = correctChars
                    .stream()
                    .filter(m -> m.position() == index)
                    .findAny();
            if(letterForPosition.isPresent()) {
                // Nice, we already know what this letter is, check it.
                if(letterForPosition.get().letter() != lettersInTryWord[i]) {
                    return false;
                }
            } else {
                // We do not know the value of this index, but we should check it against all the letters we know it isn't.
                for (Character absentCharacter : absentCharacters) {
                    if(absentCharacter == lettersInTryWord[i]) {
                        // Word does not contain the letter in the wrong place... so it can't be correct.
                        return false;
                    }
                }
            }
        }

        for (int i = 0; i < presentChars.size(); i++) {
            final MatchedLetters presentCharacter = presentChars.get(i);
            if(!tryWord.contains(String.valueOf(presentCharacter.letter())) // we know the word should contain the letter.
               || tryWord.charAt(presentCharacter.position()) == presentCharacter.letter()) { //but it has to be in a different place.
                return false;
            }
        }

        return true;
    }
}
