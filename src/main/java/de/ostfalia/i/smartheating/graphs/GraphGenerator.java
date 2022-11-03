package de.ostfalia.i.smartheating.graphs;

import org.charts.dataviewer.DataViewer;
import org.charts.dataviewer.api.config.DataViewerConfiguration;
import org.charts.dataviewer.api.data.PlotData;
import org.charts.dataviewer.api.trace.BarTrace;
import org.charts.dataviewer.api.trace.LineTrace;
import org.charts.dataviewer.utils.TraceColour;

import java.util.Vector;
import java.util.stream.IntStream;

public final class GraphGenerator {

    private static LineTrace<Object> createLineTrace(Vector<Double> measurements) {
        LineTrace<Object> lineTrace = new LineTrace<>();
        lineTrace.setxArray(IntStream.rangeClosed(1, measurements.size()).boxed().toArray());
        lineTrace.setyArray(measurements.toArray());
        lineTrace.setTraceName("Measurements");
        lineTrace.setTraceColour(TraceColour.RED);
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
    public static void showLinePlot(Vector<Double> measurements) {

        DataViewer dataviewer = new DataViewer("heating");

        DataViewerConfiguration config = new DataViewerConfiguration();
        config.setPlotTitle("Line Trace Example");
        config.setxAxisTitle("Measurement No.");
        config.setyAxisTitle("Temperature");
        config.showLegend(true);
        config.setLegendInsidePlot(false);
        dataviewer.updateConfiguration(config);

        PlotData plotData = new PlotData();

        plotData.addTrace(createLineTrace(measurements));

        dataviewer.updatePlot(plotData);

    }

}
