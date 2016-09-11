package com.lokman.event_calendar.model;

import android.graphics.Color;

/**
 * Created by lokmannicholas on 5/9/16.
 */
public class CEvent {
    String title;
    int color=Color.TRANSPARENT,title_color=Color.BLACK;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setColor(int resource_color){
        color= resource_color;
    }
    public void setColor(String hexcode){
        color= Color.parseColor("#"+hexcode);
    }
    public void setColor(int r,int g,int b){
        color= Color.rgb(r, g, b);
    }
    public void setColor(int a,int r,int g,int b){
        color= Color.argb(a,r, g, b);
    }
    public int getColor(){
        return color;
    }

    public void setTitleColor(int resource_color){
        title_color= resource_color;
    }
    public void setTitleColor(String hexcode){
        title_color= Color.parseColor("#"+hexcode);
    }
    public void setTitleColor(int r,int g,int b){
        title_color= Color.rgb(r, g, b);
    }
    public void setTitleColor(int a,int r,int g,int b){
        title_color= Color.argb(a,r, g, b);
    }
    public int getTitleColor(){
        return title_color;
    }
}
