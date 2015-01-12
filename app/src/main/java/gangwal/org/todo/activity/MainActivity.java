package gangwal.org.todo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import gangwal.org.todo.R;
import gangwal.org.todo.adapters.TaskListAdapters;
import gangwal.org.todo.bean.TodoTask;
import gangwal.org.todo.database.TaskDataSource;
import gangwal.org.todo.fragments.TaskDialog;


public class MainActivity extends FragmentActivity implements TaskDialog.ItemAddListener {

    private TaskDataSource mTaskDataSource;
    private ArrayList<TodoTask> mListItems;
    private ArrayAdapter<TodoTask> mListAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mTaskDataSource = new TaskDataSource(this);
        mListView = (ListView) findViewById(R.id.todoList);

        RelativeLayout empty=(RelativeLayout)findViewById(R.id.empty);
        mListView.setEmptyView(empty);
        getAllTask();
        mListAdapter = new TaskListAdapters(getBaseContext(), mListItems);
        mListView.setAdapter(mListAdapter);

        setupSingleClickListner();
        setupLongClickListner();
    }

    /**
     * Add a new Todo task
     * @param v
     */
    public void addNewTodoTask(View v) {
        showAddDialog(mListItems.size(), null);
    }

    /**
     * getAllTask will call the dataSource and get all the task
     */
    private void getAllTask() {
        mListItems = mTaskDataSource.getAllTask();
    }

    /**
     * Delete the task on Long Click
     */
    private void setupLongClickListner() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTaskDataSource.deleteTask(mListItems.get(i));
                generateList();
                return true;
            }
        });
    }

    /**
     * Edit the task on Single click
     */
    private void setupSingleClickListner() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showAddDialog(position, mListItems.get(position));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    private void showAddDialog(int index, TodoTask item) {
        TaskDialog dailog = TaskDialog.
                getInstance(item, getSupportFragmentManager());
        dailog.show(getSupportFragmentManager(),"");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addNew) {
            showAddDialog(mListItems.size(), null);
            return true;
        }
        else if (id == R.id.clear) {
            mTaskDataSource.deleteAllTask();
            generateList();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void generateList() {
        mListItems.clear();
        mListItems.addAll(mTaskDataSource.getAllTask());
        mListAdapter.notifyDataSetChanged();
    }

}
