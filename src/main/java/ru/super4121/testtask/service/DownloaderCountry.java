package ru.super4121.testtask.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.super4121.testtask.exception.ProjectException;
import ru.super4121.testtask.model.Country;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class DownloaderCountry {
    @Value("${directory.tmp}")
    private String directory;

    @Async("executorThreads")
    public void download(Country country) throws ProjectException {
        countryIsNotNull(country);
        String nameFile = String.format("%s.%s", country.getName(), parseFormatFileFromUrl(country));
        downloadAndSaveImage(country.getFlagUrl(), directory, nameFile);
        log.info("Save country image {}", nameFile);
    }

    private String parseFormatFileFromUrl(Country country) throws ProjectException {
        try {
            String flagUrl = country.getFlagUrl();
            return flagUrl.substring(1 + flagUrl.lastIndexOf("."));
        } catch (IndexOutOfBoundsException e) {
            throw new ProjectException("Country FlagUrl is bad");
        }
    }

    private void downloadAndSaveImage(String url, String directory, String name) throws ProjectException {
        try (InputStream in = new URL(url).openStream()) {
            new File(directory).mkdirs();
            Files.copy(in, Paths.get(String.format("%s/%s", directory, name)), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.warn("Cant download or save image {} {} {}", url, directory, name, e);
            throw new ProjectException("Cant download or save image");
        }
    }

    private void countryIsNotNull(Country country) throws ProjectException {
        if (country == null) {
            throw new ProjectException("Country is null");
        }
    }

}
