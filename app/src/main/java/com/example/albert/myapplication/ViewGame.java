package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewGame extends AppCompatActivity {

    private TextView ViewTitle;
    private TextView ViewPlatform;
    private TextView ViewYear;
    private TextView ViewStudio;
    private TextView ViewState;
    private DBSQLiteHelper gamesDB, objectsDB, missionsDB, charactersDB, game_levelsDB;
    private Cursor cursor;
    private ArrayList<CharObjMis>list;
    private ListView lstView;
    private Vibrator vibrator;
    private SQLiteDatabase db;

    String Title, Platform, Year, Studio, State;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        ViewPlatform = (TextView)findViewById(R.id.ViewPlatform);
        ViewYear = (TextView)findViewById(R.id.ViewYear);
        ViewStudio = (TextView)findViewById(R.id.ViewStudio);
        ViewState = (TextView)findViewById(R.id.ViewState);

        list = new ArrayList<CharObjMis>();
        AdapterCharObjMis adapter = new AdapterCharObjMis(this, list);
        lstView = (ListView)findViewById(R.id.listView);
        lstView.setAdapter(adapter);

        Bundle bundle = this.getIntent().getExtras();


        Title = bundle.getString("title");
        setTitle(Title);

        gamesDB = new DBSQLiteHelper(this, "games", null, 1);
        objectsDB = new DBSQLiteHelper(this, "objects", null, 1);
        missionsDB = new DBSQLiteHelper(this, "missions", null, 1);
        charactersDB = new DBSQLiteHelper(this, "characters", null, 1);
        game_levelsDB = new DBSQLiteHelper(this, "game_levels", null, 1);

        cursor = gamesDB.getReadableDatabase().rawQuery("SELECT * FROM games WHERE title='"+Title+"'", null);

        while(cursor.moveToNext()) {
            ViewPlatform.setText(ViewPlatform.getText() + cursor.getString(1));
            ViewYear.setText(ViewYear.getText() + cursor.getString(2));
            ViewStudio.setText(ViewStudio.getText() + cursor.getString(3));
            ViewState.setText(ViewState.getText() + cursor.getString(4));
        }
        cursor.close();
        gamesDB.close();

        game_levelsDB.getReadableDatabase().execSQL("DELETE FROM game_levels");

        cursor = objectsDB.getReadableDatabase().rawQuery("SELECT name_obj, level FROM objects WHERE title='" + bundle.getString("title") + "';", null);
        while (cursor.moveToNext()) {
            game_levelsDB.getWritableDatabase().execSQL("INSERT INTO game_levels (name, type, level, state) VALUES ('" + cursor.getString(0) + "', 'object', " + Integer.parseInt(cursor.getString(1)) + ", '-')");
        }
        objectsDB.close();

        cursor = missionsDB.getReadableDatabase().rawQuery("SELECT name_mis, level, state FROM missions WHERE title='" + bundle.getString("title")+"';", null);
        while (cursor.moveToNext()) {
            game_levelsDB.getWritableDatabase().execSQL("INSERT INTO game_levels (name, type, level, state) VALUES ('" + cursor.getString(0) + "', 'mission', " + Integer.parseInt(cursor.getString(1)) + ", '"+cursor.getString(2)+"')");
        }
        missionsDB.close();

        cursor = charactersDB.getReadableDatabase().rawQuery("SELECT name_char, level FROM characters WHERE title='" + bundle.getString("title")+"';", null);
        while (cursor.moveToNext()) {
            game_levelsDB.getWritableDatabase().execSQL("INSERT INTO game_levels (name, type, level, state) VALUES ('" + cursor.getString(0) + "', 'character', " + Integer.parseInt(cursor.getString(1)) + ", '-')");
        }
        charactersDB.close();


        cursor = game_levelsDB.getReadableDatabase().rawQuery("SELECT * FROM game_levels ORDER BY level DESC", null);
        while (cursor.moveToNext()) {
            String Name = cursor.getString(0);
            String Type = cursor.getString(1);
            String State = cursor.getString(4);
            int Level = Integer.parseInt(cursor.getString(2));
            CharObjMis charobjmis = new CharObjMis(Name, Type, Level, bundle.getString("title"), State);
            list.add(charobjmis);
        }
        cursor.close();
        game_levelsDB.close();


        vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goto_view(position);
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //vibrator.vibrate(100);
                final CharSequence options[] = new CharSequence[]{"View", "Edit", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewGame.this);
                builder.setTitle(list.get(position).getName());
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (options[which] == "View") goto_view(position);
                        else if (options[which] == "Edit") edit(position);
                        else if (options[which] == "Delete") delete(position);
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    private void delete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewGame.this);
        builder.setMessage("Are you sure you want to delete the "+list.get(position).getType()+" "+list.get(position).getName()+"?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("TYPE: ", list.get(position).getType() );
                if (list.get(position).getType().equals("object")) {
                    objectsDB.getWritableDatabase().execSQL("DELETE FROM objects WHERE title='"+list.get(position).getTitle()+"' AND name_obj='"+list.get(position).getName()+"'");
                    objectsDB.close();
                }
                else if (list.get(position).getType().equals("character")) {
                    charactersDB.getWritableDatabase().execSQL("DELETE FROM characters WHERE title='"+list.get(position).getTitle()+"' AND name_char='"+list.get(position).getName()+"'");
                    charactersDB.close();
                }
                else {
                    missionsDB.getWritableDatabase().execSQL("DELETE FROM missions WHERE title='"+list.get(position).getTitle()+"' AND name_mis='" + list.get(position).getName()+"'");
                    missionsDB.close();
                }
                Intent intent = new Intent(ViewGame.this, ViewGame.class);
                Bundle b = new Bundle();
                b.putString("title", Title);
                intent.putExtras(b);
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
    }

    private void edit(int position) {
        if(list.get(position).getType().equals("object")) {
            Intent intent = new Intent(ViewGame.this, EditObject.class);
            Bundle b = new Bundle();
            b.putString("name", list.get(position).getName());
            b.putInt("level", list.get(position).getLevel());
            b.putString("title", list.get(position).getTitle());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
        else if (list.get(position).getType().equals("character")) {
            Intent intent = new Intent(ViewGame.this, EditCharacter.class);
            Bundle b = new Bundle();
            b.putString("name", list.get(position).getName());
            b.putInt("level", list.get(position).getLevel());
            b.putString("title", list.get(position).getTitle());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
        else {
            //go to view mission
            Intent intent = new Intent(ViewGame.this, EditMission.class);
            Bundle b = new Bundle();
            b.putString("name", list.get(position).getName());
            b.putInt("level", list.get(position).getLevel());
            b.putString("title", list.get(position).getTitle());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }

    private void goto_view(int position) {
        if(list.get(position).getType().equals("object")) {
            Intent intent = new Intent(ViewGame.this, ViewObject.class);
            Bundle b = new Bundle();
            b.putString("name", list.get(position).getName());
            b.putInt("level", list.get(position).getLevel());
            b.putString("title", list.get(position).getTitle());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
        else if (list.get(position).getType().equals("character")) {
            Intent intent = new Intent(ViewGame.this, ViewCharacter.class);
            Bundle b = new Bundle();
            b.putString("name", list.get(position).getName());
            b.putInt("level", list.get(position).getLevel());
            b.putString("title", list.get(position).getTitle());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
        else {
            //go to view mission
            Intent intent = new Intent(ViewGame.this, ViewMission.class);
            Bundle b = new Bundle();
            b.putString("name", list.get(position).getName());
            b.putInt("level", list.get(position).getLevel());
            b.putString("title", list.get(position).getTitle());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_game, menu);
        return true;
    }


    class AdapterCharObjMis extends ArrayAdapter<CharObjMis> {

        public AdapterCharObjMis(Context context, ArrayList<CharObjMis> list) {
            super(context, R.layout.listitem_char_obj_mis, list);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_char_obj_mis, null);

            TextView title = (TextView)item.findViewById(R.id.Name);
            title.setText(list.get(position).getName());

            TextView level = (TextView)item.findViewById(R.id.Level);
            level.setText("lvl: " + String.valueOf(list.get(position).getLevel()));

            String type = list.get(position).getType();
            ImageView imagetype = (ImageView)item.findViewById(R.id.imageType);
            if (type.equals("character")) imagetype.setImageResource(R.mipmap.character);
            else if (type.equals("mission")) {
                if (list.get(position).getState().equals("Achieved")) imagetype.setImageResource(R.mipmap.mission_finished);
                else imagetype.setImageResource(R.mipmap.mission);
            }
            else if (type.equals("object")) imagetype.setImageResource(R.mipmap.object);

            return(item);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        final CharSequence options[] = new CharSequence[]{"Add a character", "Add a mission", "Add an object"};
        final CharSequence options2[] = new CharSequence[]{"character", "mission", "object"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewGame.this);
        switch (item.getItemId()) {
            case R.id.Add:
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (options[which].equals("Add a character")) addCharacter();
                        else if (options[which].equals("Add a mission")) addMission();
                        else if (options[which].equals("Add an object")) addObject();
                    }
                });
                builder.show();
                return true;
            case R.id.add:
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (options[which].equals("Add a character")) addCharacter();
                        else if (options[which].equals("Add a mission")) addMission();
                        else if (options[which].equals("Add an object")) addObject();
                    }
                });
                builder.show();
                return true;
            case R.id.AddHidden:
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (options[which].equals("Add a character")) addCharacter();
                        else if (options[which].equals("Add a mission")) addMission();
                        else if (options[which].equals("Add an object")) addObject();
                    }
                });
                builder.show();
                return true;
            case R.id.SortBy:
                builder.setItems(options2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (options2[which].equals("character")) gotoShowby("character");
                        else if (options2[which].equals("mission")) gotoShowby("mission");
                        else if (options2[which].equals("object")) gotoShowby("object");
                    }
                });
                builder.show();
                return true;
            case R.id.EditGame:
                editGame();
                return true;
            case R.id.DeleteGame:
                deleteGame();
                return true;
            case R.id.Help:
                Intent intent = new Intent(ViewGame.this, InformationGame.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoShowby(String type)
    {
        Intent intent = new Intent(ViewGame.this, ViewGameSort.class);
        Bundle b = new Bundle();
        b.putString("type", type);
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void deleteGame(){

        final DBSQLiteHelper usdbhChar = new DBSQLiteHelper(this, "characters", null, 1);
        final DBSQLiteHelper usdbhObj = new DBSQLiteHelper(this, "objects", null, 1);
        final DBSQLiteHelper usdbhMis = new DBSQLiteHelper(this, "missions", null, 1);
        final DBSQLiteHelper usdbhGam = new DBSQLiteHelper(this, "games", null, 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewGame.this);
        builder.setMessage("Are you sure you want to delete "+Title+"?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                db = usdbhChar.getWritableDatabase();
                db.execSQL("DELETE FROM characters WHERE title='" + Title + "'");
                db.close();
                db = usdbhObj.getWritableDatabase();
                db.execSQL("DELETE FROM objects WHERE title='" + Title + "'");
                db.close();
                db = usdbhMis.getWritableDatabase();
                db.execSQL("DELETE FROM missions WHERE title='" + Title + "'");
                db.close();
                db = usdbhGam.getWritableDatabase();
                db.execSQL("DELETE FROM games WHERE title='" + Title + "'");
                db.close();
                Intent intent = new Intent(ViewGame.this, MainActivity.class);
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
    }

    private void editGame() {
        Intent intent = new Intent(ViewGame.this, EditGame.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void addObject() {
        Intent intent = new Intent(ViewGame.this, RegisterObject.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void addMission() {
        Intent intent = new Intent(ViewGame.this, RegisterMission.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void addCharacter() {
        Intent intent = new Intent(ViewGame.this, RegisterCharacter.class);
        Bundle b = new Bundle();
        b.putString("title", Title);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void onBackPressed(){
        Intent intent = new Intent(ViewGame.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
