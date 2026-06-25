package com.rayssmp.utilities;

import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntervalTask implements Runnable {
    private int seconds = 0;
    private final int intervalTime;
    private Runnable onCompleted;
    private Consumer<Integer> onInterval;
    private Runnable onCancelled;
    private UUID taskUUID;

    /**
     * @param intervalTime How long till the task is considered complete (seconds).
     */
    public IntervalTask(int intervalTime, UUID taskUUID) {
        this.intervalTime = intervalTime;
        this.taskUUID = taskUUID;
    }

    @Override
    public void run() {
        if (++seconds >= intervalTime){
            onCompleted.run();
            return;
        }

        onInterval.accept(seconds);
    }

    private void cancel(){
        onCancelled.run();
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setOnInterval(Consumer<Integer> onInterval) {
        this.onInterval = onInterval;
    }

    public void setOnCompleted(Runnable onCompleted) {
        this.onCompleted = onCompleted;
    }

    public void setOnCancelled(Runnable onCancelled) {
        this.onCancelled = onCancelled;
    }
}
