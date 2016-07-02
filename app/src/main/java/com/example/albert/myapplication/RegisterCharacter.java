package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterCharacter extends AppCompatActivity {

    private EditText textName;
    private EditText textRace;
    private EditText textLevel;;

    private Button btnSave;
    private Button btnCancel;

    private SQLiteDatabase db;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_character);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        textName = (EditText)findViewById(R.id.editName);
        textRace = (EditText)findViewById(R.id.editRace);
        textLevel = (EditText)findViewById(R.id.editLevel);

        title = this.getIntent().getExtras().getString("title");

        DBSQLiteHelper usdbh = new DBSQLiteHelper(this, "characters", null, 1);
        db = usdbh.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = textName.getText().toString();
                String race = textRace.getText().toString();
                String level = textLevel.getText().toString();

                if(name.isEmpty() || level.isEmpty() || race.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCharacter.this);
                    builder.setMessage("You must fill out all the blancks.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (level.length() > 7){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterCharacter.this);
                    builder.setMessage("The level must be lower than 10.000.000.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {

                    String sql = "INSERT INTO characters (title, name_char, race, level) VALUES ('" + title + "', '" + name + "', '" + race + "', '" + level + "')";
                    db.execSQL(sql);

                    Intent intent = new Intent(RegisterCharacter.this, ViewGame.class);
                    Bundle b = new Bundle();
                    b.putString("title", title);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCharacter.this, ViewGame.class);
                Bundle b = new Bundle();
                b.putString("title", title);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(RegisterCharacter.this, ViewGame.class);
        Bundle b = new Bundle();
        b.putString("title", title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        return;
    }
}
