package com.working.time;
import java.time.LocalDateTime;

public class TimeEmployee
{
    private LocalDateTime start,end;

    public TimeEmployee(LocalDateTime start, LocalDateTime end)
    {
        this.start=start;
        this.end=end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
