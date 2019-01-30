/**
 *
 */
package com.myou.sample.hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("hello")
public class HelloWorldController {

    @GetMapping("/")
    public String showHello(Model model) {

        model.addAttribute("message","Hello World!!!!!");
        return "hello/HelloWorld";
    }

}
