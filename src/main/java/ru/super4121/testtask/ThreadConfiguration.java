package ru.super4121.testtask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfiguration {

    @Value("${bot.count}")
    private int countBots;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskHandlers() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(countBots);
        executor.setMaxPoolSize(countBots);
        executor.setThreadNamePrefix("task_executor_thread_");
        executor.initialize();
        return executor;
    }
}
