package ba.sum.fpmoz.quizapp.models;

import java.util.List;

public class Question {
    private String title;
    private List<String> options;
    private String correct_ans;

    public Question(String title, List<String> options, String correct_ans) {
        this.title = title;
        this.options = options;
        this.correct_ans = correct_ans;
    }

    public Question() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String question) {
        this.title = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }

    public String toString() {
        return "Question{" +
                "question='" + title + '\'' +
                "options='" + options + '\'' +
                "correct_ans='" + correct_ans + '\'' +
                '}';
    }
}
