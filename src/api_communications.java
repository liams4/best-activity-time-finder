import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// This class contains methods relating to the 

public class api_communications {
	public static void main(String[] args) throws FileNotFoundException, JSONException {
		
		JSONObject a = getWeatherForecast(new JSONObject("{lng:-122.3321, lat:47.6062}"));
		System.out.print(a);
		// if hot look at min temp and rain - above 75
		// if cold look at max temp - under 35
		// if mild look at min and max temp and rain - 35 - 75
		// if indoor look if it's sunny amd see if it's 50-75 and then 
		//    suggest to go to outside
		double[] temps = new double[30];
		double[] times = new double[30];
		String[] weatherConditions = new String[30];
		JSONArray weatherForecastInfo = a.getJSONArray("list");
		int count = 0;
		for (Object pointInTime: weatherForecastInfo) {
			JSONObject pointInTimeAsJSON = (JSONObject)pointInTime;
			String dateAndTime = pointInTimeAsJSON.getString("dt_txt");
			String hour = dateAndTime.substring(dateAndTime.indexOf(' '), dateAndTime.indexOf(':'));
			if (hour != "00" || hour != "03") {
				temps[count] = convertKelvinToFahrenheit(pointInTimeAsJSON.getJSONObject("main").getDouble("temp"));
				times[count] = pointInTimeAsJSON.getDouble("dt");
				weatherConditions[count] = pointInTimeAsJSON.getJSONArray("weather").getJSONObject(0).getString("main");
			}
		}
	}
	
	public static JSONObject getCityLatLong(String cityAndStateName) throws FileNotFoundException {
		cityAndStateName = cityAndStateName.replaceAll(" ", "");
		String geocodeKey = getGeocodeAPIKey();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet findCityCoordinatesRequest = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address=" +
														cityAndStateName + "&key=" + geocodeKey);
		
		JSONObject latLong = null;
		try {
			CloseableHttpResponse response = client.execute(findCityCoordinatesRequest);
			String responseAsString = EntityUtils.toString(response.getEntity());
			JSONObject responseAsJSON = new JSONObject(responseAsString);
			latLong = (responseAsJSON.getJSONArray("results").getJSONObject(0).
					   getJSONObject("geometry").getJSONObject("location"));
			response.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latLong;
	}
	
	// Returns JSONObject containing the 3-day weather forecast for the location passed as a
	// parameter (as a longitude and a latitude).
	public static JSONObject getWeatherForecast(JSONObject latLong) throws FileNotFoundException {
		double lat = latLong.getDouble("lat");
		double long_ = latLong.getDouble("lng");
		String weatherKey = getWeatherAPIKey();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet weatherForecastRequest = new HttpGet("https://api.openweathermap.org/data/2.5/forecast?lat=" +
													lat + "&lon=" + long_ + "&appid=" + weatherKey);
		
		JSONObject responseAsJSON = null;
		try {
			CloseableHttpResponse response = client.execute(weatherForecastRequest);
			String responseAsString = EntityUtils.toString(response.getEntity());
			responseAsJSON = new JSONObject(responseAsString);
			response.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseAsJSON;
	}
	
	// Returns the API key as a String for the Google API.
	public static String getGeocodeAPIKey() throws FileNotFoundException {
		Scanner input = new Scanner(new File("/Users/liam/PersonalProjects/geocode_API_key.txt"));
		String key = input.nextLine();
		input.close();
		
		return key;
	}
	
	// Returns the API key as a String for the OpenWeather API.
	public static String getWeatherAPIKey() throws FileNotFoundException {
		Scanner input = new Scanner(new File("/Users/liam/PersonalProjects/weather_API_key.txt"));
		String key = input.nextLine();
		input.close();
		
		return key;
	}
	
	public static double convertKelvinToFahrenheit(double kelvinTemp) {
		return (kelvinTemp - 273.15) * 1.8 + 32;
	}
}
