package edu.polina.docscountingwords;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DocLoader {
    public static int docsSum = 0;
    static List<String> list = new ArrayList<>();

    public static List<String> load(Path path) {
        if (!Files.exists(path)) {
            System.out.println("Файл не найден");
        }
        try {
            list = Files.readAllLines(path, Charset.forName("Windows-1251"));
            List<String> result = list;
            docsSum++;
            list.clear();
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return result;
        } catch (IOException e) {
            System.out.println("Ошибка обработки файла");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}