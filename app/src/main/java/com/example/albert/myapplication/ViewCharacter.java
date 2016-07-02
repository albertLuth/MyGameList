package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewCharacter extends AppCompatActivity {

    String Name, Level, Title, Race;
    TextView ViewName, ViewLevel, ViewRace;
    DBSQLiteHelper gamesDB, objectsDB, missionsDB, charactersDB, game_levelsDB;
    Cursor cursor;
    private Bundle bundle;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_character);

        bundle = this.getIntent().getExtras();

        ViewName = (TextView)findViewById(R.id.ViewName);
        ViewLevel = (TextView)findViewById(R.id.ViewLevel);
        ViewRace = (TextView)findViewById(R.id.ViewRace);

        Name = bundle.getString("name");
        Title = bundle.getString("title");
        Level = String.valueOf(bundle.getInt("level"));


        charactersDB = new DBSQLiteHelper(this, "characters", null, 1);

        cursor = charactersDB.getReadableDatabase().rawQuery("SELECT race FROM characters WHERE title='" + Title + "' AND name_char='" + Name + "'", null);

        cursor.moveToNext();

        ViewName.setText(ViewName.getText() + Name);
        ViewLevel.setText(ViewLevel.getText() + Level);

        String race = cursor.getString(0);
        if (race.equals("")) race = "It doesn't have";
        ViewRace.setText(ViewRace.getText() + race);

        setTitle(Title + " - Character");

        cursor.close();
        charactersDB.close();

    }

    public void onBackPressed(){
        Intent intent = new Intent(ViewCharacter.this, ViewGame.class);
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
                Intent intent = new Intent(ViewCharacter.this, EditCharacter.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                return true;
            case R.id.Delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCharacter.this);
                builder.setMessage("Are you sure you want to delete "+Name+"?");
                final DBSQLiteHelper usdbhChar = new DBSQLiteHelper(this, "characters", null, 1);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db = usdbhChar.getWritableDatabase();
                        db.execSQL("DELETE FROM characters WHERE title='" + Title + "' AND name_char='"+Name+"'");
                        db.close();
                        Intent intent = new Intent(ViewCharacter.this, ViewGame.class);
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
