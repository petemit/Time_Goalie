package us.mindbuilders.petemit.timegoalie.data;

/**
 * Created by Peter on 10/10/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import us.mindbuilders.petemit.timegoalie.GoalRecyclerViewAdapter;
import us.mindbuilders.petemit.timegoalie.TimeGoalieDO.GoalEntry;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import us.mindbuilders.petemit.timegoalie.TimeGoalieDO.Goal;
import us.mindbuilders.petemit.timegoalie.TimeGoalieDO.GoalEntry;
import us.mindbuilders.petemit.timegoalie.TimeGoalieDO.GoalEntryGoalCounter;
import us.mindbuilders.petemit.timegoalie.utils.TimeGoalieDateUtils;

/**
 * Created by Peter on 9/27/2017.
 */

public class GetSuccessfulGoalCount extends AsyncTask<GoalEntryGoalCounter, Void, Void> {

    Context context;
    Cursor cursor;
    GoalEntry goalEntry;
    private String date;
    GoalRecyclerViewAdapter.GoalCounter goalCounter;

    public GetSuccessfulGoalCount(Context context){
        this.context=context;
    }

    @Override
    protected Void doInBackground(GoalEntryGoalCounter... goalEntryGoalCounters) {
        if (goalEntryGoalCounters[0] != null) {
            goalCounter= goalEntryGoalCounters[0].getGc();
            date = goalEntryGoalCounters[0].getDate();
            if (goalEntryGoalCounters[0].getGoalEntry() != null) {
                goalEntry = goalEntryGoalCounters[0].getGoalEntry();
            }
        }
        if (goalEntry != null) {
            ContentValues goalEntries_cv = new ContentValues();
            goalEntries_cv.put(TimeGoalieContract.GoalEntries.GOALENTRIES_COLUMN_GOAL_ID,
                    goalEntry.getGoal_id());
            goalEntries_cv.put(TimeGoalieContract.GoalEntries.GOALENTRIES_COLUMN_DATETIME,
                    goalEntry.getDate());
            goalEntries_cv.put(TimeGoalieContract.GoalEntries.GOALENTRIES_COLUMN_SECONDSELAPSED,
                    goalEntry.getSecondsElapsed());
            goalEntries_cv.put(TimeGoalieContract.GoalEntries.GOALENTRIES_COLUMN_GOALAUGMENT,
                    goalEntry.getGoalAugment());
            goalEntries_cv.put(TimeGoalieContract.GoalEntries.GOALENTRIES_COLUMN_SUCCEEDED,
                    goalEntry.getHasSucceeded());

            context.getContentResolver().insert(TimeGoalieContract.GoalEntries.CONTENT_URI,goalEntries_cv);
        }


            cursor = context.getContentResolver().query(TimeGoalieContract.getSuccessfulGoalsForToday(date),
                    null,
                    null,
                    null,
                    null);


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (cursor != null) {
            goalCounter.updateGoalCounter(cursor.getCount());
        }
    }
}