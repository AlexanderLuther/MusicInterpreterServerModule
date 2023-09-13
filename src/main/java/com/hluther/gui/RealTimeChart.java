package com.hluther.gui;



import com.hluther.entity.Channel;
import com.hluther.entity.Note;
import com.hluther.entity.Pause;
import org.jfree.chart.ChartFactory;   
import org.jfree.chart.ChartPanel;   
import org.jfree.chart.JFreeChart;   
import org.jfree.chart.axis.ValueAxis;   
import org.jfree.chart.plot.XYPlot;   
import org.jfree.data.time.Millisecond;   
import org.jfree.data.time.TimeSeries;   
import org.jfree.data.time.TimeSeriesCollection;   
   
public class RealTimeChart extends ChartPanel implements Runnable   
{   
    private static TimeSeries timeSeries;   
    private boolean exit;
    private Channel channel;
       
    public RealTimeChart(String chartContent, String title, String yaxisName, Channel channel){   
        super(createChart(chartContent, title, yaxisName));   
        this.exit = false;
        this.channel = channel;
    }   
       
    private static JFreeChart createChart(String chartContent,String title,String yaxisName){   
                 // Crear un objeto de diagrama de tiempos   
        timeSeries = new TimeSeries(chartContent);   
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeSeries);   
                 JFreeChart jfreechart = ChartFactory.createTimeSeriesChart (title, "tiempo (segundos)", yaxisName, timeseriescollection, true, true, false);   
        XYPlot xyplot = jfreechart.getXYPlot();   
                 // Configuración de coordenadas verticales   
        ValueAxis valueaxis = xyplot.getDomainAxis();   
                 // Establecer el rango de datos del eje de datos automáticamente   
        valueaxis.setAutoRange(true);   
                 // Eje de datos rango de datos fijo 30s   
    
        return jfreechart;   
      }   

    public void setExit(boolean exit) {
        this.exit = exit;
    }
    
    public void run(){   
        while(!exit)   {   
            try{   
                int time = 0;
                for(int i = 0; i < channel.getActions().size(); i++){
                    if(channel.getActions().get(i) instanceof Note){
                        timeSeries.add(new Millisecond(), ((Note)channel.getActions().get(i)).getFrecuency());
                        Thread.sleep(((Note)channel.getActions().get(i)).getDuration()*2);
                    } else{
                        Thread.sleep(((Pause)channel.getActions().get(i)).getDuration()*2);
                    }
                } 
                exit = true;
            }   
            catch (InterruptedException e)  {   }   
        }          
    }   
   
}