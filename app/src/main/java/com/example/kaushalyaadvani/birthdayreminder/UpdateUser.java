package com.example.kaushalyaadvani.birthdayreminder;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by kaushalyaadvani on 06/07/16.
 */
public class UpdateUser extends AppCompatActivity {

    EditText edtName,edtPhone;
    TextView edtDd,edtMm,edtYy;
    CheckBox cb1;
    Button btnUpdate,btnCancel,btnDatePicker;
    ImageView imgView;
    DBHandler dbh;
    String filePath;
    int uid;
    Calendar calendar;
    int day,month,yr;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws ArrayIndexOutOfBoundsException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user);

        Toolbar toolbar = (Toolbar)findViewById(R.id.updateUserToolbar);
        setSupportActionBar(toolbar);

        /*ActionBar ab= getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);*/


        dbh=new DBHandler(UpdateUser.this);

        edtName=(EditText)findViewById(R.id.edtName);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        imgView=(ImageView)findViewById(R.id.profile_pic);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        cb1= (CheckBox) findViewById(R.id.cb1);
        edtDd= (TextView) findViewById(R.id.edtDd);
        edtMm=(TextView) findViewById(R.id.edtMm);
        edtYy=(TextView) findViewById(R.id.edtYy);
        btnDatePicker=(Button)findViewById(R.id.datePicker);


        Intent intent = getIntent();
        String name=intent.getStringExtra("name");
        String phone=intent.getStringExtra("phone");
        String dob= intent.getStringExtra("dob");
        uid =intent.getIntExtra("uid",0);

        dbh.openReadable();
        Cursor cursor =dbh.retrieveImageid(phone);
        cursor.moveToFirst();
        Bitmap yourSelectedImage =null;
        if(cursor.getCount()>0)
        {
            String photoid1=cursor.getString(2);
            Toast.makeText(UpdateUser.this,uid+"-"+photoid1,Toast.LENGTH_LONG).show();
            try
            {
            yourSelectedImage = BitmapFactory.decodeFile(photoid1);
            /* Now you have chosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
            Bitmap yourSelectedImage2 =Bitmap.createScaledBitmap(yourSelectedImage,70,70,true);


            imgView.setImageBitmap(yourSelectedImage2);}
            catch (Exception e) {
                imgView.setImageBitmap(yourSelectedImage);
            }

        }

        edtName.setText(name);
        edtPhone.setText(phone);
        String s[]=dob.split("/");

        edtDd.setText(s[0]);
        edtMm.setText(s[1]);
        //check if year is null or not
        String str = "";
        try{
            if (str.equals(s[2]))
            {
                edtYy.setText("");
            }
            else
            {
                edtYy.setText(s[2]);
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            edtYy.setText("");
            e.printStackTrace();
        }



        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();

            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String yy;
                String name=edtName.getText().toString();
                String phone=edtPhone.getText().toString();
                String dd=edtDd.getText().toString();
                String mm=edtMm.getText().toString();
                if(cb1.isChecked())
                {
                    yy="";
                }
                else
                {
                    yy=edtYy.getText().toString();
                }

                String photoid2 =filePath ;

                long id = dbh.updateUser(uid,name,phone,dd,mm,yy,photoid2);

                if (id>0)
                {
                    //Snackbar.make(null,"Record inserted Succesfully",Snackbar.LENGTH_LONG).setAction("action",null).show();
                    Toast.makeText(UpdateUser.this, "Record Updated",Toast.LENGTH_LONG).show();
                }

                else
                {
                    Toast.makeText(UpdateUser.this, "No Record Updated",Toast.LENGTH_LONG).show();
                }


                Intent backToMain =new Intent(UpdateUser.this,MainActivity.class);
                startActivity(backToMain);
                dbh.close();

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbh.close();
                Intent cancel =new Intent(UpdateUser.this,MainActivity.class);
                startActivity(cancel);
                finish();

            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
            /* Now you have chosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                    Bitmap yourSelectedImage2 =Bitmap.createScaledBitmap(yourSelectedImage,70,70,true);


                    imgView.setImageBitmap(yourSelectedImage2);

                }
        }

    }

    //choosing date
    public void showDatePicker()
    {
        DatePickerDialog d = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                yr= year;
                month =monthOfYear;
                day =dayOfMonth;

                String strDate = day+"/"+(month+1)+"/"+yr;

                String s[] =strDate.split("/");
                edtDd.setText(s[0]);
                edtMm.setText(s[1]);
                edtYy.setText(s[2]);

            }
        },1993,1,1);
        d.show();
    }






    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUser.this);
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
