package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;

@RestController
public class HelloController {

    @GetMapping("/isFriday")
    public boolean isTodayFriday() {
        return LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY;
    }

}