package ru.super4121.testtask;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.super4121.testtask.model.Country;
import ru.super4121.testtask.service.DownloaderCountry;
import ru.super4121.testtask.service.SearcherCountry;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class UpdateCountries implements CommandLineRunner {
    @Autowired
    private SearcherCountry searcherCountry;
    @Autowired
    private DownloaderCountry downloaderCountry;
    @Value("${codes.all}")
    private String[] codes;

    @Override
    public void run(String... args) {
        try {
            List<String> codesList = Arrays.asList(codes);
            List<Country> countriesList = searcherCountry.getCountry(codesList);

            log.info("Find {} countries", countriesList.size());

            for (Country country : countriesList) {
                downloaderCountry.download(country);
            }

        } catch (Exception e) {
            log.error("Cant find countries with codes {}", codes);
        }
    }
}
