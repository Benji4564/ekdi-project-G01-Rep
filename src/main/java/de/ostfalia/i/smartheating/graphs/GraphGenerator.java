package de.ostfalia.i.smartheating.graphs;

import org.charts.dataviewer.DataViewer;
import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.api.trace.BarTrace;
import org.charts.dataviewer.api.trace.LineTrace;
import org.charts.dataviewer.utils.TraceColour;

import org.charts.dataviewer.javafx.JavaFxDataViewer;
import de.ostfalia.i.smartheating.SmartHeating;

import java.util.Vector;
import java.util.stream.IntStream;

public final class GraphGenerator {

   


    private static LineTrace<Object> createLineTrace(  Object[] xArray, SmartHeating smartHeating) {
        LineTrace<Object> lineTrace = new LineTrace<>();
        if (xArray != null) {
            lineTrace.setxArray(xArray);
        } else {
            lineTrace.setxArray(IntStream.range(0, smartHeating.getMeasurements().size()).boxed().toArray());
        }
        lineTrace.setyArray(smartHeating.getMeasurements().toArray());
        lineTrace.setTraceName(smartHeating.getName());
        lineTrace.setTraceColour(smartHeating.getTraceColour());

        return lineTrace;
    }

    private static BarTrace<Object> createBarTrace(Vector<Double> measurements, String name, TraceColour traceColour, SmartHeating smartHeating) {
        BarTrace<Object> barTrace = new BarTrace<>();
        barTrace.setxArray(IntStream.rangeClosed(1, measurements.size()).boxed().toArray());
        barTrace.setyArray(measurements.toArray());
        barTrace.setTraceName(smartHeating.getName());
        barTrace.setTraceColour(smartHeating.getTraceColour());
        return barTrace;
    }

    /**
     * Show line plot on browser, navigate to http://localhost:8090/view/heating
     */
    public static DataViewer dataviewer = new DataViewer("heating");
    //public static JavaFxDataViewer dataviewer;
    /**
     * 
     * @param rooms An SmartHeating object array with all rooms
     * @param x    The x-axis label
     * @param y    The y-axis label
     * @param headline The headline of the graph
     * @return The generated graph
     */

     public static void showLinePlot(SmartHeating[] rooms, String x, String y, String headline, Object[] xArray) {
        dataviewer.resetPlot();
        DataViewerConfiguration config = new DataViewerConfiguration();
        config.setPlotTitle(headline);
        config.setxAxisTitle(x);
        config.setyAxisTitle(y);
        config.showLegend(true);
        config.setLegendInsidePlot(false);
        
        dataviewer.updateConfiguration(config);
        PlotData plotData = new PlotData();
        for (SmartHeating room : rooms) {
            plotData.addTrace(createLineTrace( xArray, room));

        }

        dataviewer.updatePlot(plotData);
     }

     /**
      * 
      * @param rooms An SmartHeating object array with all rooms
      * @param x   The x-axis label
      * @param y    The y-axis label   
      * @param headline The headline of the graph
      */
     public static void showBarPlot(SmartHeating[] rooms, String x, String y, String headline) {
        //DataViewer dataviewer = new DataViewer("heating");
        dataviewer.resetPlot();
        DataViewerConfiguration config = new DataViewerConfiguration();
        config.setPlotTitle(headline);
        config.setxAxisTitle(x);
        config.setyAxisTitle(y);
        config.showLegend(true);
        config.setLegendInsidePlot(false);
        dataviewer.updateConfiguration(config);

        PlotData plotData = new PlotData();
        for (SmartHeating r : rooms) {
            plotData.addTrace(createBarTrace(r.getMeasurements(), r.getName(), r.getTraceColour(), r));        

        }
        dataviewer.updatePlot(plotData);
     }

}
