package com.entity;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author tobias
 */
public class WeatherAPIProperties {

    public static String PROPERTY_FILE_NAME = "WeatherAPI.properties";
        
    private static Properties prop = null;
    
    public static synchronized Properties loadProperty(){
        
        if(prop == null){
        
            try {

                prop = new Properties();

                // load a properties file
                prop.load(WeatherAPIProperties.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
        }
        
        return prop;
        
    }
       
}
