package chart;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

// Vẽ biểu đồ thống kê
public class LineChart extends ApplicationFrame {

    	private static final long serialVersionUID = 1L;
	String[] thoiGian;
    int[] soHoaDon;
    long[] doanhThu;

        public LineChart(String[] thoiGian, int[] soHoaDon, long[] doanhThu) {
        super("Biểu Đồ Thống Kê");
        this.thoiGian = thoiGian;
        this.soHoaDon = soHoaDon;
        this.doanhThu = doanhThu;
    }

    // Biểu đồ đường số hóa đơn bán ra
    public JFreeChart getLineChart() {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Biều Đồ Đường Thống Kê Hóa Đơn Đã Bán",
                "Tháng/Năm", "Số Hóa Đơn Bán Ra",
                createDataset(),
                PlotOrientation.VERTICAL,
                false, false, false);

        return lineChart;
    }

    // Biểu đồ đường doanh thu
    public JFreeChart getLine() {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Biều Đồ Đường Thống Kê Doanh Thu",
                "Tháng/Năm", "Doanh Thu Hóa Đơn (VNĐ)",
                createData(),
                PlotOrientation.VERTICAL,
                false, false, false);

        return lineChart;
    }

    // Tạo dữ liệu về doanh thu
    private DefaultCategoryDataset createData() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (int i=0; i<thoiGian.length;i++ ) {
            
            if(thoiGian[i]!=null && doanhThu[i]!=0){
                dataset.addValue(doanhThu[i], "Doanh Thu", thoiGian[i]);
            }
        }
        return dataset;
    }

    // Tạo dữ liệu về tổng số hóa đơn
    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i=0; i<thoiGian.length;i++ ) {
            
            if(thoiGian[i]!=null && soHoaDon[i]!=0){
                dataset.addValue(soHoaDon[i], "Hóa Đơn", thoiGian[i]);
            }
        }
        return dataset;
    }
}
