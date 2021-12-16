package com.entity;

public class WeatherResponse {

	private final int wind_speed;
	private final int temperature_degrees;

	public WeatherResponse(int wind_speed, int temperature_degrees) {
		this.wind_speed = wind_speed;
		this.temperature_degrees = temperature_degrees;
	}

    public int getWind_speed() {
        return wind_speed;
    }

    public int getTemperature_degrees() {
        return temperature_degrees;
    }
    
    public String toString(){
        
        StringBuilder sb = new StringBuilder();
        sb.append("wind_speed: ");
        sb.append(wind_speed);
        sb.append(", temperature_degrees: ");
        sb.append(temperature_degrees);
        
        return sb.toString();
    }
    

}