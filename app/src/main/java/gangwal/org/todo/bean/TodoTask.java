package gangwal.org.todo.bean;

/**
 * Created by gangwal on 1/2/2015.
 */
public class TodoTask {

    private int id;
    private String title;
    private String dueDate;
    private String dueTime;
    private Priority priority;
    private String description;

    public enum Priority {
        HIGH("HIGH"), MEDIUM("MEDIUM"), LOW("LOW");
        private String value;
        private Priority(String value) {
            this.value = value;
        }
        public String getPriorityCode() {
            return value;
        }
    };

    public TodoTask(String title, String dueDate,String dueTime, String priority, String description) {
        this.title = title;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = Priority.valueOf(priority);
        this.description = description;
    }

    public TodoTask(int id, String title, String dueDate, String dueTime,String priority, String description) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = Priority.valueOf(priority);
        this.description = description;
    }

    public TodoTask() {
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = Priority.valueOf(priority);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
