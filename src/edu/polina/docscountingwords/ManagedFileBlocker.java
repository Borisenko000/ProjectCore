package edu.polina.docscountingwords;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ManagedFileBlocker implements ForkJoinPool.ManagedBlocker {
    public volatile Path path;
    public List<String> list;


    public ManagedFileBlocker(Path path) {
        this.path = path;
    }

    @Override
    public boolean isReleasable() {
        return list != null;
    }

    @Override
    public boolean block() {
        while (!isReleasable()) {
            try {
                list = Files.readAllLines(path, Charset.forName("Windows-1251"));
            } catch (IOException _) {
                System.out.println("Ошибка чтения файла");
            }
        }
        return true;
    }

    public List<String> getResult() {
        return list;
    }
}
