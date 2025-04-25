package view;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import view.util.ConnectJsaper;
import view.util.FormatCurrency;
import view.util.FrameUtil;
import view.util.RMIUrl;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import entity.CT_HoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.LoaiSP;
import entity.NhanVien;
import entity.SanPham;
import entity.TaiKhoan;
import iRemote.ICT_HoaDon;
import iRemote.IHoaDon;
import iRemote.IKhachHang;
import iRemote.ILoaiSP;
import iRemote.INCC;
import iRemote.INhanVien;
import iRemote.ISanPham;
import iRemote.ITaiKhoan;

import java.awt.Frame;
import java.io.File;

//Frame Cập nhật hóa đơn
public class OrderForms extends javax.swing.JFrame implements Runnable {

    private Process processed;
    private Thread thread;

    private boolean AddDH = false, ChangeDH = false, thanhToan = true;
    private boolean AddSP = false, ChangeSP = false;
    private int soLuongBanDau;

    private boolean KH = false, SP = false;
    private ITaiKhoan taiKhoanDao;
    private String rmiUrl = new RMIUrl().RMIUrl();
    private FrameUtil frameUtil = new FrameUtil();
    private INCC nccDao;
    private ILoaiSP loaiSPDao;
    private ISanPham sanPhamDao;
    private ICT_HoaDon ctHDDao;
    private IKhachHang khachHangDao;
    private IHoaDon hoaDonDao;
    private INhanVien nhanVienDao;
    private NumberFormat numberFormat = new FormatCurrency().FormatCurrency();
    private TaiKhoan account;
    private SanPham product;

