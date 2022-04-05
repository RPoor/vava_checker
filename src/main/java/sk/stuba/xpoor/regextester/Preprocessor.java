package sk.stuba.xpoor.regextester;

import java.util.function.Function;

public enum Preprocessor {
    CaseInsensitive(String::toLowerCase),
    IgnoreSpecialCharacters((str) -> str.replaceAll("[^a-zA-Z0-9 ]", "")),
    TrimClean((str) -> str.replaceAll("\\P{Print}", "").trim())
    ;
    private final Function<String, String> f;
    Preprocessor(Function<String, String> preprocessor) {
        this.f = preprocessor;
    }

    public String apply(String s) {
        return f.apply(s);
    }
}
