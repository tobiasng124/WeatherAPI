package com.restservice;

import com.controller.WeatherEnquiryController;
import com.entity.WeatherResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherRequestHandler {

	@GetMapping("/v1/weather")
	public WeatherResponse enquireWeather(@RequestParam(value = "name", defaultValue = "") String name) {
                
            WeatherEnquiryController weatherControl = new WeatherEnquiryController();
            WeatherResponse weatherResponse = WeatherEnquiryController.enquireWeatherDetails(name);
            return weatherResponse;
            
	}
}