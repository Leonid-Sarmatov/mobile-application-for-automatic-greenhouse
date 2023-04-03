package com.example.automatic_greenhouse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageFromServer {
    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("humidity")
    @Expose
    private String airHumidity;

    @SerializedName("temp")
    @Expose
    private String airTemperature;

    @SerializedName("lighting") // humidity soilMoistrue temp
    @Expose
    private String lightingBrightness;

    @SerializedName("soilMoistrue")
    @Expose
    private String soilMoisture;

    public String getError() { return error; }

    public String getLightingBrightness() {
        return lightingBrightness;
    }

    public String getAirHumidity() {
        return airHumidity;
    }

    public String getAirTemperature() {
        return airTemperature;
    }

    public String getSoilMoisture() {
        return soilMoisture;
    }

    public void setError(String error) { this.error = error; }

    public void setLightingBrightness(String lightingBrightness) {
        this.lightingBrightness = lightingBrightness;
    }

    public void setAirHumidity(String airHumidity) {
        this.airHumidity = airHumidity;
    }

    public void setAirTemperature(String airTemperature) {
        this.airTemperature = airTemperature;
    }

    public void setSoilMoisture(String soilMoisture) {
        this.soilMoisture = soilMoisture;
    }
}
