import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.sun.xml.internal.ws.util.StringUtils;

public class WeatherForecast {
	private double[] forecastedTemps;
	private double[] forecastTimes;
	private String[] forecastedWeatherConditions;
	private String city;

	public WeatherForecast(String city) {
		this.forecastedTemps = new double[30];
		this.forecastTimes = new double[30];
		this.forecastedWeatherConditions  = new String[30];
		this.city = city;
	}
	
	public void updateForecast() throws FileNotFoundException {	
		JSONObject weatherForecast = getWeatherForecast(this.city);
		JSONArray forecastsAtDifferentTimes = weatherForecast.getJSONArray("list");
		
		int count = 0;		
		for (Object pointInTime: forecastsAtDifferentTimes) {
			JSONObject pointInTimeAsJSON = (JSONObject)pointInTime;
			String dateAndTime = pointInTimeAsJSON.getString("dt_txt");
			String hour = dateAndTime.substring(dateAndTime.indexOf(' ') + 1, dateAndTime.indexOf(':'));
			if (!hour.equals("00") && !hour.equals("03")) {
				this.forecastedTemps[count] = getTempInFahrenheit(pointInTimeAsJSON.getJSONObject("main").getDouble("temp"));
				this.forecastTimes[count] = pointInTimeAsJSON.getDouble("dt");
				this.forecastedWeatherConditions[count] = 
					pointInTimeAsJSON.getJSONArray("weather").getJSONObject(0).getString("main");
				count++;
			} 
		}
	}
	
	public double[] getForecastedTemps() {
		return this.forecastedTemps;
	}
	
	public double[] getForecastTimes() {
		return this.forecastTimes;
	}
	
	public String[] getForecastedWeatherConditions() {
		return this.forecastedWeatherConditions;
	}
	
	public String getCity() {
		return StringUtils.capitalize(this.city.substring(0, this.city.indexOf(',')));
	}
	
	// Returns a JSONObject containing the latitude and longitude of the city passed as a parameter.
	public static JSONObject getCityLatLong(String city) throws FileNotFoundException {
		String cityAndStateName = city.replaceAll(" ", "");
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
	public static JSONObject getWeatherForecast(String city) throws FileNotFoundException {
		JSONObject latLong = getCityLatLong(city);
		double lat = latLong.getDouble("lat");
		double long_ = latLong.getDouble("lng");
		String weatherKey = getWeatherAPIKey();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet weatherForecastRequest = new HttpGet("https://api.openweathermap.org/data/2.5/forecast?lat=" +
													lat + "&lon=" + long_ + "&appid=" + weatherKey);
		
		JSONObject weatherForecast = null;
		try {
			CloseableHttpResponse response = client.execute(weatherForecastRequest);
			String responseAsString = EntityUtils.toString(response.getEntity());
			weatherForecast = new JSONObject(responseAsString);
			
			response.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weatherForecast;
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
	
	public static double getTempInFahrenheit(double kelvinTemp) {
		return (kelvinTemp - 273.15) * 1.8 + 32;
	}
}
