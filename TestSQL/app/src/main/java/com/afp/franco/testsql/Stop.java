package com.afp.franco.testsql;

/**
 * Created by franco on 31.10.14.
 */
public class Stop {
    private int _id;
    private String _lon;
    private String _lat;
    private String _name;

    public int get_id()
    {
        return _id;
    }

    public String get_lon()
    {
        return _lon;
    }

    public String get_lat()
    {
        return _lat;
    }

    public String get_name()
    {
        return _name;
    }

    public void set_id(int id)
    {
        this._id = id;
    }

    public void set_lon(String lon)
    {
        this._lon = lon;
    }

    public void set_lat(String lat)
    {
        this._lat = lat;
    }

    public void set_name(String name)
    {
        this._name = name;
    }

    @Override
    public String toString()
    {
        return _name;
    }
}