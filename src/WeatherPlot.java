import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
 
 // This class represents the window where the weather forecast plot appears.
public class WeatherPlot extends ApplicationFrame {
	
	 public WeatherPlot(WeatherForecast weatherForecast) {
		super("");
		XYSeries dataPoints = new XYSeries("Temp (F)");
		double[] times = weatherForecast.getForecastTimes();
		double[] temps = weatherForecast.getForecastedTemps();
		for (int i = 0; i < times.length; i++) {
			dataPoints.add(times[i], temps[i]);
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection(dataPoints);
		JFreeChart chart = ChartFactory.createXYLineChart("Daytime Temperature Forecast for " + weatherForecast.getCity(),
														 "Time", "Temp (F)", dataset, 
														 PlotOrientation.VERTICAL, true, true, true);
		ChartPanel chartPanel = new ChartPanel(chart);
		setContentPane(chartPanel);
	}

	public void display() {
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}
}
