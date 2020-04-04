package control.center.bot;

import control.center.bot.bots.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Configuration
@EnableScheduling
@SpringBootApplication
public class BotApplication {


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        final AnimeBot animeBot = new AnimeBot();

        final DarkBot darkBot = new DarkBot();
        final PicBot picBot = new PicBot();
        final NewsBot newsBot = new NewsBot();
        final InfoBot infoBot = new InfoBot();
        final FapBot fapBot = new FapBot();
        final VideoBot videoBot = new VideoBot();
        final MusicBot musicBot = new MusicBot();
        final QuizBot quizBot = new QuizBot();
        final BotLikeConsumer botLikeConsumer = new BotLikeConsumer();

        try {
            botsApi.registerBot(darkBot);
            botsApi.registerBot(musicBot);
            botsApi.registerBot(picBot);
            botsApi.registerBot(newsBot);
            botsApi.registerBot(fapBot);
            botsApi.registerBot(videoBot);
            botsApi.registerBot(infoBot);
            botsApi.registerBot(animeBot);
            botsApi.registerBot(quizBot);
            botsApi.registerBot(botLikeConsumer);

        } catch (TelegramApiRequestException e1) {
            e1.printStackTrace();
        }

        SpringApplication.run(BotApplication.class, args);
    }


}
