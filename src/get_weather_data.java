import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.json.*;

public class get_weather_data {
	public static final String[] waterActivities = {"surf", "swim", "boat", "scuba-dive", "dive", "fish"};
	public static final String[] snowActivities = {"ski", "snow", "sled", "ice", "cold", "freeze", "frozen"};
	public static final String[] otherOutdoorActivities = {"walk", "run", "bike", "bicycle",
														  "cycle", "hike", "sport", "yard", "sun"};
	public static final String[] indoorActivities = {"read", "watch", "tv", "movie", "study", "work",
													"sleep", "rest", "gaming", "games", "listen",
													"music", "sing", "dance", "eat", "drink"};
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		System.out.println("Hello! Welcome to ActivityFinder. Please enter an activity you'd like"
						   + "to do and the location of the activity and I will tell you the best"
						   + "day to do it in the near future!");
		System.out.print("Please enter the nearest city of the activity (City, State): ");
		String city = console.nextLine();
		//JSONObject latLong = api_communications.getCityLatLong(city);
		//JSONObject weather = api_communications.getWeatherForecast(latLong);
		System.out.print("Please enter your activity (please be very general! Example: surf, hike): ");
		String activity = console.next();
		
		while (!Arrays.asList(waterActivities).contains(activity) && 
			   !Arrays.asList(snowActivities).contains(activity) && 
			   !Arrays.asList(otherOutdoorActivities).contains(activity) &&
			   !Arrays.asList(indoorActivities).contains(activity)) {
			System.out.print("Sorry, we don't have that activity. Please try a similar activity: ");
			activity = console.next();
		}
		String activityType = getActivityType(activity);
		// plot the data 
		// inform the user 

	}
	
	public static String getActivityType(String activity) {
		if (Arrays.asList(indoorActivities).contains(activity)) {
			return "indoor";
		} else if (Arrays.asList(otherOutdoorActivities).contains(activity)) {
			return "outdoor";
		} else if (Arrays.asList(waterActivities).contains(activity)) {
			return "water";
		}
		return "snow";
	}

}
