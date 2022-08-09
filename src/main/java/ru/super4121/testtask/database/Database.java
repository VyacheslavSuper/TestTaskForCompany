package ru.super4121.testtask.database;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.super4121.testtask.model.Country;

import java.util.concurrent.LinkedBlockingDeque;

@Service
public class Database {
    @Bean
    public LinkedBlockingDeque<Country> createCountryTasks() {
        return new LinkedBlockingDeque<>();
    }

    @Bean("FinishBotTask")
    public LinkedBlockingDeque<String> createFinishTasks() {
        return new LinkedBlockingDeque<>();
    }
}
