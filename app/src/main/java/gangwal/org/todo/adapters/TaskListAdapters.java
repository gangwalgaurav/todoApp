package gangwal.org.todo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import gangwal.org.todo.R;
import gangwal.org.todo.bean.TodoTask;

/**
 * Created by gangwal on 1/2/15.
 */
public class TaskListAdapters extends ArrayAdapter<TodoTask> {
    private class ViewHolder {
        TextView title;
        TextView desc;
        ImageView priorityColor;
        TextView description;
    }

    public TaskListAdapters(Context context, ArrayList<TodoTask> items) {
        super(context, R.layout.item_row, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoTask item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_row, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_list);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.due_by);
            viewHolder.priorityColor = (ImageView) convertView.findViewById(R.id.priority_color);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(item.getTitle());
        if(!item.getDueDate().equals("No Due")) {
            long daysLeft = getDaysToDue(stringToCalenderDate(item.getDueDate()));
            if (daysLeft < 0) {
                viewHolder.desc.setText(getContext().getString(R.string.past_due));
                viewHolder.desc.setTextColor(Color.RED);
            } else {
                viewHolder.desc.setText((MessageFormat.format(getContext().getString(R.string.due_days), daysLeft)));
                viewHolder.desc.setTextColor(Color.BLACK);

            }
        }else{
            viewHolder.desc.setText(item.getDueDate());
        }
        viewHolder.description.setText(item.getDescription());

        int color = getColorCode(item.getPriority());
        viewHolder.priorityColor.setImageResource(color);
        return convertView;
    }

    private int getColorCode(TodoTask.Priority priority) {

        switch (priority) {
            case HIGH:
                return R.drawable.red;
            case MEDIUM:
                return R.drawable.yellow;
            case LOW:
                return R.drawable.green;
        }
        return Color.WHITE;
    }


    private Calendar stringToCalenderDate(String duedate) {
        try {
            final Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(duedate);
            cal.setTime(date);
            return cal;
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * Return the difference between due date and Current date
     * @param endDate
     * @return
     */
    public static long getDaysToDue(Calendar endDate){
        if(endDate == null) return 0;
        Calendar date = Calendar.getInstance();
        long left = endDate.getTimeInMillis() - date.getTimeInMillis();
        left = left/(1000*60*60*24);
        return left;

    }
}
