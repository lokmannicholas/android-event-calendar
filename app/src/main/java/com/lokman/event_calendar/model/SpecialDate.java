package com.lokman.event_calendar.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by lokmannicholas on 9/9/16.
 */
public class SpecialDate {

    private static SpecialDate instance;
    private Context context;
    private HashMap<String,List<CEvent>> mCEventMap = new HashMap<String,List<CEvent>>();

    public static SpecialDate getInstance(Context context)
    {
        if (instance == null)
        {
            // Create the instance
            instance = new SpecialDate(context);
        }
        return instance;
    }
    public SpecialDate reloadLanguage(){
        instance = new SpecialDate(context);
        return this;
    }
    private SpecialDate(Context context){
        this.context = context;
        try{
            String language = Locale.getDefault().getLanguage();
            JSONObject specialDateJSON = new JSONObject(loadJSONFromAsset());
            JSONObject holiday = specialDateJSON.getJSONObject("public holiday");
            JSONObject holiday_of_local = holiday.getJSONObject(language);
            Iterator<String> keysItr = holiday_of_local.keys();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                String value = holiday_of_local.getString(key);
                CEvent mCEvent = new CEvent();
                mCEvent.setTitle(value);
                List<CEvent> ceventList = new ArrayList<CEvent>();
                ceventList.add(mCEvent);
                this.mCEventMap.put(key, ceventList);
            }
        }catch(Exception exp){
            Log.e("SpecialDate error :",exp.getMessage());
        }

    }
    public HashMap<String,List<CEvent>> getAllSpecialDate(){
        return this.mCEventMap;
    }

    private String loadJSONFromAsset() throws IOException {
        String json = null;
        try {

            InputStream is = context.getAssets().open("special_date.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            String value = object.getString(key);
            map.put(key, value);
        }
        return map;
    }

}
