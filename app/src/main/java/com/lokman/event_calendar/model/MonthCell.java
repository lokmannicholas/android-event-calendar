package com.lokman.event_calendar.model;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lokman.event_calendar.model.CEvent;
import com.lokman.event_calendar.utility.DateFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lokmannicholas on 5/9/16.
 */
public class MonthCell {
    private int type = 0 ;
    private Date dateOfMonth = new Date();
    private List<CEvent> cEventList = new ArrayList<>();
    private boolean isHoliday=false;
    private String title="";

    public MonthCell (){

    }

    public boolean isHoliday(){
        return isHoliday;
    }
    public void isHoliday(boolean isHoliday){
        this.isHoliday=isHoliday;
    }
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
        //check holiday
    }
    public  List<CEvent> getEvents(){
        return cEventList;
    }
    public void setEvents(List<CEvent> cEventList){
        if(cEventList==null){
            return;
        }
        if(this.cEventList==null){
            this.cEventList = cEventList;
        }else{
            this.cEventList.addAll(cEventList);
        }

    }
    public void setEvents(CEvent mCEvent){
        if(this.cEventList==null){
            this.cEventList = new ArrayList<CEvent>();
        }
        this.cEventList.add(mCEvent);
    }


}
