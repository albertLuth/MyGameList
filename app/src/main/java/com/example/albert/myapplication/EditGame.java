package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditGame extends AppCompatActivity {

    private String Title;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);
        ;
        final DBSQLiteHelper gamesDB;
        Cursor cursor;

        Button btnSave = (Button)findViewById(R.id.btnSave);
        final Button btnCancel = (Button)findViewById(R.id.btnCancel);
        final Button btnState = (Button)findViewById(R.id.btnState);

        Bundle bundle = this.getIntent().getExtras();

        final EditText editTitle = (EditText)findViewById(R.id.editTitle);
        final EditText editPlatform = (EditText)findViewById(R.id.editPlatform);
        final EditText editYear = (EditText)findViewById(R.id.editYear);
        final EditText editStudio = (EditText)findViewById(R.id.editStudio);

        Title = bundle.getString("title");

        gamesDB = new DBSQLiteHelper(this, "games", null, 1);

        cursor = gamesDB.getReadableDatabase().rawQuery("SELECT title, platform, year, studio, state FROM games WHERE title='"+Title+"'", null);

        cursor.moveToNext();

        final String Platform = cursor.getString(1);
        final String Year = String.valueOf(cursor.getInt(2));
        final String Studio = cursor.getString(3);
        final String State = cursor.getString(4);

        editTitle.setText(editTitle.getText() + Title);
        editPlatform.setText(editPlatform.getText() + Platform);
        editYear.setText(editYear.getText() + Year);
        editStudio.setText(editStudio.getText() + Studio);
        btnState.setText(State);

        setTitle(Title);
        cursor.close();

        DBSQLiteHelper usdbh = new DBSQLiteHelper(this, "games", null, 1);
        final DBSQLiteHelper usdbhChar = new DBSQLiteHelper(this, "characters", null, 1);
        final DBSQLiteHelper usdbhMis = new DBSQLiteHelper(this, "missions", null, 1);
        final DBSQLiteHelper usdbhObj = new DBSQLiteHelper(this, "objects", null, 1);
        db = usdbh.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String title = editTitle.getText().toString();
                String platform = editPlatform.getText().toString();
                String year = editYear.getText().toString();
                String studio = editStudio.getText().toString();
                String state = btnState.getText().toString();

                if(title.isEmpty() || platform.isEmpty() || year.isEmpty() || studio.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditGame.this);
                    builder.setMessage("You must fill out all the blancks.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (year.length() > 4){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditGame.this);
                    builder.setMessage("The year must be closer than 10000.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {
                    Cursor cursor = gamesDB.getReadableDatabase().rawQuery("SELECT * FROM games WHERE title='" + title + "'", null);

                    if (!cursor.moveToNext() || title.equals(Title)) {
                        String sql = "UPDATE games SET title='" + title + "', platform='" + platform + "', year=" + year + ", studio='" + studio + "', state='" + state + "' " +
                                "WHERE title='" + Title + "' AND platform='" + Platform + "' AND year=" + Year + " AND studio='" + Studio + "' AND state='" + State + "'";
                        db.execSQL(sql);
                        db.close();
                        db = usdbhChar.getWritableDatabase();
                        sql = "UPDATE characters SET title='" + title + "' WHERE title='" + Title + "'";
                        db.execSQL(sql);
                        db.close();
                        db = usdbhObj.getWritableDatabase();
                        sql = "UPDATE objects SET title='" + title + "' WHERE title='" + Title + "'";
                        db.execSQL(sql);
                        db.close();
                        db = usdbhMis.getWritableDatabase();
                        sql = "UPDATE missions SET title='" + title + "' WHERE title='" + Title + "'";
                        db.execSQL(sql);
                        db.close();

                        Intent intent = new Intent(EditGame.this, ViewGame.class);
                        Bundle b = new Bundle();
                        b.putString("title", title);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditGame.this);
                        builder.setMessage("There's already a game with this title.");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    cursor.close();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditGame.this, ViewGame.class);
                Bundle b = new Bundle();
                b.putString("title", Title);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence States[] = new CharSequence[] {"Playing", "Pending", "Finished"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditGame.this);
                builder.setTitle("Pick a state");
                builder.setItems(States, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnState.setText(States[which].toString());
                    }
                });
                builder.show();
            }
        });

    }

    public void onBackPressed(){
        Intent intent = new Intent(EditGame.this, ViewGame.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        return;
    }
}
