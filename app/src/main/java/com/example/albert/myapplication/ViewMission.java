package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ViewMission extends AppCompatActivity {

    String Name, Level, Title;
    TextView ViewName, ViewLevel, ViewDescription, ViewPoints, ViewState;
    ImageView Image;
    private DBSQLiteHelper missionsDB;
    private SQLiteDatabase db;
    Cursor cursor;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mission);

        bundle = this.getIntent().getExtras();

        ViewName = (TextView)findViewById(R.id.ViewName);
        ViewLevel = (TextView)findViewById(R.id.ViewLevel);
        ViewDescription = (TextView)findViewById(R.id.ViewDescription);
        ViewPoints = (TextView)findViewById(R.id.ViewPoints);
        ViewState = (TextView)findViewById(R.id.ViewState);
        Image = (ImageView)findViewById(R.id.imageView);

        Name = bundle.getString("name");
        Title = bundle.getString("title");
        Level = String.valueOf(bundle.getInt("level"));


        missionsDB = new DBSQLiteHelper(this, "missions", null, 1);

        cursor = missionsDB.getReadableDatabase().rawQuery("SELECT description, points, state FROM missions WHERE title='" + Title + "' AND name_mis='" + Name + "'", null);

        cursor.moveToNext();

        ViewName.setText(ViewName.getText() + Name);
        ViewLevel.setText(ViewLevel.getText() + Level);
        ViewDescription.setText(ViewDescription.getText() + cursor.getString(0));
        ViewPoints.setText(ViewPoints.getText() + String.valueOf(cursor.getInt(1)));
        ViewState.setText(ViewState.getText() + String.valueOf(cursor.getString(2)));

        if (cursor.getString(2).equals("Achieved")) Image.setImageResource(R.mipmap.mission_finished);
        else Image.setImageResource(R.mipmap.mission);

        setTitle(Title + " - Mission");

        cursor.close();
        missionsDB.close();

    }

    public void onBackPressed(){
        Intent intent = new Intent(ViewMission.this, ViewGame.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_objgamchar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Edit:
                Intent intent = new Intent(ViewMission.this, EditMission.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                return true;
            case R.id.Delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMission.this);
                builder.setMessage("Are you sure you want to delete "+Name+"?");
                final DBSQLiteHelper usdbhMis = new DBSQLiteHelper(this, "missions", null, 1);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db = usdbhMis.getWritableDatabase();
                        db.execSQL("DELETE FROM missions WHERE title='" + Title + "' AND name_mis='"+Name+"'");
                        db.close();
                        Intent intent = new Intent(ViewMission.this, ViewGame.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
