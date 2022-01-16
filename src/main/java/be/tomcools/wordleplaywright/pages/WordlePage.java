package be.tomcools.wordleplaywright.pages;

import be.tomcools.wordleplaywright.wordselector.MatchedLetters;
import be.tomcools.wordleplaywright.state.LetterState;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class WordlePage {
    private Page activePage;

    public WordlePage(Page page) {
        this.activePage = page;
    }

    public void startWordle() {
        this.activePage.navigate("https://www.powerlanguage.co.uk/wordle/");
        // Clear the popup
        this.activePage.click(".close-icon");
    }

    public void selectLetter(char character) {
        activePage.click("button[data-key=%s]".formatted(character));
    }

    public void typeWord(String word) {
        if (isNull(word) || word.length() != 5) {
            throw new IllegalArgumentException("Invalid length of word, expected 5 but got " + word);
        }

        for (char c : word.toLowerCase().toCharArray()) {
            selectLetter(c);
        }
        enter();
        try {
            // It takes about 2 seconds for all the cards to flip around... Find way to wait for that animation.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Not professional :-)
            e.printStackTrace();
        }
    }

    public LetterState determineLetterState() {
        Set<Character> absentLetters = new HashSet<>();
        List<MatchedLetters> presentLetters = new ArrayList<>();
        List<MatchedLetters> correctLetters = new ArrayList<>();

        List<ElementHandle> elementHandles = activePage.querySelectorAll("#board .row");
        for (ElementHandle elementHandle : elementHandles) {
            int position = 0;
            for (ElementHandle handle : elementHandle.querySelectorAll(".tile")) {
                String letter = handle.textContent();
                switch (handle.getAttribute("data-state")) {
                    case "present":
                        presentLetters.add(new MatchedLetters(letter.charAt(0), position));
                        break;
                    case "absent":
                        absentLetters.add(letter.charAt(0));
                        break;
                    case "correct":
                        correctLetters.add(new MatchedLetters(letter.charAt(0), position));
                        break;
                }
                position += 1;
            }
        }
        return new LetterState(correctLetters, presentLetters, absentLetters);
    }

    public void enter() {
        activePage.click("text=Enter");
    }

    public boolean isGameOver() {
        ElementHandle element = activePage.querySelector("#game-toaster");
        return nonNull(element) && element.isVisible();
    }
}
