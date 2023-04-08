package com.unigrad.funiverseappservice.util;

import java.time.Duration;
import java.time.LocalTime;

public class SlotCalculator {

    public static LocalTime calculateStartTime(int order, LocalTime morningStartTime, LocalTime morningEndTime,
                                                    LocalTime afternoonStartTime, int slotDuration, int restTime) {
        int numSlotInMorning = ((int) Duration.between(morningStartTime, morningEndTime).toMinutes() + restTime) / (slotDuration + restTime);

        LocalTime startTime;
        LocalTime endTime;
        if (order <= numSlotInMorning) {
            // slot is in the morning
            startTime = morningStartTime.plusMinutes((long) (order - 1) *(slotDuration + restTime));
        } else {
            // slot is in the afternoon
            startTime = afternoonStartTime.plusMinutes((long) (order - numSlotInMorning - 1) *(slotDuration + restTime));
        }

        return startTime;
    }
}