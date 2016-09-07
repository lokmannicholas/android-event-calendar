package com.lokman.event_calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lokman.event_calendar.R;
import com.lokman.event_calendar.model.CEvent;
import com.lokman.event_calendar.utility.DateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by lokmannicholas on 5/9/16.
 */
public class CalenderMonthView extends RelativeLayout {

    public interface OnMonthCellSelectListener{
        void selectedCell(Date date);
    }

    private OnMonthCellSelectListener mOnMonthCellSelectListener;
    private LayoutInflater mInflater;
    private GridView gridView;
    private List<String> monthList;
    private HashMap<String,List<CEvent>> mCEventMap;
    private MonthCellAdapter mMonthCellAdapter;
    private Context context;
    private TextView txt_title;
    private Date startDate,calendarStartDay;
    private Date endDate,calendarEndDay;
    private Date today;
    private int weekday;
    private Calendar mCalendar = Calendar.getInstance();

    public CalenderMonthView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        this.context = context;
        init();

    }
    public CalenderMonthView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        this.context = context;
        init();
    }
    public CalenderMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        this.context = context;
        init();
    }


    private void init()
    {
        today = new Date();
        monthList = new ArrayList<String>();
        for(int i=1;i<=31;i++){
            monthList.add(String.valueOf(i));
        }
        mCEventMap = new HashMap<String,List<CEvent>>();

        View v = mInflater.inflate(R.layout.calender_month_view, this, true);
        txt_title = (TextView) findViewById(R.id.txt_title);
        gridView = (GridView) findViewById(R.id.month_view);

         Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        this.calendarStartDay = calendar.getTime();
        mCalendar = calendar;

        calendar.add(Calendar.MONTH,4);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        this.calendarEndDay = calendar.getTime();

        setMonth(9, year);



    }

    /****** Listener *****/
    public void setOnMonthCellSelectListener(final OnMonthCellSelectListener mOnMonthCellSelectListener){

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                mOnMonthCellSelectListener.selectedCell(((MonthCellView)v.getTag(R.layout.week_cell)).dateOfMonth);

            }
        });
    }
    /******** DataSetting ********/
    private void setMonth(int month, int year){


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        weekday = calendar.get(Calendar.DAY_OF_WEEK) -1; //weekday start from 1
        int numDays = calendar.getActualMaximum(Calendar.DATE);


        //get min date of month
        startDate = calendar.getTime();

        //get max date of month
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        endDate = calendar.getTime();

        mCalendar = calendar;
        mMonthCellAdapter =new MonthCellAdapter(numDays);
        gridView.setAdapter(mMonthCellAdapter);
        mMonthCellAdapter.notifyDataSetChanged();
        txt_title.setText(String.format(getResources().getConfiguration().locale, "%tB", calendar) + year);


    }
    public void loadLastMonth(){
        //monthList.clear();
        if(startDate.getTime() > calendarStartDay.getTime() && calendarEndDay.getTime() >= endDate.getTime()) {
            mCalendar.add(Calendar.MONTH, -1);
            this.setMonth(mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR));
        }
    }
    public void loadNextMonth(){
       // monthList.clear();
        if(startDate.getTime() >= calendarStartDay.getTime() && calendarEndDay.getTime() > endDate.getTime()) {
            mCalendar.add(Calendar.MONTH, 1);
            this.setMonth(mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR));
        }
    }
    public CalenderMonthView setCalendarDateRange(Date calendarStartDay,Date calendarEndDay) throws ParseException {

        this.calendarStartDay = DateFormatter.date_month_year.parse(DateFormatter.date_month_year.format(calendarStartDay.getTime()));
        this.calendarEndDay = DateFormatter.date_month_year.parse(DateFormatter.date_month_year.format(calendarEndDay.getTime()));
        return this;
    }
    public CalenderMonthView setCalendarViewDays(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.calendarStartDay);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        mCalendar =calendar;
        calendar.add(Calendar.DAY_OF_YEAR,days);

