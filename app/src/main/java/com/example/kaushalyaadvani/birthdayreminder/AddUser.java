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
 * Created by kaushalyaadvani on 30/06/16.
 */
public class AddUser extends AppCompatActivity {

    EditText edtName,edtPhone;
    TextView edtDd,edtMm,edtYy;
    CheckBox cb1;
    ImageView imageView;
    String filePath;
    Button btnSave,btnCancel,btnDatePicker;
    DBHandler dbh;
    Calendar calendar;
    int day,month,yr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        Toolbar toolbar = (Toolbar)findViewById(R.id.addUserToolbar);
        setSupportActionBar(toolbar);

        ActionBar ab= getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        dbh=new DBHandler(AddUser.this);

        edtName=(EditText)findViewById(R.id.edtName);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        imageView=(ImageView)findViewById(R.id.profile_pic);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        cb1= (CheckBox) findViewById(R.id.cb1);
        edtDd= (TextView) findViewById(R.id.edtDd);
        edtMm=(TextView) findViewById(R.id.edtMm);
        edtYy=(TextView) findViewById(R.id.edtYy);
        btnDatePicker=(Button)findViewById(R.id.datePicker);

        //date picker

        calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        yr = calendar.get(Calendar.YEAR);

       /* edtDd.setText(day);
        edtMm.setText(month);
        edtYy.setText(yr);*/
        String strDate = day+"/"+(month+1)+"/"+yr;

        String s[] =strDate.split("/");
        edtDd.setText(s[0]);
        edtMm.setText(s[1]);
        edtYy.setText(s[2]);


        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();

            }
        });





        Intent intent = getIntent();
        String name=intent.getStringExtra("name");
        String phone=intent.getStringExtra("num2");

        //String image=intent.getStringExtra("image");

        edtName.setText(name);
        edtPhone.setText(phone);
        //imageView.setImageResource(image);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
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
                /*if(dd==null)
                {
                    dd="";
                }
                if(mm==null)
                {
                    mm="";
                }
                if(yy==null)
                {
                    yy="";
                }*/

                String photoid = filePath;

                long id = dbh.addBday(name,phone,dd,mm,yy,photoid);

                if (id>0)
                {
                    //Snackbar.make(null,"Record inserted Succesfully",Snackbar.LENGTH_LONG).setAction("action",null).show();
                    Toast.makeText(AddUser.this, "Record Inserted",Toast.LENGTH_LONG).show();
                }

                else
                {
                    Toast.makeText(AddUser.this, "No Record Inserted",Toast.LENGTH_LONG).show();
                }

                Intent addUser =new Intent(AddUser.this,MainActivity.class);
                startActivity(addUser);
                dbh.close();
                finish();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbh.close();
                Intent cancel =new Intent(AddUser.this,MainActivity.class);
                startActivity(cancel);
                finish();

            }
        });


    }
/*

    public DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            yr= year;
            month =monthOfYear;
            day =dayOfMonth;

            String strDate = day+"/"+(month+1)+"/"+yr;

            edtDd.setText(day);
            edtMm.setText(month);
            edtYy.setText(yr);

        }
    };*/

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
                    Bitmap yourSelectedImage=null;

                    try {
                        yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    /* Now you have chosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                        Bitmap yourSelectedImage2 = Bitmap.createScaledBitmap(yourSelectedImage, 150, 150, false);

                        //yourSelectedImage2.compress(Bitmap.CompressFormat.PNG,50,null);
                        imageView.setImageBitmap(yourSelectedImage2);
                    }
                    catch (Exception e) {
                        imageView.setImageBitmap(yourSelectedImage);
                    }


                }
        }

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddUser.this);
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
    /*Bitmap mBitmap=null;

//Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

try {


        Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
        if (yourSelectedImage != null) {

        Bitmap newBitmap = Bitmap.createScaledBitmap(yourSelectedImage, 170, 170, true);
        yourSelectedImage.recycle();

        if (newBitmap != null) {

        mBitmap = newBitmap;
        }
        }
        } catch (Exception e) {
        e.printStackTrace();
        }





        imageView.setImageBitmap(mBitmap); */
