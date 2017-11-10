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
            paceText = ConversionManager.paceToString(currentItem.getMilePace(), "mi");
        } else {
            paceText = ConversionManager.paceToString(currentItem.getKilometerPace(), "km");
        }

        return paceText;
    }

    public static String getValueText(PersonalRecord currentItem, SharedPreferenceRepository sharedPref){
        String text;
        String distanceUnit = getDistanceUnit(sharedPref);

        if (currentItem.showDistanceAsValue()){
            if (distanceUnit.equals("mi")){
                text = ConversionManager.distanceToString(currentItem.getDistanceMiles(), "mi");
            } else {
                text = ConversionManager.distanceToString(currentItem.getDistanceKilometers(), "km");
            }
        } else {
            if (distanceUnit.equals("mi")){
                text = ConversionManager.paceToString(currentItem.getMilePace(), "mi");
            } else {
                text = ConversionManager.paceToString(currentItem.getKilometerPace(), "km");
            }
        }

        return text;
    }

    public static String getDateText(PersonalRecord personalRecord){
        return ConversionManager.dateToString(personalRecord.getDate());
    }
}
