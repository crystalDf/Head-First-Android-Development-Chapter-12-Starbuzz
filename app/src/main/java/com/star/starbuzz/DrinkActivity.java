package com.star.starbuzz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    private ImageView mImageView;
    private TextView mNameTextView;
    private TextView mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        mImageView = (ImageView) findViewById(R.id.photo);
        mNameTextView = (TextView) findViewById(R.id.name);
        mDescriptionTextView = (TextView) findViewById(R.id.description);

        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;

        try {
            StarbuzzDatabaseHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            sqLiteDatabase = starbuzzDatabaseHelper.getWritableDatabase();

            cursor = sqLiteDatabase.query(
                    StarbuzzDatabaseHelper.TABLE_DRINK,
                    new String[] {StarbuzzDatabaseHelper.COLUMN_NAME,
                            StarbuzzDatabaseHelper.COLUMN_DESCRIPTION,
                            StarbuzzDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID},
                    StarbuzzDatabaseHelper.COLUMN_ID + " = ? ",
                    new String[] {getIntent().getLongExtra(EXTRA_DRINKNO, 0) + ""},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(
                        StarbuzzDatabaseHelper.COLUMN_NAME
                ));

                String description = cursor.getString(cursor.getColumnIndex(
                        StarbuzzDatabaseHelper.COLUMN_DESCRIPTION
                ));

                int imageResourceId = cursor.getInt(cursor.getColumnIndex(
                        StarbuzzDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID
                ));

                mNameTextView.setText(name);
                mDescriptionTextView.setText(description);
                mImageView.setImageResource(imageResourceId);
                mImageView.setContentDescription(name);
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

    }

}
