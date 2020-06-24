package control.center.bot;

import control.center.bot.bots.*;
import control.center.bot.youtube.YouTubeAuthHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@Configuration
@EnableScheduling
@SpringBootApplication
public class BotApplication {


    public static void main(String[] args) {
        YouTubeAuthHolder.initClient();
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        final DarkBot darkBot = new DarkBot();
        final PicBot picBot = new PicBot();
        final NewsBot newsBot = new NewsBot();
        final InfoBot infoBot = new InfoBot();
        final FapBot fapBot = new FapBot();
        final VideoBot videoBot = new VideoBot();
        final MusicBot musicBot = new MusicBot();
        final QuizBot quizBot = new QuizBot();
        final AllThreadBot threadBot = new AllThreadBot();
        //final BotLikeConsumer botLikeConsumer = new BotLikeConsumer();


        try {
            botsApi.registerBot(darkBot);
            botsApi.registerBot(musicBot);
            botsApi.registerBot(picBot);
            botsApi.registerBot(newsBot);
            botsApi.registerBot(fapBot);
            botsApi.registerBot(videoBot);
            botsApi.registerBot(infoBot);
            botsApi.registerBot(quizBot);
            botsApi.registerBot(threadBot);
            //botsApi.registerBot(botLikeConsumer);
        } catch (Exception ignored) {

        }

        SpringApplication.run(BotApplication.class, args);
    }


}
