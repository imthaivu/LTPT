/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import entity.KhachHang;
import entity.TaiKhoan;
import iRemote.IKhachHang;
import iRemote.ITaiKhoan;
import view.util.FrameUtil;
import view.util.RMIUrl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

// Frame khách hàng
public class CustomerFrame extends javax.swing.JFrame {
    private TaiKhoan account;
    private boolean AddKH = false, changeKH = false;
    private ITaiKhoan taiKhoanDao;
    private IKhachHang khachHangDao;
    private FrameUtil frameUtil = new FrameUtil();
    private String rmiUrl = new RMIUrl().RMIUrl();

    public CustomerFrame(TaiKhoan tk) {
        initComponents();
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        lblStatus.setForeground(Color.red);
        lblStatus.setForeground(Color.red);

        try {
            taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://" + rmiUrl + ":3030/iTaiKhoan");
            khachHangDao = (IKhachHang) Naming.lookup("rmi://" + rmiUrl + ":3030/iKhachHang");
            account = tk;
            lblNhanVien.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
            loadOrderCustomerTable();
            DisabledCustomer(); // Tắt các input trong khách hàng
            tblCustomerSearch.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Gọi đến đối tượng Process kết nối đến csdl


    }

    //Kiểm tra nhập liệu khách hàng
    private boolean checkNullCustomer() {
        boolean kq = true;
        if (String.valueOf(this.txbIDCustomer.getText()).length() == 0) {
            lblStatus.setText("Bạn chưa ID cho khách hàng!");
            return false;
        }
        if (String.valueOf(this.txbNameCustomer.getText()).length() == 0) {
            lblStatus.setText("Bạn chưa nhập tên khách hàng!");
            return false;
        }
        if (String.valueOf(this.txbAddressCustomer.getText()).length() == 0) {
            lblStatus.setText("Bạn chưa nhập địa chỉ của khách hàng!");
            return false;
        }
        if (String.valueOf(this.txbPhoneCustomer.getText()).length() == 0) {
            lblStatus.setText("Bạn chưa nhập số điện thoại của khách hàng!");
            return false;
        }
        return kq;
    }

    //Bật các ô input nhập liệu khách hàng
    private void EnabledCustomer() {
        txbIDCustomer.setEnabled(true);
        txbNameCustomer.setEnabled(true);
        txbAddressCustomer.setEnabled(true);
        txbPhoneCustomer.setEnabled(true);
    }

    // Tắt các ô input nhập liệu khách hàng
    private void DisabledCustomer() {
        txbIDCustomer.setEnabled(false);
        txbNameCustomer.setEnabled(false);
        txbAddressCustomer.setEnabled(false);
        txbPhoneCustomer.setEnabled(false);
    }

    //Refresh lại các bảng và ô nhập liệu
    private void refreshCustomer() throws RemoteException {
        AddKH = false;
        changeKH = false;
        txbIDCustomer.setEnabled(false);
        txbNameCustomer.setText("");
        txbAddressCustomer.setText("");
        txbPhoneCustomer.setText("");
        btnThemKH.setEnabled(true);
        btnSuaKH.setEnabled(false);
        btnSaveCustomer.setEnabled(false);
        DisabledCustomer();
        loadOrderCustomerTable();
    }

    //Tự động phát sinh mã khi thêm khách hàng
    private void newCustomer() throws RemoteException {
        int numberCustomer = khachHangDao.getAllKH().size();
        if (numberCustomer < 9) {
            txbIDCustomer.setText("KH" + "0000" + String.valueOf(numberCustomer + 1));
        } else {
            txbIDCustomer.setText("KH" + "000" + String.valueOf(numberCustomer + 1));
        }
    }

    //Load thông tin khách hàng ra bảng    
    private void loadOrderCustomerTable() throws RemoteException {
        tblCustomer.removeAll();
        tblCustomer.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        String[] arr = {"Mã Khách Hàng", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        ArrayList<KhachHang> arry = (ArrayList<KhachHang>) khachHangDao.getAllKH();
        for (int i = 0; i < arry.size(); i++) {
            Vector vector = new Vector();
            vector.add(((KhachHang) arry.get(i)).getMaKH());
            vector.add(((KhachHang) arry.get(i)).getTenKH());
            vector.add(((KhachHang) arry.get(i)).getDiaChi());
            vector.add(((KhachHang) arry.get(i)).getSoDT());
            modle.addRow(vector);
        }
        tblCustomer.setModel(modle);
    }

    //Load thông tin khách hàng cần tìm ra bảng
    private void findCustomerTable(List<KhachHang> arry) {
        tblCustomerSearch.removeAll();
        tblCustomerSearch.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        String[] arr = {"Mã Khách Hàng", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại"};
        DefaultTableModel modle = new DefaultTableModel(arr, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        for (int i = 0; i < arry.size(); i++) {
            Vector vector = new Vector();
            vector.add(((KhachHang) arry.get(i)).getMaKH());
            vector.add(((KhachHang) arry.get(i)).getTenKH());
            vector.add(((KhachHang) arry.get(i)).getDiaChi());
            vector.add(((KhachHang) arry.get(i)).getSoDT());
            modle.addRow(vector);
        }
        tblCustomerSearch.setModel(modle);
    }

    //Hàm tìm kiếm khách hàng
    private void FindCustomer(String sql) throws RemoteException {
        List<KhachHang> arry = khachHangDao.findKH(sql);
        findCustomerTable(arry);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel15 = new javax.swing.JPanel();
        btnBackHome2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCustomer = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txbAddressCustomer = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        txbPhoneCustomer = new javax.swing.JTextField();
        txbNameCustomer = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txbIDCustomer = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        btnRefresh3 = new javax.swing.JButton();
        btnSaveCustomer = new javax.swing.JButton();
        btnThemKH = new javax.swing.JButton();
        btnSuaKH = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        btnBackHome4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        btnBackHome3 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCustomerSearch = new javax.swing.JTable();
        tbxSearch = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        btnRefresh4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        lblNhanVien1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        maKHrdo = new javax.swing.JRadioButton();
        tenKHrdo = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        lblStatusSearchCustomer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1550, 1100));
        setPreferredSize(new java.awt.Dimension(1550, 1100));
        setSize(new java.awt.Dimension(1080, 1080));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        btnBackHome2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Cập Nhật Khách Hàng");

        tblCustomer.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        tblCustomer.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{

                }
        ));
        tblCustomer.setRowHeight(26);
        tblCustomer.setRowMargin(2);
        tblCustomer.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblCustomer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomerMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblCustomer);

        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel48.setText("Tên Khách Hàng:");

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel49.setText("Địa Chỉ:");

