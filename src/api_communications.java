import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

// This class contains methods relating to the 

public class api_communications {
		
	public static void getCityLongLat(String cityAndStateName) throws FileNotFoundException { 
		String geocodeKey = getGeocodeAPIKey();
		HttpClient client = HttpClients.createDefault();
		HttpGet findCityCoordinatesRequest = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address=springfield,OH&key=" + geocodeKey);
		
			
		try {
			//HttpResponse response = client.execute(weatherForecastRequest);
			//String responseAsString = EntityUtils.toString(response.getEntity());
			//HttpResponse response = client.execute(weatherForecastRequest);
			//String responseAsString = EntityUtils.toString(response.getEntity());
			HttpResponse response = client.execute(findCityCoordinatesRequest);
			String responseAsString = EntityUtils.toString(response.getEntity());
			JSONObject responseAsJSON = new JSONObject(responseAsString);
			System.out.println(responseAsJSON);
			//System.out.print(responseAsJSON.getJSONArray("list"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getWeatherForecast() throws FileNotFoundException {
		String weatherKey = getWeatherAPIKey();
		HttpGet weatherForecastRequest = new HttpGet("https://api.openweathermap.org/data/2.5/forecast?q=Seattle,us&appid=" + weatherKey);
	}
	
	
	// Returns the API key as a String for the Google API.
	public static String getGeocodeAPIKey() throws FileNotFoundException {
		Scanner input = new Scanner(new File("/Users/liam/PersonalProjects/weather-charts/geocode_API_key.txt"));
		String key = input.nextLine();
		input.close();
		
		return key;
	}
	
	// Returns the API key as a String for the OpenWeather API.
	public static String getWeatherAPIKey() throws FileNotFoundException {
		Scanner input = new Scanner(new File("/Users/liam/PersonalProjects/weather-charts/weather_API_key.txt"));
		String key = input.nextLine();
		input.close();
		
		return key;
	}
}
