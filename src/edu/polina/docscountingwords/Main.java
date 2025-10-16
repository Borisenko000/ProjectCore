package edu.polina.docscountingwords;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Path> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Path path = Paths.get("C:\\Test\\Текстовый документ " + i + ".txt");
            list.add(path);
        }
        ForkJoinDocTask task = new ForkJoinDocTask(list, 1000);
        Map<String, Long> map = task.compute();
        map.forEach((k, v) -> System.out.println(k + ":" + v));

    }
}
