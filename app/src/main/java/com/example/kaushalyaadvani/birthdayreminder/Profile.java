package com.example.kaushalyaadvani.birthdayreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kaushalyaadvani on 07/07/16.
 */
public class Profile extends AppCompatActivity {
    TextView txtPhone,txtName,txtDob;
    Button btnEdit,btnDelete;
    ImageView profileImage;
    DBHandler dbh;
    int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        ActionBar ab= getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        dbh=new DBHandler(Profile.this);
        txtName= (TextView) findViewById(R.id.txtNname);
        txtPhone= (TextView) findViewById(R.id.txtPphone);
        txtDob= (TextView) findViewById(R.id.txtDdob);
        profileImage= (ImageView) findViewById(R.id.profile_image);
        btnEdit=(Button)findViewById(R.id.btnEdit);
        btnDelete=(Button)findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        String name=intent.getStringExtra("name");
        String phone=intent.getStringExtra("phone");
        String dob= intent.getStringExtra("dob");


        dbh.openReadable();
        Cursor cursor =dbh.retrieveImageid(phone);
        cursor.moveToFirst();
        if(cursor.getCount()>0)
        {
            uid= Integer.parseInt(cursor.getString(0));
            String photoid=cursor.getString(2);
            //Toast.makeText(Profile.this,uid+"-"+photoid,Toast.LENGTH_LONG).show();
            Bitmap yourSelectedImage=null;
            try {
                //setting the fucking image . Did itttttt yaaaaaaaaaaa. B)
                 yourSelectedImage = BitmapFactory.decodeFile(photoid);
            /* Now you have chosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                Bitmap yourSelectedImage2 = Bitmap.createScaledBitmap(yourSelectedImage, 70, 70, true);

                profileImage.setImageBitmap(yourSelectedImage2);
            }
            catch (Exception e) {
                profileImage.setImageBitmap(yourSelectedImage);
            }
        }
        txtName.setText(name);
        txtPhone.setText(phone);
        txtDob.setText(dob);
        //Toast.makeText(Profile.this,dob,Toast.LENGTH_SHORT).show();
        dbh.close();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent updateIntent =new Intent(Profile.this,UpdateUser.class);
                updateIntent.putExtra("name",txtName.getText());
                updateIntent.putExtra("phone",txtPhone.getText());
                updateIntent.putExtra("dob",txtDob.getText());
                updateIntent.putExtra("uid",uid);
                startActivity(updateIntent);
                dbh.close();
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.openReadable();
               int i= dbh.deleteUser(uid);
                if (i>0)
                    Toast.makeText(Profile.this,"Record Deleted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Profile.this,"No Record Deleted",Toast.LENGTH_SHORT).show();


                Intent delete =new Intent(Profile.this,MainActivity.class);
                startActivity(delete);
                dbh.close();
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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
