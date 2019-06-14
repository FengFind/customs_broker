package com.zwu.customs_broker.util;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    private static final String TAG = "DateDeserializer";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json != null) {
            Log.d(TAG, "将为您处理的内容:"+json);
            final String jsonString = json.getAsString();
            try {
                return dateFormat.parse(jsonString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
           final long jsonLong = json.getAsLong();
            try {
                return new Date(jsonLong);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}