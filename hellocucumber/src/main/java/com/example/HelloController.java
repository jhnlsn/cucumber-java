package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.DateService;

import java.time.DayOfWeek;

@RestController
public class HelloController {

    private final DateService dateService;

    public HelloController(DateService dateService) {
        this.dateService = dateService;
    }

    @GetMapping("/isFriday")
    public boolean isTodayFriday() {
        return dateService.getToday() == DayOfWeek.FRIDAY;
    }

}