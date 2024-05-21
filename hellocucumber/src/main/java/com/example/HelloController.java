package com.example;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.DateService;

import java.time.DayOfWeek;

@RestController
public class HelloController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final DateService dateService;

    public HelloController(DateService dateService, KafkaTemplate<String, String> kafkaTemplate) {
        this.dateService = dateService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/isFriday")
    public boolean isTodayFriday(@RequestParam(required = false) Boolean emitEvent) {
        boolean isFriday = dateService.getToday() == DayOfWeek.FRIDAY;
        if (Boolean.TRUE.equals(emitEvent)) {
            // Emit an event to Kafka
            kafkaTemplate.send("isFridayTopic", String.valueOf(isFriday));
        }
        return isFriday;
    }

}