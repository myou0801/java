package com.myou.sample.app.controller.sample01;

import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sample01")
public class Sample01Controller {
    @Autowired
    private MessageSource messageSource;

    @ModelAttribute
    public Sample01Form setupForm() {
      return new Sample01Form();
    }

    @GetMapping("/")
    public String init() {
        return "sample/sample01";
    }

    @PostMapping("confirm")
    public String confirm(@Validated Sample01Form sample01Form, BindingResult result) {
        if (result.hasErrors()) {

            String msg = result.getAllErrors().stream().map(e -> messageSource.getMessage(e, Locale.JAPANESE))
                    .collect(Collectors.joining(","));
            System.out.println(msg);
        }
        return "sample/sample01";
    }

}
