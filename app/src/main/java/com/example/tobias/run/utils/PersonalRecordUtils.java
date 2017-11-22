package com.example.tobias.run.utils;

import com.example.tobias.run.data.PersonalRecord;
import com.example.tobias.run.data.SharedPreferenceRepository;

/**
 * Created by Tobi on 11/8/2017.
 */

public class PersonalRecordUtils {

    private static String getDistanceUnit(SharedPreferenceRepository sharedPref){
        return sharedPref.get(SharedPreferenceRepository.DISTANCE_UNIT_KEY);
    }

    public static String getPaceText(PersonalRecord currentItem, SharedPreferenceRepository sharedPref){
        String paceText;
        if (getDistanceUnit(sharedPref).equals("mi")){
            paceText = ConversionUtils.paceToString(currentItem.getRun().getMilePace(), "mi");
        } else {
            paceText = ConversionUtils.paceToString(currentItem.getRun().getKmPace(), "km");
        }

        return paceText;
    }

    public static String getValueText(PersonalRecord currentItem, SharedPreferenceRepository sharedPref){
        String text;
        String distanceUnit = getDistanceUnit(sharedPref);

        if (distanceUnit.equals("mi")){
            text = ConversionUtils.paceToString(currentItem.getRun().getMilePace(), "mi");
        } else {
            text = ConversionUtils.paceToString(currentItem.getRun().getKmPace(), "km");
        }


        return text;
    }

    public static String getDateText(PersonalRecord personalRecord){
        return ConversionUtils.dateToString(personalRecord.getRun().getDate());
    }
}
