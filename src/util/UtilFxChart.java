/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Marlon
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
public class UtilFxChart {
    // Create PieChart Data<font></font>
    public static ObservableList<PieChart.Data> getChartData()
    {
        ObservableList<PieChart.Data> data = FXCollections. observableArrayList();
        data.add(new PieChart.Data("China", 1275));
        data.add(new PieChart.Data("India", 1017));
        data.add(new PieChart.Data("Brazil", 172));
        data.add(new PieChart.Data("UK", 59));
        data.add(new PieChart.Data("USA", 285));
        return data;
    }
    // Create XYChart Country Data <font></font>
    public static ObservableList<XYChart.Series<Number, Number>> getCountrySeries() 
    {
        XYChart.Series<Number, Number> seriesChina = new XYChart.Series<Number, Number>();
        seriesChina.setName("China");
        seriesChina.getData().add(new XYChart.Data<Number, Number>(1950, 555));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2000, 1275));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2050, 1395));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2100, 1182));
        seriesChina.getData().add(new XYChart.Data<Number, Number>(2150, 1149));
        
        XYChart.Series<Number, Number> seriesIndia = new XYChart.Series<Number, Number>();
        seriesIndia.setName("India");
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(1950, 358));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2000, 1017));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2050, 1531));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2100, 1458));
        seriesIndia.getData().add(new XYChart.Data<Number, Number>(2150, 1308));
       
        XYChart.Series<Number, Number> seriesUSA = new XYChart.Series<Number, Number>();
        seriesUSA.setName("USA");
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(1950, 158));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2000, 285));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2050, 409));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2100, 437));
        seriesUSA.getData().add(new XYChart.Data<Number, Number>(2150, 453)); 
        
        ObservableList<XYChart.Series<Number, Number>> data =
            FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
        data.add(seriesChina);
        data.add(seriesIndia);
        data.add(seriesUSA);
        return data;
    }
    // Create XYChart Year Data <font></font>
    public static ObservableList<XYChart.Series<String, Number>> getYearSeries()
    {
        XYChart.Series<String, Number> series1950 = new XYChart.Series<String, Number>();
        series1950.setName("1950");
        series1950.getData().add(new XYChart.Data<String, Number>("China", 555));
        series1950.getData().add(new XYChart.Data<String, Number>("India", 358));
        series1950.getData().add(new XYChart.Data<String, Number>("Brazil", 54));
        series1950.getData().add(new XYChart.Data<String, Number>("UK", 50));
        series1950.getData().add(new XYChart.Data<String, Number>("USA", 158));
       
        XYChart.Series<String, Number> series2000 = new XYChart.Series<String, Number>();
        series2000.setName("2000");
        series2000.getData().add(new XYChart.Data<String, Number>("China", 1275));
        series2000.getData().add(new XYChart.Data<String, Number>("India",1017));
        series2000.getData().add(new XYChart.Data<String, Number>("Brazil", 172));
        series2000.getData().add(new XYChart.Data<String, Number>("UK", 59));
        series2000.getData().add(new XYChart.Data<String, Number>("USA", 285));
       
        XYChart.Series<String, Number> series2050 = new XYChart.Series<String, Number>();
        series2050.setName("2050");
        series2050.getData().add(new XYChart.Data<String, Number>("China", 1395));
        series2050.getData().add(new XYChart.Data<String, Number>("India",1531));
        series2050.getData().add(new XYChart.Data<String, Number>("Brazil", 233));
        series2050.getData().add(new XYChart.Data<String, Number>("UK", 66));
        series2050.getData().add(new XYChart.Data<String, Number>("USA", 409));

                ObservableList<XYChart.Series<String, Number>> data =
            FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        data.add(series1950);
        data.add(series2000);
        data.add(series2050);
        return data;
    }   
}