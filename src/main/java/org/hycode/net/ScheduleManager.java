package org.hycode.net;

import java.util.HashSet;

public class ScheduleManager implements SchedulableAdvisor {
    private final HashSet<Schedule> schedules = new HashSet<>();

    protected void startAllSchedule() {
        schedules.forEach(schedule -> {
            schedule.start();
            schedule.setStartTime(System.currentTimeMillis());
            WorkerThreadPool.POOL.execute(schedule);
        });
    }

    @Override
    public SchedulableAdvisor schedule(Schedule schedule) {
        schedules.add(schedule);
        return this;
    }
}
