package be.tomcools.wordleplaywright;

import be.tomcools.wordleplaywright.dictionary.Dictionary;
import be.tomcools.wordleplaywright.dictionary.ExtractFromScriptDictionary;
import be.tomcools.wordleplaywright.dictionary.FileDictionary;
import be.tomcools.wordleplaywright.dictionary.InstaWinDictionary;
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

            // Choose one of these ;)
            // Read dictionary file: FileDictionary fileDictionary = new FileDictionary();
            // Extract dictionary from JS: ExtractFromScriptDictionary extractFromJsFile = new ExtractFromScriptDictionary(p);
            // Strip solution from LocalStorage using JS: InstaWinDictionary instaWinDictionary = new InstaWinDictionary(p);
            Dictionary dictionary = new InstaWinDictionary(p);

            final WordSelector wordSelector = new WordSelector(dictionary);

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
