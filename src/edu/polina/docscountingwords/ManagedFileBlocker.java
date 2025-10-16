package edu.polina.docscountingwords;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ManagedFileBlocker implements ForkJoinPool.ManagedBlocker {
    List<Path> docs;
    List<String> result = new ArrayList<>();
    List<String> list = new ArrayList<>();

    public ManagedFileBlocker(List<Path> docs) {
        this.docs = docs;
    }

    @Override
    public boolean isReleasable() {
        return !list.isEmpty();
    }

    @Override
    public boolean block() throws InterruptedException {
        if (list.isEmpty()) {
            try {
                for (Path p : docs) {
                    list = Files.readAllLines(p, Charset.forName("Windows-1251"));
                    result.addAll(list);
                    DocLoader.docsSum++;
                    list.clear();
                }
            } catch (IOException e) {
                System.out.println("Ошибка обработки файла");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public List<String> getResult() {
        return result;
    }
}
