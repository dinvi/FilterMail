package com.clientREST.bizMail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity {

    private static final String TAG = "InboxActivity";

    private static final String ACTION_UPDATE = "UPDATE_MODEL";
    private static final String ACTION_QUERY = "QUERY";

    private static final String CLASSIFICATION = "classification";

    private static final String ID_REQ = "id";
    private static final String TYPE_REQ = "type";
    private static final String STATE_REQ = "state";


    CustomAdapter adapter;
    List<Mail> ham_mail_list = new ArrayList<>();
    Request req;

    Mail m1 = new Mail(MailExample.M1_SENDER, MailExample.M1_SUBJECT, MailExample.M1_TEXT, "HAM");
    Mail m2 = new Mail(MailExample.M2_SENDER, MailExample.M2_SUBJECT, MailExample.M2_TEXT, "HAM");
    Mail m3 = new Mail(MailExample.M3_SENDER, MailExample.M3_SUBJECT, MailExample.M3_TEXT, "HAM");
    Mail m4 = new Mail(MailExample.M4_SENDER, MailExample.M4_SUBJECT, MailExample.M4_TEXT, "HAM");
    Mail m5 = new Mail(MailExample.M5_SENDER, MailExample.M5_SUBJECT, MailExample.M5_TEXT, "HAM");
    Mail m6 = new Mail(MailExample.M6_SENDER, MailExample.M6_SUBJECT, MailExample.M6_TEXT, "HAM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        ham_mail_list.add(m1);
        ham_mail_list.add(m2);
        ham_mail_list.add(m3);
        ham_mail_list.add(m4);
        ham_mail_list.add(m5);
        ham_mail_list.add(m6);

        ListView listView = (ListView) findViewById(R.id.listView);


        if (ham_mail_list.size() > 0) {
            adapter = new CustomAdapter(InboxActivity.this, R.layout.listview_layout, ham_mail_list);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(receiver, new IntentFilter(ACTION_QUERY));
        this.registerReceiver(receiver, new IntentFilter(ACTION_UPDATE));
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(receiver);
    }

    /**
     * Our Broadcast Receiver. We get notified that the data is ready, and then we
     * put the content we receive (a string) into the TextView.
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // clear the progress indicator
            if (adapter.progress != null) {
                adapter.progress.dismiss();
            }

            String response = intent.getStringExtra(RestTask.HTTP_RESPONSE);
            String classification = null;
            String state = null;
            JSONObject jsonObj = null;

            if(intent.getAction().equalsIgnoreCase(ACTION_QUERY)) {

                try {
                    jsonObj = new JSONObject(response);
                    classification = jsonObj.getString(CLASSIFICATION);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Mail Classification: " + classification, Toast.LENGTH_LONG).show();
            }else if(intent.getAction().equalsIgnoreCase(ACTION_UPDATE)){
                try {
                    jsonObj = new JSONObject(response);
                    state = jsonObj.getString(STATE_REQ);

                    if(state.equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(context, "Update Model Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Update Model Failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            Log.i(TAG, "RESPONSE = " + response);

        }
    };
}
