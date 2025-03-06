package com.example.excluzscheduler.common.scheduler;

import lombok.Getter;

@Getter
public enum SchedulerSkipTime {
    REAL_TIME_SKIP(
        3,  // SKIP_HOUR: 새벽 3시
        0,  // SKIP_MINUTE_START: 0분
        20  // SKIP_MINUTE_END: 20분
    );

    private final int skipHour;
    private final int skipMinuteStart;
    private final int skipMinuteEnd;

    SchedulerSkipTime(int skipHour, int skipMinuteStart, int skipMinuteEnd) {
        this.skipHour = skipHour;
        this.skipMinuteStart = skipMinuteStart;
        this.skipMinuteEnd = skipMinuteEnd;
    }

    /**
     * 주어진 시간이 스킵 시간 범위 내에 있는지 확인
     * @param hour 현재 시간 (0-23)
     * @param minute 현재 분 (0-59)
     * @return true if 시간 범위 내에 있음
     */
    public boolean isWithinSkipRange(int hour, int minute) {
        return hour == skipHour && minute >= skipMinuteStart && minute <= skipMinuteEnd;
    }
}