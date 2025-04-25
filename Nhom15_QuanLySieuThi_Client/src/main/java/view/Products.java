package view;

import java.awt.Color;

import javax.swing.JFrame;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import view.util.ConnectJsaper;
import view.util.FrameUtil;
import view.util.RMIUrl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import entity.NCC;
import entity.SanPham;
import entity.TaiKhoan;
import entity.CT_HoaDon;
import entity.LoaiSP;
import iRemote.ICT_HoaDon;
import iRemote.ILoaiSP;
import iRemote.INCC;
import iRemote.ISanPham;
import iRemote.ITaiKhoan;

import java.util.HashMap;
import java.util.List;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

//Frame Cập Nhật Sản Phẩm
public class Products extends javax.swing.JFrame {

	private Process processed; // Khai báo controller
	private Boolean Add = false, Change = false; // Khai báo trạng thái thêm hoặc sửa
	private ITaiKhoan taiKhoanDao;
	private TaiKhoan account;
	private String rmiUrl = new RMIUrl().RMIUrl();
	private Connection connectJsaper = new ConnectJsaper().getConnection();
	private FrameUtil frameUtil = new FrameUtil();
	private INCC nccDao;
	private ILoaiSP loaiSPDao;
	private ISanPham sanPhamDao;
	private ICT_HoaDon ctHDDao;

