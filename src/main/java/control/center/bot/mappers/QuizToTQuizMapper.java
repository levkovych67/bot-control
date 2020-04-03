package control.center.bot.mappers;

import control.center.bot.object.Quiz;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class QuizToTQuizMapper {

    private static Integer count = 0;

    public static SendPoll map(Quiz quiz, Long id) {
        SendPoll sendPoll = new SendPoll();
        sendPoll.setQuestion(Jsoup.parse(quiz.getQuestion()).text());
        List<Map.Entry<String, Boolean>> answers = quiz.getAnswers();
        sendPoll.setType("quiz");
        sendPoll.setAnonymous(true);

        AtomicReference<Integer> counter = new AtomicReference<>(0);
        List<String> stringAnswers = new ArrayList<>();
        answers.forEach(e -> {
            if (e.getValue().equals(Boolean.TRUE)) {
                sendPoll.setCorrectOptionId(counter.getAndSet(counter.get() + 1));
                stringAnswers.add(Jsoup.parse(e.getKey()).text());
            } else {
                stringAnswers.add(Jsoup.parse(e.getKey()).text());
                counter.getAndSet(counter.get() + 1);
            }
        });
        sendPoll.setOptions(stringAnswers);

        sendPoll.setChatId(id);


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(
                Arrays.asList(
                        Arrays.asList(
                                new InlineKeyboardButton()
                                        .setCallbackData("next")
                                        .setText("next"))));

        sendPoll.setReplyMarkup(inlineKeyboardMarkup);

        count = count + 1;
        System.out.println(count + " quiz requested");

        return sendPoll;
    }
}
