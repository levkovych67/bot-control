package control.center.bot.bots;


import control.center.bot.mappers.QuizToTQuizMapper;
import control.center.bot.object.Quiz;
import control.center.bot.object.QuizSubscription;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
public class QuizBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.getMessage().getText().equals("/start")) {
                Quiz quiz = new Quiz();
                quiz.setCorrectAnswer("DA");
                quiz.setIncorrectAnswers(Arrays.asList("NE", "MOZHLIVO", "NE, MYZHIK GAMNO"));
                quiz.setQuestion("BOMZH LOX ?");
                SendPoll poll = QuizToTQuizMapper.map(quiz, update.getMessage().getChatId());
                send(poll);
            }
        } catch (Exception e) {
            QuizSubscription quizSubscription = new QuizSubscription(update);
            SendPoll sendPoll = QuizToTQuizMapper.map(quizSubscription.getQuiz(), update.getCallbackQuery().getMessage().getChatId());
            send(sendPoll);
        }
    }

    @Override
    public String getBotUsername() {
        return "QuizCacaoBot";
    }

    void send(SendPoll sendPoll) {
        try {
            execute(sendPoll);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "1030453447:AAEMu6b7vttqXKNE-MQCFtIFeOrXOePhfgc";
    }
}
