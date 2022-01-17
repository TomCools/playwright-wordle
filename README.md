# Automated Wordle using Playwright (Java)

This is an experiment project I used to learn more about UI Automation with Playwright.

What's in this repository:

- 2 ways to get a list of words: from a file, or directly from the game's javascript file;
- A solver, to select appropriate next words, with some minor optimizations (avoid words with double occurances of the same characters);
- A [Page Object](WordlePage) class containing the Playwright code to interact with the page;

I also described my experience building this on [my blog](https://www.tomcools.be/post/jan-2022-playwright-wordle/).

I built the initial setup of this project during a live [Twitch Stream](https://www.twitch.tv/videos/1263512453).
One of the people in chat (thanks for joining DasBrain!) decided make their own implementation, which can be found [on their github page](https://github.com/DasBrain/playwright-wordle/tree/master)
