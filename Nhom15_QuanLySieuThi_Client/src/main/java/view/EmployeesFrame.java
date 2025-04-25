package view;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import iRemote.IChucVu;
import iRemote.INhanVien;
import iRemote.ITaiKhoan;
import view.util.FormatCurrency;
import view.util.FrameUtil;
import view.util.RMIUrl;

import entity.ChucVu;
import entity.NhanVien;
import entity.TaiKhoan;

//Frame Quản Lí Nhân Viên
public class EmployeesFrame extends javax.swing.JFrame {

    private Process processed; // khai báo tới controller
    private Boolean Add = false, Change = false; //
    private TaiKhoan account;
    private ITaiKhoan taiKhoanDao;
    private IChucVu chucVuDao;
    private INhanVien nhanVienDao;
    private FrameUtil frameUtil = new FrameUtil();
    private String rmiUrl = new RMIUrl().RMIUrl();
    private NumberFormat numberFormat = new FormatCurrency().FormatCurrency();

    public EmployeesFrame(TaiKhoan Ac) {
        // Thiết kế cái giao diện
        initComponents();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        lblStatus.setForeground(Color.red);
        lblPosition.setForeground(Color.red);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        try {
            taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://" + rmiUrl + ":3030/iTaiKhoan");
            chucVuDao = (IChucVu) Naming.lookup("rmi://" + rmiUrl + ":3030/iChucVu");
            nhanVienDao = (INhanVien) Naming.lookup("rmi://" + rmiUrl + ":3030/iNhanVien");
            account = Ac;// Gọi đến đối tượng Process liên kết với csdl
            btnEmployeeOff.setEnabled(true);
            lblNhanVien.setText("(" + account.getNhanVien().getMaNV() + ") " + account.getNhanVien().getHoTen());
            Disabled(); // Tắt ô input khi vừa vào Frame
            loadMaChucVu();// Load mã chức vụ có tên chức vụ theo sau
            loadEmployees();// Load danh sách nhân viên

            loadPosition();// Load bản chức vụ
            DisabledPosition(); // tắt ô input chức vụ
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

    // Bật các ô input tiến hành nhập liệu
    private void Enabled() {
        cbxPosition.setEnabled(true);
        txbCode.setEnabled(true);
        txbName.setEnabled(true);
        txbBirthday.setEnabled(true);
        cbxSex.setEnabled(true);
        txbPhone.setEnabled(true);
        txbCaLam.setEnabled(true);
        txbPayroll.setEnabled(true);
        lblStatus.setText("Trạng Thái!");
    }

    // Tắt các ô input
    private void Disabled() {
        cbxPosition.setEnabled(false);
        txbCode.setEnabled(false);
        txbName.setEnabled(false);
        txbBirthday.setEnabled(false);
        cbxSex.setEnabled(false);
        txbPhone.setEnabled(false);
        txbCaLam.setEnabled(false);
        txbPayroll.setEnabled(false);
        cbxStatus.setEnabled(false);
    }

    // Refresh lại dữ liệu màn hình
    private void Refresh() throws RemoteException {
        Add = false; // Không thêm
        Change = false; // Không sửa

        cbxPosition.removeAllItems();
        Disabled(); // Tắt các ô input
        loadMaChucVu();
        loadEmployees();
        txbCode.setText("");
        txbName.setText("");
        ((JTextField) txbBirthday.getDateEditor().getUiComponent()).setText("");
        txbPhone.setText("");
        txbPayroll.setText("");
        btnEmployeeOff.setEnabled(true);
        btnAdd.setEnabled(true);
        btnChange.setEnabled(false);
        btnSaveEmployees.setEnabled(false);
        lblStatus.setText("Trạng Thái");
    }

    // Kiểm tra null nhân viên khi nhập
    private boolean checkNull() {
        boolean kq = true;
        if (this.txbCode.getText().equals("")) {
            lblStatus.setText("Bạn chưa nhập mã nhân viên!");
            return false;
        }
        if (this.txbName.getText().equals("")) {
            lblStatus.setText("Bạn chưa nhập tên nhân viên!");
            return false;
        }
        if (this.txbPhone.getText().equals("")) {
            lblStatus.setText("Bạn chưa nhập số điện thoại!");
            return false;
        }
        if (((JTextField) txbBirthday.getDateEditor().getUiComponent()).getText().equals("")) {
            lblStatus.setText("Bạn chưa chọn ngày sinh!");
            return false;
        }

        return kq;
    }

    // Phương thức trả lại mã chức vụ có tên chức vụ theo sau
    private void loadMaChucVu() throws RemoteException {
        ArrayList arry = (ArrayList) chucVuDao.getAllCV();
        for (int i = 0; i < arry.size(); i++) {
            this.cbxPosition.addItem(((ChucVu) arry.get(i)).getMaCV() + " (" + ((ChucVu) arry.get(i)).getTenCV() + ")");
        }
    }

    // Đặt giá trị cbx giới tính
    private void gioiTinh(String s) {
        if (s.equals("Nam")) {
            cbxSex.setSelectedIndex(0);
        } else {
            cbxSex.setSelectedIndex(1);
        }
    }

    // Đặt giá trị cbx ca làm việc
    private void caLamViec(String s) {
        if (s.equals("Ca 1")) {
            txbCaLam.setSelectedIndex(0);
        } else {
            txbCaLam.setSelectedIndex(1);
        }
    }

    private void trangThaiLamViec(String s) {
        if (s.equals("Đang làm")) {
            txbCaLam.setSelectedIndex(0);
        } else {
            txbCaLam.setSelectedIndex(1);
        }
    }

    // Load danh sách nhân viên ra bảng
    private void loadEmployees() throws RemoteException {
        tblNhanVien.removeAll();
        tblNhanVien.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        String[] arr = {"Mã Nhân Viên", "Mã Chức Vụ", "Họ Tên", "Ngày Sinh", "Số Điện Thoại", "Giới Tính",
                "Ca Làm Việc", "Lương", "Trạng Thái"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<NhanVien> arry = nhanVienDao.getAllNV(); // Gọi phương thức ở controller lấy danh sách nhân viên
        for (int i = 0; i < arry.size(); i++) {
            Vector vector = new Vector();
            ChucVu chucVu = chucVuDao.getCV(((NhanVien) arry.get(i)).getChucVu().getMaCV());

            vector.add(((NhanVien) arry.get(i)).getMaNV());
            vector.add(chucVu.getMaCV() + " " + chucVu.getTenCV());
            vector.add(((NhanVien) arry.get(i)).getHoTen());
            vector.add(new SimpleDateFormat("dd/MM/yyyy").format(((NhanVien) arry.get(i)).getNgaySinh()));
            vector.add(((NhanVien) arry.get(i)).getSoDT());
            vector.add(((NhanVien) arry.get(i)).isGioiTinh() == true ? "Nam" : "Nữ");
            vector.add(((NhanVien) arry.get(i)).getCaLamViec());
            vector.add(numberFormat.format(((NhanVien) arry.get(i)).getLuong()));
            vector.add(((NhanVien) arry.get(i)).getTrangThai());
            modle.addRow(vector);
        }
        tblNhanVien.setModel(modle);
    }

    // Load danh sách nhân viên ra bảng
    private void loadEmployeesOff() throws RemoteException {
        tblNhanVien.removeAll();
        tblNhanVien.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        String[] arr = {"Mã Nhân Viên", "Mã Chức Vụ", "Họ Tên", "Ngày Sinh", "Số Điện Thoại", "Giới Tính",
                "Ca Làm Việc", "Lương", "Trạng Thái"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList arry = (ArrayList) nhanVienDao.getAllNVNghi(); // Gọi phương thức ở controller lấy danh sách nhân viên
        for (int i = 0; i < arry.size(); i++) {
            Vector vector = new Vector();

            vector.add(((NhanVien) arry.get(i)).getMaNV());
            vector.add(((NhanVien) arry.get(i)).getChucVu().getTenCV());
            vector.add(((NhanVien) arry.get(i)).getHoTen());
            vector.add(new SimpleDateFormat("dd/MM/yyyy").format(((NhanVien) arry.get(i)).getNgaySinh()));
            vector.add(((NhanVien) arry.get(i)).getSoDT());
            vector.add(((NhanVien) arry.get(i)).isGioiTinh() == true ? "Nam" : "Nữ");
            vector.add(((NhanVien) arry.get(i)).getCaLamViec());
            vector.add(((NhanVien) arry.get(i)).getLuong());
            vector.add(((NhanVien) arry.get(i)).getTrangThai());
            modle.addRow(vector);
        }
        tblNhanVien.setModel(modle);
    }

    // Bật ô input ở tab chức vụ
    private void EnabledPosition() {
        txbIDPosition.setEnabled(true);
        txbPosition.setEnabled(true);
        txbPayroll1.setEnabled(true);

    }

    // tắt ô input ở tab chức vụ
    private void DisabledPosition() {
        txbIDPosition.setEnabled(false);
        txbPosition.setEnabled(false);
        txbPayroll1.setEnabled(false);
    }

    // Refresh chức vụ ở tab chức vụ
    private void RefreshPosition() {
        Change = false;
        Add = false;
        txbIDPosition.setText("");
        txbPosition.setText("");
        txbPayroll1.setText("");
        lblPosition.setText("Trạng Thái!");

        btnAddPosition.setEnabled(true);
        btnChangePosition.setEnabled(false);
        btnDeletePosition.setEnabled(false);
        btnSavePosition.setEnabled(false);

        DisabledPosition();
    }

    // Kiểm tra chức vụ có null không
    private boolean checkNullPosition() {
        boolean kq = true;
        if (String.valueOf(this.txbIDPosition.getText()).length() == 0) {
            lblPosition.setText("Bạn chưa mã chức vụ!");
            return false;
        }
        if (String.valueOf(this.txbPosition.getText()).length() == 0) {
            lblPosition.setText("Bạn chưa nhập tên chức vụ!");
            return false;
        }
        if (String.valueOf(this.txbPayroll1.getText()).length() == 0) {
            lblPosition.setText("Bạn chưa nhập lương cơ bản của chức vụ!");
            return false;
        }
        return kq;
    }

    // Load ds chức vụ ở tab chức vụ
    private void loadPosition() throws RemoteException {
        tblChucVu.removeAll();
        tblChucVu.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        String[] arr = {"Mã Chức Vụ", "Tên Chức Vụ", "Lương"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<ChucVu> arry = chucVuDao.getAllCV();
        for (int i = 0; i < arry.size(); i++) {
            Vector vector = new Vector();
            vector.add(((ChucVu) arry.get(i)).getMaCV());
            vector.add(((ChucVu) arry.get(i)).getTenCV());
            vector.add(numberFormat.format(((ChucVu) arry.get(i)).getLuong()));
            modle.addRow(vector);
        }
        tblChucVu.setModel(modle);
    }

    // Cắt chuỗi
    private String cutChar(String arry) {
        return arry.replaceAll("\\D+", "");
    }

    // Đổi String thành kiểu số
    private double convertedToNumbers(String s) {
        String number = ""; // Khai báo mảng rỗng
        String[] array = s.replace(",", " ").split("\\s"); // cắt mảng
        for (String i : array) {
            number = number.concat(i); // nối các chuỗi
        }
        return Double.parseDouble(number); // Convert sang số
    }

    // Tự phát sinh mã nhân viên dựa trên cbx chức vụ
    private void automatedCode() throws RemoteException {
        String[] arry = cbxPosition.getSelectedItem().toString().split("\\s"); // Mảng cbx chức vụ khhi chọn
        if (nhanVienDao.getNVByChucVu(arry[0]).size() < 10) {
            txbCode.setText(arry[0] + "0000" + String.valueOf(nhanVienDao.getNVByChucVu(arry[0]).size() + 1));
        } else {
            txbCode.setText(arry[0] + "0000" + String.valueOf(nhanVienDao.getNVByChucVu(arry[0]).size() + 1));
        }
    }

    // Tự phát sinh lương nhân viên dựa trên chức vụ
    private void automatedPay() throws RemoteException {
        String[] chuoi = cbxPosition.getSelectedItem().toString().split("\\s");
        ChucVu position = chucVuDao.getCV(chuoi[0]);
        txbPosition.setText(position.getTenCV());
        Locale locale = new Locale("vi", "VN");
        Currency currency = Currency.getInstance("VND");

        DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
        df.setCurrency(currency);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setCurrency(currency);
        String arry = numberFormat.format(position.getLuong());
        txbPayroll.setText(arry);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnHomeBack = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        tabIFEmployees = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txbCode = new javax.swing.JTextField();
        txbName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbxPosition = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cbxSex = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txbBirthday = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        txbPhone = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txbPayroll = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txbCaLam = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cbxStatus = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnSaveEmployees = new javax.swing.JButton();
        btnEmployeeOff = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnRefresh2 = new javax.swing.JButton();
        btnAddPosition = new javax.swing.JButton();
        btnChangePosition = new javax.swing.JButton();
        btnSavePosition = new javax.swing.JButton();
        btnDeletePosition = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        txbIDPosition = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txbPosition = new javax.swing.JTextField();
        txbPayroll1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        lblPosition = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChucVu = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        lblNhanVien1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnHomeBack.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHomeBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Previous-icon.png"))); // NOI18N
        btnHomeBack.setText("Hệ Thống");
        btnHomeBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeBackActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Quản Lý Nhân Viên");

        tabIFEmployees.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel2.setText("Mã nhân viên:");

        txbCode.setEditable(false);
        txbCode.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        txbName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel3.setText("Họ và tên:");

        cbxPosition.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxPosition.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                try {
                    cbxPositionPopupMenuWillBecomeInvisible(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPositionActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel1.setText("Chức vụ:");

        cbxSex.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxSex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Nam", "Nữ"}));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel5.setText("Giới tính:");

        txbBirthday.setDateFormatString("dd/MM/yyyy");
        txbBirthday.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText("Ngày Sinh:");

        txbPhone.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPhoneKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel8.setText("Ca Làm Việc");

        txbPayroll.setEditable(false);
        txbPayroll.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel10.setText("Lương:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel7.setText("Số điện thoại:");

        txbCaLam.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbCaLam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Ca 1", "Ca 2"}));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel11.setText("Trạng Thái:");

        cbxStatus.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Đang Làm", "Nghỉ Việc"}));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addComponent(jLabel1))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel4)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cbxPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txbBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(51, 51, 51)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(txbCode, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(61, 61, 61)
                                                .addComponent(jLabel3))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(txbCaLam, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cbxSex, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txbName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(100, 100, 100)
                                                .addComponent(jLabel7)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel11)
                                                        .addComponent(jLabel10))
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(21, 21, 21)
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txbPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(18, 18, 18)
                                                                .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(100, 100, 100))))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(cbxPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(txbCode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(txbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(38, 38, 38)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel8)
                                                                .addComponent(txbCaLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel5)
                                                                .addComponent(cbxSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(txbBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel10)
                                                        .addComponent(txbPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel11)
                                                        .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(11, 11, 11))))
        );

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

        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnAdd.setText("Thêm");
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
        btnChange.setText("Sửa");
        btnChange.setEnabled(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnChangeActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnSaveEmployees.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSaveEmployees.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Save.png"))); // NOI18N
        btnSaveEmployees.setText("Lưu");
        btnSaveEmployees.setEnabled(false);
        btnSaveEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnSaveEmployeesActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnEmployeeOff.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnEmployeeOff.setText("Nhân Viên Nghỉ");
        btnEmployeeOff.setEnabled(false);
        btnEmployeeOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnEmployeeOffActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(153, 153, 153)
                                .addComponent(btnEmployeeOff, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(134, 134, 134)
                                .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92)
                                .addComponent(btnSaveEmployees, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(179, 179, 179))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                                                        .addComponent(btnEmployeeOff, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(15, 15, 15))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(btnSaveEmployees, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                                        .addComponent(btnChange, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        tblNhanVien.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        tblNhanVien.setRowHeight(26);
        tblNhanVien.setRowMargin(2);
        tblNhanVien.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblNhanVien);

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Trạng Thái");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 51));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel9.setText("Nhân Viên:");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien.setText("(QL02) vuthai1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 1339, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jScrollPane2)))
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNhanVien))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(34, Short.MAX_VALUE))
        );

        tabIFEmployees.addTab("Thông Tin Nhân Viên", jPanel1);

        btnRefresh2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefresh2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh2ActionPerformed(evt);
            }
        });

        btnAddPosition.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnAddPosition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnAddPosition.setText("Thêm");
        btnAddPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPositionActionPerformed(evt);
            }
        });

        btnChangePosition.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnChangePosition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change.png"))); // NOI18N
        btnChangePosition.setText("Sửa");
        btnChangePosition.setEnabled(false);
        btnChangePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePositionActionPerformed(evt);
            }
        });

        btnSavePosition.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSavePosition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Save.png"))); // NOI18N
        btnSavePosition.setText("Lưu");
        btnSavePosition.setEnabled(false);
        btnSavePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnSavePositionActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnDeletePosition.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnDeletePosition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Delete.png"))); // NOI18N
        btnDeletePosition.setText("Xóa");
        btnDeletePosition.setEnabled(false);
        btnDeletePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnDeletePositionActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(btnRefresh2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(146, 146, 146)
                                .addComponent(btnAddPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnChangePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(130, 130, 130)
                                .addComponent(btnDeletePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(137, 137, 137)
                                .addComponent(btnSavePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(163, 163, 163))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnDeletePosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnChangePosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnRefresh2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSavePosition, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                        .addComponent(btnAddPosition, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        txbIDPosition.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel14.setText("Mã Chức Vụ:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("Chức Vụ:");

        txbPosition.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        txbPayroll1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbPayroll1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPayroll1KeyReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel16.setText("Lương Cơ Bản:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(txbIDPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15)
                                .addGap(31, 31, 31)
                                .addComponent(txbPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(217, 217, 217)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbPayroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(164, 164, 164))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel16)
                                                .addComponent(txbPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel15)
                                                .addComponent(txbPayroll1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel14)
                                                .addComponent(txbIDPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(47, Short.MAX_VALUE))
        );

        lblPosition.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblPosition.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosition.setText("Trạng Thái");

        tblChucVu.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblChucVu.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        tblChucVu.setRowHeight(26);
        tblChucVu.setRowMargin(2);
        tblChucVu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblChucVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChucVuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblChucVu);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 51));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel17.setText("Nhân Viên: ");

        lblNhanVien1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien1.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien1.setText("(QL02) vuthai1");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1737, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNhanVien1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 855, Short.MAX_VALUE)
        );

        tabIFEmployees.addTab("Cập Nhật Chức Vụ", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tabIFEmployees)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnHomeBack, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1550, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnHomeBack, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabIFEmployees)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        frameUtil.exit(this); // thoát khỏi frame
    }

    private void cbxPositionPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) throws RemoteException {
        automatedCode();// Tự phát sinh mã nhân viên dựa trên chức vụ
        automatedPay();// Tự phát sinh lương khi chọn chức vụ
    }

    private void txbPhoneKeyReleased(java.awt.event.KeyEvent evt) {
        txbPhone.setText(cutChar(txbPhone.getText()));// Cắt chuỗi sdt cho hợp lí
    }

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        Refresh();
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        Refresh();
        Add = true;
        Enabled();
        btnAdd.setEnabled(false);
        btnSaveEmployees.setEnabled(true);
    }

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        Add = false;
        Change = true;
        Enabled();
        lblStatus.setText("Trạng Thái!");
        loadMaChucVu();
        btnChange.setEnabled(false);
        btnAdd.setEnabled(true);
        cbxStatus.setEnabled(true);
        btnSaveEmployees.setEnabled(true);
        cbxPosition.setEnabled(false);
        txbCode.setEnabled(false);
    }

    private void btnSaveEmployeesActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        //Chọn cbx chức vụ
        String[] arry = cbxPosition.getSelectedItem().toString().split("\\s");
        int year = Calendar.getInstance().get(Calendar.YEAR); // Năm hiện tại
        int yearBirthday = txbBirthday.getCalendar().get(Calendar.YEAR); // Năm sinh của nhân viên
        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$"; // regex số điện thoại
        double luong = 0;
        if (arry[0].equals("BH")) {
            luong = 10000000;
        } else if (arry[0].equals("QL")) {
            luong = 25000000;
        } else {
            luong = 15000000;
        }
        if (checkNull()) {//Kiểm tra null trong các ô input
            if (Add == true) { // chọn thêm
                if (nhanVienDao.getNhanVien(txbCode.getText()) == null) { // Gọi tới phương thức kiểm tra nhân viên ở controller
                    if (year - yearBirthday >= 18 && txbPhone.getText().matches(reg)) {

                        ChucVu chucVu = chucVuDao.getCV(arry[0]);
                        NhanVien nhanVien = new NhanVien(txbCode.getText(), chucVu, txbName.getText(), new java.sql.Date(txbBirthday.getDate().getTime()), txbPhone.getText(), cbxSex.getSelectedItem().toString().equals("Nam"), txbCaLam.getSelectedItem().toString(), luong, cbxStatus.getSelectedItem().toString());
                        nhanVienDao.addNhanVien(nhanVien); //Thêm nhân viên
                        Disabled();// Tắt các ô input
                        Refresh(); // Refresh lại

                        lblStatus.setText("Thêm nhân viên thành công!");
                    } else if (year - yearBirthday < 18) { // nếu nhân viên nhỏ hơn 18 tuổi
                        lblStatus.setText("Tuổi nhân viên nhỏ hơn 18 tuổi!");
                    } else {
                        lblStatus.setText("Số điện thoại nhập không đúng định dạng!");
                    }

                } else {
                    lblStatus.setText("Mã nhân viên đã tồn tại!");
                }
            } else if (Change == true) { //Chọn sửa
                int click = tblNhanVien.getSelectedRow(); //Chọn dòng phát sinh ra số dòng
                TableModel Entity = tblNhanVien.getModel();

                if (Entity.getValueAt(click, 0).toString().equals(txbCode.getText())) { //Chọn dòng trùng với mã nhân viên
                    if (year - yearBirthday >= 18 && txbPhone.getText().matches(reg)) {
                        ChucVu chucVu = chucVuDao.getCV(arry[0]);
                        NhanVien nhanVien = new NhanVien(txbCode.getText(), chucVu, txbName.getText(), new java.sql.Date(txbBirthday.getDate().getTime()), txbPhone.getText(), cbxSex.getSelectedItem().toString().equals("Nam"), txbCaLam.getSelectedItem().toString(), luong, cbxStatus.getSelectedItem().toString());
                        nhanVienDao.updateNhanVien(nhanVien); //Sửa nhân viên
                        Disabled();//Tắt thông tin các ô input
                        Refresh();//Refresh lại

                        lblStatus.setText("Lưu thay đổi thành công!"); //Thông báo
                    } else if (year - yearBirthday < 18) {// nếu nhân viên nhỏ hơn 18 tuổi
                        lblStatus.setText("Tuổi nhân viên nhỏ hơn 18 tuổi!");
                    } else {
                        lblStatus.setText("Số điện thoại nhập không đúng định dạng!");
                    }
                } else if (nhanVienDao.getNhanVien(txbCode.getText()) != null) { //Kiểm tra nhân viên
                    if (year - yearBirthday >= 18 && txbPhone.getText().matches(reg)) {
                        ChucVu chucVu = chucVuDao.getCV(arry[0]);
                        NhanVien nhanVien = new NhanVien(txbCode.getText(), chucVu, txbName.getText(), new java.sql.Date(txbBirthday.getDate().getTime()), txbPhone.getText(), cbxSex.getSelectedItem().toString().equals("Nam"), txbCaLam.getSelectedItem().toString(), luong, cbxStatus.getSelectedItem().toString());
                        nhanVienDao.updateNhanVien(nhanVien); //Sửa nhân viên
                        Refresh();//Refresh lại
                        lblStatus.setText("Lưu thay đổi thành công!"); //Thông báo
                    } else if (year - yearBirthday < 18) {
                        lblStatus.setText("Tuổi nhân viên nhỏ hơn 18 tuổi!");
                    } else {
                        lblStatus.setText("Số điện thoại nhập không đúng định dạng!");
                    }
                }
            }
        }
    }

    private void btnEmployeeOffActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        int click = JOptionPane.showConfirmDialog(null, "Bạn có muốn hiện danh sách nhân viên nghỉ việc hay không?",
                "Thông Báo", 2);
        if (click == JOptionPane.YES_OPTION) {
            int clickTable = tblNhanVien.getSelectedRow(); // Chọn 1 dòng phát sinh ra số dòng

            loadEmployeesOff();
            this.lblStatus.setText("Danh Sách Nhân Viên Nghỉ Việc!");
        }
    }

    private void btnRefresh2ActionPerformed(java.awt.event.ActionEvent evt) {
        RefreshPosition();
    }

    private void btnAddPositionActionPerformed(java.awt.event.ActionEvent evt) {
        RefreshPosition();
        Add = true;
        EnabledPosition();
        btnAddPosition.setEnabled(false);
        btnSavePosition.setEnabled(true);
    }

    private void btnChangePositionActionPerformed(java.awt.event.ActionEvent evt) {
        Add = false;
        Change = true;
        EnabledPosition();
        lblPosition.setText("Trạng Thái!");

        btnChangePosition.setEnabled(false);
        btnDeletePosition.setEnabled(false);
        btnAddPosition.setEnabled(false);
        btnSavePosition.setEnabled(true);
    }

    private void btnSavePositionActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        if (checkNullPosition()) { // Kiểm tra chức vụ
            if (Add == true) { // Thêm chức vụ
                if (chucVuDao.getCV(txbIDPosition.getText()) == null) { // Kiểm tra mã chức vụ
                    String newStr = txbPayroll1.getText().replaceAll(",", "");
                    String newLuongStr = newStr.replaceAll(" đ", "");

                    double luong = Double.parseDouble(newLuongStr);
                    ChucVu chucVu = new ChucVu(txbIDPosition.getText(), txbPosition.getText(), luong);
                    chucVuDao.addChucVu(chucVu); // Thêm
                    // chức
                    // vụ
                    DisabledPosition();
                    RefreshPosition();
                    loadPosition();
                    lblPosition.setText("Thêm chức vụ thành công!");
                } else {
                    lblPosition.setText("Mã chức vụ đã tồn tại!");
                }
            } else if (Change == true) { // Sửa chức vụ
                int click = tblChucVu.getSelectedRow();
                TableModel Entity = tblChucVu.getModel();

                if (Entity.getValueAt(click, 0).toString().equals(txbIDPosition.getText())) { // Kiểm tra mã chức vụ
                    String newStr = txbPayroll1.getText().replace(".", "").replace(" ₫", "");
                    double luong = Double.parseDouble(newStr);
                    ChucVu chucVu = new ChucVu(txbIDPosition.getText(), txbPosition.getText(), luong);
                    chucVuDao.updateChucVu(chucVu); // Sửa chức vụ
                    DisabledPosition();
                    RefreshPosition();
                    loadPosition();
                    lblPosition.setText("Lưu thay đổi thành công!");
                } else if (chucVuDao.getCV(txbIDPosition.getText()) != null) {
                    String newStr = txbPayroll1.getText().replace(".", "").replace(" ₫", "");
                    double luong = Double.parseDouble(newStr);
                    ChucVu chucVu = new ChucVu(txbIDPosition.getText(), txbPosition.getText(), luong);
                    chucVuDao.updateChucVu(chucVu);
                    DisabledPosition();
                    RefreshPosition();
                    loadPosition();
                    lblPosition.setText("Lưu thay đổi thành công!");
                } else {
                    lblPosition.setText("Mã chức vụ bạn thay đổi đã tồn tại!!");
                }
            }
        }
    }

    private void btnDeletePositionActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        int click = JOptionPane.showConfirmDialog(null, "Bạn có muốn chức vụ hay không?", "Thông Báo", 2); // Hiện thông
        // báo bạn
        // có muốn
        // xoá hay
        // không
        if (click == JOptionPane.YES_OPTION) {
            ChucVu chucVu = new ChucVu(txbIDPosition.getText());
            chucVuDao.deleteChucVu(chucVu); // Xóa chức vụ
            RefreshPosition();
            loadPosition();
            this.lblStatus.setText("Xóa chức vụ thành công!");
        }
    }

    private void txbPayroll1KeyReleased(java.awt.event.KeyEvent evt) {
        DecimalFormat formatter = new DecimalFormat("###,###,###"); // Gọi đối tượng format
        txbPayroll1.setText(cutChar(txbPayroll1.getText())); // Format lại lương
        if (txbPayroll1.getText().equals("")) { // Nếu lương null
            return;
        } else {
            txbPayroll1.setText(numberFormat.format(txbPayroll1.getText()));
        }
    }

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {
        int click = tblNhanVien.getSelectedRow();
        TableModel Entity = tblNhanVien.getModel();
        cbxPosition.removeAllItems();
        txbCode.setText(Entity.getValueAt(click, 0).toString());
        cbxPosition.addItem(Entity.getValueAt(click, 1).toString());
        txbName.setText(Entity.getValueAt(click, 2).toString());
        ((JTextField) txbBirthday.getDateEditor().getUiComponent()).setText(Entity.getValueAt(click, 3).toString());
        txbPhone.setText(Entity.getValueAt(click, 4).toString());
        gioiTinh(Entity.getValueAt(click, 5).toString());
        caLamViec(Entity.getValueAt(click, 6).toString());
        txbPayroll.setText(Entity.getValueAt(click, 7).toString());
        trangThaiLamViec(Entity.getValueAt(click, 8).toString());
        btnChange.setEnabled(true);
    }

    private void tblChucVuMouseClicked(java.awt.event.MouseEvent evt) {
        int click = tblChucVu.getSelectedRow();
        TableModel Entity = tblChucVu.getModel();

        txbIDPosition.setText(Entity.getValueAt(click, 0).toString());
        txbPosition.setText(Entity.getValueAt(click, 1).toString());
        String[] s = Entity.getValueAt(click, 2).toString().split("\\s");
        txbPayroll1.setText(s[0]);

        btnChangePosition.setEnabled(true);
        btnDeletePosition.setEnabled(true);
    }

    private void cbxPositionActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void btnHomeBackActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        HomeQuanLi home = new HomeQuanLi(account);
        this.setVisible(false);
        home.setVisible(true);

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
            java.util.logging.Logger.getLogger(EmployeesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaiKhoan account = new TaiKhoan();
                new EmployeesFrame(account).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddPosition;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnChangePosition;
    private javax.swing.JButton btnDeletePosition;
    private javax.swing.JButton btnEmployeeOff;
    private javax.swing.JButton btnHomeBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefresh2;
    private javax.swing.JButton btnSaveEmployees;
    private javax.swing.JButton btnSavePosition;
    private javax.swing.JComboBox<String> cbxPosition;
    private javax.swing.JComboBox<String> cbxSex;
    private javax.swing.JComboBox<String> cbxStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblNhanVien1;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTabbedPane tabIFEmployees;
    private javax.swing.JTable tblChucVu;
    private javax.swing.JTable tblNhanVien;
    private com.toedter.calendar.JDateChooser txbBirthday;
    private javax.swing.JComboBox<String> txbCaLam;
    private javax.swing.JTextField txbCode;
    private javax.swing.JTextField txbIDPosition;
    private javax.swing.JTextField txbName;
    private javax.swing.JTextField txbPayroll;
    private javax.swing.JTextField txbPayroll1;
    private javax.swing.JTextField txbPhone;
    private javax.swing.JTextField txbPosition;
    // End of variables declaration//GEN-END:variables
}
