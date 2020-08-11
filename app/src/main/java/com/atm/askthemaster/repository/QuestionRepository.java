package com.atm.askthemaster.repository;

import com.atm.askthemaster.callback.CallBack;
import com.atm.askthemaster.model.Question;

import java.util.HashMap;

public interface QuestionRepository {
    void createQuestion(Question question, CallBack callBack);

    void updateQuestion(String questionId, HashMap map, CallBack callBack);
}
