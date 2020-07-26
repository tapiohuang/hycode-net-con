package org.hycode.net;

public abstract class Schedule extends Worker {
    protected final ScheduleManager scheduleManager;
    protected int cycleTime;
    protected long startTime;

    public Schedule(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Schedule cycleTime(int seconds) {
        this.cycleTime = seconds * 1000;
        return this;
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public abstract void task();

    @Override
    public void work() {
        this.thread = Thread.currentThread();
        while (!stop) {
            synchronized (object) {
                if (run) {
                    try {
                        this.task();
                        object.wait(cycleTime);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
