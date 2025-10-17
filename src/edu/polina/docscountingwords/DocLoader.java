package edu.polina.docscountingwords;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DocLoader {

    public List<String> load(Path path) {
        if (!Files.exists(path)) {
            System.out.println("Файл не найден");
        }
        try {
            List<String> list = Files.readAllLines(path, Charset.forName("Windows-1251"));
            return list;
        } catch (IOException e) {
            System.out.println("Ошибка обработки файла");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}