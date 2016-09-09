package com.lokman.event_calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lokman.event_calendar.R;
import com.lokman.event_calendar.model.CEvent;
import com.lokman.event_calendar.model.MonthCell;
import com.lokman.event_calendar.model.SpecialDate;
import com.lokman.event_calendar.utility.DateFormatter;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by lokmannicholas on 5/9/16.
 */
public class CalenderMonthView extends RelativeLayout {

    public interface OnMonthCellSelectListener{
        void selectedCell(MonthCell mMonthCell);
    }
    //constant
    public final int ALL_EVENT = -1;
    public final int Default_Cell_Height = 200;
    private int border_size=0;
    private int header_text_color = Color.parseColor("#000000");
    private int border_color= Color.parseColor("#e5e5e5");
    private int cell_color= Color.parseColor("#ffffff");
    private int cell_text_color= Color.parseColor("#000000");
    private int header_color =  Color.parseColor("#ff961b");
    private int monthCellHeight = Default_Cell_Height;
    private int eventTitleSize = 10;
    private int noOfdisplayEvent = ALL_EVENT;

    private LayoutInflater mInflater;
    private GridView gridView;
    private List<MonthCell> monthCellList;
    private HashMap<String,List<CEvent>> mCEventMap;
    private MonthCellAdapter mMonthCellAdapter;
    private Context context;
    private TextView txt_title;
    private Date startDate,calendarStartDay;
    private Date endDate,calendarEndDay;
    private Date today;
    private int weekday,num_of_row;
    private Calendar mCalendar = Calendar.getInstance();

    public CalenderMonthView(Context context) {
        super(context);
        this.context = context;
        init();

    }
    public CalenderMonthView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;

        init();
    }
    public CalenderMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }


    private void init()
    {
        today = new Date();
        monthCellList = new ArrayList<MonthCell>();

        mCEventMap = new HashMap<String,List<CEvent>>();
        mInflater = LayoutInflater.from(context);
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

        calendar.add(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        this.calendarEndDay = calendar.getTime();

        setMonth(month, year);

        resize();
    }

    /****** Listener *****/
    public void setOnMonthCellSelectListener(final OnMonthCellSelectListener mOnMonthCellSelectListener){

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                mOnMonthCellSelectListener.selectedCell(monthCellList.get(position));

            }
        });
    }
    /******** DataSetting ********/
    public void setDisplayEventSize(int noOfdisplayEvent){
        this.noOfdisplayEvent = noOfdisplayEvent;
    }
    public String getMonthTitle(){
        return String.format(context.getResources().getConfiguration().locale, "%tB", mCalendar) ;
    }
    public String getYear(){
        return mCalendar.get(Calendar.YEAR)+"";
    }
    private void setMonth(int month, int year){
        monthCellList.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        weekday = calendar.get(Calendar.DAY_OF_WEEK) -1; //weekday start from 1


        //get min date of month
        startDate = calendar.getTime();

        //get max date of month
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        endDate = calendar.getTime();

        mCalendar = calendar;
        setCalendarWeekTitle();
        for(int i=0;i<weekday;i++){
            monthCellList.add(new MonthCell());
        }

        HashMap<String,List<CEvent>> holidays = SpecialDate.getInstance(context).getAllSpecialDate();
        int numDays = calendar.getActualMaximum(Calendar.DATE);
        for(int i=0;i<numDays;i++){
            MonthCell mMonthCell =new MonthCell();
            mMonthCell.setType(1);
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_YEAR, i);
            mMonthCell.setDate(calendar.getTime());

            if(!holidays.isEmpty() && holidays.get(mMonthCell.getDateString())!=null){
                mMonthCell.setEvents(holidays.get(mMonthCell.getDateString()));
                mMonthCell.isHoliday(true);
            }
            if(!this.mCEventMap.isEmpty() && this.mCEventMap.get(mMonthCell.getDateString())!=null){
                mMonthCell.setEvents(this.mCEventMap.get(mMonthCell.getDateString()));
            }
            monthCellList.add(mMonthCell);
        }
        int last_cells = 7 - monthCellList.size()%7;
        for(int i=0;i<last_cells;i++){
            monthCellList.add(new MonthCell());
        }

        mMonthCellAdapter =new MonthCellAdapter();
        gridView.setAdapter(mMonthCellAdapter);

        txt_title.setText(String.format(getResources().getConfiguration().locale, "%tB", calendar) + year);
        num_of_row = (int)((numDays + weekday)%7 ==0 ? Math.round((numDays + weekday) / 7 ):Math.round((numDays + weekday) / 7 +0.5));
