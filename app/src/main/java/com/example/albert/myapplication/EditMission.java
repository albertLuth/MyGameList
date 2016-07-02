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
import android.widget.TextView;

public class EditMission extends AppCompatActivity {

    private String Title;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mission);

        final String Name, Level;
        TextView EditName, EditLevel, EditDescription, EditPoints;
        DBSQLiteHelper missionsDB;
        Cursor cursor;

        Button btnSave = (Button)findViewById(R.id.btnSave);
        Button btnCancel = (Button)findViewById(R.id.btnCancel);
        final Button btnState = (Button)findViewById(R.id.btnState);

        Bundle bundle = this.getIntent().getExtras();

        EditName = (TextView)findViewById(R.id.editName);
        EditLevel = (TextView)findViewById(R.id.editLevel);
        EditDescription = (TextView)findViewById(R.id.editDescription);
        EditPoints = (TextView)findViewById(R.id.editPoints);

        Name = bundle.getString("name");
        Title = bundle.getString("title");
        Level = String.valueOf(bundle.getInt("level"));



        missionsDB = new DBSQLiteHelper(this, "missions", null, 1);

        cursor = missionsDB.getReadableDatabase().rawQuery("SELECT description, points, state FROM missions WHERE title='" + Title + "' AND name_mis='" + Name + "'", null);

        cursor.moveToNext();

        final String Description = cursor.getString(0);
        final String Points = String.valueOf(cursor.getInt(1));
        btnState.setText(cursor.getString(2));

        EditName.setText(EditName.getText() + Name);
        EditLevel.setText(EditLevel.getText() + Level);
        EditDescription.setText(EditDescription.getText() + Description);
        EditPoints.setText(EditPoints.getText() + Points);

        setTitle(Title + " - Mission");

        cursor.close();
        missionsDB.close();

        DBSQLiteHelper usdbh = new DBSQLiteHelper(this, "missions", null, 1);
        db = usdbh.getWritableDatabase();


        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence States[] = new CharSequence[] {"Achieved", "Not achieved"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditMission.this);
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

                EditText textName = (EditText)findViewById(R.id.editName);
                EditText textLevel = (EditText)findViewById(R.id.editLevel);
                EditText textDescription = (EditText)findViewById(R.id.editDescription);
                EditText textPoints = (EditText)findViewById(R.id.editPoints);


                String name = textName.getText().toString();
                String level = textLevel.getText().toString();
                String description = textDescription.getText().toString();
                String points = textPoints.getText().toString();
                String state = btnState.getText().toString();

                if(name.isEmpty() || level.isEmpty() || description.isEmpty() || points.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditMission.this);
                    builder.setMessage("You must fill out all the blancks.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (level.length() > 7){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditMission.this);
                    builder.setMessage("The level must be lower than 10.000.000.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (points.length() > 7){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditMission.this);
                    builder.setMessage("The points must be lower than 10.000.000.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {


                    String sql = "UPDATE missions SET name_mis='" + name + "', level=" + level + ", description='" + description + "', points=" + points + ",state='"+state+"' " +
                            "WHERE title='" + Title + "' AND name_mis='" + Name + "' AND level=" + Level + " AND description='" + Description + "' AND points=" + Points + "";
                    db.execSQL(sql);

                    Intent intent = new Intent(EditMission.this, ViewGame.class);
                    Bundle b = new Bundle();
                    b.putString("title", Title);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMission.this, ViewGame.class);
                Bundle b = new Bundle();
                b.putString("title", Title);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onBackPressed(){
        Intent intent = new Intent(EditMission.this, ViewGame.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        return;
    }
}
