package com.example.quiz.data;

import com.example.quiz.model.Question;

import java.util.ArrayList;

public interface AnwserListAsyncRespone {
    void processFinished(ArrayList<Question> questionArrayList);
}
