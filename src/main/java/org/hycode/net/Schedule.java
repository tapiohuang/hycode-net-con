package org.hycode.net;

public abstract class Schedule implements Runnable {
    protected final ScheduleManager scheduleManager;
    protected final Object object = new Object();
    protected boolean run;
    protected boolean stop = false;
    protected Thread thread;
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

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    protected abstract void task();


    public void resume() {
        synchronized (object) {
            if (!run) {
                this.object.notify();
            }
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void pause() {
        if (run) {
            this.run = false;
        }
    }

    @Override
    public void run() {
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

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    public void start() {
        this.run = true;
    }
}
