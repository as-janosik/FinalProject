package org.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    SmsManager smsManager = SmsManager.getDefault();
    Button sendBtn;
    EditText txtphoneNo;
    EditText txtMessage;
    EditText txtContNam;
    String phoneNo; //Phone number will be saved after first test text message
    String message;//Message will not be saved
    String contName;//contact name will be saved after first test text message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        txtphoneNo = (EditText) findViewById(R.id.editText);
        txtMessage = (EditText) findViewById(R.id.editText2);
        txtContNam = (EditText) findViewById(R.id.editText3);
        //###################################################  CODE TO RECALL PHONE NUMBER AND NAME
        //ADDED DEFAULT VALUES IF NO SAVED CONTACT YET
        //CONTACT WILL BE SAVED AFTER SENDING TEST TEXT MESSAGE TO FAVORITE CONTACT.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String number = preferences.getString("Number", "");
        String name = preferences.getString("Name", "");
            if(number == ""){
                txtContNam.setText("911 Dispatch");
                txtphoneNo.setText("911"); //DEFAULT NUMBER IS 911 IF FORGOT A NUMBER
            }
            else{
                txtContNam.setText(name); //SETTING PREVIOUS NAME FROM CONFIG FILE
                txtphoneNo.setText(number); //SETTING PREVIOUS NUMBER FROM CONFIG FILE
            }

        //###################################################
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage();
            }
        });
    }

    protected void sendSMSMessage() {
        //make phoneNo always 911?
        contName = txtContNam.getText().toString();
        phoneNo = txtphoneNo.getText().toString();
        message = txtMessage.getText().toString();

        //##################################################
        //SAVING NUMBER AND NAME TO CONFIG FILE FOR FUTURE USE.
        //ONLY SAVES AFTER FIRST TEXT MESSAGE.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Number",phoneNo);
        editor.putString("Name", contName);
        editor.apply();

//##################################################
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }






}
