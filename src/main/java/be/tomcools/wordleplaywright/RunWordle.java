package be.tomcools.wordleplaywright;

import be.tomcools.wordleplaywright.dictionary.ExtractFromScriptDictionary;
import be.tomcools.wordleplaywright.wordselector.WordSelector;
import be.tomcools.wordleplaywright.pages.WordlePage;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.function.Consumer;

public class RunWordle {

    private static final int MAX_TURNS = 6;

    public static void main(String[] args) {
        runWithPlayWright((browser -> {
            final Page p = browser.newContext().newPage();
            final ExtractFromScriptDictionary ex = new ExtractFromScriptDictionary(p);
            final WordSelector wordSelector = new WordSelector(ex.wordList());

            solveWordle(new WordlePage(p), wordSelector);

            p.close();
        }));
    }

    private static void runWithPlayWright(Consumer<Browser> browserConsumer) {
        try (Playwright playwright = Playwright.create()) {
            final Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(false));
            browserConsumer.accept(browser);
        }
    }

    private static int solveWordle(WordlePage wordlePage, WordSelector wordSelector) {
        wordlePage.startWordle();

        int turns = 0;
        while(!wordlePage.isGameOver() && turns < MAX_TURNS) {
            String nextWord = wordSelector.determineNextWord(wordlePage.determineLetterState());
            wordlePage.typeWord(nextWord);
            turns += 1;
        };

        System.out.println("Finished after: " + turns);
        return turns;
    }


}
