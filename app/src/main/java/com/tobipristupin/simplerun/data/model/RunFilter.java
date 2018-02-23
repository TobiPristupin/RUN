package com.tobipristupin.simplerun.data.model;


import android.content.res.Resources;

import com.tobipristupin.simplerun.R;

/**
 * Enum to represent different date filters that can be applied to sort Runs
 */

public enum RunFilter {
    WEEK(R.string.all_week), MONTH(R.string.all_month), YEAR(R.string.all_year), ALL(R.string.all_all);

    private int resId;

    RunFilter(int resId) {
        this.resId = resId;
    }

    public static RunFilter fromResId(int resId) {
        for (RunFilter filter : RunFilter.values()){
            if (filter.getResId() == resId){
                return filter;
            }
        }

        throw new IllegalArgumentException("resId doesn't exist");
    }

    public static RunFilter fromString(String value, Resources resources){
        for (RunFilter filter : RunFilter.values()){
            String stringFromResources = resources.getString(filter.getResId());

            if (value.equalsIgnoreCase(stringFromResources)){
                return filter;
            }
        }

        throw new IllegalArgumentException("Value doesn't exist");
    }

    public String toStringLocalized(Resources resources) {
        return resources.getString(resId);
    }

    public int getResId(){
        return resId;
    }
}
