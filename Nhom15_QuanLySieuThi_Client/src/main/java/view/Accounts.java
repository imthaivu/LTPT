package view;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import iRemote.INhanVien;
import iRemote.ITaiKhoan;
import view.util.FrameUtil;
import view.util.RMIUrl;

import java.util.Vector;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import entity.NhanVien;
import entity.TaiKhoan;

//Frame Cập Nhật Tài Khoản
public class Accounts extends javax.swing.JFrame {

	private Process processed; // Khai báo controller processed
	private Boolean Add = false, Change = false; // Khai báo khi bấm thêm hoặc sửa
	private TaiKhoan account;
	private String rmiUrl = new RMIUrl().RMIUrl();
	private ITaiKhoan taiKhoanDao;
	private INhanVien nhanVienDao;
	private FrameUtil frameUtil = new FrameUtil();

	public Accounts(TaiKhoan Ac) {
		initComponents();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		lblStatus.setForeground(Color.red);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		try {
			taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://" + rmiUrl + ":3030/iTaiKhoan");
			nhanVienDao = (INhanVien) Naming.lookup("rmi://" + rmiUrl + ":3030/iNhanVien");
			account = Ac; // gọi đến Entity tài khoản lấy thông tin tài khoản, đăng nhập và thoát Frame
							// phù hợp
			lblNhanVien.setText("(" + account.getNhanVien().getMaNV() + ") " + account.getNhanVien().getHoTen());
			loadDSNhanVien(); // load ra danh sách nhân viên
			Disabled(); // Tắt đi những ô input và nút
			loadAccounts(); // load danh sách tài khoản
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

	// Phương thức tắt đi những ô input nhập liệu và nút
	private void Disabled() {
		txbIDAccount.setEnabled(false);
		tbxUser.setEnabled(false);
		tbxPass.setEnabled(false);
		btn.setEnabled(false);
		cbxEmployees.setEnabled(false);
		cbxLoaiTK.setEnabled(false);
	}

	// Phương thức bật những ô input nhập liệu và nút
	private void Enabled() {
		txbIDAccount.setEnabled(true);
		tbxUser.setEnabled(true);
		tbxPass.setEnabled(true);
		btn.setEnabled(true);
		cbxEmployees.setEnabled(true);
		cbxLoaiTK.setEnabled(true);
	}

	// Phương thức khởi động lại các ô input
	private void Refresh() throws RemoteException {
		cbxEmployees.removeAllItems(); // refresh lại combo nhân viên

		loadDSNhanVien(); // load lại danh sách nhân viên
		Disabled(); // tắt đi ô input và nút

		Add = false;
		Change = false;
		tbxUser.setText("");
		txbIDAccount.setText("");
		tbxPass.setText("");

		btnChange.setEnabled(false);
		btnAdd.setEnabled(true);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);
		lblStatus.setText("Trạng Thái!");
	}

	// Phương thức kiểm tra tài khoản có bị trống hay không khi nhập liệu
	private boolean checkNull() {
		boolean kq = true;
		if (this.txbIDAccount.getText().equals("")) {
			lblStatus.setText("Bạn chưa nhập mã tài khoản");
			return false;
		}

		if (this.tbxUser.getText().equals("")) {
			lblStatus.setText("Bạn chưa nhập tên đăng nhập");
			return false;
		}

		if (this.tbxPass.getText().equals("")) {
			lblStatus.setText("Bạn chưa nhập mật khẩu!");
			return false;
		}
		return kq;
	}

	// Tự phát sinh mã tài khoản
	private void newAccount() throws RemoteException {
		if (taiKhoanDao.getAllTaiKhoan().size() < 9) {
			txbIDAccount.setText("TK" + "0000" + String.valueOf(taiKhoanDao.getAllTaiKhoan().size() + 1));
		} else {
			txbIDAccount.setText("TK" + "000" + String.valueOf(taiKhoanDao.getAllTaiKhoan().size() + 1));
		}
	}

	// Lấy danh sách thông tin nhân viên đã có trong csdl
	private void loadDSNhanVien() throws RemoteException {
		List<NhanVien> arry = nhanVienDao.getAllNV();
		for (NhanVien nv : arry) {
			this.cbxEmployees.addItem(nv.getMaNV().trim() + " (" + nv.getHoTen().trim() + ")");
		}
	}

	// Load danh sách các account ra bảng
	private void loadAccounts() throws RemoteException {
		tblAccount.removeAll();
		tblAccount.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		String[] arr = { "Mã Tài Khoản", "Mã Nhân Viên", "Tên Đăng Nhập", "Mật Khẩu", "Vai Trò" };
		DefaultTableModel modle = new DefaultTableModel(arr, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		List<TaiKhoan> arry = taiKhoanDao.getAllTaiKhoan();
		for (TaiKhoan tk : arry) {
			Vector vector = new Vector();
			vector.add(tk.getMaTK());
			vector.add(tk.getNhanVien().getMaNV() + " (" + tk.getNhanVien().getHoTen() + ")");
			vector.add(tk.getTaiKhoan());
			vector.add(tk.getMatKhau());
			vector.add(tk.getVaiTro());
			modle.addRow(vector);
		}
		tblAccount.setModel(modle);
	}

	// Trả về thông tin vai trò của tài khoản
	private String taiKhoan() {
		if (cbxLoaiTK.getSelectedIndex() == 0) {
			return "BanHang";
		}

		if (cbxLoaiTK.getSelectedIndex() == 1) {
			return "ThongKe";
		}

		if (cbxLoaiTK.getSelectedIndex() == 2) {
			return "QuanLi";
		}
		return null;
	}

	// Trả thông tin tài khoản khi có vai trò của tài khoản
	private void loaiTaiKhoan(String s) {
		if (s.equals("BH")) {
			cbxLoaiTK.setSelectedIndex(0);
		} else if (s.equals("TK")) {
			cbxLoaiTK.setSelectedIndex(1);
		} else {
			cbxLoaiTK.setSelectedIndex(2);
		}
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAccount = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txbIDAccount = new javax.swing.JTextField();
        cbxEmployees = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbxLoaiTK = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        btn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        tbxUser = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tbxPass = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1550, 1154));
        setSize(new java.awt.Dimension(800, 1080));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel4KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPanel4KeyReleased(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Previous-icon.png"))); // NOI18N
        btnBack.setText("Hệ Thống");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        btnBack.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBackKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Cập Nhật Tài Khoản Đăng Nhập");
        jLabel6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jLabel6KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        tblAccount.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        tblAccount.setModel(new javax.swing.table.DefaultTableModel(
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
        tblAccount.setRowHeight(26);
        tblAccount.setRowMargin(2);
        tblAccount.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAccountMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAccount);

        txbIDAccount.setEditable(false);
        txbIDAccount.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        cbxEmployees.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxEmployees.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxEmployeesPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxEmployees.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxEmployeesKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel1.setText("Tên Đăng Nhập:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel2.setText("Mật Khẩu:");

        cbxLoaiTK.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbxLoaiTK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BH", "TK", "QL" }));
        cbxLoaiTK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbxLoaiTKKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel7.setText("Vai Trò:");

        btn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn.setText("...");
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel5.setText("Mã Tài Khoản:");

        tbxUser.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tbxUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbxUserKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel4.setText("Danh Sách Nhân Viên:");

        tbxPass.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tbxPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbxPassActionPerformed(evt);
            }
        });
        tbxPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbxPassKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txbIDAccount)
                    .addComponent(cbxLoaiTK, 0, 223, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tbxUser)
                    .addComponent(cbxEmployees, 0, 251, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tbxPass, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txbIDAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tbxUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addComponent(tbxPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(cbxLoaiTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbxEmployees, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Trạng Thái");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 51, 51));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel3.setText("Nhân Viên: ");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien.setText("(QL01) Mai Văn Trường");

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

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Delete.png"))); // NOI18N
        btnDelete.setText("Xóa");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 1371, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 395, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(115, 115, 115)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(217, 217, 217)
                        .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(176, 176, 176)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(7, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNhanVien))
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(278, 278, 278))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
		// Event khi thoát cửa sổ

	private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
		frameUtil.exit(this);
	}// GEN-LAST:event_formWindowClosing
		// Event khi nhấn vào table account

