package com.controller;

import com.entity.WeatherResponse;
import com.entity.WeatherAPIProperties;
import java.util.Properties;
import org.json.JSONObject;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.ext.json.*;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import com.google.common.cache.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;

public class WeatherEnquiryController {
        
    private static Cache<String,WeatherResponse> cache = null;
    
    public WeatherEnquiryController(){
        super();
    }
    
    public static synchronized WeatherResponse enquireWeatherDetails(String city){
                
        WeatherResponse result = null;
        
        if(cache == null){
            CacheLoader<String, WeatherResponse> loader;
            loader = new CacheLoader<String, WeatherResponse>() {
		@Override
		public WeatherResponse load(String city) throws Exception {
			return processRequest(city);
		}
           };
           cache = CacheBuilder.newBuilder().expireAfterAccess(3,TimeUnit.SECONDS).build(loader);
        }
                
        try{
            result = cache.get(city);
        }catch(ExecutionException e){
           e.printStackTrace();
        }
        
        return result;
    }
    
    public static WeatherResponse processRequest(String city){ 
                
        //Default result
        WeatherResponse result = new WeatherResponse(-999,-999);
        
        Properties weatherAPIProperties = WeatherAPIProperties.loadProperty();
        
        ClientResource resource = new ClientResource(weatherAPIProperties.getProperty("weatherStackUrl"));

        resource.addQueryParameter("access_key", weatherAPIProperties.getProperty("weatherStackKey"));
        resource.addQueryParameter("query", city);
                
        Representation rep = resource.get(MediaType.APPLICATION_JSON);
        Response response = resource.getResponse();
        
        boolean success = true;
        
        try{
            
            if (response.getStatus().isSuccess()) {
                
                if(rep != null){
                    JsonRepresentation jsonRep = new JsonRepresentation(rep);
                    JSONObject jsonResponse = jsonRep.getJsonObject().getJSONObject("current");                    
                    result = 
                            new WeatherResponse(
                                    jsonResponse.getInt("wind_speed"),
                                    jsonResponse.getInt("temperature"));
                }else{
                    System.out.println("ERROR! - Response Empty");
                    success = false;
                }
                
            } else {
                
                System.out.println("ERROR! Response Status: " + response.getStatus());
                if(rep != null){           
                    System.out.println(rep.getText());
                }
                success = false;
                
            }
        
        }catch (JSONException e){
            System.out.println("ERROR! JSONException " + e.getMessage());
            success = false;
        }catch (Exception e){
            e.printStackTrace();
            success = false;
        }
        
        if(!success){
            result = processOpenWeatherRequest(city);
        }
        
        return result;
    }
    
    public static WeatherResponse processOpenWeatherRequest(String city){
        
        //Default result
        WeatherResponse result = new WeatherResponse(-999,-999);
        
        Properties weatherAPIProperties = WeatherAPIProperties.loadProperty();
        
        ClientResource resource = new ClientResource(weatherAPIProperties.getProperty("openWeatherUrl"));

        //Hardcode city name with country code
        resource.addQueryParameter("q", "melbourne,AU");
        resource.addQueryParameter("appid", weatherAPIProperties.getProperty("openWeatherKey"));
                
        Representation rep = resource.get(MediaType.APPLICATION_JSON);
        Response response = resource.getResponse();
        
        try{
            
            if (response.getStatus().isSuccess()) {
                
                if(rep != null){            
                    JsonRepresentation jsonRep = new JsonRepresentation(rep);
                    
                    JSONObject jsonResponse = jsonRep.getJsonObject();    
                    JSONObject mainJsonResponse = jsonResponse.getJSONObject("main");      
                    JSONObject windJsonResponse = jsonResponse.getJSONObject("wind");
                    
                    result = 
                            new WeatherResponse(
                                    (int) (windJsonResponse.getDouble("speed") * 3.6),
                                    (int) (mainJsonResponse.getDouble("temp") - 273.15));

                }else{
                    System.out.println("ERROR! - Response Empty");
                }
                
            } else {
                
                System.out.println("ERROR! Response Status: " + response.getStatus());
                if(rep != null){           
                    System.out.println(rep.getText());
                }                
            }
        
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return result;
    }
    
}
