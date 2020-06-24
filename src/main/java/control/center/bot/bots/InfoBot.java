package control.center.bot.bots;

import control.center.bot.configuration.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class InfoBot extends TelegramLongPollingBot {

    private static String bliskavka2() {
        return "Самий топовий канал в галактике - Блискавка 2.0: " + Configuration.deganchanchannellink;
    }

    private static String fap() {
        return "FAP ФАП AFG THREAD " + Configuration.fap;
    }

    private static String black() {
        return "DARK WEBM THREAD: " + Configuration.black;
    }
    private static String music() {
        return "MUSIC THREAD: " + Configuration.music;
    }

//    private static String quziBot() {
//        return "Quiz bot бот з впросами на английском " + Configuration.quizBot;
//    }

//    private static String chatBot() {
//        return "Анонимный чат бот " + Configuration.chatBot;
//    }

//    private static String messageAboutTele2ch() {
//        return "Tele2ch BETA двач в телеграме, разберешся - " + Configuration.tele2ch;
//    }

    private static String other() {
        return "Разработка всех праектов ведется на рандоме поетому пиши сюда что тебе нравиться, шо не нравиться и шо сапортити бо я заебався ";
    }

    private void processTextMessage(Update update) {
        final Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        if ("/info".equals(text) || "/start".equals(text)) {
            System.out.println("Якийта хуй : " + text);
            sendMessage("Здаров маладой", chatId);
            sendMessage(bliskavka2(), chatId);
            sendMessage(music(), chatId);
            sendMessage(black(), chatId);
//            sendMessage(messageAboutTele2ch(), chatId);
            sendMessage(fap(), chatId);
//            sendMessage(quziBot(), chatId);
//            sendMessage(chatBot(), chatId);
//            sendMessage(onime(), chatId);
            sendMessage(other(), chatId);

            sendMessage("ПИШИ СЮДА", chatId);
        } else {
            if (isAdminsResponse(update)) {
                System.out.println("Admen : " + text);
                responseToBomzh(update);
            } else {
                System.out.println("Якийта хуй : " + text);
                final String userName = getUserName(update);
                String messageToBohdan = String.format("%s is asking : \n%s \nin chat №%s", userName, text, chatId);
                sendLog(messageToBohdan);
                sendMessage("Скоро отвечу табы", chatId);
            }
        }
    }

//    private String onime() {
//        return "Канал Аниме для реальнux getero: https://t.me/sho_pacani_onime";
//    }

    private void sendStickerToOdmen(Update update) {
        try {
            execute(new SendSticker()
                    .setChatId(Configuration.BOHDAN_ADMIN_ID)
                    .setSticker(update.getMessage().getSticker().getFileId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean isSticker(Update update) {
        return update.getMessage().getSticker() != null;
    }

    private void responseToBomzh(Update update) {
        final String adminsReply = update.getMessage().getReplyToMessage().getText();
        Long chatId = Long.valueOf(adminsReply.substring(adminsReply.indexOf("№") + 1));
        sendMessage(update.getMessage().getText(), chatId);
    }

    private void sendMessage(String text, Long chatId) {
        try {
            execute(new SendMessage()
                    .enableHtml(true)
                    .setChatId(chatId)
                    .setText(text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendLog(String message) {
        try {
            execute(
                    new SendMessage().enableHtml(true)
                            .setChatId(Configuration.BOHDAN_ADMIN_ID)
                            .setText(message));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getUserName(Update update) {
        return update.getMessage().getFrom().getFirstName() + " " +
                update.getMessage().getFrom().getLastName() + " @" +
                update.getMessage().getFrom().getUserName();
    }

    private Boolean isAdminsResponse(Update update) {
        if (update.getMessage().getReplyToMessage() != null) {
            final String reply = update.getMessage().getReplyToMessage().getText();
            return reply.contains("is asking : ") && reply.contains("in chat");
        } else {
            return false;
        }
    }

    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText() != null) {
            processTextMessage(update);
        }
        if (isSticker(update)) {
            sendStickerToOdmen(update);
        }
    }


    public String getBotUsername() {
        return "ThrashChannelInfoBot";
    }

    @Override
    public String getBotToken() {
        return "800857946:AAEX7GG1Tdrv4W3nKJmD4O4z_dUqgxqRJCY";
    }
}

