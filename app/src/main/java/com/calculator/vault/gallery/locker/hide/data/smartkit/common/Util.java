package com.calculator.vault.gallery.locker.hide.data.smartkit.common;

import android.content.Context;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.City;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Continent;

import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

public class Util {
    public static final String TAG = "Util";

    public static List<Continent> getContinentList(Context context) {
        int i;
        List<Continent> continentList = new Vector();
        String[] continentNameList = context.getResources().getStringArray(R.array.continent);
        for (i = 0; i < continentNameList.length; i++) {
            Continent continent = new Continent();
            continent.setId(i);
            continent.setName(continentNameList[i]);
            continent.setCityList(new Vector());
            continentList.add(continent);
        }
        String[] availableIDs = TimeZone.getAvailableIDs();

        for (i = 0; i < availableIDs.length; i++) {

            Log.e("pooja", "getContinentList: " + availableIDs );
            for (int j = 0; j < continentNameList.length; j++) {
                Log.e("pooja", "getContinentList: " + continentNameList );

                if (availableIDs[i].startsWith(continentNameList[j])) {
                    City city = new City();
                    city.setId(((Continent) continentList.get(j)).getCityList().size());
                    city.setContinentId(j);
                    city.setName(availableIDs[i].replace(continentNameList[j] + "/", "").replace("_", " "));

                    Log.e("zxcv", "getContinentList: " +availableIDs[i] );
                    Log.e("zxcv", "getContinentList: "+ availableIDs[i].replace(continentNameList[j] + "/", "").replace("_", " ") );
                    city.setTimeZoneId(availableIDs[i]);
                    ((Continent) continentList.get(city.getContinentId())).getCityList().add(city);
                    break;
                }
            }
        }
        return continentList;
    }

    public static String getDisplay(String timeZoneId) {
        return timeZoneId.substring(timeZoneId.indexOf("/") + 1).replace("_", " ");
    }
}
