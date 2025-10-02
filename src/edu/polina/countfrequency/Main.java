package edu.polina.countfrequency;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\fact.txt");
        List<String> text = new ArrayList<>();
        try {
            text = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (text.isEmpty()) {
                System.out.println("Файл пустой");
            }
        } catch (IOException _) {
            System.out.println("Ошибка чтения файла");
        }
        Map<String, Long> res = text.stream()
                .map(s -> s.toLowerCase().replaceAll("\\p{Punct}", ""))
                .flatMap(k -> Arrays.stream(k.split("\\s+")))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        res.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}