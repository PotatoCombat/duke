public class Task {
    /** Details about the task. */
    protected final String description;
    /** Keeps track of whether the task has been completed. */
    protected final boolean isDone;

    /**
     * Constructs a new incomplete task.
     *
     * @param description details about the task.
     */
    public Task(String description) {
        this(description, false);
    }

    private Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns a tick or X symbol depending if the task has been completed.
     *
     * @return a tick (if task is completed) or an X (if not complete).
     */
    public String getStatusIcon() {
        return (isDone ? "✓" : "✘");
        //return (isDone ? "\u2713" : "\u2718");
    }

    /**
     * Returns a copy of this task that has been marked as completed.
     *
     * @return a copy of this task that has been marked as completed.
     */
    public Task markDone() {
        return new Task(description, true);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }
}