    public OrderForms(TaiKhoan tk) {
        setExtendedState(Frame.MAXIMIZED_BOTH);
        initComponents();
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        lblStatus.setForeground(Color.red);

        // Gọi đến đối tượng Process liên kết với csdl
        try {
            taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://" + rmiUrl + ":3030/iTaiKhoan");
            loaiSPDao = (ILoaiSP) Naming.lookup("rmi://" + rmiUrl + ":3030/iLoaiSP");
            sanPhamDao = (ISanPham) Naming.lookup("rmi://" + rmiUrl + ":3030/iSanPham");
            ctHDDao = (ICT_HoaDon) Naming.lookup("rmi://" + rmiUrl + ":3030/iCTHoaDon");
            hoaDonDao = (IHoaDon) Naming.lookup("rmi://" + rmiUrl + ":3030/iHoaDon");
            khachHangDao = (IKhachHang) Naming.lookup("rmi://" + rmiUrl + ":3030/iKhachHang");
            nhanVienDao = (INhanVien) Naming.lookup("rmi://" + rmiUrl + ":3030/iNhanVien");
            account = tk;
            lblNhanVien.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
            tableListOrder.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            tblOrderDetail.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            tblCTHD.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            Start();
            Disabled();
            loadMaKhachHang();
            loadTenKhachHang();
            setIFEmployee();
            loadMaHD();
            checkhoaDon();
            thanhToanBtn.setEnabled(false);
            jPanel1.setLayout(null);
            jPanel1.add(jPanel6);
            jPanel1.add(btnBackHome);
            jPanel1.add(jLabel2);
            jPanel1.add(btnCustomerFrame);
            jPanel1.add(jLabel25);
            jPanel1.add(cbxMaHD);
            jPanel1.add(jPanel7);
            jPanel1.add(btnRefresh);
            jPanel1.add(btnAdd);
            jPanel1.add(btnThemDonHang);
            jPanel1.add(btnChange);
            jPanel1.add(btnSuaDonHang);
            jPanel1.add(btnDelete);
            jPanel1.add(btnXuatDonHang);
            jPanel1.add(btnSave);
            jPanel1.add(thanhToanBtn);
            jPanel1.add(jPanel5);
            jPanel1.add(jScrollPane1);
            jPanel1.add(jLabel26);
            jPanel1.add(lblTongTien);
            jPanel1.add(jPanel4);
            jPanel1.add(lblStatus);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void Start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    // Kiểm tra hóa đơn
    private void checkhoaDon() {
        if (cbxMaHD.getItemCount() == 0) {
            cbxMaHD.setEnabled(false);
        } else {
            cbxMaHD.setEnabled(true);
        }

    }

    // Load thông tin loại sản phẩm
    private void loadClassifys() throws RemoteException {
        cbxClassify.removeAllItems();
        List<LoaiSP> arry = loaiSPDao.getAllLoaiSP();
        for (int i = 0; i < arry.size(); i++) {
            cbxClassify
                    .addItem(((LoaiSP) arry.get(i)).getMaLoaiSP() + " (" + ((LoaiSP) arry.get(i)).getTenLoaiSP() + ")");
        }
    }

    // Load thông tin sản phẩm
    private void loadProducts() throws RemoteException {
        cbxProduct.removeAllItems();
        String[] test = cbxClassify.getSelectedItem().toString().split("\\s");
        String sql = "SELECT * FROM SANPHAM WHERE loaiSP_MaLoaiSP = '" + test[0] + "'";
        List<SanPham> arry = sanPhamDao.findSP(sql);

        for (int i = 0; i < arry.size(); i++) {
            cbxProduct.addItem(((SanPham) arry.get(i)).getMaSP() + " (" + ((SanPham) arry.get(i)).getTenSP() + ")");
        }
    }


    // Load thông tin khách hàng
    private void loadIFCustomer() throws RemoteException {
        KhachHang khachHang = khachHangDao.getKhachHang(cbxMaKH.getSelectedItem().toString());

        cbxTenKH.setSelectedItem(khachHang.getTenKH());
        txbPhone.setText(khachHang.getSoDT());
        txbAddress.setText(khachHang.getDiaChi());
    }

    // Load thông tin hóa đơn
    private void loadIFOrder() throws RemoteException {
        HoaDon hoaDon = hoaDonDao.getHD(cbxMaHD.getSelectedItem().toString());
        KhachHang khachHang = khachHangDao.getKhachHang(hoaDon.getKhachHang().getMaKH());
        NhanVien nhanVien = nhanVienDao.getNhanVien(hoaDon.getNhanVien().getMaNV());
        txbEmployeeID.setText(nhanVien.getMaNV());
        cbxMaKH.setSelectedItem(khachHang.getMaKH());
        cbxTenKH.setSelectedItem(khachHang.getTenKH());
        txbPhone.setText(khachHang.getSoDT());
        txbAddress.setText(khachHang.getDiaChi());
        txbDate.setDate(hoaDon.getNgayBan());
    }

    // Hàm tính thành tiền hóa đơn
    private void Pays() throws RemoteException {
        lblTongTien.setText("0 VNĐ");
        HoaDon hoaDon = hoaDonDao.getHD(cbxMaHD.getSelectedItem().toString());
        String sql = "SELECT * FROM CT_HOADON WHERE MAHD = '" + hoaDon.getMaHD() + "'";
        double tongTien = 0;
        List<CT_HoaDon> ctHD = ctHDDao.findCTHD(sql);
        tongTien = hoaDon.getThanhTien(ctHD);
        lblTongTien.setText(numberFormat.format(tongTien));
    }

    private void loadOrderDetailFind(List<CT_HoaDon> arry) {
        tblCTHD.removeAll();
        tblCTHD.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        String[] arr = {"Mã Sản Phẩm", "Loại Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Thành Tiền"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        int Click = tableListOrder.getSelectedRow();
        TableModel model = tableListOrder.getModel();
        for (CT_HoaDon i : arry) {
            Vector vector = new Vector();
            vector.add(i.getSanPham().getMaSP());
            vector.add(i.getSanPham().getLoaiSP().getTenLoaiSP());
            vector.add(i.getSanPham().getTenSP());
            vector.add(i.getSoLuong());
            vector.add(numberFormat.format(i.getThanhTien()));
            modle.addRow(vector);
        }
        tblCTHD.setModel(modle);
    }

    // Hàm kiểm tra số lượng sản phẩm
    private void checkSLSP() throws RemoteException {
        if (cbxProduct.getSelectedItem() != null) {
            String maSP = cbxProduct.getSelectedItem().toString().split("\\s")[0];
            product = sanPhamDao.getSP(maSP);
            // Không có xử lý thêm sau khi lấy sản phẩm
        }
    }

    // load thông tin sản phẩm trong hóa đơn
    private void loadSPHoaDon() throws RemoteException {
        tblOrderDetail.removeAll();
        tblOrderDetail.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        String[] arr = {"Mã Sản Phẩm", "Loại Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Thành Tiền"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        System.out.println(cbxMaHD.getSelectedItem().toString());
        String sql = "SELECT * FROM CT_HOADON WHERE maHD = '" + cbxMaHD.getSelectedItem().toString() + "'";
        List<CT_HoaDon> arry = ctHDDao.findCTHD(sql);
        for (CT_HoaDon i : arry) {
            Vector vector = new Vector();
            SanPham sanPham = sanPhamDao.getSP(i.getSanPham().getMaSP());
            LoaiSP loaiSP = loaiSPDao.getLoaiSP(sanPham.getLoaiSP().getMaLoaiSP());
            vector.add(sanPham.getMaSP());
            vector.add(loaiSP.getTenLoaiSP());
            vector.add(sanPham.getTenSP());
            vector.add(i.getSoLuong());
            vector.add(numberFormat.format(i.getThanhTien()));
            modle.addRow(vector);
        }

        tblOrderDetail.setModel(modle);

    }

    // load thông tin hóa đơn ra bảng
    private void loadListHoaDon(List<HoaDon> arry) throws RemoteException {
        tableListOrder.removeAll();
        String[] arr = {"Mã Hóa Đơn", "Mã Nhân Viên", "Mã Khách Hàng", "Tên Khách Hàng", "Ngày Bán", "Tổng Tiền"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(modle);
        Vector vector = new Vector();
        for (HoaDon i : arry) {
            vector.add(i.getMaHD());
            vector.add(i.getNhanVien().getMaNV());
            vector.add(i.getKhachHang().getMaKH());
            vector.add(i.getKhachHang().getTenKH());
            vector.add(new SimpleDateFormat("dd/MM/yyyy").format(i.getNgayBan()));
            String sql = "SELECT * FROM CT_HOADON WHERE MAHD = '" + i.getMaHD() + "'";
            List<CT_HoaDon> ctHD = ctHDDao.findCTHD(sql);
            vector.add(numberFormat.format(i.getThanhTien(ctHD)));
            modle.addRow(vector);
        }
        tableListOrder.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tableListOrder.setModel(modle);
    }

    // Chuyển thành tiền từ kiểu chuỗi sang kiểu số
    private double convertedToNumbers(String s) {
        String number = "";
        String[] array = s.replace(",", " ").replace("VNĐ", " ").split("\\s");
        for (String i : array) {
            number = number.concat(i);
        }
        return Double.parseDouble(number);
    }

    // Cắt chuỗi
    private String cutChar(String arry) {
        return arry.replaceAll("\\D+", "");
    }

    // Bật các ô input nhập liệu hóa đơn
    private void Enabled() {
        cbxMaKH.setEnabled(true);
        cbxMaHD.setEnabled(true);
        cbxTenKH.setEnabled(true);
        txbAddress.setEnabled(true);
        txbPhone.setEnabled(true);
        txbDate.setEnabled(true);
        txbEmployeeID.setEnabled(true);
        lblStatus.setText("Trạng Thái!");
    }

    // Bật các ô nhập liệu sản phẩm trong hóa đơn
    private void EnabledAddSP() {
        cbxClassify.setEnabled(true);
        cbxProduct.setEnabled(true);
        txbAmount.setEnabled(true);
        txbPrice.setEnabled(true);
        txbIntoMoney.setEnabled(true);
        lblStatus.setText("Trạng Thái!");
    }

    // Tắt các ô input nhập liệu hóa đơn
    private void Disabled() {
        cbxClassify.setEnabled(false);
        cbxProduct.setEnabled(false);
        txbAmount.setEnabled(false);
        txbPrice.setEnabled(false);
        txbIntoMoney.setEnabled(false);
        cbxClassify.removeAllItems();
        cbxProduct.removeAllItems();
    }

    // Refresh lại các thông tin hóa đơn
    private void Refresh() throws RemoteException {
        checkhoaDon();
        AddDH = false;
        ChangeDH = false;
        AddSP = false;
        ChangeSP = false;
        thanhToan = true;
        txbAmount.setText("");
        txbPrice.setText("");
        txbIntoMoney.setText("");
        lblTongTien.setText("0 VNĐ");
        cbxClassify.removeAllItems();
        cbxProduct.removeAllItems();
        cbxMaHD.removeAllItems();
        btnDelete.setEnabled(false);
        btnChange.setEnabled(false);
        btnSave.setEnabled(false);
        btnSuaDonHang.setEnabled(false);
        btnAdd.setEnabled(false);
        thanhToanBtn.setEnabled(false);
        btnXuatDonHang.setEnabled(false);
        btnThemDonHang.setEnabled(true);
        txbEmployeeID.setText("");
        loadMaNhanVien();
        loadMaHD();
        Disabled();
    }

    // Refresh lại các thông tin sản phẩm
    private void refreshProduct() {
        txbPrice.setText("");
        txbAmount.setText("");
        txbIntoMoney.setText("");
        lblTongTien.setText("0 VNĐ");
    }

    // Thêm mới hóa đơn tự phát sinh mã
    private void newOrder() throws RemoteException {
        if (hoaDonDao.getAllHD().size() < 9) {
            cbxMaHD.addItem("HD" + "0000" + String.valueOf(hoaDonDao.getAllHD().size() + 1));
            cbxMaHD.setSelectedItem("HD" + "0000" + String.valueOf(hoaDonDao.getAllHD().size() + 1));
        } else {
            cbxMaHD.addItem("HD" + "000" + String.valueOf(hoaDonDao.getAllHD().size() + 1));
            cbxMaHD.setSelectedItem("HD" + "000" + String.valueOf(hoaDonDao.getAllHD().size() + 1));
        }
    }

    private void loadIFProducts() throws RemoteException {
        String[] test = cbxProduct.getSelectedItem().toString().split("\\s");

        SanPham sanPham = sanPhamDao.getSP(test[0]);
        txbPrice.setText(String.valueOf(sanPham.getGia()));
        cbxClassify.addItem(sanPham.getLoaiSP().getMaLoaiSP().trim() + " (" + sanPham.getLoaiSP().getTenLoaiSP() + ")");
    }

    // load Mã hóa đơn ra combobox
    private void loadMaHD() throws RemoteException {
        List<HoaDon> arry = hoaDonDao.getAllHD();
        for (int i = 0; i < arry.size(); i++) {
            this.cbxMaHD.addItem(((HoaDon) arry.get(i)).getMaHD());
        }
        AutoCompleteDecorator.decorate(cbxMaHD);
    }

    // Load thông tin nhân viên làm việc
    private void loadMaNhanVien() {
        txbEmployeeID.setText(account.getNhanVien().getMaNV());
    }

    // Load thông tin khách hàng từ mã khách hàng
    private void loadMaKhachHang() throws RemoteException {
        List<KhachHang> arry = khachHangDao.getAllKH();
        for (int i = 0; i < arry.size(); i++) {
            this.cbxMaKH.addItem(
                    ((KhachHang) arry.get(i)).getMaKH());
        }
        AutoCompleteDecorator.decorate(cbxMaKH);
    }

    // Load thông tin khách hàng từ tên khách hàng
    private void loadTenKhachHang() throws RemoteException {
        List<KhachHang> arry = khachHangDao.getAllKH();
        for (int i = 0; i < arry.size(); i++) {
            this.cbxTenKH.addItem(
                    ((KhachHang) arry.get(i)).getTenKH() + " (" + ((KhachHang) arry.get(i)).getSoDT().trim() + ")");
        }
        AutoCompleteDecorator.decorate(cbxTenKH);
    }

    // Tự động load thông tin khách hàng khi chọn mã khách hàng từ combobox
    private void automatedIDCustomer() throws RemoteException {
        String[] chuoi = cbxMaKH.getSelectedItem().toString().split("\\s");
        KhachHang customer = khachHangDao.getKhachHang(chuoi[0]);
        cbxTenKH.setSelectedItem(customer.getTenKH());
        txbAddress.setText(customer.getDiaChi());
        txbPhone.setText(customer.getSoDT());
    }

    // Tự động load thông tin khách hàng khi chọn tên khách hàng từ combobox
    private void automatedNameCustomer() throws RemoteException {
        String[] chuoi = cbxTenKH.getSelectedItem().toString().split("\\s");
        String tenKh = "";
        for (int i = 0; i < chuoi.length - 1; i++) {
            if (i == 0) {
                tenKh = chuoi[0];
            } else {
                tenKh += " " + chuoi[i];
            }

        }
        String sql = "Select * from KhachHang where TenKH = N'" + tenKh + "'";
        KhachHang customer = khachHangDao.findKH(sql).get(0);
        cbxMaKH.setSelectedItem(customer.getMaKH());
        txbAddress.setText(customer.getDiaChi());
        txbPhone.setText(customer.getSoDT());
    }

    // Lưu thông tin Nhân viên vào ô thông tin nhân viên trên Frame
    private void setIFEmployee() {
        lblTenNV.setText(account.getNhanVien().getHoTen());
        lblNgayLam.setText(String.valueOf(new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())));
    }

    // Cập nhật giờ làm việc
    private void updateTime() {
        lblThoiGianLamViec.setText(String.valueOf(new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())));
    }

    // Tìm kiếm hóa đơn
    private void FindOrder(String sql) throws RemoteException {
        List<HoaDon> arry = hoaDonDao.findHD(sql);
        loadListHoaDon(arry);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnCustomerFrame = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txbPrice = new javax.swing.JTextField();
        txbIntoMoney = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cbxProduct = new javax.swing.JComboBox<>();
        txbAmount = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cbxClassify = new javax.swing.JComboBox<>();
        lblStatus = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txbAddress = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txbPhone = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        cbxMaKH = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txbDate = new com.toedter.calendar.JDateChooser();
        txbEmployeeID = new javax.swing.JTextField();
        cbxTenKH = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        cbxMaHD = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        lblTenNV = new javax.swing.JLabel();
        lblNgayLam = new javax.swing.JLabel();
        lblThoiGianLamViec = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrderDetail = new javax.swing.JTable();
        btnBackHome = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnThemDonHang = new javax.swing.JButton();
        btnSuaDonHang = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnXuatDonHang = new javax.swing.JButton();
        thanhToanBtn = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        btnBackHome2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableListOrder = new javax.swing.JTable();
        btnRefresh1 = new javax.swing.JButton();
        txbSearch = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        searchBtn1 = new javax.swing.JButton();
        maHDrdo = new javax.swing.JRadioButton();
        ngayBanrdo = new javax.swing.JRadioButton();
        maNVrdo = new javax.swing.JRadioButton();
        tenKHrdo = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCTHD = new javax.swing.JTable();
        lblStatusSearch = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        setSize(new java.awt.Dimension(1920, 1080));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTabbedPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabbedPane1KeyPressed(evt);
            }
        });

        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1730, 1080));
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });

        btnCustomerFrame.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnCustomerFrame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Next-icon.png"))); // NOI18N
        btnCustomerFrame.setText("Khách Hàng");
        btnCustomerFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerFrameActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Bán Hàng");

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txbPrice.setEditable(false);
        txbPrice.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        txbIntoMoney.setEditable(false);
        txbIntoMoney.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel19.setText("Thành Tiền:");

        cbxProduct.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxProduct.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                try {
                    cbxProductPopupMenuWillBecomeInvisible(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        txbAmount.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbAmountKeyReleased(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel20.setText("Số Lượng:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel8.setText("Sản Phẩm:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel21.setText("Giá:");
        jLabel21.setToolTipText("");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel27.setText("Loại Sản Phẩm:");

        cbxClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxClassify.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                try {
                    cbxClassifyPopupMenuWillBecomeInvisible(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel19)
                                        .addComponent(jLabel27)
                                        .addComponent(jLabel20))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cbxClassify, 0, 228, Short.MAX_VALUE)
                                        .addComponent(txbAmount)
                                        .addComponent(txbIntoMoney))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel21)
                                        .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txbPrice)
                                        .addComponent(cbxProduct, 0, 254, Short.MAX_VALUE))
                                .addGap(95, 95, 95))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(cbxProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbxClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel27))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txbPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel21)
                                        .addComponent(txbAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txbIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel19))
                                .addContainerGap())
        );

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Trạng Thái");

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel3.setText("Tên KH:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText("Địa Chỉ:");

        txbAddress.setEditable(false);
        txbAddress.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel5.setText("SĐT:");

        txbPhone.setEditable(false);
        txbPhone.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbPhoneActionPerformed(evt);
            }
        });
        txbPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPhoneKeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel24.setText("Mã KH:");

        cbxMaKH.setEditable(true);
        cbxMaKH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxMaKH.setMaximumRowCount(10);
        cbxMaKH.setAutoscrolls(true);
        cbxMaKH.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                try {
                    cbxMaKHPopupMenuWillBecomeInvisible(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxMaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMaKHActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel22.setText("Nhân Viên:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel23.setText("Ngày Đặt:");

        txbDate.setDateFormatString("dd/MM/yyyy");
        txbDate.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        txbEmployeeID.setEditable(false);
        txbEmployeeID.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbEmployeeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbEmployeeIDActionPerformed(evt);
            }
        });

        cbxTenKH.setEditable(true);
        cbxTenKH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxTenKH.setMaximumRowCount(10);
        cbxTenKH.setAutoscrolls(true);
        cbxTenKH.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                try {
                    cbxTenKHPopupMenuWillBecomeInvisible(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxTenKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTenKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel22)
                                        .addComponent(jLabel23))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txbDate, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                                        .addComponent(cbxTenKH, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txbEmployeeID))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txbAddress)
                                        .addComponent(txbPhone)
                                        .addComponent(cbxMaKH, 0, 246, Short.MAX_VALUE))
                                .addGap(60, 60, 60))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txbEmployeeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel24)
                                        .addComponent(cbxMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel5)
                                                .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cbxTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel23)
                                        .addComponent(txbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(txbDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel25.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel25.setText("Mã Hóa Đơn:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel26.setFont(new java.awt.Font("Tahoma", 3, 20)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Tổng Tiền Đơn Hàng:");

        lblTongTien.setFont(new java.awt.Font("Tahoma", 3, 20)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 0, 0));
        lblTongTien.setText("0 VNĐ");
        lblTongTien.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        cbxMaHD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbxMaHD.setMaximumRowCount(10);
        cbxMaHD.setAutoscrolls(true);
        cbxMaHD.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                try {
                    cbxMaHDPopupMenuWillBecomeInvisible(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMaHDActionPerformed(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTenNV.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lblTenNV.setForeground(new java.awt.Color(255, 0, 51));
        lblTenNV.setText("Name");

        lblNgayLam.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lblNgayLam.setForeground(new java.awt.Color(255, 0, 51));
        lblNgayLam.setText("Date");

        lblThoiGianLamViec.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lblThoiGianLamViec.setForeground(new java.awt.Color(255, 0, 51));
        lblThoiGianLamViec.setText("Time");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Clock-icon.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 51));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/calendar-icon.png"))); // NOI18N
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 51));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel9.setText("Tên Nhân Viên:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("Ngày Làm Việc:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("Giờ Làm Việc:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(7, 7, 7)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblThoiGianLamViec, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNgayLam, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTenNV)
                                        .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lblNgayLam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jLabel13))
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblThoiGianLamViec, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(32, 32, 32))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel7)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        tblOrderDetail.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblOrderDetail.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String[]{
                        "Mã Sản Phẩm", "Loại Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Thành Tiền"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblOrderDetail.setRowHeight(22);
        tblOrderDetail.setRowMargin(2);
        tblOrderDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    tblOrderDetailMouseClicked(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        jScrollPane1.setViewportView(tblOrderDetail);

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Previous-icon.png"))); // NOI18N
        btnBackHome.setText("Hệ Thống");
        btnBackHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackHomeMouseClicked(evt);
            }
        });
        btnBackHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeActionPerformed(evt);
            }
        });

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    btnRefreshMouseClicked(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnRefreshActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnThemDonHang.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnThemDonHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnThemDonHang.setText("Đơn Hàng");
        btnThemDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnThemDonHangActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnSuaDonHang.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSuaDonHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change.png"))); // NOI18N
        btnSuaDonHang.setText("Đơn Hàng");
        btnSuaDonHang.setEnabled(false);
        btnSuaDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaDonHangActionPerformed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnAdd.setText("Sản Phẩm");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnAddActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnChange.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change.png"))); // NOI18N
        btnChange.setText("Sản Phẩm");
        btnChange.setEnabled(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Delete.png"))); // NOI18N
        btnDelete.setText("Sản Phẩm");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnDeleteActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Save.png"))); // NOI18N
        btnSave.setText("Lưu");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnSaveActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnXuatDonHang.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnXuatDonHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Print Sale.png"))); // NOI18N
        btnXuatDonHang.setText("Xuất Hóa Đơn");
        btnXuatDonHang.setEnabled(false);
        btnXuatDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatDonHangActionPerformed(evt);
            }
        });

        thanhToanBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        thanhToanBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Pay.png"))); // NOI18N
        thanhToanBtn.setText("Thanh Toán");
        thanhToanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thanhToanBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 859, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(36, 36, 36)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnThemDonHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnChange, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(63, 63, 63)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnSuaDonHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(65, 65, 65)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnXuatDonHang, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(37, 37, 37)
                                                .addComponent(thanhToanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(102, 102, 102))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel25)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbxMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 1439, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(117, 117, 117)
                                                .addComponent(btnCustomerFrame)
                                                .addGap(120, 120, 120))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(31, 31, 31))
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 1550, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1715, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(btnCustomerFrame, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(60, 60, 60)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(btnThemDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(btnSuaDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(btnXuatDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)))
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(12, 12, 12))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18))))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel25)
                                                        .addComponent(cbxMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addComponent(thanhToanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(81, 81, 81)))))
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel26)
                                        .addComponent(lblTongTien))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 801, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1687, 1687, 1687))
        );

        jTabbedPane1.addTab("Bán Hàng", jPanel1);

        jPanel15.setPreferredSize(new java.awt.Dimension(1797, 1080));

        btnBackHome2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        btnBackHome2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Previous-icon.png"))); // NOI18N
        btnBackHome2.setText("Hệ Thống");
        btnBackHome2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackHome2MouseClicked(evt);
            }
        });
        btnBackHome2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHome2ActionPerformed(evt);
            }
        });
        btnBackHome2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBackHome2KeyPressed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Danh Sách Hóa Đơn");

        tableListOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableListOrder.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "Mã Hóa Đơn", "Mã Nhân Viên", "Mã Khách Hàng", "Tên Khách Hàng", "Ngày Bán", "Tổng Tiền Hóa Đơn"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tableListOrder.setAlignmentX(1.0F);
        tableListOrder.setAlignmentY(1.0F);
        tableListOrder.setRowHeight(24);
        tableListOrder.setRowMargin(2);
        tableListOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    tableListOrderMouseClicked(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        jScrollPane4.setViewportView(tableListOrder);

        btnRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefresh1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefresh1MouseClicked(evt);
            }
        });
        btnRefresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh1ActionPerformed(evt);
            }
        });

        txbSearch.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbSearch.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txbSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txbSearchFocusGained(evt);
            }
        });
        txbSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbSearchActionPerformed(evt);
            }
        });
        txbSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txbSearchKeyPressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 51));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel11.setText("Nhân Viên");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien.setText("(BH01) Nguyễn Thị Diễm Phúc");

        searchBtn1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        searchBtn1.setText("Tìm Kiếm");
        searchBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    searchBtn1ActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        buttonGroup1.add(maHDrdo);
        maHDrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        maHDrdo.setSelected(true);
        maHDrdo.setText("Theo Mã Hóa Đơn");
        maHDrdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maHDrdoActionPerformed(evt);
            }
        });

        buttonGroup1.add(ngayBanrdo);
        ngayBanrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        ngayBanrdo.setText("Theo Ngày Bán");
        ngayBanrdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ngayBanrdoActionPerformed(evt);
            }
        });

        buttonGroup1.add(maNVrdo);
        maNVrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        maNVrdo.setText("Theo Mã Nhân Viên");
        maNVrdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maNVrdoActionPerformed(evt);
            }
        });

        buttonGroup1.add(tenKHrdo);
        tenKHrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tenKHrdo.setText("Theo Tên Khách Hàng");
        tenKHrdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tenKHrdoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Tìm Kiếm Hóa Đơn Theo:");

        tblCTHD.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblCTHD.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String[]{
                        "Mã Sản Phẩm", "Loại Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Thành Tiền"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblCTHD.setRowHeight(24);
        tblCTHD.setRowMargin(2);
        jScrollPane2.setViewportView(tblCTHD);

        lblStatusSearch.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblStatusSearch.setForeground(new java.awt.Color(255, 51, 51));
        lblStatusSearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatusSearch.setText("Thông Báo");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(btnBackHome2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 1530, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                                .addComponent(btnRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(56, 56, 56)
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(maHDrdo)
                                                                        .addComponent(maNVrdo))
                                                                .addGap(42, 42, 42)
                                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(ngayBanrdo)
                                                                        .addComponent(tenKHrdo)))
                                                        .addComponent(jScrollPane4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                                .addComponent(txbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(29, 29, 29)
                                                                .addComponent(searchBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(lblStatusSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 933, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 873, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnBackHome2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(lblNhanVien))
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(btnRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(tenKHrdo)
                                                                        .addComponent(jLabel1)
                                                                        .addComponent(maHDrdo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(14, 14, 14))
                                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(txbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(searchBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)))
                                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(maNVrdo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ngayBanrdo)
                                                        .addComponent(lblStatusSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1659, Short.MAX_VALUE)
                                        .addComponent(jScrollPane4))
                                .addGap(1379, 1379, 1379))
        );

        jTabbedPane1.addTab("Tìm Kiếm Hóa Đơn", jPanel15);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1797, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 3386, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jTabbedPane1)
                                        .addContainerGap()))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Cập nhật đơn hàng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1730, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxTenKHActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        frameUtil.exit(this);
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
    }

    private void jTabbedPane1KeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            btnCustomerFrame.doClick();
        }
    }

    private void btnRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        tableListOrder.removeAll();
        String[] arr = {"Mã Hóa Đơn", "Mã Nhân Viên", "Mã Khách Hàng", "Tên Khách Hàng", "Ngày Bán", "Tổng Tiền"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] arr1 = {"Mã Sản Phẩm", "Loại Sản Phẩm", "Tên Sản Phẩm", "Số Lượng", "Thành Tiền"};
        DefaultTableModel modle1 = new DefaultTableModel(arr1, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableListOrder.setModel(modle);
        tblCTHD.setModel(modle1);
        txbSearch.setText("");
    }

    private void btnRefresh1MouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void searchBtn1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {        // TODO add your handling code here:
        String sql = "";
        lblStatusSearch.setText("Thông Báo");
        if (!txbSearch.getText().equals("")) {

            if (maHDrdo.isSelected()) {
                sql = "SELECT * FROM HOADON WHERE MaHD like '%" + txbSearch.getText() + "%' ORDER BY NgayBan DESC";
                FindOrder(sql);
            } else if (maNVrdo.isSelected()) {
                sql = "SELECT * FROM HOADON WHERE nhanVien_MaNv like '%" + txbSearch.getText() + "%' ORDER BY NgayBan DESC";
                FindOrder(sql);
            } else if (tenKHrdo.isSelected()) {
                sql = "SELECT * FROM HoaDon JOIN KhachHang ON HoaDon.khachHang_MaKH = KhachHang.MaKH\n"
                        + " WHERE TenKH LIKE N'%" + txbSearch.getText() + "%' ORDER BY NgayBan DESC";
                FindOrder(sql);
            } else if (ngayBanrdo.isSelected()) {
                String regex = "\\\\d{2}/\\\\d{2}/\\\\d{4}$";
                if (!txbSearch.getText().matches(regex)) {
                    lblStatusSearch.setText("Vui lòng nhập ngày tháng theo định dạng dd/MM/yyyy");
                }
                sql = "SELECT * FROM HOADON WHERE convert(varchar, NgayBan, 103) LIKE '%" + this.txbSearch.getText()
                        + "%' ORDER BY NgayBan DESC";
                FindOrder(sql);
            }
        } else {
            lblStatusSearch.setText("Vui lòng nhập thông tin cần tìm vào ô tìm kiếm");
        }
    }

    private void txbSearchKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchBtn1.doClick();
        }
    }

    private void txbSearchActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void txbSearchFocusGained(java.awt.event.FocusEvent evt) {        // TODO add your handling code here:

    }

    private void btnBackHome2KeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            btnBackHome2.doClick();
        }
    }

    private void btnBackHome2ActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        HomeBanHang home = new HomeBanHang(account);
        this.setVisible(false);
        home.setVisible(true);
    }

    private void btnBackHome2MouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
    }

    private void btnBackHomeActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        HomeBanHang home = new HomeBanHang(account);
        this.setVisible(false);
        home.setVisible(true);
    }

    private void btnBackHomeMouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void tblOrderDetailMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {
        cbxProduct.removeAllItems();
        cbxClassify.removeAllItems();

        int Click = tblOrderDetail.getSelectedRow();
        TableModel Entity = tblOrderDetail.getModel();

        cbxProduct.addItem(Entity.getValueAt(Click, 0).toString());
        cbxClassify.addItem(Entity.getValueAt(Click, 1).toString());
        txbAmount.setText(Entity.getValueAt(Click, 3).toString());
        txbIntoMoney.setText(Entity.getValueAt(Click, 4).toString());

        loadIFProducts();
        if (thanhToan == false) {
            btnChange.setEnabled(true);
            btnDelete.setEnabled(true);
            btnAdd.setEnabled(true);
        }

    }

    private void cbxMaHDActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void cbxMaHDPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) throws RemoteException {        // TODO add your handling code here:
        loadIFOrder();
        loadSPHoaDon();
        Pays();
        thanhToanBtn.setEnabled(true);
        btnSuaDonHang.setEnabled(true);
        btnAdd.setEnabled(true);
        btnXuatDonHang.setEnabled(true);
        if (thanhToan == true) {
            btnSuaDonHang.setEnabled(false);
            btnAdd.setEnabled(false);
            btnChange.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnXuatDonHang.setEnabled(false);
            thanhToanBtn.setEnabled(false);
        }
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        thanhToan = false;
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm khỏi hóa đơn hay không?", "Thông Báo",
                2);
        String[] arry1 = cbxProduct.getSelectedItem().toString().split("\\s");
        SanPham sanPham = sanPhamDao.getSP(arry1[0]);
        soLuongBanDau = sanPham.getSoLuong();
        if (Click == JOptionPane.YES_OPTION) {
            sanPham.setSoLuong(soLuongBanDau + Integer.parseInt(txbAmount.getText()));
            sanPhamDao.updateSanPham(sanPham);
            ctHDDao.deleteSPHoaDon(cbxMaHD.getSelectedItem().toString(), arry1[0]);
            loadSPHoaDon();
            Pays();
            btnAdd.setEnabled(true);
            btnChange.setEnabled(false);
            btnDelete.setEnabled(false);
            this.lblStatus.setText("Xóa sản phẩm thành công thành công!");
        }
    }

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {
        AddSP = false;
        KH = false;
        SP = true;
        ChangeSP = true;
        thanhToan = false;
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnChange.setEnabled(false);
        btnSave.setEnabled(true);
        txbAmount.setEnabled(true);
        txbPrice.setEnabled(true);
        txbIntoMoney.setEnabled(true);
        soLuongBanDau = Integer.parseInt(txbAmount.getText());
    }

    private void btnSuaDonHangActionPerformed(java.awt.event.ActionEvent evt) {
        ChangeDH = true;
        AddDH = false;
        KH = true;
        SP = false;
        btnThemDonHang.setEnabled(false);
        btnSuaDonHang.setEnabled(false);
        btnAdd.setEnabled(false);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        loadMaNhanVien();
        Enabled();
    }

    private void btnThemDonHangActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn tạo 1 hóa đơn bán hàng mới hay không?",
                "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            Refresh();
            AddDH = true;
            ChangeDH = false;
            thanhToan = false;
            KH = true;
            btnThemDonHang.setEnabled(true);
            btnSave.setEnabled(true);
            Enabled();
            newOrder();
            cbxMaHD.setEnabled(false);
            AutoCompleteDecorator.decorate(cbxMaHD);


        }
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        thanhToan = false;
        if (KH == true) {
            String[] arry2 = cbxMaKH.getSelectedItem().toString().split("\\s");
            String[] chuoi = cbxTenKH.getSelectedItem().toString().split("\\s");
            String tenKh = "";
            for (int i = 0; i < chuoi.length - 1; i++) {
                if (i == 0) {
                    tenKh = chuoi[0];
                } else {
                    tenKh += " " + chuoi[i];
                }
            }
            Date ngayHienTai = (new java.util.Date()); // Lấy ngày hiện tại
            Date ngayBan = txbDate.getDate(); // Lấy ngày bán hóa đơn
            String strNgayBan = new SimpleDateFormat("dd/MM/yyyy").format(ngayBan); // format ngày bán
            String strNgayHienTai = new SimpleDateFormat("dd/MM/yyyy").format(ngayHienTai); // format ngày hiện tại
            if (AddDH == true) {
                if (ngayBan.after(ngayHienTai) || strNgayBan.equals(strNgayHienTai)) { // Nếu ngày hiện tại lớn hơn hoặc
                    // bằng với ngày hiện tại
                    newOrder();
                    NhanVien nhanVien = nhanVienDao.getNhanVien(txbEmployeeID.getText());
                    KhachHang khachHang = khachHangDao.getKhachHang(arry2[0]);
                    HoaDon hoaDon = new HoaDon(cbxMaHD.getSelectedItem().toString(), nhanVien, khachHang, new java.sql.Date(txbDate.getDate().getTime()));
                    hoaDonDao.addHoaDon(hoaDon); // Thêm hóa đơn
                    checkhoaDon();// Kiểm tra hóa đơn
                    Refresh();
                    cbxMaHD.setSelectedIndex(cbxMaHD.getItemCount() - 1); // Truyền tới hóa đơn vừa tạo

                    loadSPHoaDon(); // load các sản phẩm trong hóa đơn
                    Pays(); // Tính thành tiền
                    btnThemDonHang.setEnabled(false);
                    btnSuaDonHang.setEnabled(true);
                    btnAdd.setEnabled(true);
                    btnXuatDonHang.setEnabled(true);
                    thanhToanBtn.setEnabled(true);
                    lblStatus.setText("Thêm đơn đặt hàng thành công!!");
                    cbxMaHD.setEnabled(false);
                } else {
                    lblStatus.setText("Ngày bán hàng nhỏ hơn ngày hiện tại!!");
                }

            } else if (ChangeDH == true) {
                if (ngayBan.after(ngayHienTai) || strNgayBan.equals(strNgayHienTai)) { // Nếu ngày hiện tại lớn hơn hoặc
                    // bằng với ngày hiện tại
                    loadMaNhanVien();
                    NhanVien nhanVien = nhanVienDao.getNhanVien(txbEmployeeID.getText());
                    KhachHang khachHang = khachHangDao.getKhachHang(arry2[0]);
                    HoaDon hoaDon = new HoaDon(cbxMaHD.getSelectedItem().toString(), nhanVien, khachHang, new java.sql.Date(txbDate.getDate().getTime()));
                    hoaDonDao.updateHoaDon(hoaDon); // Sửa hóa đơn
                    Refresh();
                    Pays();
                    Disabled();
                    cbxMaHD.setSelectedIndex(cbxMaHD.getItemCount() - 1);
                    cbxMaHD.setEnabled(false);
                    btnSave.setEnabled(false);
                    btnAdd.setEnabled(true);
                    thanhToanBtn.setEnabled(true);
                    btnXuatDonHang.setEnabled(true);
                    thanhToan = false;
                    lblStatus.setText("Sửa đơn đặt hàng thành công!!");
                } else {
                    lblStatus.setText("Ngày bán hàng nhỏ hơn ngày hiện tại!!");
                }

            }
        } else if (SP == true) {
            String[] arry1 = cbxProduct.getSelectedItem().toString().split("\\s"); // Chọn sản phẩm
            product = sanPhamDao.getSP(arry1[0]); // Lấy thông tin sản phẩm
            int soLuongSP = product.getSoLuong();
            if (AddSP == true) { // Thêm sản phẩm

                if (!ctHDDao.checkSPHoaDon(cbxMaHD.getSelectedItem().toString(), arry1[0])) { // Kiểm tra sản phẩm hóa
                    // đơn
                    if (Integer.parseInt(txbAmount.getText()) <= 0) {
                        lblStatus.setText("Số lượng sản phẩm phải lớn hơn không!!");
                    } else if (Integer.parseInt(txbAmount.getText()) > product.getSoLuong()) {
                        lblStatus.setText("Số lượng sản phẩm bán phải ít hơn số lượng sản phẩm tồn kho!!");
                    } else {
                        HoaDon hoaDon = hoaDonDao.getHD(cbxMaHD.getSelectedItem().toString());
                        SanPham sanPham = sanPhamDao.getSP(arry1[0]);
                        CT_HoaDon ctHD = new CT_HoaDon(hoaDon, sanPham, Integer.parseInt(txbAmount.getText()));
                        ctHDDao.addCTHD(ctHD); // Thêm sản phẩm vào chi
                        // tiết hóa đơn
                        loadSPHoaDon(); // load các sản phẩm trong hóa đơn
                        Disabled();
                        sanPham.setSoLuong(soLuongSP - Integer.parseInt(txbAmount.getText()));
                        sanPhamDao.updateSanPham(sanPham);
                        Pays();
                        btnSave.setEnabled(false);
                        btnAdd.setEnabled(true);
                        lblStatus.setText("Thêm thông tin sản phẩm thành công!!");
                    }

                } else {
                    lblStatus.setText("Sản phẩm đã tồn tại trong đơn hàng!!");
                }
            } else if (ChangeSP == true) { // sửa sản phẩm
                if (Integer.parseInt(txbAmount.getText()) <= 0) {
                    lblStatus.setText("Số lượng sản phẩm phải lớn hơn không!!");
                } else if (Integer.parseInt(txbAmount.getText()) > product.getSoLuong()) {
                    lblStatus.setText("Số lượng sản phẩm bán phải ít hơn số lượng sản phẩm tồn kho!!");
                } else {
                    HoaDon hoaDon = hoaDonDao.getHD(cbxMaHD.getSelectedItem().toString());
                    SanPham sanPham = sanPhamDao.getSP(arry1[0]);
                    CT_HoaDon ctHD = new CT_HoaDon(hoaDon, sanPham, Integer.parseInt(txbAmount.getText()));
                    ctHDDao.updateCTHD(ctHD); // sửa sản phẩm trong hóa
                    // đơn
                    sanPham.setSoLuong(soLuongSP - Integer.parseInt(txbAmount.getText()));
                    sanPhamDao.updateSanPham(sanPham); // update lại số lượng
                    // sản phẩm
                    loadSPHoaDon(); // load các sản phẩm trong hóa đơn
                    Disabled();
                    Pays(); // tính toán thành tiền
                    btnSave.setEnabled(false);
                    btnAdd.setEnabled(true);
                    lblStatus.setText("Sửa sản phẩm thành công !!");
                }

            }
        }
        // checkDH();
    }

    private void btnXuatDonHangActionPerformed(java.awt.event.ActionEvent evt) {
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xuất hóa đơn hay không?", "Thông Báo", 2);

        if (Click == JOptionPane.YES_OPTION) {
            try {
                // Debug: In ra thông tin để kiểm tra
                System.out.println("Current directory: " + System.getProperty("user.dir"));

                // Tạo parameters
                HashMap<String, Object> param = new HashMap<>();
                param.put("MaHD", cbxMaHD.getSelectedItem().toString());
                param.put("TongTien", lblTongTien.getText());

                // Lấy kết nối database
                Connection connectJsaper = new ConnectJsaper().getConnection();
                if (connectJsaper == null) {
                    JOptionPane.showMessageDialog(this, "Không thể kết nối database", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tìm file report
                String reportPath = null;

                // Thử tìm trong classpath
                java.net.URL reportUrl = getClass().getClassLoader().getResource("Report/HoaDon.jrxml");
                if (reportUrl != null) {
                    reportPath = reportUrl.getPath();
                    System.out.println("Found report at: " + reportPath);
                }

                // Thử tìm trong thư mục resources
                if (reportPath == null) {
                    File resourceFile = new File("src/main/resources/Report/HoaDon.jrxml");
                    if (resourceFile.exists()) {
                        reportPath = resourceFile.getAbsolutePath();
                        System.out.println("Found report at: " + reportPath);
                    }
                }

                if (reportPath == null) {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy file mẫu báo cáo (HoaDon.jrxml)\n" +
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
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        AddSP = true;
        KH = false;
        SP = true;
        ChangeSP = false;
        txbAmount.requestFocus();
        btnAdd.setEnabled(false);
        btnSave.setEnabled(true);
        EnabledAddSP();
        loadClassifys();
    }

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {        // TODO add your handling code here:
        Refresh();
        DefaultTableModel Entity = new DefaultTableModel();
        Entity.setNumRows(0);
        tblOrderDetail.setModel(Entity);
    }

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {
        Refresh();
    }

    private void cbxTenKHPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) throws RemoteException {        // TODO add your handling code here:
        automatedNameCustomer();
    }

    private void txbEmployeeIDActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void cbxMaKHActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void cbxMaKHPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) throws RemoteException {
        automatedIDCustomer();
    }

    private void txbPhoneKeyReleased(java.awt.event.KeyEvent evt) {        // txbPhone.setText(cutChar(txbPhone.getText()));
    }

    private void txbPhoneActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void cbxClassifyPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) throws RemoteException {        // TODO add your handling code here:
        loadProducts();
        if (cbxProduct.getItemCount() == 0) {
            cbxProduct.setEnabled(false);
            txbAmount.setEnabled(false);
            refreshProduct();
        } else {
            refreshProduct();
            cbxProduct.setEnabled(true);
        }
    }

    private void txbAmountKeyReleased(java.awt.event.KeyEvent evt) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txbAmount.setText(cutChar(txbAmount.getText()));
        if (txbAmount.getText().equals("")) {
            String[] s = txbPrice.getText().split("\\s");
            txbIntoMoney.setText("0" + " " + s[1]);
        } else if (product != null) { // Thêm kiểm tra null
            int soluong = Integer.parseInt(txbAmount.getText());
            txbPrice.setText(numberFormat.format(product.getGia()));
            double thanhTien = product.getGia() * soluong;
            txbIntoMoney.setText(numberFormat.format(thanhTien));
        }
    }

    private void cbxProductPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) throws RemoteException {        //loadProducts();
        txbAmount.setEnabled(true);
        checkSLSP();
    }

    private void btnCustomerFrameActionPerformed(java.awt.event.ActionEvent evt) {
        CustomerFrame customerFrame = new CustomerFrame(account);
        this.setVisible(false);
        customerFrame.setVisible(true);
    }

    private void maHDrdoActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void ngayBanrdoActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void maNVrdoActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void tenKHrdoActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void tableListOrderMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {        // TODO add your handling code here:
        int Click = tableListOrder.getSelectedRow();
        TableModel model = tableListOrder.getModel();
        String sql = "SELECT * FROM CT_HoaDon where MaHD like N'%" + model.getValueAt(Click, 0).toString() + "'";
        List<CT_HoaDon> arr = ctHDDao.findCTHD(sql);
        loadOrderDetailFind(arr);
        // FindProducts(sql);
    }

    private void thanhToanBtnActionPerformed(java.awt.event.ActionEvent evt) {
        thanhToan = true;
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn thanh toán hóa đơn không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            btnSuaDonHang.setEnabled(false);
            btnAdd.setEnabled(false);
            btnChange.setEnabled(false);
            btnDelete.setEnabled(false);
            btnXuatDonHang.setEnabled(false);
            btnSave.setEnabled(false);
            btnThemDonHang.setEnabled(true);
            thanhToanBtn.setEnabled(false);
            cbxMaHD.setEnabled(true);
            lblStatus.setText("Thanh Toán Hóa Đơn Thành Công");
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrderForms.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderForms.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderForms.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderForms.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaiKhoan account = new TaiKhoan();
                new OrderForms(account).setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBackHome;
    private javax.swing.JButton btnBackHome2;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnCustomerFrame;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefresh1;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSuaDonHang;
    private javax.swing.JButton btnThemDonHang;
    private javax.swing.JButton btnXuatDonHang;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxClassify;
    private javax.swing.JComboBox<String> cbxMaHD;
    private javax.swing.JComboBox<String> cbxMaKH;
    private javax.swing.JComboBox<String> cbxProduct;
    private javax.swing.JComboBox<String> cbxTenKH;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblNgayLam;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusSearch;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JLabel lblThoiGianLamViec;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JRadioButton maHDrdo;
    private javax.swing.JRadioButton maNVrdo;
    private javax.swing.JRadioButton ngayBanrdo;
    private javax.swing.JButton searchBtn1;
    private javax.swing.JTable tableListOrder;
    private javax.swing.JTable tblCTHD;
    private javax.swing.JTable tblOrderDetail;
    private javax.swing.JRadioButton tenKHrdo;
    private javax.swing.JButton thanhToanBtn;
    private javax.swing.JTextField txbAddress;
    private javax.swing.JTextField txbAmount;
    private com.toedter.calendar.JDateChooser txbDate;
    private javax.swing.JTextField txbEmployeeID;
    private javax.swing.JTextField txbIntoMoney;
    private javax.swing.JTextField txbPhone;
    private javax.swing.JTextField txbPrice;
    private javax.swing.JTextField txbSearch;
    // End of variables declaration//GEN-END:variables

    public void run() {
        while (true) {
            updateTime();
            try {
                Thread.sleep(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
