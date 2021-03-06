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

public class EditObject extends AppCompatActivity {

    private String Title;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_object);

        final String Name, Level;
        TextView EditName, EditLevel;


        Button btnSave = (Button)findViewById(R.id.btnSave);
        Button btnCancel = (Button)findViewById(R.id.btnCancel);

        Bundle bundle = this.getIntent().getExtras();

        EditName = (TextView)findViewById(R.id.editName);
        EditLevel = (TextView)findViewById(R.id.editLevel);

        Name = bundle.getString("name");
        Title = bundle.getString("title");
        Level = String.valueOf(bundle.getInt("level"));


        EditName.setText(EditName.getText() + Name);
        EditLevel.setText(EditLevel.getText() + Level);


        setTitle(Title + " - Object");


        DBSQLiteHelper usdbh = new DBSQLiteHelper(this, "objects", null, 1);
        db = usdbh.getWritableDatabase();

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText textName = (EditText)findViewById(R.id.editName);
                EditText textLevel = (EditText)findViewById(R.id.editLevel);

                String name = textName.getText().toString();
                String level = textLevel.getText().toString();

                if(name.isEmpty() || level.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditObject.this);
                    builder.setMessage("You must fill out all the blancks.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (level.length() > 7){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditObject.this);
                    builder.setMessage("The level must be lower than 10.000.000.");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {

                    String sql = "UPDATE objects SET name_obj='" + name + "', level=" + level +
                            " WHERE title='" + Title + "' AND name_obj='" + Name + "' AND level=" + Level;
                    db.execSQL(sql);
                    db.close();

                    Intent intent = new Intent(EditObject.this, ViewGame.class);
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
                Intent intent = new Intent(EditObject.this, ViewGame.class);
                Bundle b = new Bundle();
                b.putString("title", Title);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(EditObject.this, ViewGame.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        return;
    }
}
