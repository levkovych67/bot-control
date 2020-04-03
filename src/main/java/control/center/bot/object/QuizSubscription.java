package control.center.bot.object;

import control.center.bot.service.QuizGetter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedList;

public class QuizSubscription {

    private Long id;
    private LinkedList<Quiz> quizzes;
    private Integer rate;

    public QuizSubscription(Update update) {
        this.id = update.getCallbackQuery().getMessage().getChatId();
        quizzes = QuizGetter.get();
        rate = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LinkedList<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(LinkedList<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Quiz getQuiz() {
        Quiz poll = quizzes.poll();
        if (poll == null) {
            quizzes = QuizGetter.get();
            return quizzes.poll();
        } else {
            return poll;
        }
    }
}
