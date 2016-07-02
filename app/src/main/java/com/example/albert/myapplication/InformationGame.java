package com.example.albert.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InformationGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TextView information = (TextView)findViewById(R.id.textInformation);
        information.setText("Click on the item to view the details of item game. \n" +
                "The information of the game is displayed on the top.\n" +
                "If you want to add a new item, click on the + icon \n" +
                "You can show by type your items going to, delete the game or edit it on the left button \n"+
                "To check more option, as delete or edit, do a long click on that item.");
    }
}
