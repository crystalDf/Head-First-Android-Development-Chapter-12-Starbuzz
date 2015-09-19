package com.star.starbuzz;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DrinkCategoryActivity extends ListActivity {

    private SQLiteDatabase mSQLiteDatabase;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            StarbuzzDatabaseHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            mSQLiteDatabase = starbuzzDatabaseHelper.getWritableDatabase();

            mCursor = mSQLiteDatabase.query(
                    StarbuzzDatabaseHelper.TABLE_DRINK,
                    new String[]{StarbuzzDatabaseHelper.COLUMN_ID,
                            StarbuzzDatabaseHelper.COLUMN_NAME},
                    null, null, null, null, null);

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                    this, android.R.layout.simple_list_item_1, mCursor,
                    new String[] {StarbuzzDatabaseHelper.COLUMN_NAME},
                    new int[] {android.R.id.text1}, 0);

            setListAdapter(simpleCursorAdapter);

        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, DrinkActivity.class);

        intent.putExtra(DrinkActivity.EXTRA_DRINKNO, id);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCursor != null) {
            mCursor.close();
        }

        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
        }
    }
}
