public class Worker {
    OnTaskDoneListener callback;
    OnTaskErrorListener errorCallback;

    public Worker(OnTaskDoneListener callback, OnTaskErrorListener errorCallback) {
        this.callback = callback;
        this.errorCallback = errorCallback;
    }

    public void start() {
        for (int i = 1; i < 101; i++) {
            if (i == 33) {
                errorCallback.onError("Task " + i + " has errors");
                continue;
            }
            callback.onDone("Task " + i + " is done");
        }
    }
}
