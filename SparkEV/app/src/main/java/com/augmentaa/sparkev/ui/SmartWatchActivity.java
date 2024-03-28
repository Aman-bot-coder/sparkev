package com.augmentaa.sparkev.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.utils.Logger;
import com.augmentaa.sparkev.utils.Pref;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

;

public class SmartWatchActivity extends AppCompatActivity {

    ImageView talkbutton;
    TextView textview;
    protected Handler myHandler;
    int receivedMessageNumber = 1;
    int sentMessageNumber = 1;
    ImageView img_back;
    ProgressDialog progress;
    Timer swipeTimer = new Timer();
    String message;
    boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_watch);
        talkbutton = findViewById(R.id.btn_connect_smart_watch);
        img_back = findViewById(R.id.back);

        textview = findViewById(R.id.textview);
        getSupportActionBar().hide();
        progress = new ProgressDialog(this);
        //Create a message handler//

        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                messageText(stuff.getString("messageText"));
                return true;
            }
        });

        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        talkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkConnected(SmartWatchActivity.this)) {
                    String message = Pref.getValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), null);
//        textview.setText(message);

//Sending a message can block the main UI thread, so use a new thread//

                    new NewThread("/my_path", message).start();
                    progress.setMessage("Pairing Device, Please wait...");
                    progress.show();
                    progress.setCancelable(true);
                    swipeTimer = new Timer();

                    try {
                        if (Utils.isNetworkConnected(SmartWatchActivity.this)) {
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                progress.dismiss();
                                                if (isChecked) {
                                                    Intent intent1=new Intent(SmartWatchActivity.this,MainActivity.class);
                                                    startActivity(intent1);
                                                    finish();
                                                } else {
                                                    progress.dismiss();
                                                    Toast.makeText(SmartWatchActivity.this, "Please try again", Toast.LENGTH_LONG).show();

                                                }
                                                swipeTimer.cancel();


                                            } catch (Exception e) {
                                                Logger.e("Timer exp");

                                            }


                                        }
                                    });


                                }
                            }, 5000, 5000);

                        } else {
                            Toast.makeText(SmartWatchActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception e) {
                        Logger.e("Timer exp111111");

                    }


                } else {
                    Toast.makeText(SmartWatchActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    public void messageText(String newinfo) {

        Logger.e("New Info   " + newinfo);
        if (newinfo.compareTo("") != 0) {
            textview.append("\n" + newinfo);
        }
    }



   /* public void talkClick(View v) {
        String message = Pref.getValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), null);
//        textview.setText(message);

//Sending a message can block the main UI thread, so use a new thread//

        new NewThread("/my_path", message).start();

    }*/

//Use a Bundle to encapsulate our message//

    public void sendmessage(String messageText) {
        Bundle bundle = new Bundle();
        bundle.putString("messageText", messageText);
        Message msg = myHandler.obtainMessage();
        msg.setData(bundle);
        myHandler.sendMessage(msg);

    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

//Upon receiving each message from the wearable, display the following text//
            message = "I just received a message from the wearable " + receivedMessageNumber++;
            progress.dismiss();
            textview.setText(message);
            isChecked = true;
            Toast.makeText(SmartWatchActivity.this, "Device paired successfully.", Toast.LENGTH_LONG).show();


        }
    }

    class NewThread extends Thread {
        String path;
        String message;

//Constructor for sending information to the Data Layer//

        NewThread(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {

//Retrieve the connected devices, known as nodes//

            Task<List<Node>> wearableList = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();


            try {

                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =

//Send the message//
                            Wearable.getMessageClient(SmartWatchActivity.this).sendMessage(node.getId(), path, message.getBytes());

                    try {

                        //Block on a task and get the result synchronously//
                        Integer result = Tasks.await(sendMessageTask);

                        sendmessage(Pref.getValue(Pref.TYPE.CHARGER_LIST_DATA.toString(), null));

                        //if the Task fails, then…..//

                    } catch (ExecutionException exception) {

                        //TO DO: Handle the exception//

                    } catch (InterruptedException exception) {
                        //TO DO: Handle the exception//

                    }

                }

            } catch (ExecutionException exception) {

                //TO DO: Handle the exception//

            } catch (InterruptedException exception) {

                //TO DO: Handle the exception//
            }

        }


    }

    @Override
    public void onResume() {
        super.onResume();
//        Logger.e("Timer   " + swipeTimer);
       /* swipeTimer = new Timer();

        try {
            if (Utils.isNetworkConnected(SmartWatchActivity.this)) {
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    progress.dismiss();
                                    if (isChecked) {

                                    } else {
                                        progress.dismiss();
                                        Toast.makeText(SmartWatchActivity.this, "Please try again", Toast.LENGTH_LONG).show();

                                    }
                                    swipeTimer.cancel();


                                } catch (Exception e) {
                                    Logger.e("Timer exp");

                                }


                            }
                        });


                    }
                }, 5000, 5000);

            } else {
                Toast.makeText(SmartWatchActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Logger.e("Timer exp111111");

        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        if (swipeTimer != null)
            swipeTimer.cancel();
    }

}