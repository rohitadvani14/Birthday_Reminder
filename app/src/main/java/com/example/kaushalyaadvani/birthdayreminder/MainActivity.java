package com.example.kaushalyaadvani.birthdayreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    final int PICK_CONTACT=1;
    ListView listView;
    DBHandler dbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting custom toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        /*to use functions various utility methods provided by
        // the v7 appcompat support library's ActionBar class.
        // This approach lets you do a number of useful things, like hide and show the app bar.
        */
        dbh=new DBHandler(MainActivity.this);
        listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(new MyCustomAdapter(this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView name =(TextView)view.findViewById(R.id.listName);
                TextView dob=(TextView)view.findViewById(R.id.listDob);
                TextView phone=(TextView)view.findViewById(R.id.listPhone);
                //ImageView img = (ImageView) view.findViewById(R.id.listImage);
               // Toast.makeText(getBaseContext(),name.getText()+"-"+dob.getText()+"-"+phone.getText() , Toast.LENGTH_SHORT).show();
                String n =name.getText().toString();
                String d =dob.getText().toString();
                String p =phone.getText().toString();

                //To get Image from listview
                //final BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
                //final Bitmap yourBitmap = bitmapDrawable.getBitmap();


                Intent profileIntent = new Intent(MainActivity.this,Profile.class);
                profileIntent.putExtra("name",n);
                profileIntent.putExtra("phone",p);
                profileIntent.putExtra("dob",d);
                //profile.putExtra("bitmap",yourBitmap);
                startActivity(profileIntent);



            }
        });

        setAlarm();

    }


    private void setAlarm()
    {

        Intent i = new Intent("com.cdac.birthday");

        PendingIntent pi =  PendingIntent.getBroadcast(this,0,i, 0);
        AlarmManager alarmMgr;
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //Intent intent = new Intent(this, AlarmReceiver.class);
       // alarmIntent = PendingIntent.getBroadcast(this, 0, pi, 0);
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 00);
        // setRepeating() lets you specify a precise custom interval--in this case,
        // 10 minutes.
        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60 * 10, pi);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor =  managedQuery(contactData, null, null, null, null);

                //Toast.makeText(MainActivity.this,"onactivityresult",Toast.LENGTH_LONG).show();
                cursor.moveToFirst();
                //Snackbar.make(null,"onactivityresult",Snackbar.LENGTH_SHORT).show();
                String number =cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name =cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //contactName.setText(name);
                //contactNumber.setText(number);
                //contactEmail.setText(email);

                String num = number.replaceAll("\\s","");
                String num1 = num.replaceAll("\\+","");
                String num2 = num1.replaceAll("\\-","");
                Intent intent3= new Intent(MainActivity.this,AddUser.class);
                intent3.putExtra("name",name);
                //intent3.putExtra("number",number);
                intent3.putExtra("num2",num2);
                startActivity(intent3);





            }
        }
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);

        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
            //or
        //return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_new_birthday:

                //Alert box config

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Add New B'day");
                builder.setIcon(R.mipmap.ic_dialog_addblack);
                builder.setItems(R.array.addmenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //// The 'which' argument contains the index position
                        // of the selected item

                        switch (which)
                        {
                            //from contacts
                            case 0:
                               // Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
                                 //       startActivityForResult(intent, PICK_CONTACT);
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                                startActivityForResult(intent, PICK_CONTACT);
                                Toast.makeText(MainActivity.this,"Choose from contacts",Toast.LENGTH_SHORT).show();
                                break;

                            //New Entry
                            case 1:

                                Intent intent2 = new Intent(MainActivity.this,AddUser.class);
                                Toast.makeText(MainActivity.this,"Create New",Toast.LENGTH_SHORT).show();
                                startActivity(intent2);
                                finish();
                                break;

                        }


                    }
                });


                Toast.makeText(MainActivity.this,"Add Entry",Toast.LENGTH_SHORT).show();
                builder.show();
                return true;


            /*case R.id.settings:

                Intent intent2 =new Intent(MainActivity.this,Settings.class);
                Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_LONG).show();
                startActivity(intent2);
                return true;*/


            default:
                return super.onOptionsItemSelected(item);

        }


    }


    public class MyCustomAdapter extends BaseAdapter {

        ArrayList<SingleRow> list;
        Context context;

        MyCustomAdapter(Context context)
        {
            this.context=context;
            dbh.openReadable();

            list= dbh.fillList();

            /*list= new ArrayList<SingleRow>();

            for(int i=0;i<3;i++)
            {
                //Passing the items of array in Single row one by one and adding it into a array list.
                SingleRow s= new SingleRow(images[i],ages[i],names[i],dobs[i]);
                list.add(s);

            }*/



        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {

            //this method is used in case of databases to return id but for now our array indexes
            //are the ids.

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row =inflater.inflate(R.layout.single_row,parent,false);

            ImageView imgView=(ImageView)row.findViewById(R.id.listImage);
            TextView name=(TextView)row.findViewById(R.id.listName);
            TextView age=(TextView)row.findViewById(R.id.listAge);
            TextView dob=(TextView)row.findViewById(R.id.listDob);
            TextView phone=(TextView)row.findViewById(R.id.listPhone);

            SingleRow temp= list.get(position);


            name.setText(temp.name);
            dob.setText(temp.dd+"/"+temp.mm+"/"+temp.yy);
            phone.setText(temp.phone);

            //set image
            Bitmap yourSelectedImage=null;
            try {
                yourSelectedImage = BitmapFactory.decodeFile(temp.image);
            /* Now you have chosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                Bitmap yourSelectedImage2 = Bitmap.createScaledBitmap(yourSelectedImage, 100, 100, true);


                imgView.setImageBitmap(yourSelectedImage2);
            }
            catch (Exception e) {
                imgView.setImageBitmap(yourSelectedImage);
            }

            //Toast.makeText(MainActivity.this,temp.yy.toString(),Toast.LENGTH_LONG).show();
            //age.setText();
            String str="";
            if (temp.yy.equals(str))
            {
                age.setText("Unknown");
            }
            else {
                String age1 = calcAge(Integer.parseInt(temp.mm), Integer.parseInt(temp.dd), Integer.parseInt(temp.yy));
                age.setText(age1);
            }

            return row;


        }

    }

    //calculating age

    public String calcAge(int mm,int dd,int yy)
    {
        int day,month,year;
        int age;
        Calendar calendar;

        calendar= Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        age = (year-yy);

        if ((month+1)<mm)
        {
            return age+"";
        }
        else if ((month+1)>mm)
        {
            return age+1+"";
        }
        else if ((month+1)==mm && day<dd)
        {
            return age+"";
        }
        else if ((month+1)==mm && day==dd)
        {
            return age+1+"";
        }
        else
            return age+1+"";
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit?");
        builder.setMessage("Are you sure?");


        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                dbh.close();
            }
        });


        builder.show();

    }

}
