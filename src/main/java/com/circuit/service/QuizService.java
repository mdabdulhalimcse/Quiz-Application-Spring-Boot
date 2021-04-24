package com.circuit.service;

import com.circuit.model.Question;
import com.circuit.model.QuestionForm;
import com.circuit.model.Result;
import com.circuit.repository.QuestionRepo;
import com.circuit.repository.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuizService {

    @Autowired
    Question question;
    @Autowired
    QuestionForm questionForm;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    Result result;
    @Autowired
    ResultRepo resultRepo;

    public QuestionForm getQuestions(){
        List<Question> allQues = questionRepo.findAll();
        List<Question> qList= new ArrayList<Question>();

        Random random = new Random();

        for (int i=0;i<5;i++){
            int rand = random.nextInt(allQues.size());
            qList.add(allQues.get(rand));
            allQues.remove(rand);
        }
        questionForm.setQuestions(qList);
        return questionForm;
    }

    public int getResult(QuestionForm questionForm){
        int correct = 0;

        for (Question q : questionForm.getQuestions()){
            if (q.getAns() == q.getChose()){
                correct++;
            }
        }
        return correct;
    }

    public  void saveScore(Result result){
        Result saveResult = new Result();
        saveResult.setUsername(result.getUsername());
        saveResult.setTotalCorrect(result.getTotalCorrect());
        resultRepo.save(saveResult);
    }

    public List<Result> getTopScore(){
        List<Result> sList = resultRepo.findAll(Sort.by(Sort.Direction.DESC,"totalCorrect"));

        return sList;
    }
}
