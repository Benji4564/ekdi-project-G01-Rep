package de.ostfalia.i.smartheating.graphs;

import org.charts.dataviewer.DataViewer;
import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.api.trace.BarTrace;
import org.charts.dataviewer.api.trace.LineTrace;
import org.charts.dataviewer.utils.TraceColour;

import de.ostfalia.i.smartheating.SmartHeating;

import java.util.Vector;
import java.util.stream.IntStream;

public final class GraphGenerator {
    private static LineTrace<Object> createLineTrace(Vector<Double> measurements, String name, TraceColour traceColour) {
        LineTrace<Object> lineTrace = new LineTrace<>();
        lineTrace.setxArray(IntStream.rangeClosed(1, measurements.size()).boxed().toArray());
        lineTrace.setyArray(measurements.toArray());
        lineTrace.setTraceName(name);
        lineTrace.setTraceColour(traceColour);
        return lineTrace;
    }

    private static BarTrace<Object> createBarTrace(Vector<Double> measurements) {
        BarTrace<Object> barTrace = new BarTrace<>();
        barTrace.setxArray(IntStream.rangeClosed(1, measurements.size()).boxed().toArray());
        barTrace.setyArray(measurements.toArray());
        barTrace.setTraceName("Measurements");
        barTrace.setTraceColour(TraceColour.BLUE);
        return barTrace;
    }

    /**
     * Show line plot on browser, navigate to http://localhost:8090/view/heating
     */
    public static DataViewer dataviewer = new DataViewer("heating");

     public static void showLinePlot(SmartHeating[] rooms, String x, String y, String headline) {
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
            plotData.addTrace(createLineTrace(r.getMeasurements(), r.getName(), r.getTraceColour()));
        }

        
        dataviewer.updatePlot(plotData);
     }

}
