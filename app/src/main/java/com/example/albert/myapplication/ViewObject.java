package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewObject extends AppCompatActivity {

    String Name, Level, Title;
    TextView ViewName, ViewLevel;
    private SQLiteDatabase db;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_object);

        bundle = this.getIntent().getExtras();

        ViewName = (TextView)findViewById(R.id.ViewName);
        ViewLevel = (TextView)findViewById(R.id.ViewLevel);

        Name = bundle.getString("name");
        Level = String.valueOf(bundle.getInt("level"));
        Title = bundle.getString("title");

        setTitle(Title + " - Object");

        ViewName.setText(ViewName.getText() + Name);
        ViewLevel.setText(ViewLevel.getText() + Level);

    }

    public void onBackPressed(){
        Intent intent = new Intent(ViewObject.this, ViewGame.class);
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
                Intent intent = new Intent(ViewObject.this, EditObject.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                return true;
            case R.id.Delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewObject.this);
                builder.setMessage("Are you sure you want to delete "+Name+"?");
                final DBSQLiteHelper usdbhObj = new DBSQLiteHelper(this, "objects", null, 1);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db = usdbhObj.getWritableDatabase();
                        db.execSQL("DELETE FROM objects WHERE title='" + Title + "' AND name_obj='"+Name+"'");
                        db.close();
                        Intent intent = new Intent(ViewObject.this, ViewGame.class);
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
