package com.example.oppai.top10;

public class Country {
    private String Name;
    private String Url;
    private String code;

    public Country(String name, String url, String code) {
        Name = name;
        Url = url;
        this.code = code;
    }

    public String getName() {
        return Name;
    }

    public String getUrl() {
        return Url;
    }

    public String getCode() {
        return code;
    }
}
