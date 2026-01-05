package db;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SupabaseWorker extends Thread {

    private static final BlockingQueue<DbTask> queue =
            new LinkedBlockingQueue<>();

    public static void submit(DbTask task) {
        queue.offer(task);
    }

    @Override
    public void run() {
        while (true) {
            try {
                DbTask task = queue.take();
                task.execute(); // DB I/O happens here
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}


