package view;


import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import chart.LineChart;
import entity.CT_HoaDon;
import entity.HoaDon;
import entity.SanPham;
import entity.TaiKhoan;
import iRemote.ICT_HoaDon;
import iRemote.IHoaDon;
import iRemote.ISanPham;
import iRemote.ITaiKhoan;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import view.util.ConnectJsaper;
import view.util.FrameUtil;
import view.util.RMIUrl;

import org.jfree.chart.ChartPanel;

//Frame thống kê
public class Revenues extends javax.swing.JFrame {

    private String sql = "SELECT * FROM HOADON";
    private String date = "19/10/2021"; // Ngày mặc định
    private ITaiKhoan taiKhoanDao;
	private String rmiUrl = new RMIUrl().RMIUrl();
    private FrameUtil frameUtil = new FrameUtil();
	private TaiKhoan tk;
	private ISanPham sanPhamDao;
	private ICT_HoaDon ctHDDao;
	private IHoaDon hoaDonDao;

    public Revenues(TaiKhoan Ac) throws MalformedURLException, RemoteException, NotBoundException {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://" + rmiUrl +":3030/iTaiKhoan");
        sanPhamDao = (ISanPham) Naming.lookup("rmi://" + rmiUrl +":3030/iSanPham");
		ctHDDao = (ICT_HoaDon) Naming.lookup("rmi://" + rmiUrl +":3030/iCTHoaDon");
		hoaDonDao = (IHoaDon) Naming.lookup("rmi://" + rmiUrl + ":3030/iHoaDon");
        tk = Ac; // gọi đến Entity tài khoản lấy thông tin tài khoản, đăng nhập và thoát Frame phù hợp
        loadDate(); //load ngày thống kê mặc định
        lblNhanVien.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
    }

