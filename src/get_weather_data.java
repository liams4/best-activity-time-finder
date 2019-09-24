import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.json.*;

public class get_weather_data {
	public static final String[] indoorActivities = {"read", "watch", "tv", "movie", "study", "work",
													"sleep", "rest", "gaming", "games", "listen",
													"music", "sing", "dance", "eat", "drink"};
	public static final String[] mildActivities = {"walk", "run", "bike", "bicycle",
												  "cycle", "hike", "sport", "yard"};
	public static final String[] hotActivities = {"surf", "swim", "boat", "dive", "fish", "sun"};
	public static final String[] coldActivities = {"ski", "snow", "sled", "ice", "cold", "freeze", "frozen"};


	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		System.out.println("Welcome to ActivityFinder. Please enter an activity you'd like "
						   + "to do and the location of the activity and I will tell you the best "
						   + "time to do it in the near future (based on weather)!");
		System.out.print("Please enter the nearest city of the activity (City, State): ");
		String city = console.nextLine();
		System.out.print("Please enter your activity (please be very general and use just one word!"
						 + "Example: surf, hike): ");
		String activity = console.next();
		while (!Arrays.asList(indoorActivities).contains(activity) && 
			   !Arrays.asList(mildActivities).contains(activity) && 
			   !Arrays.asList(hotActivities).contains(activity) &&
			   !Arrays.asList(coldActivities).contains(activity)) {
			System.out.print("Sorry, we don't have that activity. Please try a similar activity: ");
			activity = console.next();
		}
		console.close();
		
		WeatherForecast weatherForecast = new WeatherForecast(city);
		weatherForecast.updateForecast();
		WeatherPlot weatherPlot = new WeatherPlot(weatherForecast);
		weatherPlot.display();

		// printSuggestion(weatherForecast, activity);
		// wait for like 5 seconds so the user can read, and say generating plot.
	}
	
	public static String getActivityType(String activity) {
		if (Arrays.asList(indoorActivities).contains(activity)) {
			return "indoor";
		} else if (Arrays.asList(mildActivities).contains(activity)) {
			return "mild";
		} else if (Arrays.asList(hotActivities).contains(activity)) {
			return "hot";
		}
		return "cold";
	}
	
	public static void printSuggestion(WeatherForecast weatherForecast, String activity) {
		
		String activityType = getActivityType(activity);
	
	}

}
