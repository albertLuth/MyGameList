package com.example.albert.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private ListView lstGames;
    private Vibrator vibrator;
    private DBSQLiteHelper usdbh;
    private ArrayList<Game>games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        games = new ArrayList<Game>();
        AdapterGame adapter = new AdapterGame(this, games);
        lstGames = (ListView)findViewById(R.id.listGames);
        lstGames.setAdapter(adapter);

        usdbh = new DBSQLiteHelper(this, "games", null, 1);
        db = usdbh.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM games", null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(0);
            String platform = cursor.getString(1);
            String year = cursor.getString(2);
            String studio = cursor.getString(3);
            String state = cursor.getString(4);

            Game game = new Game(title, platform, year, studio, state);
            games.add(game);
        }
        usdbh.close();


        vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);

        lstGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoGameView(position);
            }
        });

        lstGames.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                vibrator.vibrate(100);
                final CharSequence options[] = new CharSequence[]{"View game","Edit game", "Delete game", "Add a character", "Add a mission", "Add an object"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(games.get(position).getTitle());
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (options[which].equals("View game")) gotoGameView(position);
                        else if (options[which].equals("Delete game")) deleteGame(position);
                        else if (options[which].equals("Add a character")) addCharacter(position);
                        else if (options[which].equals("Add a mission")) addMission(position);
                        else if (options[which].equals("Add an object")) addObject(position);
                        else if (options[which].equals("Edit game")) editGame(position);
                    }
                });
                builder.show();
                return false;
            }
        });

    }

    public void onBackPressed(){
        finish();
        return;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        final CharSequence optionsHelp[] = new CharSequence[]{"Information", "Contact"};
        final CharSequence optionsSort[] = new CharSequence[]{"Playing", "Pending", "Finished"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        switch (item.getItemId()) {
            case R.id.add:
                intent = new Intent(MainActivity.this, RegisterGame.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.About:
                intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                return true;
            case R.id.Help:
                intent = new Intent(MainActivity.this, Information.class);
                startActivity(intent);
                return true;
            case R.id.Sort:
                builder.setItems(optionsSort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (optionsSort[which].equals("Playing")) gotoSort("Playing");
                        else if (optionsSort[which].equals("Pending")) gotoSort("Pending");
                        else if (optionsSort[which].equals("Finished")) gotoSort("Finished");
                    }
                });
                builder.show();
                return true;
            case R.id.AddHidden:
                intent = new Intent(MainActivity.this, RegisterGame.class);
                startActivity(intent);
                finish();
                return true;
            case  R.id.Contact:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"albert.masip.vela@est.fib.upc.edu"});
                try {
                    startActivity(Intent.createChooser(intent, "Send a mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There is no email applications installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void addObject(int position) {
        Intent intent = new Intent(MainActivity.this, RegisterObject.class);
        Bundle b = new Bundle();
        b.putString("title", games.get(position).getTitle());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void addMission(int position) {
        Intent intent = new Intent(MainActivity.this, RegisterMission.class);
        Bundle b = new Bundle();
        b.putString("title", games.get(position).getTitle());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void addCharacter(int position) {
        Intent intent = new Intent(MainActivity.this, RegisterCharacter.class);
        Bundle b = new Bundle();
        b.putString("title", games.get(position).getTitle());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void deleteGame(final int position) {
        final DBSQLiteHelper usdbhChar = new DBSQLiteHelper(this, "characters", null, 1);
        final DBSQLiteHelper usdbhObj = new DBSQLiteHelper(this, "objects", null, 1);
        final DBSQLiteHelper usdbhMis = new DBSQLiteHelper(this, "missions", null, 1);
        final DBSQLiteHelper usdbhGam = new DBSQLiteHelper(this, "games", null, 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to delete " + games.get(position).getTitle() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                db = usdbhChar.getWritableDatabase();
                db.execSQL("DELETE FROM characters WHERE title='" + games.get(position).getTitle() + "'");
                db.close();
                db = usdbhObj.getWritableDatabase();
                db.execSQL("DELETE FROM objects WHERE title='" + games.get(position).getTitle() + "'");
                db.close();
                db = usdbhMis.getWritableDatabase();
                db.execSQL("DELETE FROM missions WHERE title='" + games.get(position).getTitle() + "'");
                db.close();
                db = usdbhGam.getWritableDatabase();
                db.execSQL("DELETE FROM games WHERE title='" + games.get(position).getTitle() + "'");
                db.close();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
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

    private void gotoSort(String state){
        Intent intent = new Intent(MainActivity.this, MainActivitySort.class);
        Bundle b = new Bundle();
        b.putString("state", state);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void gotoGameView(int position) {
        Intent intent = new Intent(MainActivity.this, ViewGame.class);
        Bundle b = new Bundle();
        b.putString("title", games.get(position).getTitle());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void gotoInformation(){
        Intent intent = new Intent(MainActivity.this, Information.class);
        startActivity(intent);
    }


    private void editGame(int position){
        Intent intent = new Intent(MainActivity.this, EditGame.class);
        Bundle b = new Bundle();
        b.putString("title", games.get(position).getTitle());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    class AdapterGame extends ArrayAdapter<Game> {

        public AdapterGame(Context context, ArrayList<Game> games) {
            super(context, R.layout.listitem_game, games);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_game, null);

            TextView title = (TextView)item.findViewById(R.id.title);
            title.setText(games.get(position).getTitle());

            TextView platform = (TextView)item.findViewById(R.id.platform);
            platform.setText(games.get(position).getPlatform());

            String type = games.get(position).getState();
            ImageView imagetype = (ImageView)item.findViewById(R.id.imageState);
            if (type.equals("Playing")) imagetype.setImageResource(R.mipmap.playing);
            else if (type.equals("Pending")) imagetype.setImageResource(R.mipmap.pending);
            else if (type.equals("Finished")) imagetype.setImageResource(R.mipmap.finished);

            return(item);
        }
    }
}