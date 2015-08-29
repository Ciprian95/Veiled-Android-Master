package com.Veiled.SqlConnection.Tables;

import java.util.Date;

public class Campaign {
    public String Name;
    public String id;
    public String campaign_id;
    public Date start_date;
    public Date end_date;
    public double radius;
    public double latitude;
    public double longitude;


    public double alive_time;
    public double dead_time;
    public double preference_id;

    public String location;
    public String description;

    public String image_id;


    public Campaign(String name, String id, String companyId,
                    Date startDate, Date endDate){
        this.Name = name;
        this.id = id;
        this.campaign_id = companyId;
        this.start_date = startDate;
        this.end_date = endDate;
    }
}
