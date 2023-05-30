package com.example.hiny;

public class Question {
    private String question;
    private String answer;
    private String drug;

    private Integer number;

    // 생성자
    public Question(String question, String answer, String drug) {
        this.question = question;
        this.answer = answer;
        this.drug = drug;
    }

    // Getter 메서드
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDrug() {
        return drug;
    }
}