//        MonthCellHeight =

//        (int) (100 + num_of_row * monthCellHeight)));
        mMonthCellAdapter.notifyDataSetChanged();
        resize();
    }
    public void resize(){
        ViewGroup.LayoutParams param = gridView.getLayoutParams();
        if(param != null){
            param.height =  (100 + num_of_row * monthCellHeight);
            gridView.setLayoutParams(param);
        }

    }
    private void setCalendarWeekTitle(){
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
// f
        String[] dayNames = symbols.getShortWeekdays();
        if(monthCellList.size()>7){
            for(int i=1;i< dayNames.length;i++){
                MonthCell mMonthCell =new MonthCell();
                mMonthCell.setType(-1);
                mMonthCell.setTitle(dayNames[i]);
                monthCellList.set(i-1,mMonthCell);
            }
        }else{
            for(int i=1;i< dayNames.length;i++){
                MonthCell mMonthCell =new MonthCell();
                mMonthCell.setType(-1);
                mMonthCell.setTitle(dayNames[i]);
                monthCellList.add(mMonthCell);
            }
        }

    }
    public void setMonthCellHeight(int height){
        this.monthCellHeight = height;

        mMonthCellAdapter.notifyDataSetChanged();
        resize();
    }
    public void loadLastMonth(){
        //reach minimum
        if(startDate.getTime() <= calendarStartDay.getTime()){
            //do smth
        }else {
            mCalendar.add(Calendar.MONTH, -1);
            this.setMonth(mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR));

        }
    }
    public void loadNextMonth(){
        // reach maximum
        if(endDate.getTime() >= calendarEndDay.getTime()){
            //do smth
        }else {
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

    public void setEvent(HashMap<String,List<CEvent>> mCEvent ){
        if(  this.mCEventMap == null){
            this.mCEventMap = new HashMap<>();
        }
        for(String key : mCEvent.keySet()){
            if(this.mCEventMap.containsKey(key)){
                this.mCEventMap.get(key).addAll(mCEvent.get(key));
            }else{
                this.mCEventMap.put(key,mCEvent.get(key));
            }
        }

        for(int i =0;i<monthCellList.size() ; i++){
            Log.e("monthCellList",monthCellList.get(i).getDateString());
            if(mCEvent.get(monthCellList.get(i).getDateString())!=null){

                monthCellList.get(i).setEvents(mCEvent.get(monthCellList.get(i).getDateString()));

            }
        }
        mMonthCellAdapter.notifyDataSetChanged();
    }

    public void reload(){
        txt_title.setText(String.format(getResources().getConfiguration().locale, "%tB", mCalendar) + mCalendar.get(Calendar.YEAR));
        SpecialDate.getInstance(context).reloadLanguage();
        setMonth(mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.YEAR));
    }
    public void setCurrent(Date today){
        this.today = today;
    }

    public Date getCurrentDay(){return this.today;}
    public void setEventTitleSize(int size){
        eventTitleSize = size;
    }


    public void setHeaderColor(String hexcode){
        header_color= Color.parseColor("#" + hexcode);
    }
    public void setHeaderColor(int r,int g,int b){
        header_color= Color.rgb(r, g, b);
    }
    public void setHeaderColor(int a,int r,int g,int b){
        header_color= Color.argb(a, r, g, b);
    }
    public void setHeaderTextColor(String hexcode){
        header_text_color= Color.parseColor("#" + hexcode);
    }
    public void setHeaderTextColor(int r,int g,int b){
        header_text_color= Color.rgb(r, g, b);
    }
    public void setHeaderTextColor(int a,int r,int g,int b){
        header_text_color= Color.argb(a, r, g, b);
    }


    public void setCellBorder(int border_size){
        border_size = border_size;
    }
    public void setCellBorderColor(String hexcode){
        border_color= Color.parseColor("#" + hexcode);
    }
    public void setCellBorderColor(int r,int g,int b){
        border_color= Color.rgb(r, g, b);
    }
    public void setCellBorderColor(int a,int r,int g,int b){
        border_color= Color.argb(a,r, g, b);
    }
    public void setCellColor(String hexcode){
        cell_color= Color.parseColor("#" + hexcode);
    }
    public void setCellColor(int r,int g,int b){
        cell_color= Color.rgb(r, g, b);
    }
    public void setCellColor(int a,int r,int g,int b){
        cell_color= Color.argb(a,r, g, b);
    }
    public void setCellTextColor(String hexcode){
        cell_text_color= Color.parseColor("#" + hexcode);
    }
    public void setCellTextColor(int r,int g,int b){
        cell_text_color= Color.rgb(r, g, b);
    }
    public void setCellTextColor(int a,int r,int g,int b){
        cell_text_color= Color.argb(a,r, g, b);
    }
    /******** MonthCellAdapter ********/
    protected class MonthCellAdapter extends BaseAdapter {

        class ViewHolder {
            TextView txt_monthday;
            LinearLayout ll_dayEvent;
            RelativeLayout rl_cell;
        }
        public MonthCellAdapter() {
            super();
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder viewHolder = null;
            MonthCell mMonthCell = monthCellList.get(position);

            //get view
            if (convertView == null ) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.month_cell, null);
                viewHolder.txt_monthday=(TextView) convertView.findViewById(R.id.txt_monthday);
                viewHolder.ll_dayEvent=(LinearLayout) convertView.findViewById(R.id.ll_dayEvent);
                viewHolder.rl_cell = (RelativeLayout) convertView.findViewById(R.id.rl_cell);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            if(mMonthCell.getType() == -1){
                convertView.setClickable(false);
                viewHolder.txt_monthday.setTextColor(header_text_color);
                viewHolder.txt_monthday.setText(mMonthCell.getDayOfMonth());
                viewHolder.txt_monthday.setEnabled(false);
                viewHolder.txt_monthday.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                viewHolder.txt_monthday.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                viewHolder.rl_cell.setBackgroundColor(header_color);

            }else if(mMonthCell.getType() == 0){
                convertView.setClickable(false);
                viewHolder.txt_monthday.setEnabled(false);
                viewHolder.rl_cell.setBackgroundColor(cell_color);
                viewHolder.ll_dayEvent.setBackgroundColor(cell_color);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, monthCellHeight));

            }else if(mMonthCell.getType() == 1){
                viewHolder.ll_dayEvent.removeAllViews();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_YEAR, position - (7 + weekday));
                for(int i=0;i<(noOfdisplayEvent==-1? mMonthCell.getEvents().size():noOfdisplayEvent);i++){
                    viewHolder.ll_dayEvent.addView(getEventBar(mMonthCell.getEvents().get(i)));
                }
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, monthCellHeight);

                viewHolder.txt_monthday.setText(mMonthCell.getDayOfMonth());
                viewHolder.txt_monthday.setBackgroundColor(cell_color);
                if(calendar.get(Calendar.DAY_OF_WEEK)==1 || mMonthCell.isHoliday()){
                    viewHolder.txt_monthday.setTextColor(Color.RED);
                }else{
                    viewHolder.txt_monthday.setTextColor(cell_text_color);
                }

                viewHolder.ll_dayEvent.setBackgroundColor(cell_color);
                viewHolder.rl_cell.setBackgroundColor(border_color);
                convertView.setBackgroundColor(border_color);
                if(border_size>0){
                    RelativeLayout.LayoutParams rllp = (RelativeLayout.LayoutParams) viewHolder.rl_cell.getLayoutParams();
                    rllp.setMargins(0,0,border_size,border_size);
                    viewHolder.rl_cell.setLayoutParams(rllp);
                }

                convertView.setLayoutParams(params);

            }
            return convertView;
        }
        private TextView getEventBar(CEvent mCEvent){
            TextView event_title = new TextView(context);
            event_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            event_title.setText(mCEvent.getTitle());
            event_title.setTextSize(eventTitleSize);
            event_title.setMaxLines(1);
            event_title.setBackgroundColor(mCEvent.getColor());
            return event_title;
        }
        @Override
        public int getCount() {
            return monthCellList.size();
        }

        @Override
        public Object getItem(int position) {
            return monthCellList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }
}
