package com.augmentaa.sparkev.ui;



import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.Logger;
public class Session_logs extends ChargerDetailsActivity {


    ListView listView;
    ImageView img_back;
    APIInterface apiInterface;
    String charger_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessionlogs);
        listView = findViewById(R.id.listview);
        img_back = findViewById(R.id.back);
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        try {
            data = getIntent().getParcelableExtra("data");
            Logger.e("Charger data ===" + data.toString());
            charger_id = data.serialNo;
        } catch (Exception e) {

        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


}
