package us.mindbuilders.petemit.timegoalie.widget;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import us.mindbuilders.petemit.timegoalie.BaseApplication;
import us.mindbuilders.petemit.timegoalie.R;
import us.mindbuilders.petemit.timegoalie.TimeGoalieDO.Goal;
import us.mindbuilders.petemit.timegoalie.data.TimeGoalieContract;
import us.mindbuilders.petemit.timegoalie.utils.TimeGoalieDateUtils;

/**
 * Created by Peter on 10/11/2017.
 */

public class TimeGoalieWidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private ArrayList<Goal> goalData;

    public TimeGoalieWidgetListRemoteViewsFactory(Context context){
        this.context = context;
        Log.e("findme","I got here now");
    }
    @Override
    public void onCreate() {
        Log.e("findme","I got here again");

    }

    @Override
    public void onDataSetChanged() {
        Log.e("findme","I got here");
        if (goalData != null) {
            goalData=null;
        }
        Cursor cursor = context.getContentResolver().query(
                TimeGoalieContract.getGoalsThatHaveGoalEntryForToday(),
                null,
                null,
                new String[]{TimeGoalieDateUtils.
                        getSqlDateString(BaseApplication.getActiveCalendarDate())},
                null
        );
        if (cursor != null) {
            goalData = Goal.createGoalListFromCursor(cursor);
            cursor.close();
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (goalData != null) {
            return goalData.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (goalData != null && goalData.size() > 0 ){
            Goal goal = goalData.get(i);

            RemoteViews views=null;



            if (goal.getGoalTypeId()==2) { //yes/no goal
                views = new RemoteViews(context.getPackageName(),
                        R.layout.time_goalie_widget_item_yes_no);
            }

            else{
                views = new RemoteViews(context.getPackageName(),
                        R.layout.time_goalie_widget_item);
            }

            if (views != null) {
                views.setTextViewText(R.id.widget_goal_tv, goal.getName());
                Log.e("findme", goal.getName());
            }

            return views;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
