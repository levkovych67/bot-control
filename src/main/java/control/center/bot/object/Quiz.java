package control.center.bot.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.shuffle;

public class Quiz {

    private String category;
    private String difficulty;
    private String question;
    private String type;
    private Map<String, Boolean> answers;
    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;
    @JsonProperty("correct_answer")
    private String correctAnswer;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public String getCorrectAnswer() {
        System.out.println("correct answer is " + correctAnswer);
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Map.Entry<String, Boolean>> getAnswers() {
        Map<String, Boolean> map = new HashMap<>();
        map.put(this.getCorrectAnswer(), true);
        for (String answer : this.getIncorrectAnswers()) {
            map.put(answer, false);
        }
        ArrayList<Map.Entry<String, Boolean>> entries = new ArrayList<>(map.entrySet());
        shuffle(entries);
        return entries;
    }

    public void setAnswers(Map<String, Boolean> answers) {
        this.answers = answers;
    }
}
