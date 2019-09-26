package api;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import java.util.ArrayList;

public abstract class AsyncTask {


    /**
     * Indicates the status of an {@link AsyncTask}.
     */
    private enum Status {
        FINISHED,
        PENDING,
        RUNNING
    }

    private static final String ASYNCTASK_THREAD_NAME_IDENTIFIER = "com.panickapps.javaasynctask-Thread";
    private static final ArrayList<AsyncTask> tasks = new ArrayList<>();
    private static long ASYNCTASK_THREAD_NEXT_ID = 0;
    private static JFXPanel FX_PANEL_INIT;

    private Status status;
    private Thread taskThread;
    private boolean finalized = false;

    protected AsyncTask() {
        if (FX_PANEL_INIT == null) {
            FX_PANEL_INIT = new JFXPanel();
        }
        ASYNCTASK_THREAD_NEXT_ID++;
        status = Status.PENDING;
        tasks.add(this);
    }

    protected abstract void onPreExecute();

    protected abstract void doInBackground();

    protected abstract void onPostExecute();

    public void execute() {
        onPreExecute();
        taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                status = Status.RUNNING;
                doInBackground();
                status = Status.FINISHED;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute();
                        finalized = true;
                        boolean allFinalized = true;
                        for (AsyncTask task : tasks) {
                            if (!task.finalized) {
                                allFinalized = false;
                                break;
                            }
                        }
                        if (allFinalized) {
                            PlatformImpl.tkExit();
                        }
                    }
                });
            }
        });
        taskThread.setName(ASYNCTASK_THREAD_NAME_IDENTIFIER + ASYNCTASK_THREAD_NEXT_ID);
        ASYNCTASK_THREAD_NEXT_ID++;
        taskThread.start();
    }

}
