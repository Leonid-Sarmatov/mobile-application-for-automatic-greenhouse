package com.example.automatic_greenhouse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;

public class MessageToServer {
    @SerializedName("port21On")
    @Expose
    private boolean port21On;

    @SerializedName("timeOnLighting")
    @Expose
    private Time timeOnLighting;

    @SerializedName("timeOffLighting")
    @Expose
    private Time timeOffLighting;

    @SerializedName("minimumSoilMoisture")
    @Expose
    private int minimumSoilMoisture;

    public MessageToServer(int minimumSoilMoisture, Time timeOffLighting,
                           Time timeOnLighting, boolean port21On) {
        this.minimumSoilMoisture = minimumSoilMoisture;
        this.timeOffLighting = timeOffLighting;
        this.timeOnLighting = timeOnLighting;
        this.port21On = port21On;
    }

    public int getMinimumSoilMoisture() {
        return minimumSoilMoisture;
    }

    public Time getTimeOffLighting() {
        return timeOffLighting;
    }

    public Time getTimeOnLighting() {
        return timeOnLighting;
    }

    public boolean isPort21On() {
        return port21On;
    }

    public void setPort21On(boolean port21On) {
        this.port21On = port21On;
    }

    public void setMinimumSoilMoisture(int minimumSoilMoisture) { this.minimumSoilMoisture = minimumSoilMoisture; }

    public void setTimeOffLighting(Time timeOffLighting) { this.timeOffLighting = timeOffLighting; }

    public void setTimeOnLighting(Time timeOnLighting) {
        this.timeOnLighting = timeOnLighting;
    }
}
