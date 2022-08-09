package ru.super4121.testtask;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.super4121.testtask.model.Country;
import ru.super4121.testtask.parser.ParserCountry;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

@Component
@Slf4j
public class SearcherCountries implements CommandLineRunner {

    @Autowired
    private ParserCountry parserCountry;
    @Autowired
    private LinkedBlockingDeque<Country> countries;
    @Value("${codes.all}")
    private String[] codes;

    @Value("${bot.count}")
    private int countBots;

    @Override
    public void run(String... args) {

        try {
            List<String> codesList = Arrays.asList(codes);
            List<Country> countriesList = parserCountry.getCountry(codesList);
            for (Country country : countriesList) {
                countries.putLast(country);
            }

            for (int i = 0; i < countBots; i++) {
                countries.putLast(new Country(null, null));
            }

            log.info("Find {} countries", countriesList.size());
        } catch (Exception e) {
            log.warn("Cant find countries with codes {}", codes);
        }
    }
}
