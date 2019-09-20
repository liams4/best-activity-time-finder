import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.*;



public class get_weather_data {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		System.out.println("Hello! Welcome to ActivityFinder. Please enter an activity you'd like"
						   + "to do and the location of the activity and I will tell you if there's"
						   + " a good day to do it in the near future!");
		System.out.print("Please enter the location of the activity (City, State): ");
		String location = console.next();
		// get locations coordinates (if there's an error then retry)
		// match them with weather api location
		System.out.print("\nPlease enter your activity (please be very general! Example: surf, hike): ");
		String activity = console.next();
		// if activity not in any of the activies, try again and prompt for maybe a physical activity
		// compare to the weather, inform the user and plot

	}
	
	public static ArrayList<String> getActivities() {
		ArrayList<String> outdoorActivities = new ArrayList<String>();
		// for all of these it's bad if it's rainy
		String[] waterActivites = {"surf", "swim", "boat", "scuba-dive", "dive", "fish"};
		String[] snowActivities = {"ski", "snowboard", "snowshoe", "sled", "snow/snowball", "ice"};
		String[] otherOutdoor =  {"walk", "run", "bike/bicycle/cycle", "hike", "sunbathe", "sport", "yard", "sun"};
		String[] indoor = {"read", "watch/tv/movie", "study", "work", "sleep/rest", "gaming/games", "listen/music/sing/dance"};
		return outdoorActivities;
	}

}
