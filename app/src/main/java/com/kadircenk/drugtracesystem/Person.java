package com.kadircenk.drugtracesystem;

import android.graphics.Bitmap;

public class Person {

    private String name;
    private String gender;
    private Integer age;
    private Bitmap pic;

    public Person(String mName, Integer mAge, String mGender, Bitmap mPic) {
        name = mName;
        age = mAge;
        gender = mGender;
        pic = mPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}
