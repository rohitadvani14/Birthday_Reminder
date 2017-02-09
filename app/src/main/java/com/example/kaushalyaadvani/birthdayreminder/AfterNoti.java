package com.example.kaushalyaadvani.birthdayreminder;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kaushalyaadvani on 14/07/16.
 */
public class AfterNoti extends AppCompatActivity
{
    TextView notiName,notiPhone;
    Button btnCall, btnMessage;
    EditText edtMessage;
    String SENT="SENT";
    String DELIVERED="DELIVERED";
    PendingIntent sentIntent, deliveredIntent;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    String message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_notify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        edtMessage = (EditText)findViewById(R.id.edtMessage);
        notiName = (TextView) findViewById(R.id.notiName);
        notiPhone= (TextView) findViewById(R.id.notiPhone);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        final String phone = intent.getStringExtra("phone");

        notiName.setText(name);
        notiPhone.setText(phone);

        btnCall = (Button)findViewById(R.id.btnCall);
        btnMessage = (Button)findViewById(R.id.btnMessage);

        //To Create Pending Intent
        sentIntent=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredIntent=PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED),0);
        btnCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Create Intent for Call
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));
                Toast.makeText(AfterNoti.this, "Phone Call", Toast.LENGTH_LONG).show();
                if (ActivityCompat.checkSelfPermission(AfterNoti.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                startActivity(intent);
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                message=edtMessage.getText().toString();
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(phone, null, message, sentIntent, deliveredIntent);
            }
        });
    }
    public void onResume()
    {
        super.onResume();
        //Create BroadCast Receiver for Sent Notification
        smsSentReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(AfterNoti.this, "Message SENT",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(AfterNoti.this, "No Services Available", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(AfterNoti.this, "Generic Failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(AfterNoti.this, "Null PDU",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(AfterNoti.this, "Radio Off",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        //Create BroadCast Receiver for Delivered Notification
        smsDeliveredReceiver=new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(AfterNoti.this, "Message Delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(AfterNoti.this, "Message Not Delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //To Register BroadCast Receiver for SENT and DELIVERED
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
    }
    public void onPause()
    {
        super.onPause();
        //To Unregister BroadCastReceiver
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }
}





