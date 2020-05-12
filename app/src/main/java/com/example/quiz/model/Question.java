package com.example.quiz.model;

public class Question {
    private String answer;
    private boolean anwserTrue;

    public Question() {

    }

    public Question(String answer, boolean anwserTrue) {
        this.answer = answer;
        this.anwserTrue = anwserTrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnwserTrue() {
        return anwserTrue;
    }

    public void setAnwserTrue(boolean anwserTrue) {
        this.anwserTrue = anwserTrue;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", anwserTrue=" + anwserTrue +
                '}';
    }
}
