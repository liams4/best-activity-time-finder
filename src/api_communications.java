import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

// This class contains methods relating to the 

public class api_communications {
	public static void main(String[] args){
		System.out.println(getCityLatLong("seattle, wa"));
		
	}
	
	public static int getCityId() throws FileNotFoundException, IOException {
		String cityList = new String(Files.readAllBytes(Paths.get("/Users/liam/PersonalProjects/city_list.json")));
		JSONArray a = new JSONArray(cityList);
		
		double lat = 47.6;
		double lon = -122.33;
		for (int i = 0; i < a.length(); i++) {
			JSONObject b = a.getJSONObject(i).getJSONObject("coord");
			JSONObject c = a.getJSONObject(i);
			if (b.getDouble("lat") > lat - .1 && b.getDouble("lat") < lat + .1 && b.getDouble("lon") < lon + .1 && b.getDouble("lon") > lon - .1) {
				System.out.print(c.getInt("id"));
				System.out.print(c);
				break;
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
	
	// Returns JSONObject containing the 3-day weather forecast for the city whose id is
	// passed as a parameter.
	public static JSONObject getWeatherForecast(String cityId) throws FileNotFoundException {
		String weatherKey = getWeatherAPIKey();
		HttpGet weatherForecastRequest = new HttpGet("https://api.openweathermap.org/data/2.5/forecast?id=" +
													cityId + "&appid=" + weatherKey);
		CloseableHttpClient client = HttpClients.createDefault();
		
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
}
