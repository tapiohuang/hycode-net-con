package org.hycode.net;

public abstract class Worker implements Runnable {
    protected final Object object = new Object();
    protected boolean run;
    protected boolean stop = false;
    protected Thread thread;

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

    public void resume() {
        synchronized (object) {
            if (!run) {
                this.object.notify();
            }
        }
    }

    protected abstract void work();

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
        this.work();
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    public void start() {
        this.run = true;
    }
}
