package ru.super4121.testtask.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;

@Component
public class BotHandler extends Thread implements CommandLineRunner {
    @Value("${bot.count}")
    private int countBots;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private LinkedBlockingDeque<String> finishTasks;


    @SneakyThrows
    @Override
    public void run() {
        for (int i = 0; i < countBots; i++) {
            final Thread searcher = applicationContext.getBean(Bot.class);
            executor.execute(searcher);
        }

        for (int i = 0; i < countBots; i++) {
            finishTasks.takeFirst();
        }
        executor.shutdown();
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
