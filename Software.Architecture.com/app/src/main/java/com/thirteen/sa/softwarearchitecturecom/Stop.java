package com.thirteen.sa.softwarearchitecturecom;

import java.io.Serializable;

/**
 * Created by franco on 01.11.14.
 */

// Represents one single Stop (for testing purpose only name)
public class Stop {

    private String _name;
    private String _lat;
    private String _lon;

    public Stop(String name, String lat, String lon)
    {
        _name = name; _lat = lat; _lon = lon;
    }
    public Stop(String name)
    {
        _name = name; _lat = ""; _lon = "";
    }
    public String get_name()
    {
        return _name;
    }
    public String get_lat()
    {
        return _lat;
    }
    public String get_lon()
    {
        return _lon;
    }
}
