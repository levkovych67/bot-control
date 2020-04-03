package control.center.bot.service;

import control.center.bot.object.Quiz;
import control.center.bot.object.QuizList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;

public class QuizGetter {

    public static LinkedList<Quiz> get() {
        ResponseEntity<QuizList> response = new RestTemplate().getForEntity("https://opentdb.com/api.php?amount=10000", QuizList.class);
        return new LinkedList<>(response.getBody().getResults());
    }

}
