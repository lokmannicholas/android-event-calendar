package com.lokman.event_calendar;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lokman.event_calendar.model.CEvent;
import com.lokman.event_calendar.utility.DateFormatter;
import com.lokman.event_calendar.view.CalenderMonthView;
import com.lokman.event_calendar.model.MonthCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final CalenderMonthView mCalenderMonthView = (CalenderMonthView)findViewById(R.id.calendar_view);
        mCalenderMonthView.setCellBorder(1);
//        mCalenderMonthView.setCurrent();
//        mCalenderMonthView.setMonthCellHeight(300);
        //mCalenderMonthView.reLoad();
//        mCalenderMonthView.resize();
        ((Button)findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCalenderMonthView.loadNextMonth();

            }
        });
        ((Button)findViewById(R.id.previous)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCalenderMonthView.loadLastMonth();

            }
        });
        mCalenderMonthView.setOnMonthCellSelectListener(new CalenderMonthView.OnMonthCellSelectListener() {
            @Override
            public void selectedCell(MonthCell mMonthCell) {
                Log.e("date", mMonthCell.getDateString());
            }
        });

        //dump data
        HashMap<String,List<CEvent>> mCEventMap = new HashMap<String,List<CEvent>>();
        String key = DateFormatter.date_month_year.format(mCalenderMonthView.getCurrentDay().getTime() + 5 * 24 * 60 * 60 * 1000);
        List<CEvent> CEventList = new ArrayList<CEvent>();
        CEvent mCEvent = new CEvent();
        mCEvent.setTitle("testfufkuxsdghjhgfdfdghjfdssgdfghgfdsdfghgfdsdfghgfdsdfgh");
        mCEvent.setColor("E5E5E5");
        CEventList.add(mCEvent);
        mCEventMap.put(key,CEventList);

        mCalenderMonthView.setEvent(mCEventMap);
    }
}
