package ru.super4121.testtask.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.super4121.testtask.model.Country;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
@Component
@Lazy
public class Bot extends Thread {

    @Autowired
    private LinkedBlockingDeque<Country> countries;
    @Autowired
    @Qualifier("FinishBotTask")
    private LinkedBlockingDeque<String> finishTasks;

    @Value("${directory.tmp}")
    private String directory;

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Country country = countries.takeFirst();
                if (country.getName() == null || country.getFlagUrl() == null) {
                    finishTasks.putLast("FINISH");
                    log.info("Thread bot closed");
                    break;
                }

                String nameFile = String.format("%s.%s", country.getName(), country.getFormatFileFromUrl());
                try {
                    downloadAndSaveImage(country.getFlagUrl(), directory, nameFile);
                } catch (IOException e) {
                    log.warn("Cant download or save image {} {} {}", country, directory, nameFile, e);
                }
            }
        } catch (InterruptedException ignore) {
        }
    }

    private void downloadAndSaveImage(String url, String directory, String name) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            new File(directory).mkdirs();
            Files.copy(in, Paths.get(String.format("%s/%s", directory, name)), StandardCopyOption.REPLACE_EXISTING);
        }
        log.info("Save country image {}", name);
    }

}
