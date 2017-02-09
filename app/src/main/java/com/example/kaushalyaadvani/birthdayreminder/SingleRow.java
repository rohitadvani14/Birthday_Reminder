package com.example.kaushalyaadvani.birthdayreminder;

/**
 * Created by kaushalyaadvani on 07/07/16.
 */
public class SingleRow {

    String image;
    String name,dd,mm,yy;
    int uid;
    String phone;

    SingleRow(int uid,String name,String phone,String dd,String mm,String yy,String image)
    {
        this.image=image;
        this.phone=phone;
        this.dd=dd;
        this.mm=mm;
        this.name=name;
        this.uid=uid;
        this.yy=yy;


    }



}
