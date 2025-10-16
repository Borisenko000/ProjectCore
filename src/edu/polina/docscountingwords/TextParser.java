package edu.polina.docscountingwords;

import java.util.Arrays;
import java.util.List;

public class TextParser {

    public static List<String> toWords(List<String> text) {
        return text.stream().flatMap(s -> Arrays.stream(s.split("\\s+")))
                .map(s -> s.replaceAll("\\w", ""))
                .map(String::toLowerCase)
                .filter(token -> !token.isEmpty())
                .toList();
    }
}
