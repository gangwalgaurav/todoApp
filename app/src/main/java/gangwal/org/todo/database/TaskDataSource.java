package gangwal.org.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import gangwal.org.todo.bean.TodoTask;

/**
 * Created by gangwal on 1/2/15.
 */
public class TaskDataSource {

    // Database fields
    private SQLiteDatabase database;
    private TaskDBHelper dbHelper;

    public TaskDataSource(Context context) {
        dbHelper = new TaskDBHelper(context);
        open();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Add task to Database
     * @param task
     */
    public void createTask(TodoTask task)
    {
        ContentValues values = new ContentValues();
        values.put(TaskDBHelper.TITLE, task.getTitle());
        values.put(TaskDBHelper.DUE_DATE, task.getDueDate());
        values.put(TaskDBHelper.DUE_TIME, task.getDueTime());
        values.put(TaskDBHelper.PRIORITY, task.getPriority().getPriorityCode());
        values.put(TaskDBHelper.DESCRIPTION, task.getDescription());
        try {
            database.insert(TaskDBHelper.TABLE_TASK, null, values);
        }
        catch (Exception e)
        {
            System.out.println("Error"+ e.getMessage());
        }
        database.close(); // Closing database connection
    }

    /**
     * Delete todoTask from database
     * @param todoTask
     */
    public void deleteTask(TodoTask todoTask)
    {
        long index = todoTask.getId();
        database.delete(TaskDBHelper.TABLE_TASK, TaskDBHelper.INDEX
                + " = " + index, null);
    }

    /**
     * Truncate the table
     */
    public void deleteAllTask()
    {
        database.execSQL("delete from "+ TaskDBHelper.TABLE_TASK);
    }

    /**
     * Update the current task
     * @param todoTask
     * @return value >0 if update
     */
    public int updateTask(TodoTask todoTask)
    {
        ContentValues values = new ContentValues();
        values.put(TaskDBHelper.TITLE, todoTask.getTitle());
        values.put(TaskDBHelper.DUE_DATE, todoTask.getDueDate());
        values.put(TaskDBHelper.DUE_TIME, todoTask.getDueTime());
        values.put(TaskDBHelper.PRIORITY, todoTask.getPriority().toString());
        values.put(TaskDBHelper.DESCRIPTION, todoTask.getDescription());
        return database.update(TaskDBHelper.TABLE_TASK, values, TaskDBHelper.INDEX + " = ?",
                new String[]{String.valueOf(todoTask.getId())});
    }

    public TodoTask getTask(int index)
    {
        Cursor cursor = database.query(TaskDBHelper.TABLE_TASK, new String[]{TaskDBHelper.INDEX,
                        TaskDBHelper.TITLE, TaskDBHelper.DUE_DATE,TaskDBHelper.DUE_TIME, TaskDBHelper.PRIORITY, TaskDBHelper.DESCRIPTION}, TaskDBHelper.INDEX + "=?",
                new String[]{String.valueOf(index)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TodoTask todoItem = new TodoTask(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return todoItem;
    }

    /**
     * Return list of all task
     * @return
     */
    public ArrayList<TodoTask> getAllTask() {
        ArrayList<TodoTask> taskList = new ArrayList<TodoTask>();
        String selectQuery = "SELECT  * FROM " + TaskDBHelper.TABLE_TASK;

        Cursor cursor = database.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TodoTask task = cursorToTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return taskList;
    }

    private TodoTask cursorToTask(Cursor cursor) {
        TodoTask task = new TodoTask();
        task.setId(cursor.getInt(0));
        task.setTitle(cursor.getString(1));
        task.setDueDate(cursor.getString(2));
        task.setDueTime(cursor.getString(3));
        task.setPriority(cursor.getString(4));
        task.setDescription(cursor.getString(5));
        return task;
    }

}
