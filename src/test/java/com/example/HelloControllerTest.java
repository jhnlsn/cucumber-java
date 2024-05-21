package com.example;

import com.example.service.DateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DateService dateService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testIsTodayFriday() throws Exception {
        when(dateService.getToday()).thenReturn(DayOfWeek.FRIDAY);

        mockMvc.perform(get("/isFriday"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(dateService).getToday();
    }

    @Test
    public void testIsTodayFridayWithEmitEvent() throws Exception {
        when(dateService.getToday()).thenReturn(DayOfWeek.FRIDAY);

        mockMvc.perform(get("/isFriday").param("emitEvent", "true"))
                .andExpect(status().isOk());

        verify(dateService).getToday();
        verify(kafkaTemplate).send(eq("isFridayTopic"), any(String.class));
    }
}