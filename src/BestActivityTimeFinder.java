import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Date;

public class BestActivityTimeFinder {
	public static final String[] indoorActivities = {"read", "watch", "tv", "movie", "study",
													"work", "sleep", "rest", "gaming", "games",
													"listen", "music", "sing", "dance", "eat",
													"drink"};
	public static final String[] mildActivities = {"walk", "run", "bike", "cycle", "climb", "hike",
												  "sport", "yard"};
	public static final String[] hotActivities = {"surf", "swim", "boat", "dive", "fish", "sun", 
												 "sail"};
	public static final String[] coldActivities = {"ski", "snow", "sled", "ice", "cold", "freeze",
												  "snowboard"};

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner console = new Scanner(System.in);
		System.out.println("Welcome to BestActivityTimeFinder. Please enter an activity you'd like "
						   + "to do and the location of the activity and I will tell you the best "
						   + "time to do it in the near future (based on the weather)!");
		System.out.print("Please enter the nearest city of the activity (City, State): ");
		String city = console.nextLine();
		System.out.print("Please enter your activity (please be very general and use just one word! "
						 + "Examples: surf, hike): ");
		String activity = console.next();
		while (!activityExists(activity)) {
			System.out.print("Sorry, we don't have that activity. Please try a similar activity: ");
			activity = console.next();
		}
		console.close();
		
		WeatherForecast weatherForecast = new WeatherForecast(city);
		weatherForecast.setForecast();

		printSuggestion(weatherForecast, activity);
		System.out.println("Generating weather forecast plot...");
		TimeUnit.SECONDS.sleep(3);
		WeatherPlot weatherPlot = new WeatherPlot(weatherForecast);
		weatherPlot.display();
	}
	
	// Returns true if the activity passed as a parameter is one of the activities that the program
	// supports/contains, false otherwise.
	public static boolean activityExists(String activity) {
		return Arrays.asList(indoorActivities).contains(activity) || 
		   Arrays.asList(mildActivities).contains(activity) ||
		   Arrays.asList(hotActivities).contains(activity) ||
		   Arrays.asList(coldActivities).contains(activity);
	}
	
	// Gets the "activity temp type" of an activity (temperature that the activity is usually
	// performed in).
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
	
	// Print suggestions regarding the timing of the user's activity.
	public static void printSuggestion(WeatherForecast weatherForecast, String activity) {
		double[] temps = weatherForecast.getForecastedTemps();
		String[] conditions = weatherForecast.getForecastedWeatherConditions();
		
		double minTempGoodConditions = getMinValue(temps, conditions, true);
		double minTempAnyConditions = getMinValue(temps, conditions, false);
		double maxTempGoodConditions = getMaxValue(temps, conditions, true);
		double maxTempAnyConditions = getMaxValue(temps, conditions, false);
		
		String goodConditionsTempType = getTempType(minTempGoodConditions, maxTempGoodConditions);
		String anyConditionsTempType = getTempType(minTempAnyConditions, maxTempAnyConditions);	
		String activityTempType = getActivityTempType(activity);
		
		if (!activityTempType.equals("indoor")) {
			if (activityTempType.equals(goodConditionsTempType)) {
				long goodConditionsBestTime = getBestTime(weatherForecast, activityTempType, 
														  minTempGoodConditions, maxTempGoodConditions);
				Date timeAsDate = new Date(goodConditionsBestTime * 1000);
				System.out.println(timeAsDate + " is likely the best time to " + activity + "!");
			} else if (activityTempType.equals(anyConditionsTempType)) {
				long anyConditionsBestTime = getBestTime(weatherForecast, activityTempType, 
														 minTempAnyConditions, maxTempAnyConditions);
				Date timeAsDate = new Date(anyConditionsBestTime * 1000);
				System.out.println(timeAsDate + " is likely the best time to " + activity + 
								  ", but it might be raining.");
			} else {
				System.out.println("Sorry, there's not a great time to " + activity + 
								  " in the next few days. Maybe try another activity?");
			}
		} else {
			System.out.println("It's a great time any time to " + activity + ".");
			if (activityTempType.equals(goodConditionsTempType)) {
				System.out.println("But it's also amazing weather to go outside!");
			}
		}
	}
	
	// Gets the min value of the forecastedTemps array. If parameter goodConditions is true,
	// only temperatures where there is no rain are considered.
	public static double getMinValue(double[] forecastedTemps,
									String[] forecastedWeatherConditions, 
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
	
	// Gets the max value of the forecastedTemps array. If parameter goodConditions is true,
	// only temperatures where there is no rain are considered.
	public static double getMaxValue(double[] forecastedTemps, 
									String[] forecastedWeatherConditions,
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
		if (maxTemp >= 75) {
			return "hot";
		} else if (minTemp >= 60 && maxTemp < 75) {
			return "indoor";
		} else if (minTemp < 35) {
			return "cold";
		}
		return "mild";
	}
	
	// Returns the "best" time (the time where either the minTemp or the maxTemp occurs depending
	// on the activity type).
	public static long getBestTime(WeatherForecast weatherForecast, String activityType,
								   double minTemp, double maxTemp) {
		double[] temps = weatherForecast.getForecastedTemps();
		for (int i = 0; i < temps.length; i++) {
			if ((activityType.equals("hot") || activityType.equals("mild")) && temps[i] == maxTemp) {
				return weatherForecast.getForecastTimes()[i];
			} else if (activityType.equals("cold") && temps[i] == minTemp) {
				return weatherForecast.getForecastTimes()[i];
			}
		}
		return -1;
	}
}
