 import java.util.Map;

import org.jfree.chart.ChartFactory;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.JFreeChart;
 import org.jfree.chart.plot.PlotOrientation;
 import org.jfree.data.xy.XYSeries;
 import org.jfree.data.xy.XYSeriesCollection;
 import org.jfree.ui.ApplicationFrame;
 import org.jfree.ui.RefineryUtilities;
 
 // This class represents the window where the weather forecast plot appears.
public class weatherPlot extends ApplicationFrame {
	
	public weatherPlot(String location, int[] times, int[] temps, String title) {
		super(title);
		XYSeries dataPoints = new XYSeries("Temp (F)");
		for (int i = 0; i < times.length; i++) {
			dataPoints.add(times[i], temps[i]);
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection(dataPoints);
		JFreeChart chart = ChartFactory.createXYLineChart("Daytime Temperature Forecast for place name",
														 "Time", "Temp (F)", dataset, 
														 PlotOrientation.VERTICAL, true, true, true);
		ChartPanel chartPanel = new ChartPanel(chart);
		setContentPane(chartPanel);
	}
	
	// Displays the plot to the screen.
	public void display() {
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}
	
}
