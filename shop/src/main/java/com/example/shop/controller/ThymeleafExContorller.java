package com.example.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/thymeleaf")
public class ThymeleafExContorller {

    @GetMapping(value="/ex01")
    public String thymeleafExample01(Model model){
        model.addAttribute("data","타임리프 예제 입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value="/personalEx")
    public String thymeleafPersonalEx01(Model model){
        model.addAttribute("data","김성수입니다.");
        return "thymeleafEx/thymeleafPersonalEx01";
    }
}
