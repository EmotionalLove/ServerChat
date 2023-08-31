package online.calamitycraft.serverchat.threading;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskManager {

    private Lock lock = new ReentrantLock();
    private static final Object runLock = new Object();
    private ArrayList<Task> taskQueue = new ArrayList<>();

    public TaskManager() {
    }

    /**
     * Add a Task to the queue to be executed.
     *
     * @param task  The task to run
     * @param async Whether to run it on the thread that TaskManager#tick() is being called on, or its own thread.
     */
    public void queueTask(Task task, boolean async) {
        task.async = async;
        lock.lock();
        try {
            taskQueue.add(task);
        } finally {
            lock.unlock();
            synchronized (runLock) {
                runLock.notify();
            }
        }
    }

    public void queueTask(Task task, boolean async, long delay) {
        task.async = async;
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                taskQueue.add(task);
            } finally {
                lock.unlock();
                synchronized (runLock) {
                    runLock.notify();
                }
            }
        }).start();

    }

    /**
     * Constantly updates the taskmanager and executes scheduled tasks
     * WILL BLOCK!!!!
     */
    public void tick() {
        Task someTask;
        synchronized (TaskManager.runLock) {
            while (true) {
                try {
                    if (taskQueue.isEmpty()) {
                        try {
                            runLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    someTask = taskQueue.get(0);
                    if (someTask.async) {
                        new Thread(someTask).start();
                        lock.lock();
                        try {
                            taskQueue.remove(someTask);
                        } finally {
                            lock.unlock();
                        }
                        continue;
                    }
                    someTask.run();
                    lock.lock();
                    try {
                        taskQueue.remove(someTask);
                    } finally {
                        lock.unlock();
                    }
                } catch (Exception e) {
                    //
                }
            }
        }
    }

}
