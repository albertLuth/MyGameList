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

public class RegisterMission extends AppCompatActivity {

    private EditText textName;
    private EditText textLevel;
    private EditText textPoints;
    private EditText textDescription;

    private Button btnSave;
    private Button btnCancel;
    private Button btnState;

    private SQLiteDatabase db;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mission);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnState = (Button)findViewById(R.id.btnState);

        textName = (EditText)findViewById(R.id.editName);
        textLevel = (EditText)findViewById(R.id.editLevel);
        textDescription = (EditText)findViewById(R.id.editDescription);
        textPoints = (EditText)findViewById(R.id.editPoints);

        title = this.getIntent().getExtras().getString("title");

        DBSQLiteHelper usdbh = new DBSQLiteHelper(this, "missions", null, 1);
        db = usdbh.getWritableDatabase();

        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence States[] = new CharSequence[] {"Achieved", "Not achieved"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMission.this);
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




        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = textName.getText().toString();
                String level = textLevel.getText().toString();
                String description = textDescription.getText().toString();
                String points = textPoints.getText().toString();
                String state = btnState.getText().toString();



                if(name.isEmpty() || level.isEmpty() || description.isEmpty() || points.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMission.this);
                    builder.setMessage("You must fill out all the blancks.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (points.length() > 7){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMission.this);
                    builder.setMessage("The level must be lower than 10.000.000.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (level.length() > 7){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMission.this);
                    builder.setMessage("The level must be lower than 10.000.000.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {
                    String sql = "INSERT INTO missions (title, name_mis, level, description, points, state) VALUES ('"+title+"','" + name + "', '" + level + "', '"+description+"', "+points+", '"+state+"')";
                    db.execSQL(sql);

                    Intent intent = new Intent(RegisterMission.this, ViewGame.class);
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
                Intent intent = new Intent(RegisterMission.this, ViewGame.class);
                Bundle b = new Bundle();
                b.putString("title", title);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(RegisterMission.this, ViewGame.class);
        Bundle b = new Bundle();
        b.putString("title", title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        return;
    }
}
