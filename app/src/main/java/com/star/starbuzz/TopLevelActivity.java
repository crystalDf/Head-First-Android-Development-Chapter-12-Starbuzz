package com.star.starbuzz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends AppCompatActivity {

    private ListView mOptionsListView;
    private ListView mFavoritesListView;

    private SQLiteDatabase mSQLiteDatabase;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        mOptionsListView = (ListView) findViewById(R.id.list_options);
        mOptionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(TopLevelActivity.this,
                                DrinkCategoryActivity.class);
                        startActivity(intent);
                }
            }
        });

        mFavoritesListView = (ListView) findViewById(R.id.list_favorites);

        try {
            StarbuzzDatabaseHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            mSQLiteDatabase = starbuzzDatabaseHelper.getWritableDatabase();

            mCursor = mSQLiteDatabase.query(
                    StarbuzzDatabaseHelper.TABLE_DRINK,
                    new String[]{StarbuzzDatabaseHelper.COLUMN_ID,
                            StarbuzzDatabaseHelper.COLUMN_NAME},
                    StarbuzzDatabaseHelper.COLUMN_FAVORITE + " = 1 ", null, null, null, null);

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                    this, android.R.layout.simple_list_item_1, mCursor,
                    new String[] {StarbuzzDatabaseHelper.COLUMN_NAME},
                    new int[] {android.R.id.text1}, 0);

            mFavoritesListView.setAdapter(simpleCursorAdapter);

        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }

        mFavoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, DrinkActivity.class);

                intent.putExtra(DrinkActivity.EXTRA_DRINKNO, id);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            StarbuzzDatabaseHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            mSQLiteDatabase = starbuzzDatabaseHelper.getWritableDatabase();

            Cursor newCursor = mSQLiteDatabase.query(
                    StarbuzzDatabaseHelper.TABLE_DRINK,
                    new String[]{StarbuzzDatabaseHelper.COLUMN_ID,
                            StarbuzzDatabaseHelper.COLUMN_NAME},
                    StarbuzzDatabaseHelper.COLUMN_FAVORITE + " = 1 ", null, null, null, null);

            ((CursorAdapter) mFavoritesListView.getAdapter()).changeCursor(newCursor);
            mCursor = newCursor;

        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
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
