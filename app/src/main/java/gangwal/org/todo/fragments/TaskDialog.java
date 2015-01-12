package gangwal.org.todo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import gangwal.org.todo.R;
import gangwal.org.todo.bean.TodoTask;
import gangwal.org.todo.database.TaskDataSource;


public class TaskDialog extends DialogFragment implements OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private EditText mTitle;
    private static TextView mDatePicker;
    private static TextView mTimePicker;

    private Spinner mPriority;

    private static TodoTask sTask;
    private ItemAddListener mCallback;
    private ImageButton mSubmit;
    private ImageButton mDateButton;
    private ImageButton mTimeButton;

    private ImageButton mDatePickerButton;
    private ImageButton mReset;
    private TextView mDescription;
    private static FragmentManager sFragmentManager;
    private TaskDataSource mTaskDataSource;

    public static TaskDialog getInstance(TodoTask item,  FragmentManager fragmentManager){
        sTask = item;
        sFragmentManager = fragmentManager;
        return new TaskDialog();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        final Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        CharSequence s = DateFormat.format("yyyy-MM-dd", cal.getTime());
        mDatePicker.setText(s);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String timeMode= "AM";
        if (hourOfDay>12)
        {
            hourOfDay = hourOfDay -12;
            timeMode="PM";
        }
        mTimePicker.setText(hourOfDay + ":" + minute+ " " + timeMode);
    }


    public interface ItemAddListener {
        public void generateList();
    }

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ItemAddListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ItemAddListener");
        }
    }

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        View v = inflater.inflate(R.layout.activity_edit_item, container, false);
        getDialog().setTitle(getString(R.string.new_task_title));

        mTaskDataSource = new TaskDataSource(getActivity());

        mDatePicker = (TextView)v.findViewById(R.id.date_picker_label);
        mTimePicker = (TextView)v.findViewById(R.id.time_picker_label);
        mPriority = (Spinner)v.findViewById(R.id.priority_dropdown);

        mSubmit = (ImageButton)v.findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view);
            }
        });


        mReset = (ImageButton)v.findViewById(R.id.reset);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReset(view);
            }
        });

//        mDatePickerButton = (ImageButton)v.findViewById(R.id.date_picker);
//        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                showDatePickerDialog(view);
//            }
//        });

        mDateButton = (ImageButton)v.findViewById(R.id.date_pick);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(TaskDialog.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(2014, 2018);
                datePickerDialog.setCloseOnSingleTapDay(true);
                datePickerDialog.show(sFragmentManager, "");
            }
        });

        mTimeButton = (ImageButton)v.findViewById(R.id.time_pick);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(TaskDialog.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(true);
                timePickerDialog.show(sFragmentManager, "");
            }
        });

        mTitle = (EditText)v.findViewById(R.id.title);
        mDescription = (EditText)v.findViewById(R.id.description);

        if(sTask != null) {
            getDialog().setTitle(getString(R.string.edit_item_title));
            mTitle.setText(sTask.getTitle());
            mDescription.setText(sTask.getDescription());
            ArrayAdapter mPriorityList = (ArrayAdapter) mPriority.getAdapter();
            int spinnerPosition = mPriorityList.getPosition(sTask.getPriority().getPriorityCode());
            mPriority.setSelection(spinnerPosition);
            mDatePicker.setText(sTask.getDueDate());
            mTimePicker.setText(sTask.getDueTime());
        }
        return v;
    }

    private void onReset(View view) {
        mTitle.setText("");
        mDatePicker.setText("No Due");
        mTimePicker.setText("time");
        mPriority.setSelection(0);
        mDescription.setText("");
    }

    /**
     * OnClick of Submit Button, save the task and repopulate the list
     * @param v
     */
    public void onSubmit(View v) {
        String item = mTitle.getText().toString();
        if (TextUtils.isEmpty(item)) {return;}

        if(mCallback != null){
            int id = 0;
            TodoTask todo = new TodoTask(item, mDatePicker.getText().toString(),mTimePicker.getText().toString(),
                    mPriority.getSelectedItem().toString(), mDescription.getText().toString());
            if(sTask == null) {
                mTaskDataSource.createTask(todo);
            }else{
                id = sTask.getId();
                todo.setId(id);
                mTaskDataSource.updateTask(todo);
            }
            mCallback.generateList();
        }
       this.dismiss();
    }
}
