package com.lokman.event_calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
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
    private Calendar mCalendar = Calendar.getInstance();
    private Context context;
    private TextView txt_title;
    private Date startDate;
    private Date endDate;
    private Date today;
    private int weekday;

    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

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
        mCEventMap = new HashMap<String,List<CEvent>>();

        mMonthCellAdapter = new MonthCellAdapter();
        View v = mInflater.inflate(R.layout.calender_month_view, this, true);
        txt_title = (TextView) findViewById(R.id.txt_title);
        gridView = (GridView) findViewById(R.id.month_view);

        gridView.setAdapter(mMonthCellAdapter);

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);

        setMonth(month, year);


        HashMap<String,List<CEvent>> mCEventMap = new HashMap<String,List<CEvent>>();
        String key = df.format(startDate.getTime() + 5 * 24 * 60 * 60 * 1000);
        List<CEvent> CEventList = new ArrayList<CEvent>();
        CEvent mCEvent = new CEvent();
        mCEvent.setTitle("test");
        mCEvent.setColor("E5E5E5");
        CEventList.add(mCEvent);
        mCEventMap.put(key,CEventList);

        setEvent(mCEventMap);
    }

    /****** Listener *****/
    public void setOnMonthCellSelectListener(final OnMonthCellSelectListener mOnMonthCellSelectListener){

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                mOnMonthCellSelectListener.selectedCell();

            }
        });
    }
    /******** DataSetting ********/
    public CalenderMonthView setMonth(int month, int year){


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));

        weekday = calendar.get(Calendar.DAY_OF_WEEK) -1; //weekday start from 1
        int numDays = calendar.getActualMaximum(Calendar.DATE);

        for(int i=1;i<=numDays;i++){
            monthList.add(String.valueOf(i));
        }

        startDate = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        endDate = calendar.getTime();
        mMonthCellAdapter.notifyDataSetChanged();
        txt_title.setText(String.format(getResources().getConfiguration().locale, "%tB", calendar) + year);
        return this;
    }

    public void setEvent(HashMap<String,List<CEvent>> mCEvent ){
        this.mCEventMap = mCEvent;
        mMonthCellAdapter.notifyDataSetChanged();
    }
    public void setCurrent(Date today){
        this.today = today;
    }
    /******** MonthCellAdapter ********/
    protected class MonthCellAdapter extends BaseAdapter {

        class ViewHolder {
            TextView txt_monthday;
            TextView txt_weekday;
            LinearLayout ll_dayEvent;
            Date dateOfMonth = new Date();
        }
        public MonthCellAdapter() {

        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
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
                    viewHolder = (ViewHolder) convertView.getTag(R.layout.month_cell);
                }else{
                    viewHolder = (ViewHolder) convertView.getTag(R.layout.week_cell);
                }
            }
            if(position>=7) {
                if(position<7+weekday){
                    viewHolder.txt_monthday.setText("");
                }else{
                    viewHolder.txt_monthday.setText(monthList.get(position - (7+weekday)));
                    viewHolder.dateOfMonth.setTime(startDate.getTime() + (position - (7 + weekday)) * 24 * 60 * 60 * 1000);
                    if(mCEventMap.get(df.format(viewHolder.dateOfMonth.getTime()))!=null){
//                        viewHolder.txt_monthday.setTextColor(getResources().getColor(R.color.colorPrimary));
                        List<CEvent> cEventList = mCEventMap.get(df.format(viewHolder.dateOfMonth.getTime()));
                        for(int i=0;i<cEventList.size();i++){
                            CEvent mCEvent = cEventList.get(i);
                            TextView event_title = new TextView(context);
                            event_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            event_title.setText(mCEvent.getTitle());
                            event_title.setBackgroundColor(mCEvent.getColor());
                            viewHolder.ll_dayEvent.addView(event_title);
                        }

                    }
                    if(df.format(viewHolder.dateOfMonth.getTime()).equals(df.format(today.getTime()))){
                        viewHolder.txt_monthday.setTextColor(getResources().getColor(R.color.colorAccent));
                    }

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
            return monthList.size()+7;
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
