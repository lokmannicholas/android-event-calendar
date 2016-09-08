package com.lokman.event_calendar.model;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lokman.event_calendar.model.CEvent;
import com.lokman.event_calendar.utility.DateFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lokmannicholas on 5/9/16.
 */
public class MonthCell {
    int type = 0 ;
    Date dateOfMonth = new Date();
    List<CEvent> cEventList = new ArrayList<>();
    private String title="";
    public int getType(){
        return this.type;
    }
    public void setType(int type){
        this.type= type;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getDayOfMonth(){
        if(type<1){
            return title;
        }
        return Integer.valueOf(DateFormatter.date_only.format(dateOfMonth.getTime())).toString();
    }
    public String getDateString(){
        return DateFormatter.date_month_year.format(dateOfMonth.getTime());
    }
    public Date getDate(){
        return  this.dateOfMonth;
    }
    public void setDate(Date date){
        this.dateOfMonth =date;
    }
    public  List<CEvent> getEvents(){
        return cEventList;
    }
    public void setEvents(List<CEvent> cEventList){
        this.cEventList = cEventList;
    }
}
