package duke.task;

import duke.Serializable;

import java.util.Objects;

public abstract class Task implements Serializable {
    /** Details about the task. */
    protected final String description;
    /** Keeps track of whether the task has been completed. */
    protected final boolean isDone;

    /**
     * Constructs a new incomplete task with default parameters.
     *
     * @param description details about the task.
     */
    protected Task(String description) {
        this(description, false);
    }

    /**
     * Constructs a new task.
     *
     * @param description details about the task.
     * @param isDone true if the task has been completed, false otherwise.
     */
    protected Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns the description of this task.
     *
     * @return the description of this task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the task has been completed or not.
     *
     * @return true if the task has been completed, otherwise false.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a tick or X symbol depending if the task has been completed.
     *
     * @return a tick (if task is completed) or an X (if not complete).
     */
    private String getStatusIcon() {
        // Symbol codes: tick (\u2713), cross (\u2718)
        return (isDone ? "✓" : "✘");
    }

    /**
     * Returns a copy of this task that has been marked as completed.
     *
     * @return a copy of this task that has been marked as completed.
     */
    public abstract Task markDone();

    /**
     * Returns a text representation of this task for storage purposes.
     *
     * @return a text representation of this task for storage purposes.
     */
    public abstract String serialize();

    /**
     * Returns a text representation of this task for storage purposes.
     *
     * @param taskId the unique id for the type of this task.
     * @param args optional arguments for tasks with additional serializable parameters.
     * @return a text representation of this task for storage purposes.
     */
    protected String serialize(String taskId, String... args) {
        String delimiter = " | ";
        StringBuilder serial = new StringBuilder();

        serial.append(taskId)
                .append(delimiter)
                .append(isDone ? "1" : "0")
                .append(delimiter)
                .append(description);

        for (int i = 0; i < args.length; i++) {
            serial.append(delimiter).append(args[i]);
        }

        // TODO: potential for assertion here

        return serial.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Task) {
            Task task = (Task)obj;
            return this.description.equals(task.description);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
