package com.working.time;

import java.time.LocalDateTime;

public class Utils {

    public static LocalDateTime getTimeStartWorking(LocalDateTime start){
        return LocalDateTime.of(start.getYear(),start.getMonth(),start.getDayOfMonth(), Constants.START_HOURS, Constants.START_MINUTES, Constants.START_SECONDS);
    }

    public static LocalDateTime getTimeEndWorking(LocalDateTime end){
        return LocalDateTime.of(end.getYear(),end.getMonth(), end.getDayOfMonth(), Constants.END_HOURS, Constants.END_MINUTES, Constants.END_SECONDS);
    }

}
