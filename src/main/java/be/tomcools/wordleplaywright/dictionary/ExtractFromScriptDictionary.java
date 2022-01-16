package be.tomcools.wordleplaywright.dictionary;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Instead of reading a bunch of words the game doesn't understand, we take all the words from the JS file ;)
public class ExtractFromScriptDictionary implements Dictionary {
    private final static Pattern FIVE_TOKENS_PATTERN = Pattern.compile("\"[a-z]{5}\"");
    private final Page page;

    public ExtractFromScriptDictionary(Page page) {
        this.page = page;
    }

    @Override
    public List<String> wordList() {
        final Response response = page.waitForResponse(r -> r.url().contains("main") && r.url().endsWith("js"), () -> {
            this.page.navigate("https://www.powerlanguage.co.uk/wordle/");
        });

        return extractWordList(response);
    }

    private List<String> extractWordList(Response r) {
        final String jsonFile = r.text();
        final Matcher matcher = FIVE_TOKENS_PATTERN.matcher(jsonFile);
        return matcher.results()
                .map(MatchResult::group)
                .map(s -> s.replace("\"", "")) // remove extra quotes
                .distinct() // remove the duplicates
                .collect(Collectors.toList());
    }
}
