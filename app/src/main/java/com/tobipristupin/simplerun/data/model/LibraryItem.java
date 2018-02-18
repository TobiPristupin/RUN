package com.tobipristupin.simplerun.data.model;

/**
 * Data model class used to represent a library used in the project. Used for Recycler View in LibraryItemsActivityView
 */

public class LibraryItem {

    private String name;
    private String url;

    public LibraryItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