	private void tblAccountMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblAccountMouseClicked
		int Click = tblAccount.getSelectedRow();
		TableModel Entity = tblAccount.getModel();

		cbxEmployees.removeAllItems();

		txbIDAccount.setText(Entity.getValueAt(Click, 0).toString());
		cbxEmployees.addItem(Entity.getValueAt(Click, 1).toString());
		tbxUser.setText(Entity.getValueAt(Click, 2).toString());
		tbxPass.setText(Entity.getValueAt(Click, 3).toString());

		loaiTaiKhoan(Entity.getValueAt(Click, 4).toString());

		btnDelete.setEnabled(true);
		btnChange.setEnabled(true);
	}// GEN-LAST:event_tblAccountMouseClicked

	private void cbxEmployeesPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {// GEN-FIRST:event_cbxEmployeesPopupMenuWillBecomeInvisible

	}// GEN-LAST:event_cbxEmployeesPopupMenuWillBecomeInvisible

	private void btnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnActionPerformed
		EmployeesFrame employees = new EmployeesFrame(account);
		this.setVisible(false);
		employees.setVisible(true);
	}// GEN-LAST:event_btnActionPerformed

	private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnRefreshActionPerformed
		Refresh();
	}// GEN-LAST:event_btnRefreshActionPerformed

	private void btnAddActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnAddActionPerformed
		Refresh();
		newAccount();
		Add = true;
		Enabled();
		tbxUser.requestFocus();
		btnAdd.setEnabled(false);
		btnSave.setEnabled(true);
	}// GEN-LAST:event_btnAddActionPerformed

	private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnChangeActionPerformed
		Add = false;
		Change = true;
		Enabled();
		tbxUser.requestFocus();
		cbxEmployees.removeAllItems();
		lblStatus.setText("Trạng Thái!");
		loadDSNhanVien();

		btnChange.setEnabled(false);
		btnDelete.setEnabled(false);
		btnAdd.setEnabled(false);
		btnSave.setEnabled(true);
	}// GEN-LAST:event_btnChangeActionPerformed

	private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnDeleteActionPerformed
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 18)));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
		int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa tài khoản hay không?", "Thông Báo", 2);
		if (Click == JOptionPane.YES_OPTION) {
			String[] arry = cbxEmployees.getSelectedItem().toString().split("\\s"); // Lấy mã nhân viên
			taiKhoanDao.xoaTaiKhoan(txbIDAccount.getText()); // Gọi phương thức xóa tài khoản
			Refresh();
			loadAccounts();
			this.lblStatus.setText("Xóa tài khoản thành công!");
		}
	}// GEN-LAST:event_btnDeleteActionPerformed

	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {// GEN-FIRST:event_btnSaveActionPerformed
		// Lấy mã danh sách nhân viên dựa trên cbx
		String[] arry = cbxEmployees.getSelectedItem().toString().split("\\s");
		if (checkNull()) { // kiểm tra có null không
			if (Add == true) { // Nếu bấm nút thêm
				if (taiKhoanDao.getTaiKhoan(txbIDAccount.getText()) == null) { // Kiểm tra tài khoản dựa theo mã tài khoản có
																		// trong csdl k
					NhanVien nhanVien = nhanVienDao.getNhanVien(arry[0]);
					TaiKhoan taiKhoan = new TaiKhoan(txbIDAccount.getText(), tbxUser.getText(), tbxPass.getText(), cbxLoaiTK.getSelectedItem().toString(), nhanVien);
					 taiKhoanDao.themTaiKhoan(taiKhoan); // Thêm tài khoản
					Disabled(); // Tắt đi những ô input
					Refresh(); // Refresh lại
					loadAccounts(); // load ra danh sách tài khoản
					lblStatus.setText("Thêm tài khoản thành công!"); // Thông báo
				} else {
					lblStatus.setText("Tài khoản đăng nhập đã tồn tại!!"); // thông báo
				}
			} else if (Change == true) { // Nếu bấm nút sửa
				int Click = tblAccount.getSelectedRow(); // người dùng bấm một dòng trên bảng
				TableModel Entity = tblAccount.getModel();

				if (Entity.getValueAt(Click, 0).toString().equals(txbIDAccount.getText())) { // Giá trị dầu tiên khi bấm
																								// là mã tài khoản
					NhanVien nhanVien = nhanVienDao.getNhanVien(arry[0]);
					TaiKhoan taiKhoan = new TaiKhoan(txbIDAccount.getText(), tbxUser.getText(), tbxPass.getText(), cbxLoaiTK.getSelectedItem().toString(), nhanVien);
					taiKhoanDao.suaTaiKhoan(taiKhoan); // Sửa tài khoản
					Disabled(); // Tắt đi các ô input
					Refresh(); // Refresh lại
					loadAccounts();// Trả về thông tin
					lblStatus.setText("Lưu thay đổi thành công!");// thông báo
				} else if (taiKhoanDao.getTaiKhoan(txbIDAccount.getText()) != null) {
					NhanVien nhanVien = nhanVienDao.getNhanVien(arry[0]);
					TaiKhoan taiKhoan = new TaiKhoan(txbIDAccount.getText(), tbxUser.getText(), tbxPass.getText(), cbxLoaiTK.getSelectedItem().toString(), nhanVien);
					taiKhoanDao.suaTaiKhoan(taiKhoan); // Sửa tài khoản
					Disabled();
					Refresh();
					loadAccounts();
					lblStatus.setText("Lưu thay đổi thành công!");
				} else {
					lblStatus.setText("Tài khoản đăng nhập bạn sửa đổi đã tồn tại!!");
				}
			}
		}
	}// GEN-LAST:event_btnSaveActionPerformed
		// Event khi nhấn nút thoát

	private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBackActionPerformed
		// TODO add your handling code here:
		HomeQuanLi home = new HomeQuanLi(account);
		this.setVisible(false);
		home.setVisible(true);

	}// GEN-LAST:event_btnBackActionPerformed
		// Event khi nhấn nút esc thì back

	private void btnBackKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_btnBackKeyPressed
		// TODO add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			btnBack.doClick();
		}

	}// GEN-LAST:event_btnBackKeyPressed

	private void jPanel4KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jPanel4KeyPressed
		// TODO add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			btnBack.doClick();
		}
	}// GEN-LAST:event_jPanel4KeyPressed

	private void formWindowOpened(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowOpened
		// TODO add your handling code here:

	}// GEN-LAST:event_formWindowOpened

	private void jPanel4KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jPanel4KeyReleased
		// TODO add your handling code here:
	}// GEN-LAST:event_jPanel4KeyReleased

	private void tbxPassActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tbxPassActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_tbxPassActionPerformed

	private void jLabel6KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jLabel6KeyReleased
		// TODO add your handling code here:
	}// GEN-LAST:event_jLabel6KeyReleased

	private void cbxLoaiTKKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_cbxLoaiTKKeyPressed
		// TODO add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
			cbxLoaiTK.showPopup();
		}

	}// GEN-LAST:event_cbxLoaiTKKeyPressed

	private void cbxEmployeesKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_cbxEmployeesKeyPressed
		// TODO add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
			cbxEmployees.showPopup();
		}

	}// GEN-LAST:event_cbxEmployeesKeyPressed

	private void tbxUserKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_tbxUserKeyPressed
		// TODO add your handling code here:

	}// GEN-LAST:event_tbxUserKeyPressed

	private void tbxPassKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_tbxPassKeyPressed
		// TODO add your handling code here:

	}// GEN-LAST:event_tbxPassKeyPressed

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
			java.util.logging.Logger.getLogger(Accounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Accounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Accounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Accounts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				TaiKhoan account = new TaiKhoan();
				new Accounts(account).setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbxEmployees;
    private javax.swing.JComboBox<String> cbxLoaiTK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblAccount;
    private javax.swing.JTextField tbxPass;
    private javax.swing.JTextField tbxUser;
    private javax.swing.JTextField txbIDAccount;
    // End of variables declaration//GEN-END:variables
}
