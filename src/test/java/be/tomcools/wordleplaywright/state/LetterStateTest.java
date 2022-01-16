package be.tomcools.wordleplaywright.state;

import be.tomcools.wordleplaywright.wordselector.MatchedLetters;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class LetterStateTest {

    // TACOS is the correct answer.
    LetterState sut = new LetterState(List.of(new MatchedLetters('t',0)), List.of(new MatchedLetters('a',3)), Set.of('z'));

    @Test
    public void giveMatchingWord_isPossibility_shouldReturnTrue() {
        assertTrue(sut.isPossibility("tacos"));
    }

    @Test
    public void giveNonMatchingWord_isPossibility_shouldReturnFalse() {
        assertFalse(sut.isPossibility("funky"));
    }

    @Test
    public void givenWordContainsWrongCharacter_isPossibility_shouldReturnFalse() {
        assertFalse(sut.isPossibility("tacoz"));
    }

    @Test
    public void givenNoWrongCharactersButMatchingCorrectCharacters_isPossibility_shouldReturnTrue() {
        LetterState sut = new LetterState(List.of(new MatchedLetters('d',0),new MatchedLetters('e',1)), List.of(new MatchedLetters('x',0)), Set.of());

        assertTrue(sut.isPossibility("devoxx"));
    }

    @Test
    public void givenNoCorrectCharactersAndNoneOftheIncorrectCharacters_isPossibility_shouldReturnTrue() {
        LetterState sut = new LetterState(emptyList(), emptyList(), Set.of('g','r'));

        assertTrue(sut.isPossibility("devoxx"));
    }


    @Test
    public void givenEmptyBoard_isPossibility_shouldReturnTrue() {
        LetterState sut = new LetterState(emptyList(), emptyList(), Set.of());

        assertTrue(sut.isPossibility("devoxx"));
    }

    @Test
    public void givenMatchedCharacterAlsoInAbsentPlace_givenValidWord_isPossibilityShouldReturnFalse() {
        LetterState sut = new LetterState(List.of(new MatchedLetters('t',0)), emptyList(), Set.of('t'));

        assertFalse(sut.isPossibility("tacot"));
    }

    @Test
    public void givenMatchedCharacterAlsoInPresentChars_givenValidWord_isPossibilityShouldReturnTrue() {
        // We know T is the first, and tried T as a 2nd but that didn't match.
        LetterState sut = new LetterState(List.of(new MatchedLetters('t',0)), List.of(new MatchedLetters('t',2)), Set.of());

        assertTrue(sut.isPossibility("testd"));
    }

    @Test
    public void givenWeTryAWordWhereALetterExistsButNeedsToBeOnAnotherPosition_shouldReturnFalse() {
        // We know T is in the list, but should not be on the 2nd position
        LetterState sut = new LetterState(emptyList(), List.of(new MatchedLetters('t',1)), Set.of());

        assertFalse(sut.isPossibility("ttstd"));
    }

    @Test
    public void givenDifferntWordWithSameAbsentCharacterOnSameSpace_shouldReturnFalse() {
        LetterState sut = new LetterState(emptyList(), List.of(), Set.of('t'));

        assertFalse(sut.isPossibility("ttstd"));
    }

}
