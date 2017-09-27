package us.mindbuilders.petemit.timegoalie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * ContentProvider for TimeGoalieDb
 */

public class TimeGoalieContentProvider extends ContentProvider {
    private TimeGoalieDbHelper dbHelper;
    private static final UriMatcher uriMatcher = makeUriMatcher();

    private static final int GOAL = 100;
    private static final int GOAL_BY_ID = 101;
    private static final int GOALS_BY_TODAYS_DATE = 102;

    private static final int GOALTYPES = 200;
    private static final int GOALTYPE_BY_ID = 201;

    private static final int DAYS = 300;
    private static final int DAY_BY_ID = 301;
    private static final int DAY_BY_DATE_SEQUENCE = 302;

    private static final int GOALS_ACCOMPLISHED_BY_WEEK = 401;
    private static final int GOALS_ACCOMPLISHED_BY_MONTH = 402;
    private static final int GOALS_ACCOMPLISHED_BY_WEEK_BY_GOAL_ID = 403;
    private static final int GOALS_ACCOMPLISHED_BY_MONTH_BY_GOAL_ID = 404;
    private static final int GOALS_ACCOMPLISHED_BY_GOAL_ID_BY_DATE = 405;

    private static final int GOALS_DAYS = 500;
    private static final int GOALS_BY_DAY_ID = 501;

    private static final String PARAMETER = "=? ";

    private static UriMatcher makeUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        //goals
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.Goals.GOALS_TABLE_NAME, GOAL);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.Goals.GOALS_TABLE_NAME + "/#", GOAL_BY_ID);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.Goals.GOALS_TABLE_NAME + "/date/#", GOALS_BY_TODAYS_DATE);
        //goaltypes
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalTypes.GOALTYPES_TABLE_NAME, GOALTYPES);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalTypes.GOALTYPES_TABLE_NAME + "/#", GOALTYPE_BY_ID);
        //days
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.Days.DAYS_TABLE_NAME, DAYS);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.Days.DAYS_TABLE_NAME + "/#", DAY_BY_ID);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.Days.DAYS_TABLE_NAME + "/date/#", DAY_BY_DATE_SEQUENCE);
        //goaldatesaccomplished
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalsDatesAccomplished.GOALS_DATES_ACCOMPLISHED_TABLE_NAME +
                        "/weeks", GOALS_ACCOMPLISHED_BY_WEEK);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalsDatesAccomplished.GOALS_DATES_ACCOMPLISHED_TABLE_NAME +
                        "/months", GOALS_ACCOMPLISHED_BY_MONTH);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalsDatesAccomplished.GOALS_DATES_ACCOMPLISHED_TABLE_NAME +
                        "/weeks/goal/#", GOALS_ACCOMPLISHED_BY_WEEK_BY_GOAL_ID);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalsDatesAccomplished.GOALS_DATES_ACCOMPLISHED_TABLE_NAME +
                        "/months/goal/#", GOALS_ACCOMPLISHED_BY_MONTH_BY_GOAL_ID);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalsDatesAccomplished.GOALS_DATES_ACCOMPLISHED_TABLE_NAME +
                        "/goal/*", GOALS_ACCOMPLISHED_BY_GOAL_ID_BY_DATE);
        //goaldays
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalsDays.GOALS_DAYS_TABLE_NAME + "/#", GOALS_BY_DAY_ID);
        matcher.addURI(TimeGoalieContract.AUTHORITY,
                TimeGoalieContract.GoalsDays.GOALS_DAYS_TABLE_NAME, GOALS_DAYS);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new TimeGoalieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "";
        String[] selectionArgs = null;
        switch (uriMatcher.match(uri)) {

            //goals
            case GOAL_BY_ID:
                selection = TimeGoalieContract.Goals._ID.concat(PARAMETER);
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TimeGoalieContract.Goals.GOALS_TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case GOALS_BY_TODAYS_DATE:
                selection = TimeGoalieContract.Goals.GOALS_COLUMN_CREATIONDATE.concat(PARAMETER);
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TimeGoalieContract.Goals.GOALS_TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;

            //days
            case DAYS:
                cursor = db.query(TimeGoalieContract.Days.DAYS_TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;
            case DAY_BY_ID:
                selection = TimeGoalieContract.Days._ID.concat(PARAMETER);
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TimeGoalieContract.Days.DAYS_TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case DAY_BY_DATE_SEQUENCE:
                selection = TimeGoalieContract.Days.DAYS_COLUMN_SEQUENCE.concat(PARAMETER);
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TimeGoalieContract.Days.DAYS_TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;

            //goaldays
            case GOALS_BY_DAY_ID:
                selection = TimeGoalieContract.GoalsDays.GOALS_DAYS_COLUMN_DAY_ID.concat(PARAMETER)
                        .concat(" AND ").concat(TimeGoalieContract.Goals.GOALS_TABLE_NAME
                                .concat(".")
                                .concat(TimeGoalieContract.Goals._ID))
                        .concat("=").concat(
                                TimeGoalieContract.GoalsDays.GOALS_DAYS_TABLE_NAME
                                        .concat(".")
                                        .concat(
                                                TimeGoalieContract.GoalsDays.GOALS_DAYS_COLUMN_GOAL_ID));
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(TimeGoalieContract.GoalsDays.GOALS_DAYS_TABLE_NAME + ", " +
                                TimeGoalieContract.Goals.GOALS_TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case GOAL:
                long goal_id = db.insert(TimeGoalieContract.Goals.GOALS_TABLE_NAME,
                        null,
                        contentValues);

                if (goal_id > -1) {
                    Uri returnUri = TimeGoalieContract.buildGetaGoalByIdUri(goal_id);
                    getContext().getContentResolver().notifyChange(returnUri, null);
                    return returnUri;
                } else {

                    throw new SQLException("Insert failed!");
                }
            case GOALS_DAYS:
                long goal_day_id = db.insert(TimeGoalieContract.GoalsDays.GOALS_DAYS_TABLE_NAME,
                        null,
                        contentValues);
                if (goal_day_id > -1) {
                    Uri returnUri = TimeGoalieContract.buildGetaGoalByIdUri(goal_day_id);
                    getContext().getContentResolver().notifyChange(returnUri, null);
                    return returnUri;
                } else {

                    throw new SQLException("Insert failed!");
                }
            default:
                throw new UnsupportedOperationException("That insert query didn't work, dude");
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case GOAL_BY_ID:
                String[] _id = {String.valueOf(ContentUris.parseId(uri))};
                if (_id.length == 0) {
                    return 0;
                }

                String mSelection = TimeGoalieContract.Goals._ID + "=?";

                int rowsdeleted = db.delete(TimeGoalieContract.Goals.GOALS_TABLE_NAME,
                        mSelection,
                        _id);
                if (_id.length > 0) {

                    return rowsdeleted;

                } else {
                    throw new SQLException("Delete failed!");
                }
            default:
                throw new UnsupportedOperationException("that delete query is not supported, man.");
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}