        txbAddressCustomer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel50.setText("Số Điện Thoại:");

        txbPhoneCustomer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txbPhoneCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPhoneCustomerKeyReleased(evt);
            }
        });

        txbNameCustomer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel51.setText("Mã Khách Hàng:");

        txbIDCustomer.setEditable(false);
        txbIDCustomer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbIDCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 286, Short.MAX_VALUE)
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txbNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(jLabel49)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbAddressCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77)
                                .addComponent(jLabel50)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txbPhoneCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel51)
                                        .addComponent(txbIDCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel48)
                                        .addComponent(txbNameCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel49)
                                        .addComponent(txbAddressCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel50)
                                        .addComponent(txbPhoneCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(38, Short.MAX_VALUE))
        );

        btnRefresh3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefresh3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefresh3MouseClicked(evt);
            }
        });
        btnRefresh3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnRefresh3ActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnSaveCustomer.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSaveCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Save.png"))); // NOI18N
        btnSaveCustomer.setText("Lưu");
        btnSaveCustomer.setEnabled(false);
        btnSaveCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnSaveCustomerActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnThemKH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnThemKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Add.png"))); // NOI18N
        btnThemKH.setText("Khách Hàng");
        btnThemKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnThemKHActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnSuaKH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btnSuaKH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change.png"))); // NOI18N
        btnSuaKH.setText("Khách Hàng");
        btnSuaKH.setEnabled(false);
        btnSuaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnRefresh3, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(175, 175, 175)
                                .addComponent(btnThemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSuaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(349, 349, 349)
                                .addComponent(btnSaveCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(176, 176, 176))
        );
        jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnThemKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSaveCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSuaKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(8, Short.MAX_VALUE))
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(btnRefresh3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Trạng Thái");

        btnBackHome4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Next-icon.png"))); // NOI18N
        btnBackHome4.setText("Bán Hàng");
        btnBackHome4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackHome4MouseClicked(evt);
            }
        });
        btnBackHome4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHome4ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 51, 51));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel3.setText("Nhân Viên: ");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien.setText("(QL01) vuthai2");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(btnBackHome2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 1115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(312, 312, 312)
                                                .addComponent(btnBackHome4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel15Layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel15Layout.createSequentialGroup()
                                                        .addGap(14, 14, 14)
                                                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 1536, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jScrollPane4)
                                                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBackHome2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBackHome4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblNhanVien))
                                .addGap(15, 15, 15)
                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblStatus)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(209, 209, 209))
        );

        jTabbedPane1.addTab("Cập Nhật Khách Hàng", jPanel15);

        btnBackHome3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Button-Previous-icon.png"))); // NOI18N
        btnBackHome3.setText("Hệ Thống");
        btnBackHome3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackHome3MouseClicked(evt);
            }
        });
        btnBackHome3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackHome3ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Tìm Kiếm Khách Hàng");

        tblCustomerSearch.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        tblCustomerSearch.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Mã Khách Hàng", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblCustomerSearch.setRowHeight(26);
        tblCustomerSearch.setRowMargin(2);
        tblCustomerSearch.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblCustomerSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomerSearchMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblCustomerSearch);

        tbxSearch.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tbxSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbxSearchActionPerformed(evt);
            }
        });
        tbxSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbxSearchKeyPressed(evt);
            }
        });

        searchBtn.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        searchBtn.setText("Tìm Kiếm");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    searchBtnActionPerformed(evt);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        btnRefresh4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Refresh-icon.png"))); // NOI18N
        btnRefresh4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefresh4MouseClicked(evt);
            }
        });
        btnRefresh4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefresh4ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel4.setText("Nhân Viên: ");

        lblNhanVien1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien1.setForeground(new java.awt.Color(255, 0, 51));
        lblNhanVien1.setText("(QL01) vuthai2");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Tìm Kiếm Theo: ");

        buttonGroup1.add(maKHrdo);
        maKHrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        maKHrdo.setSelected(true);
        maKHrdo.setText("Mã Khách Hàng");
        maKHrdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maKHrdoActionPerformed(evt);
            }
        });

        buttonGroup1.add(tenKHrdo);
        tenKHrdo.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tenKHrdo.setText("Tên Khách Hàng");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRadioButton3.setText("Số Điện Thoại");

        lblStatusSearchCustomer.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lblStatusSearchCustomer.setForeground(new java.awt.Color(255, 0, 0));
        lblStatusSearchCustomer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatusSearchCustomer.setText("Thông Báo");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane5)
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addComponent(btnBackHome3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addComponent(btnRefresh4, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(29, 29, 29)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(maKHrdo)
                                                .addGap(18, 18, 18)
                                                .addComponent(tenKHrdo)
                                                .addGap(18, 18, 18)
                                                .addComponent(jRadioButton3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                                .addComponent(lblStatusSearchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                                .addComponent(tbxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                                                .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnBackHome3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblNhanVien1))
                                                .addGap(24, 24, 24)
                                                .addComponent(btnRefresh4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(67, 67, 67))
                                        .addGroup(jPanel16Layout.createSequentialGroup()
                                                .addGap(103, 103, 103)
                                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(maKHrdo)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tenKHrdo)
                                                        .addComponent(jRadioButton3)
                                                        .addComponent(tbxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(searchBtn))
                                                .addGap(22, 22, 22)
                                                .addComponent(lblStatusSearchCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(12, 12, 12)))
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 699, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );

        jTabbedPane1.addTab("Tìm Kiếm Khách Hàng", jPanel16);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {        // TODO add your handling code here:
        frameUtil.exit(this);
    }

    private void btnSuaKHActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        AddKH = false;
        changeKH = true;
        EnabledCustomer();
        txbNameCustomer.requestFocus();
        lblStatus.setText("Trạng Thái!");
        btnThemKH.setEnabled(false);
        btnSuaKH.setEnabled(false);
        btnSaveCustomer.setEnabled(true);
    }

    private void btnThemKHActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {        // TODO add your handling code here:
        refreshCustomer();
        AddKH = true;
        newCustomer();
        txbNameCustomer.requestFocus();
        EnabledCustomer();
        btnThemKH.setEnabled(false);
        btnSaveCustomer.setEnabled(true);
    }

    private void btnSaveCustomerActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {        // TODO add your handling code here:
        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$"; //regex sdt
        if (checkNullCustomer()) { // Kiểm tra nhập liệu
            if (AddKH == true) { // Nếu thêm khách hàng

                if (khachHangDao.getKhachHang(txbIDCustomer.getText()) == null) { //Kiểm tra mã khách hàng
                    if (txbPhoneCustomer.getText().matches(reg)) { // Kiểm tra số điện thoại đã phù hợp chưa
                        KhachHang khachHang = new KhachHang(txbIDCustomer.getText(), txbNameCustomer.getText(), txbAddressCustomer.getText(), txbPhoneCustomer.getText());
                        khachHangDao.addCustomer(khachHang); // Thêm khách hàng
                        refreshCustomer();
                        lblStatus.setText("Thêm khách hàng thành công!");
                    } else {
                        lblStatus.setText("Số điện thoại định dạng không đúng!");
                    }

                } else {
                    lblStatus.setText("Mã khách hàng đã tồn tại!");
                }
            } else if (changeKH == true) { //Nếu là sửa khách hàng
                int Click = tblCustomer.getSelectedRow();
                TableModel Entity = tblCustomer.getModel();

                if (Entity.getValueAt(Click, 0).toString().equals(txbIDCustomer.getText()) && txbPhoneCustomer.getText().matches(reg)) { // kiểm tra mã khách hàng và số điện thoại nhập
                    KhachHang khachHang = new KhachHang(txbIDCustomer.getText(), txbNameCustomer.getText(), txbAddressCustomer.getText(), txbPhoneCustomer.getText());
                    khachHangDao.updateCustomer(khachHang); // Sửa khách hàng
                    DisabledCustomer();
                    refreshCustomer();
                    loadOrderCustomerTable();// load lại thông tin khách hàng
                    lblStatus.setText("Lưu thay đổi thành công!");
                } else if (khachHangDao.getKhachHang(txbIDCustomer.getText()) == null && txbPhoneCustomer.getText().matches(reg)) {
                    //processed.changedProducer(txbIDCustomer.getText(), txbNameCustomer.getText(), txbAddressCustomer.getText(), txbPhoneCustomer.getText(), Entity.getValueAt(Click, 0).toString());
                    DisabledCustomer();
                    refreshCustomer();
                    lblStatus.setText("Lưu thay đổi thành công!");
                } else if (txbPhoneCustomer.getText().matches(reg) == false) {
                    lblStatus.setText("Định dạng số điện thoại không đúng!");
                } else {
                    lblStatus.setText("Mã nhà cung cấp bạn sửa đổi đã tồn tại!!");
                }

            }
        }
    }

    private void btnDeleteProducerActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnRefresh3ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {        // TODO add your handling code here:
        refreshCustomer();
    }

    private void btnRefresh3MouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void txbPhoneCustomerKeyReleased(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
    }

    private void tblCustomerMouseClicked(java.awt.event.MouseEvent evt) {
        int Click = tblCustomer.getSelectedRow();
        TableModel Entity = tblCustomer.getModel();

        txbIDCustomer.setText(Entity.getValueAt(Click, 0).toString());
        txbNameCustomer.setText(Entity.getValueAt(Click, 1).toString());
        txbAddressCustomer.setText(Entity.getValueAt(Click, 2).toString());
        txbPhoneCustomer.setText(Entity.getValueAt(Click, 3).toString());
        btnSuaKH.setEnabled(true);
    }

    private void btnBackHome2ActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:)){
        HomeBanHang home = new HomeBanHang(account);
        this.setVisible(false);
        home.setVisible(true);

    }

    private void btnBackHome2MouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void btnBackHome3MouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void btnBackHome3ActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        HomeBanHang home = new HomeBanHang(account);
        this.setVisible(false);
        home.setVisible(true);
    }

    private void tblCustomerSearchMouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void tbxSearchActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
    }

    private void tbxSearchKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchBtn.doClick();
        }
    }

    private void btnRefresh4MouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void btnRefresh4ActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        tblCustomerSearch.removeAll();

        String[] arr = {"Mã Khách Hàng", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại"};
        DefaultTableModel modle = new DefaultTableModel(arr, 4) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        tblCustomerSearch.setModel(modle);
        tbxSearch.setText("");
    }

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {        // TODO add your handling code here:
        lblStatusSearchCustomer.setText("Thông Báo");
        if (tbxSearch.getText().equals("")) {
            lblStatusSearchCustomer.setText("Vui lòng nhập thông tin cần tìm");
        } else {
            String sql = "";
            if (maKHrdo.isSelected()) {
                sql = "SELECT * FROM KHACHHANG WHERE MaKH like '%" + this.tbxSearch.getText() + "%'";
                FindCustomer(sql);
            } else if (tenKHrdo.isSelected()) {
                sql = "SELECT * FROM KHACHHANG WHERE TenKH like N'%" + this.tbxSearch.getText() + "%'";
                FindCustomer(sql);
            } else {
                sql = "SELECT * FROM KHACHHANG WHERE SDT like '%" + this.tbxSearch.getText() + "%'";
                FindCustomer(sql);
            }
        }

    }

    private void btnBackHome4MouseClicked(java.awt.event.MouseEvent evt) {        // TODO add your handling code here:
    }

    private void btnBackHome4ActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
        OrderForms sale = new OrderForms(account);
        this.setVisible(false);
        sale.setVisible(true);
    }

    private void maKHrdoActionPerformed(java.awt.event.ActionEvent evt) {        // TODO add your handling code here:
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
            java.util.logging.Logger.getLogger(CustomerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaiKhoan account = new TaiKhoan();
                new CustomerFrame(account).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackHome2;
    private javax.swing.JButton btnBackHome3;
    private javax.swing.JButton btnBackHome4;
    private javax.swing.JButton btnRefresh3;
    private javax.swing.JButton btnRefresh4;
    private javax.swing.JButton btnSaveCustomer;
    private javax.swing.JButton btnSuaKH;
    private javax.swing.JButton btnThemKH;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblNhanVien1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusSearchCustomer;
    private javax.swing.JRadioButton maKHrdo;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTable tblCustomer;
    private javax.swing.JTable tblCustomerSearch;
    private javax.swing.JTextField tbxSearch;
    private javax.swing.JRadioButton tenKHrdo;
    private javax.swing.JTextField txbAddressCustomer;
    private javax.swing.JTextField txbIDCustomer;
    private javax.swing.JTextField txbNameCustomer;
    private javax.swing.JTextField txbPhoneCustomer;
    // End of variables declaration//GEN-END:variables
}
