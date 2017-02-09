package com.example.kaushalyaadvani.birthdayreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kaushalyaadvani on 12/07/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    Calendar calendar;
    ArrayList<SingleRow> list;
    DBHandler dbh;
    int day,month,yr;
    ArrayList<String> al,al1,al2;
    String str,str1,str2;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        al=new ArrayList<String>();
        al1=new ArrayList<String>();
        al2=new ArrayList<String>();
        dbh=new DBHandler(context);
        //Toast.makeText(context,"alarm done",Toast.LENGTH_SHORT).show();
        list = dbh.fillList();
        SingleRow temp;
        calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        yr = calendar.get(Calendar.YEAR);

        for (int i=0;i<list.size();i++)
        {
            temp = list.get(i);
            //Toast.makeText(context,temp.dd,Toast.LENGTH_SHORT).show();

            if (Integer.parseInt(temp.mm)== (month+1))
            {
                if (Integer.parseInt(temp.dd)== day)
                {
                    //Toast.makeText(context,temp.dd,Toast.LENGTH_LONG).show();
                    str = temp.name;
                    str1=temp.phone;
                    str2=temp.image;
                    al.add(str);
                    al1.add(str1);
                    al2.add(str2);
                }
            }
        }
        //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

        if (al == null)
        {
            Toast.makeText(context, "No B'day", Toast.LENGTH_SHORT).show();
            showNotification("No B'Day","Add a entry","",context);

        }
        else
        {

            for (int i=0;i<al.size();i++)
            {
                String str = al.get(i);
                String str1=al1.get(i);
                String str2=al2.get(i);
                showNotification(str,str1,str2,context);

                Toast.makeText(context, str+"-"+str1, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void showNotification(String message1,String message2,String message3,Context context)
    {

        Intent intent = new Intent(context,AfterNoti.class);
        intent.putExtra("name",message1);
        intent.putExtra("phone",message2);
        intent.putExtra("photoid",message3);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,intent,0);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Birthday :"+message1)
                .setContentText("Phone :"+message2)
                .setAutoCancel(true)
                //.setColor(context.getResources().getColor(R.color.color))
                .setSmallIcon(R.drawable.ic_close)
                .setContentIntent(pendingIntent)
                .setLights(0xffffffff, 1000, 1000)
                .build();
        nm.notify(0,notification);
        //Notification myNotification= notification.getNotification();
        //nm.notify(11,myNotification);
    }


}