//        this.calendarStartDay = calendarStartDay;
        this.calendarEndDay = calendar.getTime();
        return this;
    }
    public void reLoad(){
        if(startDate.getTime() >= calendarStartDay.getTime() && calendarEndDay.getTime() >= endDate.getTime()){
            mMonthCellAdapter.notifyDataSetChanged();
            txt_title.setText(String.format(getResources().getConfiguration().locale, "%tB", mCalendar) + mCalendar.get(Calendar.YEAR));
        }
    }
    public void setEvent(HashMap<String,List<CEvent>> mCEvent ){
        this.mCEventMap = mCEvent;
        mMonthCellAdapter.notifyDataSetChanged();
    }
    public void setCurrent(Date today){
        this.today = today;
    }

    public Date getCurrentDay(){return this.today;}


    /******** MonthCellAdapter ********/
    protected class MonthCellAdapter extends BaseAdapter {
        int max_day_of_mth=0;

        public MonthCellAdapter(int max_day_of_mth) {
            super();
            this.max_day_of_mth=max_day_of_mth;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            MonthCellView viewHolder;

            if (convertView == null) {
                viewHolder = new MonthCellView();
                if(position>=7) {
                    convertView = mInflater.inflate(R.layout.month_cell, parent, false);
                    viewHolder.txt_monthday=(TextView) convertView.findViewById(R.id.txt_monthday);
                    viewHolder.ll_dayEvent=(LinearLayout) convertView.findViewById(R.id.ll_dayEvent);
                    convertView.setTag(R.layout.month_cell,viewHolder);
                }else{
                    convertView = mInflater.inflate(R.layout.week_cell,  parent, false);
                    viewHolder.txt_weekday = (TextView) convertView.findViewById(R.id.txt_weekday);
                    convertView.setTag(R.layout.week_cell,viewHolder);
                }

            } else {
                if(position>=7) {
                    viewHolder = (MonthCellView) convertView.getTag(R.layout.month_cell);
                }else{
                    viewHolder = (MonthCellView) convertView.getTag(R.layout.week_cell);
                }
            }
            if(position>=7) {
                if(position<7+weekday){
                    viewHolder.txt_monthday.setText("");
                    convertView.setClickable(true);
                }else{

                    viewHolder.txt_monthday.setText(monthList.get(position - (7 + weekday)));
                    viewHolder.dateOfMonth.setTime(startDate.getTime() + (position - (7 + weekday)) * 24 * 60 * 60 * 1000);
                    if(mCEventMap.get(DateFormatter.date_month_year.format(viewHolder.dateOfMonth.getTime()))!=null){
//                        viewHolder.txt_monthday.setTextColor(getResources().getColor(R.color.colorPrimary));
                        List<CEvent> cEventList = mCEventMap.get(DateFormatter.date_month_year.format(viewHolder.dateOfMonth.getTime()));
                        for(int i=0;i<cEventList.size();i++){
                            CEvent mCEvent = cEventList.get(i);
                            TextView event_title = new TextView(context);
                            event_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            event_title.setText(mCEvent.getTitle());
                            event_title.setBackgroundColor(mCEvent.getColor());
                            viewHolder.ll_dayEvent.addView(event_title);
                        }
                    }
                    if(DateFormatter.date_month_year.format(viewHolder.dateOfMonth.getTime()).equals(DateFormatter.date_month_year.format(today.getTime()))){
                        viewHolder.txt_monthday.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                    convertView.setClickable(true);
                }
            }else{

                switch(position){
                    case 0:

                        viewHolder.txt_weekday.setText("SUN");
                        break;
                    case 1:
                        viewHolder.txt_weekday.setText("MON");
                        break;
                    case 2:
                        viewHolder.txt_weekday.setText("TUE");
                        break;
                    case 3:
                        viewHolder.txt_weekday.setText("WED");
                        break;
                    case 4:
                        viewHolder.txt_weekday.setText("THU");
                        break;
                    case 5:
                        viewHolder.txt_weekday.setText("FRI");
                        break;
                    case 6:
                        viewHolder.txt_weekday.setText("SAT");
                        break;
                }
            }


            return convertView;
        }

        @Override
        public int getCount() {
            return max_day_of_mth+7+weekday;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }
}
