package com.example.albert.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Information extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TextView information = (TextView)findViewById(R.id.textInformation);
        information.setText("Click on the game to view the details and all the items of that game. \n" +
                "If you want to add a new game, click on the + icon\n" +
                "You can show by state your games going to 'Show by' on the left button"+
                "To check more option, as delete or edit, do a long click on that item.");
    }
}
