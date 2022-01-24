package be.tomcools.wordleplaywright.dictionary;

import com.microsoft.playwright.JSHandle;
import com.microsoft.playwright.Page;

import java.util.List;

public class InstaWinDictionary implements Dictionary {

    private final Page page;

    public InstaWinDictionary(Page page) {
        this.page = page;
    }

    @Override
    public List<String> wordList() {
        // Use Javascript to read localStorage containing the solution.
        String solution = page.evaluateHandle("JSON.parse(localStorage.gameState).solution").toString();

        return List.of(solution);
    }
}
