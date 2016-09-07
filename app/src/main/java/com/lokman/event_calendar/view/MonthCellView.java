package com.lokman.event_calendar.view;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lokman.event_calendar.model.CEvent;

import java.util.Date;

/**
 * Created by lokmannicholas on 5/9/16.
 */
public class MonthCellView extends CEvent {

    TextView txt_monthday;
    TextView txt_weekday;
    LinearLayout ll_dayEvent;
    Date dateOfMonth = new Date();
}
