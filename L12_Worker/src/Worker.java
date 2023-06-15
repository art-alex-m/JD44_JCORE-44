public class Worker {
    OnTaskDoneListener onTaskDoneListener;
    OnTaskErrorListener onTaskErrorListener;

    public Worker(OnTaskDoneListener onTaskDoneListener, OnTaskErrorListener onTaskErrorListener) {
        this.onTaskDoneListener = onTaskDoneListener;
        this.onTaskErrorListener = onTaskErrorListener;
    }

    public void start() {
        for (int i = 1; i < 101; i++) {
            if (i == 33) {
                onTaskErrorListener.onError("Task " + i + " has errors");
                continue;
            }
            onTaskDoneListener.onDone("Task " + i + " is done");
        }
    }
}
