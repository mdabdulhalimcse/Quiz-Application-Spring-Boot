package com.circuit.controller;

import com.circuit.model.QuestionForm;
import com.circuit.model.Result;
import com.circuit.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    Result result;
    @Autowired
    QuizService quizService;

    Boolean submitted = false;

    @ModelAttribute("result")
    public Result getResult() {
        return result;
    }

    @GetMapping("/")
    public String showHome(){
        return "index";
    }

    @PostMapping("/quiz")
    public String showQuiz(@RequestParam String username, Model model, RedirectAttributes ra){
        if (username.equals((""))){
            ra.addFlashAttribute("warning","Must enter the username");
            return "redirect:/";
        }

        submitted = false;
        result.setUsername(username);

        QuestionForm questionForm=quizService.getQuestions();
        model.addAttribute("qForm",questionForm);

        return "quiz";
    }

    @PostMapping("/submit")
    public String getSubmit(@ModelAttribute QuestionForm questionForm,Model model){
        if (!submitted){
            result.setTotalCorrect(quizService.getResult(questionForm));
            quizService.saveScore(result);
            submitted = true;
        }


        return "result";
    }

    @GetMapping("/score")
    public  String showScore(Model model){
        List<Result> sList = quizService.getTopScore();
        model.addAttribute("sList",sList);

        return "scoreboard";
    }


}
