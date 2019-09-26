import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

// This class represents the window where the weather forecast plot appears.
public class WeatherPlot extends ApplicationFrame {
	
	 public WeatherPlot(WeatherForecast weatherForecast) {
		super("");
		TimeSeries dataPoints = new TimeSeries("Temp (F)");
		long[] times = weatherForecast.getForecastTimes();
		double[] temps = weatherForecast.getForecastedTemps();
		for (int i = 0; i < times.length; i++) {
			Date date = new Date(times[i] * 1000); // convert to milliseconds from seconds
			dataPoints.add(new Hour(date), temps[i]);
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection(dataPoints);
		JFreeChart chart = ChartFactory.createTimeSeriesChart("Daytime Temperature Forecast for " + weatherForecast.getCity(),
														     "Time", "Temp (F)", dataset, 
														     true, true, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		setContentPane(chartPanel);
	}

	public void display() {
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}
}
