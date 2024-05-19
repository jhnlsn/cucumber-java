package com.example.service;

import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class RealDateService implements DateService {
    @Override
    public DayOfWeek getToday() {
        return LocalDate.now().getDayOfWeek();
    }
}