	public Products(TaiKhoan tk) {
		initComponents();
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		lblStatus.setForeground(Color.red);
		lblStatus1.setForeground(Color.red);
		lblStatus2.setForeground(Color.red);
		tblProduct.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		try {
			taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://" + rmiUrl +":3030/iTaiKhoan");
			nccDao = (INCC) Naming.lookup("rmi://" + rmiUrl +":3030/iNCC");
			loaiSPDao = (ILoaiSP) Naming.lookup("rmi://" + rmiUrl +":3030/iLoaiSP");
			sanPhamDao = (ISanPham) Naming.lookup("rmi://" + rmiUrl +":3030/iSanPham");
			ctHDDao = (ICT_HoaDon) Naming.lookup("rmi://" + rmiUrl +":3030/iCTHoaDon");
			account = tk;
			lblNhanVien.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
			lblNhanVien1.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
			lblNhanVien2.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
			lblNhanVien3.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
			loadMaLoai(); // Load thông tin mã loại sản phẩm
			loadMaNCC(); // Load thông tin mã nhà cung cấp
			Disabled(); // Tắt các ô input ở tab cập nhật sản phẩm
			DisabledClassify();// Tắt các ô input ở tab cập nhật loại sản phẩm
			DisabledProducer(); // Tắt các ô input ở tab cập nhật nhà cung cấp
			loadProducts();//Load các sản phẩm ra bảng ở tab cập nhật loại sản phẩm
			loadTableLoaiSP();// Load các loại sản phẩm ra bảng ở tab cập nhật loại sản phẩm
			loadProducers();// Load các nhà cung cấp ra bảng ở tab cập nhật nhà cung cấp
			tblListProduct.removeAll();
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

	// Bật các ô input ở ở tab cập nhật sản phẩm
	private void Enabled() {
		txbID.setEnabled(true);
		txbName.setEnabled(true);
		txbAmount.setEnabled(true);
		txbPrice.setEnabled(true);
		cbxClassify.setEnabled(true);
		btnUpdate.setEnabled(true);
		cbxProducer.setEnabled(true);
		txbLink.setEnabled(true);
		txbUnit.setEnabled(true);
		lblStatus.setText("Trạng Thái!");
	}

	// Bật các ô input ở ở tab cập nhật loại sản phẩm
	private void EnabledClassify() {
		txbIDClassify.setEnabled(true);
		txbClassify.setEnabled(true);
		lblStatus1.setText("Trạng Thái!");
	}

	// Bật các ô input ở ở tab cập nhật nhà cung cấp
	private void EnabledProducer() {
		txbIDProducer.setEnabled(true);
		txbProducer.setEnabled(true);
		txbAdress.setEnabled(true);
		txbPhone.setEnabled(true);
		lblStatus2.setText("Trạng Thái!");
	}

	// Tắt các ô input ở tab cập nhật nhà cung cấp
	private void DisabledProducer() {
		txbIDProducer.setEnabled(false);
		txbProducer.setEnabled(false);
		txbAdress.setEnabled(false);
		txbPhone.setEnabled(false);
	}

	// Tắt các ô input ở tab cập nhật loại sản phẩm
	private void DisabledClassify() {
		txbIDClassify.setEnabled(false);
		txbClassify.setEnabled(false);
	}

	// Tắt các ô input ở tab cập nhật sản phẩm
	public void Disabled() {
		txbID.setEnabled(false);
		txbName.setEnabled(false);
		txbAmount.setEnabled(false);
		txbPrice.setEnabled(false);
		txbUnit.setEnabled(false);
		cbxClassify.setEnabled(false);
		cbxProducer.setEnabled(false);
		btnUpdate.setEnabled(false);
		txbLink.setEnabled(false);
	}

	// Hàm Refresh thông tin
	public void Refresh() throws RemoteException {
		cbxClassify.removeAllItems();
		cbxProducer.removeAllItems();
		txbID.setText("");
		txbName.setText("");
		txbAmount.setText("");
		txbPrice.setText("");
		txbLink.setText("");
		txbUnit.setText("");

		txbIDProducer.setText("");
		txbProducer.setText("");
		txbAdress.setText("");
		txbPhone.setText("");
		txbIDClassify.setText("");
		txbClassify.setText("");

		lblStatus2.setText("Trạng Thái!");
		lblStatus1.setText("Trạng Thái!");
		lblStatus.setText("Trạng Thái!");
		btnAddProduct.setEnabled(true);
		btnAddClassify.setEnabled(true);
		btnAddProducer.setEnabled(true);

		btnChangeProduct.setEnabled(false);
		btnChangeProducer.setEnabled(false);
		btnChangeClassify.setEnabled(false);

		btnDeleteProduct.setEnabled(false);
		btnDeleteProducer.setEnabled(false);
		btnDeleteClassify.setEnabled(false);

		btnSaveProduct.setEnabled(false);
		btnSaveClassify.setEnabled(false);
		btnSaveProducer.setEnabled(false);

		Add = false;
		Change = false;
		image.setIcon(null);
		txbLink.setText("");
		Disabled();
		DisabledProducer();
		DisabledClassify();
		loadMaNCC(); // Load thông tin mã nhà cung cấp
		loadMaLoai(); // Load thông tin mã loại sản phẩm
//      loadProducts();
		loadTableLoaiSP();
		loadProducers();
	}

	// Kiểm tra null các ô input tab cập nhật sản phẩm
	public boolean checkNull() {
		boolean kq = true;
		if (String.valueOf(this.txbID.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập mã cho sản phẩm!");
			return false;
		}
		if (String.valueOf(this.txbName.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập Tên sản phẩm!");
			return false;
		}
		if (String.valueOf(this.txbAmount.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập số lượng sản phẩm hiện có!");
			return false;
		}

		if (String.valueOf(this.txbUnit.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập đơn vị sản phẩm hiện có!");
			return false;
		}

		if (String.valueOf(this.txbPrice.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập giá cho sản phẩm!");
			return false;
		}
		if (String.valueOf(this.txbLink.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa chọn ảnh cho sản phẩm!");
			return false;
		}
		return kq;
	}

	// Kiểm tra null ở tab cập nhật nhà cung cấp
	private boolean checkNullProducer() {
		boolean kq = true;
		if (String.valueOf(this.txbIDProducer.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa ID cho nhà cung cấp!");
			return false;
		}
		if (String.valueOf(this.txbProducer.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập tên nhà cung cấp!");
			return false;
		}
		if (String.valueOf(this.txbAdress.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập địa chỉ của nhà cung cấp!");
			return false;
		}
		if (String.valueOf(this.txbPhone.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập số điện thoại của nhà cung cấp!");
			return false;
		}
		return kq;
	}

	// Kiểm tra null ở tab cập nhật loại sản phẩm
	private boolean checkNullClassify() {
		boolean kq = true;
		if (String.valueOf(this.txbIDClassify.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa ID cho loại sản phẩm!");
			return false;
		}
		if (String.valueOf(this.txbClassify.getText()).length() == 0) {
			lblStatus.setText("Bạn chưa nhập loại sản phẩm!");
			return false;
		}
		return kq;
	}

	// Phương thức load mã loại sản phẩm có kèm theo tên sản phẩm
	private void loadMaLoai() throws RemoteException {
		List<LoaiSP> arry = loaiSPDao.getAllLoaiSP();
		
		for (int i = 0; i < arry.size(); i++) {
			this.cbxClassify
					.addItem(((LoaiSP) arry.get(i)).getMaLoaiSP() + " (" + ((LoaiSP) arry.get(i)).getTenLoaiSP() + ")");
		}
	}

	// Phương thức load loại sản phẩm ra bảng
	private void loadTableLoaiSP() throws RemoteException {
		tblClassify.removeAll();
		tblClassify.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		tblLoaiSP.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

		String[] arr = { "Mã Loại Sản Phẩm", "Tên Loại Sản Phẩm" };
		DefaultTableModel modle = new DefaultTableModel(arr, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		List<LoaiSP> arry = loaiSPDao.getAllLoaiSP();
		for (int i = 0; i < arry.size(); i++) {
			Vector vector = new Vector();
			vector.add(((LoaiSP) arry.get(i)).getMaLoaiSP());
			vector.add(((LoaiSP) arry.get(i)).getTenLoaiSP());
			modle.addRow(vector);
		}
		tblClassify.setModel(modle);
		tblLoaiSP.setModel(modle);

	}

	// Load thông tin nhà cung cấp trong csdl ra bảng nhà cung cấp
	private void loadProducers() throws RemoteException {
		tblProducer.removeAll();
		tblProducer.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

		String[] arr = { "Mã Nhà Cung Cấp", "Tên Nhà Cung Cấp", "Địa Chỉ", "Số Điện Thoại" };
		DefaultTableModel modle = new DefaultTableModel(arr, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		List<NCC> arry = nccDao.getAllNCC();
		for (int i = 0; i < arry.size(); i++) {
			Vector vector = new Vector();
			vector.add(((NCC) arry.get(i)).getMaNCC());
			vector.add(((NCC) arry.get(i)).getTenNCC());
			vector.add(((NCC) arry.get(i)).getDiaChi());
			vector.add(((NCC) arry.get(i)).getSoDT());
			modle.addRow(vector);
		}
		tblProducer.setModel(modle);

	}

	// Load thông tin mã nhà cung cấp
	private void loadMaNCC() throws RemoteException {
		List<NCC> arry = nccDao.getAllNCC();
		for (int i = 0; i < arry.size(); i++) {
			this.cbxProducer.addItem(((NCC) arry.get(i)).getMaNCC() + " (" + ((NCC) arry.get(i)).getTenNCC() + ")");
		}
	}

	public void loadProductNull() {
		String[] arr = { "Mã Sản Phẩm", "Mã Loại Sản Phẩm", "Mã Nhà Cung Cấp", "Tên Sản Phẩm", "Số Lượng", "Đơn Vị",
				"Giá", "Ảnh" };
		DefaultTableModel modle = new DefaultTableModel(arr, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblProduct.setModel(modle);
	}


	// Load thông tin sản phẩm
	private void loadProducts() throws RemoteException {
		int Click = tblLoaiSP.getSelectedRow();
		TableModel model = tblLoaiSP.getModel();
		if(Click == -1) {
			String sql = "SELECT * FROM SANPHAM where loaiSP_MaLoaiSP = 'GVS'";
			FindProducts(sql);
		} else {
			String sql = "SELECT * FROM SANPHAM where loaiSP_MaLoaiSP like N'%" + model.getValueAt(Click, 0).toString() + "'";
			FindProducts(sql);
		}
		
	}

	// Ép kiểu String thành kiểu số
	private double convertedToNumbers(String s) {
		String number = "";
		String[] array = s.replace(",", " ").split("\\s");
		for (String i : array) {
			number = number.concat(i);
		}
		return Double.parseDouble(number);
	}

	private String cutChar(String arry) {
		return arry.replaceAll("\\D+", "");
	}

	// Load thông tin sản phẩm ra bảng
	private void View(ArrayList arry) {
		tblProduct.removeAll();
		tblProduct.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		String[] arr = { "Mã Sản Phẩm", "Mã Loại Sản Phẩm", "Mã Nhà Cung Cấp", "Tên Sản Phẩm", "Số Lượng", "Đơn Vị",
				"Giá", "Ảnh" };
		DefaultTableModel modle = new DefaultTableModel(arr, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		for (int i = 0; i < arry.size(); i++) {
			Vector vector = new Vector();
			vector.add(((SanPham) arry.get(i)).getMaSP());
			vector.add(((SanPham) arry.get(i)).getLoaiSP().getMaLoaiSP().trim() + " ("
					+ ((SanPham) arry.get(i)).getLoaiSP().getTenLoaiSP().trim() + ")");
			vector.add(((SanPham) arry.get(i)).getNhaCungCap().getMaNCC().trim() + " ("
					+ ((SanPham) arry.get(i)).getNhaCungCap().getTenNCC().trim() + ")");
			vector.add(((SanPham) arry.get(i)).getTenSP());
			vector.add(((SanPham) arry.get(i)).getSoLuong());
			vector.add(((SanPham) arry.get(i)).getDonVi().trim());
			vector.add(((SanPham) arry.get(i)).getGia());
			vector.add(((SanPham) arry.get(i)).getAnh());
			modle.addRow(vector);
		}
		tblProduct.setAutoResizeMode(tblProduct.AUTO_RESIZE_OFF);
		tblProduct.setModel(modle);
		tblProduct.getColumnModel().getColumn(3).setPreferredWidth(250);
	}

	//
	private void loadProductFind(List<SanPham> arry) {
		tblProduct.removeAll();
		tblListProduct.removeAll();
		tblListProduct.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		tblProduct.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		String[] arr = { "Mã Sản Phẩm", "Mã Loại Sản Phẩm", "Mã Nhà Cung Cấp", "Tên Sản Phẩm", "Số Lượng", "Đơn Vị",
				"Giá", "Ảnh" };
		DefaultTableModel modle = new DefaultTableModel(arr, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		for (int i = 0; i < arry.size(); i++) {
			Vector vector = new Vector();
			vector.add(((SanPham) arry.get(i)).getMaSP());
			vector.add(((SanPham) arry.get(i)).getLoaiSP().getMaLoaiSP().trim() + " ("
					+ ((SanPham) arry.get(i)).getLoaiSP().getTenLoaiSP().trim() + ")");
			vector.add(((SanPham) arry.get(i)).getNhaCungCap().getMaNCC().trim() + " ("
					+((SanPham) arry.get(i)).getNhaCungCap().getTenNCC().trim() + ")");
			vector.add(((SanPham) arry.get(i)).getTenSP());
			vector.add(((SanPham) arry.get(i)).getSoLuong());
			vector.add(((SanPham) arry.get(i)).getDonVi().trim());
			vector.add(((SanPham) arry.get(i)).getGia());
			vector.add(((SanPham) arry.get(i)).getAnh());
			modle.addRow(vector);
		}
		tblListProduct.setModel(modle);
		tblProduct.setModel(modle);
		tblProduct.getColumnModel().getColumn(2).setPreferredWidth(100);
		tblProduct.getColumnModel().getColumn(3).setPreferredWidth(150);
	}

	// Hàm sắp xếp giá thành sản phẩm
//	private void sortSGia() {
//		int Click = tblLoaiSP.getSelectedRow();
//		TableModel model = tblLoaiSP.getModel();
//
//		ArrayList arry = processed.getProducts(model.getValueAt(Click, 0).toString());
//		bubbleSortGia(arry);
//		View(arry);
//	}

	// Tự phát sinh mã sp theo loại sản phẩm
	private void automatedCode() throws RemoteException {
		String[] arry = cbxClassify.getSelectedItem().toString().split("\\s");
		if (sanPhamDao.getAllSanPham().size() < 9) {
			txbID.setText(arry[0] + "0000" + String.valueOf(sanPhamDao.getAllSanPham().size() + 1));
		} else {
			txbID.setText(arry[0] + "000" + String.valueOf(sanPhamDao.getAllSanPham().size() + 1));
		}
	}

	// Tìm sản phẩm

	private void FindProducts(String sql) throws RemoteException {
		List<SanPham> arry = sanPhamDao.findSP(sql);
		loadProductFind(arry);
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel12 = new javax.swing.JLabel();
        btnBackHome = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnAddClassify = new javax.swing.JButton();
        btnChangeClassify = new javax.swing.JButton();
        btnSaveClassify = new javax.swing.JButton();
        btnDeleteClassify = new javax.swing.JButton();
        btnRefreshClassify = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        txbIDClassify = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txbClassify = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblClassify = new javax.swing.JTable();
        lblStatus1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblNhanVien1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListProduct = new javax.swing.JTable();
        txbSearchProduct = new javax.swing.JTextField();
        searchBtn1 = new javax.swing.JButton();
        btnRefresh1 = new javax.swing.JButton();
        lblStatus4 = new javax.swing.JLabel();
        image1 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblNhanVien3 = new javax.swing.JLabel();
        maSPrdo = new javax.swing.JRadioButton();
        tenSPrdo = new javax.swing.JRadioButton();
        lblStatusSearchProduct = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txbID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbxClassify = new javax.swing.JComboBox<>();
        txbName = new javax.swing.JTextField();
        cbxProducer = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txbAmount = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txbLink = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        txbPrice = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txbUnit = new javax.swing.JTextField();
        printBtn = new javax.swing.JButton();
        sortBtn = new javax.swing.JButton();
        btnSaveProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        btnChangeProduct = new javax.swing.JButton();
        btnAddProduct = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblLoaiSP = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProducer = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        btnRefreshProducer = new javax.swing.JButton();
        btnAddProducer = new javax.swing.JButton();
        btnChangeProducer = new javax.swing.JButton();
        btnSaveProducer = new javax.swing.JButton();
        btnDeleteProducer = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txbIDProducer = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txbProducer = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txbAdress = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txbPhone = new javax.swing.JTextField();
        lblStatus2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblNhanVien2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1920, 1080));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Quản Lý Sản Phẩm");

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Previous-icon.png"))); // NOI18N
        btnBackHome.setText("Hệ Thống");
        btnBackHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHomeActionPerformed(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jPanel2.setPreferredSize(new java.awt.Dimension(1920, 970));

        btnAddClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnAddClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnAddClassify.setText("Thêm");
        btnAddClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnAddClassifyActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnChangeClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnChangeClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change.png"))); // NOI18N
        btnChangeClassify.setText("Sửa");
        btnChangeClassify.setEnabled(false);
        btnChangeClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeClassifyActionPerformed(evt);
            }
        });

        btnSaveClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSaveClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Save.png"))); // NOI18N
        btnSaveClassify.setText("Lưu");
        btnSaveClassify.setEnabled(false);
        btnSaveClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSaveClassifyActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnDeleteClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnDeleteClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Delete.png"))); // NOI18N
        btnDeleteClassify.setText("Xóa");
        btnDeleteClassify.setEnabled(false);
        btnDeleteClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnDeleteClassifyActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnRefreshClassify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefreshClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnRefreshClassifyActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        txbIDClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel14.setText("Mã Loại Sản Phẩm:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel15.setText("Tên Loại Sản Phẩm:");

        txbClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel14)
                .addGap(36, 36, 36)
                .addComponent(txbIDClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130)
                .addComponent(jLabel15)
                .addGap(186, 186, 186)
                .addComponent(txbClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(382, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbIDClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txbClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(202, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(btnRefreshClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(235, 235, 235)
                .addComponent(btnChangeClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187)
                .addComponent(btnDeleteClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(192, 192, 192)
                .addComponent(btnSaveClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnAddClassify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnRefreshClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(btnDeleteClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSaveClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnChangeClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        tblClassify.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblClassify.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblClassify.setRowHeight(26);
        tblClassify.setRowMargin(2);
        tblClassify.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClassifyMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblClassify);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblStatus1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus1.setText("Trạng Thái");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 51));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel11.setText("Nhân Viên:");

        lblNhanVien1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien1.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien1.setText("(BH01) Nguyễn Thị Diễm Phúc");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus1, javax.swing.GroupLayout.DEFAULT_SIZE, 1752, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane3)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblNhanVien1))
                .addGap(58, 58, 58)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(251, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cập Nhật Loại Sản Phẩm", jPanel2);

        tblListProduct.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblListProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblListProduct.setRowHeight(26);
        tblListProduct.setRowMargin(2);
        tblListProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListProductMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblListProduct);

        txbSearchProduct.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        txbSearchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txbSearchProductKeyPressed(evt);
            }
        });

        searchBtn1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        searchBtn1.setText("Tìm");
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

        btnRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh1ActionPerformed(evt);
            }
        });

        lblStatus4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblStatus4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus4.setText("Tìm Kiếm Theo:");

        image1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 0, 51));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel21.setText("Nhân Viên:");

        lblNhanVien3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien3.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien3.setText("(BH01) Nguyễn Thị Diễm Phúc");

        buttonGroup2.add(maSPrdo);
        maSPrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        maSPrdo.setSelected(true);
        maSPrdo.setText("Mã Sản Phẩm");

        buttonGroup2.add(tenSPrdo);
        tenSPrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tenSPrdo.setText("Tên Sản Phẩm");

        lblStatusSearchProduct.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblStatusSearchProduct.setForeground(new java.awt.Color(255, 0, 51));
        lblStatusSearchProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatusSearchProduct.setText("Thông Báo");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblStatus4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblNhanVien3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(maSPrdo))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(389, 389, 389)
                                .addComponent(tenSPrdo)
                                .addGap(86, 86, 86)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(image1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblStatusSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txbSearchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addComponent(searchBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 237, Short.MAX_VALUE)
                        .addComponent(btnRefresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21)
                                .addComponent(lblNhanVien3))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(image1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStatus4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maSPrdo)
                            .addComponent(tenSPrdo)
                            .addComponent(txbSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addComponent(lblStatusSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );

        jTabbedPane1.addTab("Tìm Kiếm Sản Phẩm", jPanel5);

        tblProduct.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Sản Phẩm", "Mã Loại Sản Phẩm", "Mã Nhà Cung Cấp", "Tên Sản Phẩm", "Số Lượng", "Đơn Vị", "Giá", "Ảnh"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProduct.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblProduct.setRowHeight(26);
        tblProduct.setRowMargin(2);
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduct);
        if (tblProduct.getColumnModel().getColumnCount() > 0) {
            tblProduct.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblProduct.getColumnModel().getColumn(7).setPreferredWidth(200);
        }

        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel1.setText("Mã Sản Phẩm:");

        txbID.setEditable(false);
        txbID.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbIDActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel2.setText("Loại Sản Phẩm:");

        cbxClassify.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxClassify.setMaximumSize(new java.awt.Dimension(207, 32767));
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

        txbName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        cbxProducer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxProducer.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxProducerPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText("Mã Nhà Cung Cấp:");

        txbAmount.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbAmountKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel6.setText("Số Lượng:");

        txbLink.setEditable(false);
        txbLink.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbLink.setMaximumSize(new java.awt.Dimension(207, 2147483647));
        txbLink.setPreferredSize(new java.awt.Dimension(207, 28));
        txbLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbLinkActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnUpdate.setText("Cập nhật ảnh..");
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        txbPrice.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPriceKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel8.setText("Giá:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel3.setText("Tên Sản Phẩm:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel7.setText("Đơn Vị:");

        txbUnit.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbUnitKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4Layout.setHorizontalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addComponent(image, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel2))
        			.addGap(18)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(cbxClassify, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(txbLink, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addGap(34)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addComponent(jLabel4)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(cbxProducer, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE))
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addComponent(jLabel1)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(txbID)))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(jLabel3)
        				.addComponent(jLabel6))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(txbName, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
        				.addComponent(txbAmount))
        			.addGap(18, 18, Short.MAX_VALUE)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel8, Alignment.TRAILING)
        				.addComponent(jLabel7, Alignment.TRAILING))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(txbPrice, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
        				.addComponent(txbUnit, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addGap(25)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(txbLink, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel1)
        				.addComponent(txbID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel3)
        				.addComponent(txbName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel7)
        				.addComponent(txbUnit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addComponent(txbPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addContainerGap())
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel2)
        						.addComponent(cbxClassify, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addGap(17))
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel4)
        						.addComponent(cbxProducer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel6)
        						.addComponent(txbAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel8))
        					.addContainerGap())))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addComponent(image, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4.setLayout(jPanel4Layout);

        printBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        printBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Print Sale.png"))); // NOI18N
        printBtn.setText("In Danh Sách");
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });

        sortBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        sortBtn.setText("Sắp Xếp Theo Giá");
        sortBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					sortBtnActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnSaveProduct.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSaveProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Save.png"))); // NOI18N
        btnSaveProduct.setText("Lưu SP");
        btnSaveProduct.setEnabled(false);
        btnSaveProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSaveProductActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnDeleteProduct.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnDeleteProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Delete.png"))); // NOI18N
        btnDeleteProduct.setText("Xóa SP");
        btnDeleteProduct.setEnabled(false);
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnDeleteProductActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnChangeProduct.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnChangeProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change.png"))); // NOI18N
        btnChangeProduct.setText("Sửa SP");
        btnChangeProduct.setEnabled(false);
        btnChangeProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnChangeProductActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnAddProduct.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnAddProduct.setText("Thêm SP");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnAddProductActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
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

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 51));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel5.setText("Nhân Viên:");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien.setText("(BH01) Nguyễn Thị Diễm Phúc");

        tblLoaiSP.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblLoaiSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã Loại Sản Phẩm", "Tên Loại Sản Phẩm"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLoaiSP.setRowHeight(26);
        tblLoaiSP.setRowMargin(2);
        tblLoaiSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
					tblLoaiSPMouseClicked(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        jScrollPane5.setViewportView(tblLoaiSP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNhanVien)
                        .addGap(0, 1351, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addComponent(btnChangeProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(btnDeleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addComponent(btnSaveProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(sortBtn)
                        .addGap(85, 85, 85)
                        .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblNhanVien))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChangeProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDeleteProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSaveProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sortBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addContainerGap(162, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thông Tin Sản Phẩm", jPanel1);

        tblProducer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblProducer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProducer.setRowHeight(26);
        tblProducer.setRowMargin(2);
        tblProducer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProducerMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblProducer);

        btnRefreshProducer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefreshProducer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnRefreshProducerActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnAddProducer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnAddProducer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnAddProducer.setText("Thêm");
        btnAddProducer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnAddProducerActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnChangeProducer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnChangeProducer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change.png"))); // NOI18N
        btnChangeProducer.setText("Sửa");
        btnChangeProducer.setEnabled(false);
        btnChangeProducer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeProducerActionPerformed(evt);
            }
        });

        btnSaveProducer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSaveProducer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Save.png"))); // NOI18N
        btnSaveProducer.setText("Lưu");
        btnSaveProducer.setEnabled(false);
        btnSaveProducer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnSaveProducerActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        btnDeleteProducer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnDeleteProducer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Delete.png"))); // NOI18N
        btnDeleteProducer.setText("Xóa");
        btnDeleteProducer.setEnabled(false);
        btnDeleteProducer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					btnDeleteProducerActionPerformed(evt);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(btnRefreshProducer, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179)
                .addComponent(btnAddProducer, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165)
                .addComponent(btnChangeProducer, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDeleteProducer, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(189, 189, 189)
                .addComponent(btnSaveProducer, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeleteProducer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnRefreshProducer, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                    .addComponent(btnSaveProducer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChangeProducer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddProducer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel17.setText("Mã Nhà Cung Cấp:");

        txbIDProducer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel10.setText("Tên Nhà Cung Cấp:");

        txbProducer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel13.setText("Địa Chỉ:");

        txbAdress.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel16.setText("Số Điện Thoại:");

        txbPhone.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(txbIDProducer, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txbProducer, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(jLabel13)
                .addGap(27, 27, 27)
                .addComponent(txbAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(61, 61, 61)
                .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txbIDProducer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txbProducer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txbAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        lblStatus2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblStatus2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus2.setText("Trạng Thái");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 0, 51));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel19.setText("Nhân Viên:");

        lblNhanVien2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien2.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien2.setText("(BH01) Nguyễn Thị Diễm Phúc");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus2, javax.swing.GroupLayout.PREFERRED_SIZE, 1511, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNhanVien2)))
                        .addGap(0, 241, Short.MAX_VALUE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNhanVien2))
                .addGap(28, 28, 28)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cập Nhật Nhà Cung Cấp", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBackHome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(299, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(21, 21, 21)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1004, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txbLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbLinkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbLinkActionPerformed

	private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
		frameUtil .exit(this);
	}// GEN-LAST:event_formWindowClosing

	private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnRefreshActionPerformed
		Refresh();
	}// GEN-LAST:event_btnRefreshActionPerformed

	private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnAddProductActionPerformed
		Refresh();
		loadProductNull();
		Add = true;
		Enabled();
		btnAddProduct.setEnabled(false);
		btnSaveProduct.setEnabled(true);
	}// GEN-LAST:event_btnAddProductActionPerformed

	private void btnChangeProductActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnChangeProductActionPerformed
		Add = false;
		Change = true;
		Enabled();

		lblStatus.setText("Trạng Thái!");
		loadMaNCC();
		loadMaLoai();
		cbxClassify.setEnabled(false);
		txbID.setEnabled(false);
		btnChangeProduct.setEnabled(false);
		btnDeleteProduct.setEnabled(false);
		btnAddProduct.setEnabled(false);
		btnSaveProduct.setEnabled(true);
	}// GEN-LAST:event_btnChangeProductActionPerformed

	private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_printBtnActionPerformed

		try {
			int Click = tblLoaiSP.getSelectedRow();
			TableModel model = tblLoaiSP.getModel();
			HashMap param = new HashMap();
			param.put("MaLoaiSP", model.getValueAt(Click, 0).toString());
			String url =  getClass().getResource("/Report/Products.jrxml").toString().split("file:/")[1];
			System.out.println(url);
			JasperReport report = JasperCompileManager.compileReport(
					url
				);

			JasperPrint print = JasperFillManager.fillReport(report, param, connectJsaper);

			JasperViewer.viewReport(print, false);
		} catch (JRException ex) {
			ex.printStackTrace();
		}
	}// GEN-LAST:event_printBtnActionPerformed

	private void btnSaveProductActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnSaveProductActionPerformed
		if (checkNull()) {
			String[] arry = cbxClassify.getSelectedItem().toString().split("\\s");
			String[] arry1 = cbxProducer.getSelectedItem().toString().split("\\s");
			if (Add == true) {
				if (sanPhamDao.getSP(txbID.getText()) == null) {
					LoaiSP loaiSP = loaiSPDao.getLoaiSP(arry[0]);
					NCC nhaCungCap = nccDao.getNCC(arry1[0]);
					String giaSP = txbPrice.getText().replace(",", "");
					double gia = Double.parseDouble(giaSP);
					SanPham sanPham = new SanPham(txbID.getText(), loaiSP, nhaCungCap, txbName.getText(), Integer.parseInt(txbAmount.getText()), txbUnit.getText(), gia, txbLink.getText());
					sanPhamDao.addSanPham(sanPham);
					Disabled();
					Refresh();
					tblListProduct.removeAll();
					loadProductNull();
					lblStatus.setText("Thêm sản phẩm mới thành công!");
				} else {
					lblStatus.setText("Mã sản phẩm đã tồn tại!");
				}
			} else if (Change == true) {
				int Click = tblProduct.getSelectedRow();
				TableModel Entity = tblProduct.getModel();
				LoaiSP loaiSP = loaiSPDao.getLoaiSP(arry[0]);
				NCC nhaCungCap = nccDao.getNCC(arry1[0]);
				String giaSP = txbPrice.getText().replace(",", "");
				double gia = Double.parseDouble(giaSP);
				SanPham sanPham = new SanPham(txbID.getText(), loaiSP, nhaCungCap, txbName.getText(), Integer.parseInt(txbAmount.getText()), txbUnit.getText(), gia, txbLink.getText());
				sanPhamDao.updateSanPham(sanPham);
				Disabled();
				Refresh();
				loadProductNull();
				lblStatus.setText("Lưu thay đổi thành công!");
			}
		}
	}// GEN-LAST:event_btnSaveProductActionPerformed

	private void btnDeleteProductActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnDeleteProductActionPerformed
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
		int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa sản phẩm này hay không?", "Thông Báo", 2);
		if (Click == JOptionPane.YES_OPTION) {
			String[] arry = cbxClassify.getSelectedItem().toString().split("\\s");
			String[] arry1 = cbxProducer.getSelectedItem().toString().split("\\s");
			List<CT_HoaDon> CTHD = new ArrayList();
			String sql = "SELECT * FROM CT_HOADON WHERE maSP = '" + txbID.getText() + "'";
			CTHD = ctHDDao.findCTHD(sql);
			if (CTHD == null) {
				sanPhamDao.deleteSanPham(txbID.getText());
				Refresh();
				loadProductNull();
				this.lblStatus.setText("Xóa sản phẩm thành công!");
			} else {
				this.lblStatus.setText("Xóa sản phẩm thất bại vì sản phẩm tồn tại trong chi tiết hóa đơn!");
			}

		}
	}// GEN-LAST:event_btnDeleteProductActionPerformed

	private void txbAmountKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txbAmountKeyReleased
		txbAmount.setText(cutChar(txbAmount.getText()));
	}// GEN-LAST:event_txbAmountKeyReleased

	private void txbPriceKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txbPriceKeyReleased
		DecimalFormat formatter = new DecimalFormat("###,###,###");

		txbPrice.setText(cutChar(txbPrice.getText()));
		if (txbPrice.getText().equals("")) {
			return;
		} else {
			txbPrice.setText(formatter.format(convertedToNumbers(txbPrice.getText())));
		}
	}// GEN-LAST:event_txbPriceKeyReleased

	private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnUpdateMouseClicked
		JFileChooser open = new JFileChooser();
		open.setDialogTitle("Chọn Đường Dẫn Đến Ảnh Sản Phẩm");
		int file = open.showOpenDialog(null);
		String[] ary = open.getCurrentDirectory().toString().toString().split("\\\\");
		if (file == JFileChooser.APPROVE_OPTION) {

			txbLink.setText(open.getCurrentDirectory().toString() + "\\" + open.getSelectedFile().getName());

			ImageIcon icon = new ImageIcon(txbLink.getText());
			image.setIcon(icon);
		} else {
			lblStatus.setText("Chọn đường dẫn tới ảnh của sản phẩm!!");
		}
	}// GEN-LAST:event_btnUpdateMouseClicked

	private void btnRefreshClassifyActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnRefreshClassifyActionPerformed
		Refresh();
	}// GEN-LAST:event_btnRefreshClassifyActionPerformed

	private void btnAddClassifyActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnAddClassifyActionPerformed
		Refresh();
		Add = true;
		txbIDClassify.requestFocus();
		EnabledClassify();
		btnAddClassify.setEnabled(false);
		btnSaveClassify.setEnabled(true);
	}// GEN-LAST:event_btnAddClassifyActionPerformed

	private void btnChangeClassifyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChangeClassifyActionPerformed
		Add = false;
		Change = true;
		EnabledClassify();
		txbIDClassify.requestFocus();
		lblStatus1.setText("Trạng Thái!");

		btnAddClassify.setEnabled(false);
		btnDeleteClassify.setEnabled(false);
		btnChangeClassify.setEnabled(false);
		btnSaveClassify.setEnabled(true);
	}// GEN-LAST:event_btnChangeClassifyActionPerformed

	private void btnSaveClassifyActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnSaveClassifyActionPerformed
		if (checkNullClassify()) {
			if (Add == true) {
				if (loaiSPDao.getLoaiSP(txbIDClassify.getText()) == null) {
					LoaiSP loaiSP = new LoaiSP(txbIDClassify.getText(), txbClassify.getText());
					loaiSPDao.addLoaiSP(loaiSP);
					DisabledClassify();
					loadTableLoaiSP();
					lblStatus1.setText("Thêm loại sản phẩm thành công!");
				} else {
					lblStatus1.setText("Không được sửa mã loại sản phẩm!");
				}
			} else if (Change == true) {
				int Click = tblClassify.getSelectedRow();
				TableModel Entity = tblClassify.getModel();

				if (Entity.getValueAt(Click, 0).toString().equals(txbIDClassify.getText())) {
					LoaiSP loaiSP = new LoaiSP(txbIDClassify.getText(), txbClassify.getText());
					loaiSPDao.updateLoaiSP(loaiSP);
					DisabledClassify();
					Refresh();
					lblStatus1.setText("Lưu thay đổi thành công!");
				} else if (loaiSPDao.getLoaiSP(txbIDClassify.getText()) != null) {
					LoaiSP loaiSP = new LoaiSP(txbIDClassify.getText(), txbClassify.getText());
					loaiSPDao.updateLoaiSP(loaiSP);
					DisabledClassify();
					Refresh();
					lblStatus1.setText("Lưu thay đổi thành công!");
				} else {
					lblStatus1.setText("Mã loại sản phẩm bạn sửa đổi đã tồn tại!!");
				}
			}

		}
	}// GEN-LAST:event_btnSaveClassifyActionPerformed

	private void btnDeleteClassifyActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnDeleteClassifyActionPerformed
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
		int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa loại sản phẩm này hay không?", "Thông Báo", 2);
		if (Click == JOptionPane.YES_OPTION) {
			loaiSPDao.deleteLoaiSP(txbIDClassify.getText());
			Refresh();
			loadTableLoaiSP();
			this.lblStatus1.setText("Xóa loại sản phẩm thành công!");
		}
	}// GEN-LAST:event_btnDeleteClassifyActionPerformed

	private void tblClassifyMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblClassifyMouseClicked
		int Click = tblClassify.getSelectedRow();
		TableModel Entity = tblClassify.getModel();

		txbIDClassify.setText(Entity.getValueAt(Click, 0).toString());
		txbClassify.setText(Entity.getValueAt(Click, 1).toString());
		btnChangeClassify.setEnabled(true);
		btnDeleteClassify.setEnabled(true);
	}// GEN-LAST:event_tblClassifyMouseClicked

	private void tblProducerMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblProducerMouseClicked
		int Click = tblProducer.getSelectedRow();
		TableModel Entity = tblProducer.getModel();

		txbIDProducer.setText(Entity.getValueAt(Click, 0).toString());
		txbProducer.setText(Entity.getValueAt(Click, 1).toString());
		txbAdress.setText(Entity.getValueAt(Click, 2).toString());
		txbPhone.setText(Entity.getValueAt(Click, 3).toString());

		btnChangeProducer.setEnabled(true);
		btnDeleteProducer.setEnabled(true);
	}// GEN-LAST:event_tblProducerMouseClicked

	private void btnRefreshProducerActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnRefreshProducerActionPerformed
		Refresh();
	}// GEN-LAST:event_btnRefreshProducerActionPerformed

	private void btnAddProducerActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnAddProducerActionPerformed
		Refresh();
		txbIDProducer.requestFocus();
		Add = true;
		btnAddProducer.setEnabled(false);
		btnSaveProducer.setEnabled(true);
		EnabledProducer();
	}// GEN-LAST:event_btnAddProducerActionPerformed

	private void btnChangeProducerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChangeProducerActionPerformed
		Add = false;
		Change = true;
		txbIDProducer.requestFocus();
		btnAddProducer.setEnabled(false);
		btnChangeProducer.setEnabled(false);
		btnDeleteProducer.setEnabled(false);
		btnSaveProducer.setEnabled(true);
		EnabledProducer();
	}// GEN-LAST:event_btnChangeProducerActionPerformed

	private void btnSaveProducerActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnSaveProducerActionPerformed
		String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
		if (checkNullProducer()) {
			if (Add == true) {
				if (nccDao.getNCC(txbIDProducer.getText()) == null && txbPhone.getText().matches(reg)) {
					NCC nhaCungCap = new NCC(txbIDProducer.getText(), txbProducer.getText(), txbAdress.getText(),
							txbPhone.getText());
					nccDao.addNCC(nhaCungCap);

					DisabledProducer();
					Refresh();
					lblStatus2.setText("Thêm nhà cung cấp thành công!");
				} else if (txbPhone.getText().matches(reg) == false) {
					lblStatus2.setText("SDT nhà cung cấp định dạng không đúng!");
				} else {
					lblStatus2.setText("Mã nhà cung cấp đã tồn tại!");
				}
			} else if (Change == true) {
				int Click = tblProducer.getSelectedRow();
				TableModel Entity = tblProducer.getModel();

				if (Entity.getValueAt(Click, 0).toString().equals(txbIDProducer.getText())
						&& txbPhone.getText().matches(reg)) {
					NCC nhaCungCap = new NCC(txbIDProducer.getText(), txbProducer.getText(), txbAdress.getText(),
							txbPhone.getText());
					nccDao.updateNCC(nhaCungCap);
					DisabledProducer();
					Refresh();
					loadProducers();
					lblStatus2.setText("Lưu thay đổi thành công!");
				} else if (nccDao.getNCC(txbIDProducer.getText()) != null && txbPhone.getText().matches(reg)) {
					NCC nhaCungCap = new NCC(txbIDProducer.getText(), txbProducer.getText(), txbAdress.getText(),
							txbPhone.getText());
					nccDao.updateNCC(nhaCungCap);
					DisabledProducer();
					Refresh();
					lblStatus2.setText("Lưu thay đổi thành công!");
				} else if (txbPhone.getText().matches(reg) == false) {
					lblStatus2.setText("SDT nhà cung cấp định dạng không đúng!");
				} else {
					lblStatus2.setText("Mã nhà cung cấp bạn sửa đổi đã tồn tại!!");
				}
			}
		}
	}// GEN-LAST:event_btnSaveProducerActionPerformed

	private void btnDeleteProducerActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnDeleteProducerActionPerformed
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
		int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa nhà cung cấp hay không?", "Thông Báo", 2);
		if (Click == JOptionPane.YES_OPTION) {
			nccDao.deleteNCC(txbIDProducer.getText());
			loadProducers();
			this.lblStatus2.setText("Xóa nhà cung cấp thành công!");
		}
	}// GEN-LAST:event_btnDeleteProducerActionPerformed

	private void cbxClassifyPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) throws RemoteException {// GEN-FIRST:event_cbxClassifyPopupMenuWillBecomeInvisible
		// loadMaLoaiSP();
		automatedCode();
	}// GEN-LAST:event_cbxClassifyPopupMenuWillBecomeInvisible

	private void cbxProducerPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {// GEN-FIRST:event_cbxProducerPopupMenuWillBecomeInvisible
		// loadProducer();
	}// GEN-LAST:event_cbxProducerPopupMenuWillBecomeInvisible

	private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblProductMouseClicked
		int Click = tblProduct.getSelectedRow();
		TableModel Entity = tblProduct.getModel();

		cbxProducer.removeAllItems();
		cbxClassify.removeAllItems();

		txbID.setText(Entity.getValueAt(Click, 0).toString());
		cbxClassify.addItem(Entity.getValueAt(Click, 1).toString());
		cbxProducer.addItem(Entity.getValueAt(Click, 2).toString());
		txbName.setText(Entity.getValueAt(Click, 3).toString());
		txbAmount.setText(Entity.getValueAt(Click, 4).toString());
		txbUnit.setText(Entity.getValueAt(Click, 5).toString());
		String[] s = Entity.getValueAt(Click, 6).toString().split("\\s");
		txbPrice.setText(s[0]);
		//txbLink.setText(Entity.getValueAt(Click, 7).toString());
		ImageIcon icon = new ImageIcon(Entity.getValueAt(Click, 7).toString());
		image.setIcon(icon);

		btnChangeProduct.setEnabled(true);
		btnDeleteProduct.setEnabled(true);
	}// GEN-LAST:event_tblProductMouseClicked

	private void sortBtnActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_sortBtnActionPerformed
		String sql = "SELECT * FROM SanPham ORDER BY Gia DESC";
		List<SanPham> listSP = sanPhamDao.findSP(sql);
		loadProductFind(listSP);
	}// GEN-LAST:event_sortBtnActionPerformed

	private void txbIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txbIDActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txbIDActionPerformed

	private void txbUnitKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txbUnitKeyReleased
		// TODO add your handling code here:
	}// GEN-LAST:event_txbUnitKeyReleased

	private void btnBackHomeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBackHomeActionPerformed
		// TODO add your handling code here:
		HomeBanHang home = new HomeBanHang(account);
		this.setVisible(false);
		home.setVisible(true);

	}// GEN-LAST:event_btnBackHomeActionPerformed

	private void tblListProductMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblListProductMouseClicked
		// TODO add your handling code here:
		int Click = tblListProduct.getSelectedRow();
		TableModel Entity = tblListProduct.getModel();
		txbLink.setText(Entity.getValueAt(Click, 7).toString());
		ImageIcon icon = new ImageIcon(Entity.getValueAt(Click, 7).toString());
		image1.setIcon(icon);

	}// GEN-LAST:event_tblListProductMouseClicked

	private void txbSearchProductKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txbSearchProductKeyPressed
		// TODO add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			searchBtn1.doClick();
		}
	}// GEN-LAST:event_txbSearchProductKeyPressed

	private void searchBtn1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_searchBtn1ActionPerformed
		// TODO add your handling code here:
		lblStatusSearchProduct.setText("Thông Báo");
		if (txbSearchProduct.getText().equals("")) {
			lblStatusSearchProduct.setText("Vui lòng nhập thông tin cần tìm");
		} else {
			String sql = "";
			if (maSPrdo.isSelected()) {
				sql = "SELECT * FROM SANPHAM where MaSP like N'%" + this.txbSearchProduct.getText() + "%'";
				FindProducts(sql);
			} else {
				sql = "SELECT * FROM SANPHAM where TenSP like N'%" + this.txbSearchProduct.getText() + "%'";
				FindProducts(sql);
			}
			tblListProduct.setRowSelectionInterval(0, 0);
			TableModel Entity = tblListProduct.getModel();
			ImageIcon icon = new ImageIcon(Entity.getValueAt(0, 7).toString());
			image1.setIcon(icon);
		}

		txbSearchProduct.setText("");
	}// GEN-LAST:event_searchBtn1ActionPerformed

	private void btnRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRefresh1ActionPerformed
		// TODO add your handling code here:
		tblListProduct.removeAll();
		String[] arr = { "Mã Sản Phẩm", "Mã Loại Sản Phẩm", "Mã Nhà Cung Cấp", "Tên Sản Phẩm", "Số Lượng", "Đơn Vị",
				"Giá", "Ảnh" };
		DefaultTableModel modle = new DefaultTableModel(arr, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblListProduct.setModel(modle);
		ImageIcon icon = new ImageIcon();
		image1.setIcon(icon);
	}// GEN-LAST:event_btnRefresh1ActionPerformed

	private void tblLoaiSPMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {// GEN-FIRST:event_tblLoaiSPMouseClicked
		// TODO add your handling code here:
		int Click = tblLoaiSP.getSelectedRow();
		TableModel model = tblLoaiSP.getModel();
		String sql = "SELECT * FROM SANPHAM where loaiSP_MaLoaiSP like N'%" + model.getValueAt(Click, 0).toString() + "'";
		FindProducts(sql);
	}// GEN-LAST:event_tblLoaiSPMouseClicked

	private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_btnUpdateActionPerformed

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
			java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				TaiKhoan account = new TaiKhoan();
				new Products(account).setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddClassify;
    private javax.swing.JButton btnAddProducer;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnBackHome;
    private javax.swing.JButton btnChangeClassify;
    private javax.swing.JButton btnChangeProducer;
    private javax.swing.JButton btnChangeProduct;
    private javax.swing.JButton btnDeleteClassify;
    private javax.swing.JButton btnDeleteProducer;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefresh1;
    private javax.swing.JButton btnRefreshClassify;
    private javax.swing.JButton btnRefreshProducer;
    private javax.swing.JButton btnSaveClassify;
    private javax.swing.JButton btnSaveProducer;
    private javax.swing.JButton btnSaveProduct;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cbxClassify;
    private javax.swing.JComboBox<String> cbxProducer;
    private javax.swing.JLabel image;
    private javax.swing.JLabel image1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblNhanVien1;
    private javax.swing.JLabel lblNhanVien2;
    private javax.swing.JLabel lblNhanVien3;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblStatus2;
    private javax.swing.JLabel lblStatus4;
    private javax.swing.JLabel lblStatusSearchProduct;
    private javax.swing.JRadioButton maSPrdo;
    private javax.swing.JButton printBtn;
    private javax.swing.JButton searchBtn1;
    private javax.swing.JButton sortBtn;
    private javax.swing.JTable tblClassify;
    private javax.swing.JTable tblListProduct;
    private javax.swing.JTable tblLoaiSP;
    private javax.swing.JTable tblProducer;
    private javax.swing.JTable tblProduct;
    private javax.swing.JRadioButton tenSPrdo;
    private javax.swing.JTextField txbAdress;
    private javax.swing.JTextField txbAmount;
    private javax.swing.JTextField txbClassify;
    private javax.swing.JTextField txbID;
    private javax.swing.JTextField txbIDClassify;
    private javax.swing.JTextField txbIDProducer;
    private javax.swing.JTextField txbLink;
    private javax.swing.JTextField txbName;
    private javax.swing.JTextField txbPhone;
    private javax.swing.JTextField txbPrice;
    private javax.swing.JTextField txbProducer;
    private javax.swing.JTextField txbSearchProduct;
    private javax.swing.JTextField txbUnit;
    // End of variables declaration//GEN-END:variables
}
