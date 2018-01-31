package com.example.tobias.run.data;

/**
 * Data model class used to represent a library used in the project. Used for Recycler View in LibraryActivityView
 */

public class Library {

    private String name;
    private String url;

    public Library(String name, String url) {
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
