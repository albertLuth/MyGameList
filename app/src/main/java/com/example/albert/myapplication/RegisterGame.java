package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterGame extends AppCompatActivity {

    private EditText textTitle;
    private EditText textPlatform;
    private EditText textYear;
    private EditText textStudio;;

    private Button btnState;
    private Button btnSave;
    private Button btnCancel;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_game);

        btnState = (Button)findViewById(R.id.btnState);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        textTitle = (EditText)findViewById(R.id.editTitle);
        textPlatform = (EditText)findViewById(R.id.editPlatform);
        textYear = (EditText)findViewById(R.id.editYear);
        textStudio = (EditText)findViewById(R.id.editStudio);

        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence States[] = new CharSequence[] {"Playing", "Pending", "Finished"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterGame.this);
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

        final DBSQLiteHelper usdbh = new DBSQLiteHelper(this, "games", null, 1);
        db = usdbh.getWritableDatabase();
        final DBSQLiteHelper gamesDB = new DBSQLiteHelper(this, "games", null, 1);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String title = textTitle.getText().toString();
                String platform = textPlatform.getText().toString();
                String year = textYear.getText().toString();
                String studio = textStudio.getText().toString();
                String state = btnState.getText().toString();



                if(title.isEmpty() || platform.isEmpty() || year.isEmpty() || studio.isEmpty() || state.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterGame.this);
                    builder.setMessage("You must fill out all the blancks.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (year.length() > 4){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterGame.this);
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

                    if (!cursor.moveToNext()) {


                        String sql = "INSERT INTO games (title, platform, year, studio, state) VALUES ('" + title + "', '" + platform + "', '" + year + "', '" + studio + "', '" + state + "')";
                        db.execSQL(sql);

                        Intent intent = new Intent(RegisterGame.this, ViewGame.class);
                        Bundle b = new Bundle();
                        b.putString("title", title);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterGame.this);
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
                Intent intent = new Intent(RegisterGame.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(RegisterGame.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
