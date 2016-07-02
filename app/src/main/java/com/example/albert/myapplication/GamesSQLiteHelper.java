package com.example.albert.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

class DBSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreateGames = "CREATE TABLE games (title TEXT, platform TEXT, year TEXT, studio TEXT, state TEXT)";
    String sqlCreateCharacters = "CREATE TABLE characters (title TEXT, name_char TEXT, race TEXT, level INTEGER)";
    String sqlCreateMissions = "CREATE TABLE missions (title TEXT, name_mis TEXT, level INTEGER, description TEXT, points INTEGER, state TEXT)";
    String sqlCreateObjects = "CREATE TABLE objects (title TEXT, name_obj TEXT, level INTEGER)";
    String sqlCreateGame_Levels = "CREATE TABLE game_levels (name TEXT, type TEXT, level INTEGER, race TEXT, state TEXT)";

    public DBSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateGames);
        db.execSQL(sqlCreateCharacters);
        db.execSQL(sqlCreateMissions);
        db.execSQL(sqlCreateObjects);
        db.execSQL(sqlCreateGame_Levels);

        db.execSQL("INSERT INTO games VALUES ('Super Mario Bros', 'NES', '1985', 'Nintendo EAD', 'Finished');");
        db.execSQL("INSERT INTO games VALUES ('Golden Sun', 'Gameboy Advance', '2001', 'Camelot Software', 'Pending');");
        db.execSQL("INSERT INTO games VALUES ('League of Legends', 'PC', '2009', 'Riot Games', 'Playing');");
        db.execSQL("INSERT INTO characters VALUES ('Super Mario Bros', 'Mario', 'Human', 10)");
        db.execSQL("INSERT INTO characters VALUES ('Super Mario Bros', 'Luigi', 'Human', 3)");
        db.execSQL("INSERT INTO characters VALUES ('Super Mario Bros', 'Princess Peach', 'Human', 1)");
        db.execSQL("INSERT INTO characters VALUES ('Super Mario Bros', 'Bowser', 'Monster', 7)");
        db.execSQL("INSERT INTO characters VALUES ('Golden Sun', 'Hans', 'Human', 50)");
        db.execSQL("INSERT INTO characters VALUES ('Golden Sun', 'Garet', 'Mage', 20)");
        db.execSQL("INSERT INTO characters VALUES ('Golden Sun', 'Mia', 'Mage', 28)");
        db.execSQL("INSERT INTO characters VALUES ('League of Legends', 'Aatrox', 'Fighter', 9)");
        db.execSQL("INSERT INTO characters VALUES ('League of Legends', 'Ashe', 'Marksman', 8)");
        db.execSQL("INSERT INTO characters VALUES ('League of Legends', 'Elise', 'Mage', 18)");
        db.execSQL("INSERT INTO characters VALUES ('League of Legends', 'Ekko', 'Assassin', 17)");
        db.execSQL("INSERT INTO objects VALUES ('Super Mario Bros', 'Key', 1)");
        db.execSQL("INSERT INTO objects VALUES ('Golden Sun', 'Sword', 10)");
        db.execSQL("INSERT INTO objects VALUES ('Golden Sun', 'Armor', 8)");
        db.execSQL("INSERT INTO objects VALUES ('Golden Sun', 'Map', 1)");
        db.execSQL("INSERT INTO objects VALUES ('Golden Sun', 'Perl', 5)");
        db.execSQL("INSERT INTO objects VALUES ('League of Legends', 'Ward', 1);");
        db.execSQL("INSERT INTO objects VALUES ('League of Legends', 'Potion', 1)");
        db.execSQL("INSERT INTO objects VALUES ('League of Legends', 'Corruption potion', 1);");
        db.execSQL("INSERT INTO objects VALUES ('League of Legends', 'Long sword', 1);");
        db.execSQL("INSERT INTO missions VALUES ('Super Mario Bros','Save the princess', 4, 'Go to the castle and find the princess.', 1000, 'Achieved');");
        db.execSQL("INSERT INTO missions VALUES ('Golden Sun','Find the perl', 2, 'Go to the castle and find the perl.', 10, 'Achieved');");
        db.execSQL("INSERT INTO missions VALUES ('Golden Sun','Find the map', 1, 'Go to the beach and find the map.', 5, 'Not achieved');");
        db.execSQL("INSERT INTO missions VALUES ('Golden Sun','Upgrade sword', 5, 'Go to the tower Tundaria and ask the magician to upgrade the sword.', 40, 'Achieved');");
        db.execSQL("INSERT INTO missions VALUES ('League of Legends','First victory', 82, 'Win the first game of the day.', 150, 'Achieved');");
        db.execSQL("INSERT INTO missions VALUES ('League of Legends','Easter week event', 8, 'Find as many easter eggs as possible', 500, 'Achieved');");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS games");
        db.execSQL(sqlCreateGames);
    }
}
