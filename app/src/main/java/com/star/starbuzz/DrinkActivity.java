package com.star.starbuzz;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    private ImageView mImageView;
    private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private CheckBox mFavoriteCheckBox;

    private SQLiteDatabase mSQLiteDatabase;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        mImageView = (ImageView) findViewById(R.id.photo);
        mNameTextView = (TextView) findViewById(R.id.name);
        mDescriptionTextView = (TextView) findViewById(R.id.description);
        mFavoriteCheckBox = (CheckBox) findViewById(R.id.favorite);

        final long drinkNo = getIntent().getLongExtra(EXTRA_DRINKNO, 0);

        try {
            StarbuzzDatabaseHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            mSQLiteDatabase = starbuzzDatabaseHelper.getWritableDatabase();

            mCursor = mSQLiteDatabase.query(
                    StarbuzzDatabaseHelper.TABLE_DRINK,
                    new String[]{StarbuzzDatabaseHelper.COLUMN_NAME,
                            StarbuzzDatabaseHelper.COLUMN_DESCRIPTION,
                            StarbuzzDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID,
                            StarbuzzDatabaseHelper.COLUMN_FAVORITE},
                    StarbuzzDatabaseHelper.COLUMN_ID + " = ? ",
                    new String[]{drinkNo + ""},
                    null, null, null);

            if (mCursor.moveToFirst()) {
                String name = mCursor.getString(mCursor.getColumnIndex(
                        StarbuzzDatabaseHelper.COLUMN_NAME
                ));

                String description = mCursor.getString(mCursor.getColumnIndex(
                        StarbuzzDatabaseHelper.COLUMN_DESCRIPTION
                ));

                int imageResourceId = mCursor.getInt(mCursor.getColumnIndex(
                        StarbuzzDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID
                ));

                boolean isFavorite = mCursor.getInt(mCursor.getColumnIndex(
                        StarbuzzDatabaseHelper.COLUMN_FAVORITE
                )) == 1;

                mNameTextView.setText(name);
                mDescriptionTextView.setText(description);
                mImageView.setImageResource(imageResourceId);
                mImageView.setContentDescription(name);
                mFavoriteCheckBox.setChecked(isFavorite);
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }

            if (mSQLiteDatabase != null) {
                mSQLiteDatabase.close();
            }
        }

        mFavoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new UpdateDrinkTask().execute(drinkNo);

            }
        });

    }

    private class UpdateDrinkTask extends AsyncTask<Long, Void, Boolean> {

        private ContentValues mContentValues;

        @Override
        protected void onPreExecute() {
            mContentValues = new ContentValues();
            mContentValues.put(StarbuzzDatabaseHelper.COLUMN_FAVORITE,
                    mFavoriteCheckBox.isChecked());
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            try {
                StarbuzzDatabaseHelper starbuzzDatabaseHelper =
                        new StarbuzzDatabaseHelper(DrinkActivity.this);
                mSQLiteDatabase = starbuzzDatabaseHelper.getWritableDatabase();

                mSQLiteDatabase.update(
                        StarbuzzDatabaseHelper.TABLE_DRINK,
                        mContentValues,
                        StarbuzzDatabaseHelper.COLUMN_ID + " = ? ",
                        new String[]{params[0] + ""});
                return true;
            } catch (SQLiteException e) {
                return false;
            } finally {
                if (mCursor != null) {
                    mCursor.close();
                }

                if (mSQLiteDatabase != null) {
                    mSQLiteDatabase.close();
                }
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (!aBoolean) {
                Toast.makeText(DrinkActivity.this, "Database unavailable",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
