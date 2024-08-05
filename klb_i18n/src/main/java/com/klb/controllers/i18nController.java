package com.klb.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class i18nController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/greet")
    public String greet(@RequestParam(name = "lang", required = false) Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault(); // Sử dụng ngôn ngữ mặc định nếu không có tham số lang
        }
        return messageSource.getMessage("greeting", null, locale);
    }

    @GetMapping("/test")
    public String test() {
        return "Test successful!";
    }
}