    //load ngày thống kê mặc định
    private void loadDate() {
        try {
            jDateChooser1.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date)); 
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        jDateChooser2.setDate(new java.util.Date());
    }
    
    
    //load bảng thống kê
    private void load() throws RemoteException {
        int count = 0;
        long tongTien = 0;
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        tblRevenue.removeAll();
        tblRevenue.getTableHeader().setFont( new Font( "Arial" , Font.BOLD, 16 ));
        String[] arr = {"Mã hóa đơn", "Nhân viên bán hàng", "Khách mua hàng", "Ngày bán", "Tổng tiền hóa đơn"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        
        List<SanPham> sp = sanPhamDao.getAllSanPham();
      
        List<HoaDon> arry = hoaDonDao.getAllHD();
        for (int i = 0; i < arry.size(); i++) {

            String sql = "SELECT * FROM CT_HOADON WHERE MaHD = '" + ((HoaDon) arry.get(i)).getMaHD() + "'";
           List<CT_HoaDon> CTHD = ctHDDao.findCTHD(sql);
            
            Vector vector = new Vector();
            vector.add(((HoaDon) arry.get(i)).getMaHD());
            vector.add(((HoaDon) arry.get(i)).getNhanVien().getMaNV() + " (" + ((HoaDon) arry.get(i)).getNhanVien().getHoTen() + ")");
            vector.add(((HoaDon) arry.get(i)).getKhachHang().getMaKH() + " (" + ((HoaDon) arry.get(i)).getKhachHang().getTenKH() + ")");
            vector.add(new SimpleDateFormat("dd/MM/yyyy").format(((HoaDon) arry.get(i)).getNgayBan()));
            vector.add(formatter.format(((HoaDon) arry.get(i)).getThanhTien(CTHD)) + " VNĐ");


            modle.addRow(vector);
            double thanhTien = ((HoaDon) arry.get(i)).getThanhTien(CTHD);

            tongTien = (long) (thanhTien + tongTien);
            count++;
        }
        tblRevenue.setModel(modle);
        lblSoHoaDon.setText(String.valueOf(count));
        
        lblTongDoanhThu.setText(formatter.format(tongTien) + " " + "VNĐ");

    }

    //load bảng thống kê khi bấm nút thống kê
    private void loadRevenue() throws RemoteException {
        int count = 0;
        long tongTien = 0;
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        tblRevenue.removeAll();
        tblRevenue.getTableHeader().setFont( new Font( "Arial" , Font.BOLD, 16 ));
        String[] arr = {"Mã hóa đơn", "Nhân viên bán hàng", "Khách mua hàng", "Ngày bán", "Tổng tiền hóa đơn"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
         List<SanPham> sp;
            sp = sanPhamDao.getAllSanPham();
            Date date1 = new java.sql.Date(jDateChooser1.getDate().getTime());
            Date date2 = new java.sql.Date(jDateChooser2.getDate().getTime());
            String sql = "SELECT * FROM HOADON WHERE NgayBan BETWEEN '" + date1 + "' AND '" + date2 + "'";
        List<HoaDon> arry = hoaDonDao.findHD(sql);
        for (int i = 0; i < arry.size(); i++) {
        	  String sql1 = "SELECT * FROM CT_HOADON WHERE MaHD = '" + ((HoaDon) arry.get(i)).getMaHD() + "'";
              List<CT_HoaDon> CTHD = ctHDDao.findCTHD(sql1);
            Vector vector = new Vector();
            vector.add(((HoaDon) arry.get(i)).getMaHD());
            vector.add(((HoaDon) arry.get(i)).getNhanVien().getMaNV() + " (" + ((HoaDon) arry.get(i)).getNhanVien().getHoTen() + ")");
            vector.add(((HoaDon) arry.get(i)).getKhachHang().getMaKH() + " (" + ((HoaDon) arry.get(i)).getKhachHang().getTenKH() + ")");
            vector.add(new SimpleDateFormat("dd/MM/yyyy").format(((HoaDon) arry.get(i)).getNgayBan()));
            vector.add(formatter.format(((HoaDon) arry.get(i)).getThanhTien(CTHD)) + " VNĐ");


            modle.addRow(vector);
            double thanhTien = ((HoaDon) arry.get(i)).getThanhTien(CTHD);

            tongTien = (long) (thanhTien + tongTien);
            count++;
        }
        
        tblRevenue.setModel(modle);
        lblSoHoaDon.setText(String.valueOf(count));
        lblTongDoanhThu.setText(formatter.format(tongTien) + " " + "VNĐ");
    }

    //Đổi kiểu chuỗi sang kiểu số
    private long convertedToNumbers(String s) {
        String number = "";
        String[] array = s.replace(",", " ").split("\\s");
        for (String i : array) {
            number = number.concat(i);
        }
        return Long.parseLong(number);
    }
    
    // Biểu đồ
    private void bieuDo() throws RemoteException{
        
    	 Date date1 = new java.sql.Date(jDateChooser1.getDate().getTime());
         Date date2 = new java.sql.Date(jDateChooser2.getDate().getTime());
         String sql = "SELECT * FROM HOADON WHERE NgayBan BETWEEN '" + date1 + "' AND '" + date2 + "'";
         List<HoaDon> arry = hoaDonDao.findHD(sql);
        int count = 0;

        String[] thoiGianKT = new String[arry.size()];
        int[] soHoaDonKT = new int[arry.size()];
        long[] doanhThu = new long[arry.size()];

        int j = 0;
        List<SanPham> sp;
        sp = sanPhamDao.getAllSanPham();
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        for (int i = 0; i < arry.size(); i++) {

             String sql1 = "SELECT * FROM CT_HOADON WHERE MaHD = '" + ((HoaDon) arry.get(i)).getMaHD() + "'";
             List<CT_HoaDon> CTHD = ctHDDao.findCTHD(sql1);
            String[] chuoi = ((HoaDon) arry.get(i)).getNgayBan().toString().split("-");

            long doanhso = (long) ((HoaDon) arry.get(i)).getThanhTien(CTHD);

            if (i == 0) {
                thoiGianKT[j] = chuoi[1] + "/" + chuoi[0];
                soHoaDonKT[j] = 1;
                doanhThu[j] = doanhso;
                j++;
                count = count + 1;
            } else {
                String[] ktra = thoiGianKT[j - 1].replace("/", " ").split("\\s");


                if (chuoi[1].equals(ktra[0]) && chuoi[0].equals(ktra[1])) {
                    soHoaDonKT[j - 1] = soHoaDonKT[j - 1] + 1;
                    doanhThu[j - 1] = doanhThu[j - 1] + doanhso;
                } else {
                    thoiGianKT[j] =  chuoi[1] + "/" + chuoi[0];
                    soHoaDonKT[j] = 1;
                    doanhThu[j] = doanhso;
                    j++;
                    count = count + 1;
                }

            }
        }

        int k = 0;


        //Gọi đến LineChart Entity
        LineChart lineChart = new LineChart(thoiGianKT, soHoaDonKT, doanhThu);

        ChartPanel chartPanel = new ChartPanel(lineChart.getLineChart());
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 400));

        ChartPanel chart = new ChartPanel(lineChart.getLine());
        chart.setPreferredSize(new java.awt.Dimension(400, 400));

        JPanel jpanel = new JPanel();
        jpanel.setSize(1000, 400);

        jpanel.setLayout(new GridLayout());
        jpanel.add(chartPanel);
        jpanel.add(chart);

        JFrame jframe = new JFrame();
        jframe.setTitle("Biểu Đồ Thống Kê");

        jframe.add(jpanel);

        jframe.setSize(1280, 600);
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);
        jframe.setVisible(true);

    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBackHome = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        btnRevenue = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRevenue = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        lblSoHoaDon = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTongDoanhThu = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Previous-icon.png"))); // NOI18N
        btnBackHome.setText("Hệ Thống");
        btnBackHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thống Kê Doanh Thu");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel2.setText("Thống Kê Từ Ngày:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel3.setText("Đến Ngày:");

        jDateChooser1.setDateFormatString("dd/MM/yyyy");
        jDateChooser1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jDateChooser2.setDateFormatString("dd/MM/yyyy");
        jDateChooser2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        btnRevenue.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnRevenue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Revenue.png"))); // NOI18N
        btnRevenue.setText("Thống Kê");
        btnRevenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnRevenueActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnThongKe.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Print Sale.png"))); // NOI18N
        btnThongKe.setText("In Thống Kê");
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnThongKeActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jLabel2)
                .addGap(38, 38, 38)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblRevenue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblRevenue.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        tblRevenue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblRevenue.setRowHeight(26);
        tblRevenue.setRowMargin(2);
        tblRevenue.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblRevenue);
        tblRevenue.setColumnModel(tblRevenue.getColumnModel());

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText("Tổng Số Hóa Đơn Bán Ra:");

        lblSoHoaDon.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblSoHoaDon.setText("0");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel6.setText("Tổng Tiền Hóa Đơn:");

        lblTongDoanhThu.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblTongDoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongDoanhThu.setText("0 VNĐ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 51));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel5.setText("Nhân Viên:");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien.setText("(TK01) Lê Thị Mỹ Thọ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1040, 1040, 1040))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(247, 247, 247)))
                        .addGap(0, 126, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblNhanVien))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTongDoanhThu)
                    .addComponent(jLabel4)
                    .addComponent(lblSoHoaDon))
                .addContainerGap(224, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRevenueActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btnRevenueActionPerformed
        loadRevenue();
        bieuDo();
    }//GEN-LAST:event_btnRevenueActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadDate();
        DefaultTableModel Entity = new DefaultTableModel();
        Entity.setNumRows(0);
        tblRevenue.setModel(Entity);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        frameUtil.exit(this);
    }//GEN-LAST:event_formWindowClosing

    private void btnBackHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackHomeActionPerformed
        if(tk.getVaiTro().equals("QuanLi")){
            HomeQuanLi login = new HomeQuanLi(tk);
            this.setVisible(false);
            login.setVisible(true);
        }
        else {
            Login login = new Login();
            this.setVisible(false);
            login.setVisible(true);
        } 
        
        
    }//GEN-LAST:event_btnBackHomeActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
    try {
        loadRevenue();
        
        // Tạo parameters
        HashMap<String, Object> param = new HashMap<>();
        SimpleDateFormat dcn = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = dcn.format(jDateChooser1.getDate());
        String date2 = dcn.format(jDateChooser2.getDate());
        param.put("TuNgay", date1);
        param.put("DenNgay", date2);
        param.put("TongHD", lblSoHoaDon.getText());
        param.put("TongDoanhThu", lblTongDoanhThu.getText());
        
        // Lấy kết nối database
        Connection connectJsaper = new ConnectJsaper().getConnection();
        if (connectJsaper == null) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối database", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tìm file report
        String reportPath = null;
        
        // Thử tìm trong classpath
        java.net.URL reportUrl = getClass().getClassLoader().getResource("Report/Revenues.jrxml");
        if (reportUrl != null) {
            reportPath = reportUrl.getPath();
            System.out.println("Found report at: " + reportPath);
        }
        
        // Thử tìm trong thư mục resources
        if (reportPath == null) {
            File resourceFile = new File("src/main/resources/Report/Revenues.jrxml");
            if (resourceFile.exists()) {
                reportPath = resourceFile.getAbsolutePath();
                System.out.println("Found report at: " + reportPath);
            }
        }

        if (reportPath == null) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy file mẫu báo cáo (Revenues.jrxml)\n" +
                "Vui lòng kiểm tra file tồn tại trong thư mục Report",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Compile và fill report
        JasperReport report = JasperCompileManager.compileReport(reportPath);
        JasperPrint print = JasperFillManager.fillReport(report, param, connectJsaper);

        // Hiển thị report
        JasperViewer.viewReport(print, false);

    } catch (Exception e) {
        System.err.println("Error details:");
        e.printStackTrace();
        
        JOptionPane.showMessageDialog(this,
            "Lỗi khi tạo báo cáo: " + e.getMessage(),
            "Lỗi",
            JOptionPane.ERROR_MESSAGE);
    }
}

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Revenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Revenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Revenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Revenues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackHome;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRevenue;
    private javax.swing.JButton btnThongKe;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblSoHoaDon;
    private javax.swing.JLabel lblTongDoanhThu;
    private javax.swing.JTable tblRevenue;
    // End of variables declaration//GEN-END:variables
}
