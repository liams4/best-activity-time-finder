import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.*;



public class get_weather_data {

	public static void main(String[] args) {
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet("https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=YOUR_API_KEY");
		try {
			HttpResponse response = client.execute(request);
			String responseAsString = EntityUtils.toString(response.getEntity());
			JSONObject responseAsJSON = new JSONObject(responseAsString);
			System.out.print(responseAsJSON.getJSONObject("main").getDouble("temp"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
