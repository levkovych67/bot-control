package control.center.bot.bots;

import control.center.bot.object.NewsThread;
import control.center.bot.object.Post;
import control.center.bot.object.ThreadHead;
import control.center.bot.scrapers.ThreadService;
import control.center.bot.service.PostService;
import control.center.bot.util.TYPE;
import control.center.bot.videoprocessor.SendVideoPreprocessor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static control.center.bot.util.Util.*;
import static control.center.bot.util.Util.createKeyBoardRow;

public class AllThreadBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        try {
            if(update.getMessage().getText().equals("/start")){
                sendThreadMenu(update);
            }
        } catch (Exception e){
            processThreadMedia(update.getCallbackQuery().getData(), update.getCallbackQuery().getMessage().getChatId());
        }
    }

    private void processThreadMedia(String data, Long id) {
        Set<Post> allPostsFromThreadByLink = PostService.getAllPostsFromThreadByLink(data);
        Set<String> videosFromPosts = PostService.getVideosFromPosts(allPostsFromThreadByLink);
        for (String e : videosFromPosts) {
            processAndSend(e, id);
        }
        send(new SendMessage().setText("ETO KONEZ TREDOV").setChatId(id));
    }

    private void processAndSend(String videoURL, Long chatId){
        SendVideo process = SendVideoPreprocessor.process(videoURL);
        process.setChatId(chatId);
        process.setReplyMarkup(createLikeMenu());
        send(process);
    }

    @Override
    public String getBotUsername() {
        return "RandomContentStreamingBot";
    }

    @Override
    public String getBotToken() {
        return "1053152102:AAHryko9R5tA7ZI9-WKVcVKjgPCPvcPTHKU";
    }

    private void sendThreadMenu(Update update){
        getThreadsForMenu(40).forEach(e -> {
            send(e, update.getMessage().getChatId());
        });
        send(new SendMessage().setText("ETO KONEZ TREDOV").setChatId(update.getMessage().getChatId()));


    }

    public static Set<NewsThread> getThreadsForMenu(Integer limit) {
        final Set<ThreadHead> news = ThreadService.getNews(limit);
        return news.stream().map(NewsThread::new).collect(Collectors.toSet());
    }

    private void send(NewsThread newsThread, Long chatId) {

        final List<InlineKeyboardButton> inlineKeyboardButtons =
                Arrays.asList(
                        createInlineButtonText("media️", newsThread.getLink()),
                        createInlineButtonLink("В тред", newsThread.getLink()));

        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setParseMode("HTML")
                .setText(newsThread.getText())
                .setReplyMarkup(createKeyBoardRow(inlineKeyboardButtons));
        send(sendMessage);
    }

    private void send(SendMessage sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendPhoto sendVideo) {
        try {
            Message execute = execute(sendVideo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
