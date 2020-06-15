package control.center.bot.bots;

import control.center.bot.object.SendVideoFileHolder;
import control.center.bot.service.ContentGetter;
import control.center.bot.util.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Component
public class DarkBot extends TelegramLongPollingBot {

    private Long DARK_ID = -1001483764823L;
    private List<String> searchWords = Arrays.asList("dark", "black", "дарк", "блек", "military", "милитари");
    private ContentGetter contentGetter = new ContentGetter(searchWords, Collections.emptyList());
    public static final String DARK_CRON = "0 */60  * * * *";

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "ElephantDark";
    }

    @Override
    public String getBotToken() {
        return "1066288750:AAEo2RXrgICINHI1UVqltfkUIy0i5rZ1j1c";
    }

    @Scheduled(cron = DARK_CRON)
    public void send() {
        SendVideoFileHolder video = contentGetter.getVideo();
        if (video != null) {
            send(buildWithButton(video));
            video.deleteFiles();
        }
    }

    public SendVideo buildWithButton(SendVideoFileHolder video){
        if (createButton()){
            return video.getSendVideo()
                    .setReplyMarkup(Util.createKeyBoardRow(Arrays.asList(Util.createInlineButtonLink("\uD83D\uDCB5 на мивинку чи контент", "https://www.donationalerts.com/r/bliskavka"))))
                    .setChatId(DARK_ID);

        } else {
             return video.getSendVideo().setChatId(DARK_ID);
        }
    }

    public static Boolean createButton(){
       return new Random().nextBoolean();
    }



    private void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            System.out.println("Could not send video " + sendVideo.toString());
        }
    }
}
