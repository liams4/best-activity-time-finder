import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class get_weather_data {
	public static final String[] indoorActivities = {"read", "watch", "tv", "movie", "study", "work",
													"sleep", "rest", "gaming", "games", "listen",
													"music", "sing", "dance", "eat", "drink"};
	public static final String[] mildActivities = {"walk", "run", "bike", "cycle",
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
		System.out.print("Please enter your activity (please be very general and use just one word! "
						 + "Examples: surf, hike): ");
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
		System.out.println(Arrays.toString(weatherForecast.getForecastedWeatherConditions()));
		WeatherPlot weatherPlot = new WeatherPlot(weatherForecast);
		weatherPlot.display();

		// printSuggestion(weatherForecast, activity);
		// wait for like 5 seconds so the user can read, and say generating plot.
	}
	
	public static String getActivityTempType(String activity) {
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
		double minTempGoodConditions = getMinValue(weatherForecast.getForecastedTemps(), weatherForecast.getForecastedWeatherConditions(), true);
		double minTempAnyConditions = getMinValue(weatherForecast.getForecastedTemps(), weatherForecast.getForecastedWeatherConditions(), false);
		double maxTempGoodConditions = getMaxValue(weatherForecast.getForecastedTemps(), weatherForecast.getForecastedWeatherConditions(), true);
		double maxTempAnyConditions = getMaxValue(weatherForecast.getForecastedTemps(), weatherForecast.getForecastedWeatherConditions(), false);
		
		String goodConditionsTempType = getTempType(minTempGoodConditions, maxTempGoodConditions);
		String anyConditionsTempType = getTempType(minTempAnyConditions, maxTempAnyConditions);	
		String activityTempType = getActivityTempType(activity);
			
		if (!activityTempType.equals("indoor")) {
			if (activityTempType.equals(goodConditionsTempType)) {
				// print best time
			} else if (activityTempType.equals(anyConditionsTempType)) {
				// print best time
			} else {
				// print sorry, there's not a great time in the next few days. maybe try another activity
			}
		} else if (activityTempType.equals(goodConditionsTempType)){
			// print it's a great time any time, but it's also amazing weather outside in the next few days
		} else {
			// print it's a great time any time
		}		
	}
	
	public static double getMinValue(double[] forecastedTemps, String[] forecastedWeatherConditions, 
									boolean goodConditions) {
		double min = 1000;
		for (int i = 0; i < forecastedTemps.length; i++) {
			if (forecastedTemps[i] < min) {
				if (goodConditions) {
					if (!forecastedWeatherConditions[i].equals("Rain")) {
						min = forecastedTemps[i];
					}
				} else {
					min = forecastedTemps[i];
				}
			}
		}
		return min;
	}
	
	public static double getMaxValue(double[] forecastedTemps, String[] forecastedWeatherConditions,
									boolean goodConditions) {
		double max = -1000;
		for (int i = 0; i < forecastedTemps.length; i++) {
			if (forecastedTemps[i] > max) {
				if (goodConditions) {
					if (!forecastedWeatherConditions[i].equals("Rain")) {
						max = forecastedTemps[i];
					}
				} else {
					max = forecastedTemps[i];
				}
			}
		}
		return max;
	}
	
	// Returns the the "temperature type" for a period based on the minimum and maximum temps
	// during that period.
	public static String getTempType(double minTemp, double maxTemp) {
		String tempType;
		if (minTemp >= 75) {
			tempType = "hot";
		} else if (minTemp >= 60 && maxTemp < 75) {
			tempType = "indoor";
		} else if (maxTemp < 40) {
			tempType = "cold";
		} else {
			tempType = "mild";
		}
		return tempType;
	}
}
