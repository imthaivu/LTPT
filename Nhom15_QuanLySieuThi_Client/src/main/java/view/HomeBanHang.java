
package view;

import entity.TaiKhoan;
import iRemote.ITaiKhoan;
import view.util.FrameUtil;
import view.util.RMIUrl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

//Frame home của nhân viên bán hàng
public class HomeBanHang extends javax.swing.JFrame implements Runnable {

    private Thread thread;
    private ITaiKhoan taiKhoanDao;
    private TaiKhoan account = new TaiKhoan();
    private FrameUtil frameUtil = new FrameUtil();
    //private TaiKhoan account;
    private String rmiUrl = new RMIUrl().RMIUrl();


    public HomeBanHang(TaiKhoan tk) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        lblSoftwareName.setForeground(Color.GREEN);
        lblRun.setForeground(Color.GREEN);
        try {
            taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://" + rmiUrl + ":3030/iTaiKhoan");
            account = tk;
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
        lblNhanVien.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
        Start();
    }

    //Cung cấp dịch vụ 
    private void Start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    // Giao diện thông tin shop chạy
    private void Update() {
        lblRun.setForeground(Color.GREEN);
        lblRun.setLocation(lblRun.getX() - 1, lblRun.getY());
        if (lblRun.getX() + lblRun.getWidth() < 0) {
            lblRun.setLocation(this.getWidth(), lblRun.getY());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel() {
            ImageIcon icon = new ImageIcon(getClass().getResource("/IconImage/Background2.png"));

            public void paintComponent(Graphics g) {

                Dimension d = getSize();
                g.drawImage(icon.getImage(), 0, 0, d.width, d.height, this);
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        lblSoftwareName = new javax.swing.JLabel();
        banHangBtn = new javax.swing.JButton();
        productBtn = new javax.swing.JButton();
        customerBtn = new javax.swing.JButton();
        signOutBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();
        lblRun = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Giao Diện Hệ Thống");
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

        lblSoftwareName.setFont(new java.awt.Font("Times New Roman", 0, 45)); // NOI18N
        lblSoftwareName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoftwareName.setText("Phần Mềm Quản Lý Siêu Thị IUH MART");

        banHangBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        banHangBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Sale.png"))); // NOI18N
        banHangBtn.setText("Bán Hàng");
        banHangBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        banHangBtn.setPreferredSize(new java.awt.Dimension(134, 127));
        banHangBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        banHangBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        banHangBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                banHangBtnActionPerformed(evt);
            }
        });
        banHangBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                banHangBtnKeyPressed(evt);
            }
        });

        productBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        productBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Product.png"))); // NOI18N
        productBtn.setText("Quản Lý Sản Phẩm");
        productBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        productBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        productBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        productBtn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                productBtnFocusGained(evt);
            }
        });
        productBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productBtnActionPerformed(evt);
            }
        });
        productBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                productBtnKeyPressed(evt);
            }
        });

        customerBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        customerBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/customerIcon.png"))); // NOI18N
        customerBtn.setText("Cập Nhật Khách Hàng");
        customerBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        customerBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        customerBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        customerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerBtnActionPerformed(evt);
            }
        });
        customerBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customerBtnKeyPressed(evt);
            }
        });

        signOutBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        signOutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/LogOut.png"))); // NOI18N
        signOutBtn.setText("Đăng Xuất");
        signOutBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        signOutBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        signOutBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        signOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signOutBtnActionPerformed(evt);
            }
        });
        signOutBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                signOutBtnKeyPressed(evt);
            }
        });

        exitBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Exit.png"))); // NOI18N
        exitBtn.setText("Thoát");
        exitBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        exitBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });
        exitBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                exitBtnKeyPressed(evt);
            }
        });

        lblRun.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblRun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRun.setText("Siêu Thị Mini IUH: 12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, TPHCM Điện thoại: 0333333333; Email: IUH@gmail.com.vn");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 255, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/User.png"))); // NOI18N
        jLabel1.setText("Nhân Viên: ");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(51, 255, 51));
        lblNhanVien.setText("(QL01) vuthai2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblRun, javax.swing.GroupLayout.DEFAULT_SIZE, 1343, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblSoftwareName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(30, 30, 30))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(52, 52, 52)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(banHangBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(85, 85, 85)
                                                                .addComponent(productBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(lblNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addGap(86, 86, 86)
                                                .addComponent(customerBtn)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(signOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(83, 83, 83)
                                                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(44, 44, 44))))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblSoftwareName)
                                .addGap(46, 46, 46)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                                        .addComponent(lblNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(54, 54, 54)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(productBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(banHangBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(customerBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(signOutBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(exitBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(103, 103, 103)
                                .addComponent(lblRun)
                                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        frameUtil.exit(this);
    }

    private void banHangBtnActionPerformed(java.awt.event.ActionEvent evt) {
        OrderForms sale = new OrderForms(account);
        this.setVisible(false);
        sale.setVisible(true);
    }

    private void productBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Products product = new Products(account);
        this.setVisible(false);
        product.setVisible(true);
    }

    private void customerBtnActionPerformed(java.awt.event.ActionEvent evt) {
        CustomerFrame customerFrame = new CustomerFrame(account);
        this.setVisible(false);
        customerFrame.setVisible(true);
    }

    private void signOutBtnActionPerformed(java.awt.event.ActionEvent evt) {
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
                "Arial", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất khỏi tài khoản hay không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            Login login = new Login();
            this.setVisible(false);
            login.setVisible(true);
        }
    }

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {
        frameUtil.exit(this);
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
    }

    private void signOutBtnKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            signOutBtn.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            signOutBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            productBtn.nextFocus();
        }


    }

    private void banHangBtnKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            banHangBtn.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            banHangBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            signOutBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            signOutBtn.doClick();
        }
    }

    private void productBtnKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            productBtn.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            productBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            exitBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            signOutBtn.doClick();
        }
    }

    private void customerBtnKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            customerBtn.doClick();
        }

        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            customerBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            banHangBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            signOutBtn.doClick();
        }
    }

    private void exitBtnKeyPressed(java.awt.event.KeyEvent evt) {        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            exitBtn.doClick();

        }

        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            exitBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            customerBtn.nextFocus();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            signOutBtn.doClick();
        }
    }

    private void productBtnFocusGained(java.awt.event.FocusEvent evt) {        // TODO add your handling code here:
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
            java.util.logging.Logger.getLogger(HomeBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaiKhoan account = new TaiKhoan();
                new HomeBanHang(account).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton banHangBtn;
    private javax.swing.JButton customerBtn;
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblRun;
    private javax.swing.JLabel lblSoftwareName;
    private javax.swing.JButton productBtn;
    private javax.swing.JButton signOutBtn;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        long FPS = 80;
        long period = 1000 * 1000000 / FPS;
        long beginTime, sleepTime;

        beginTime = System.nanoTime();
        while (true) {

            Update();

            long deltaTime = System.nanoTime() - beginTime;
            sleepTime = period - deltaTime;
            try {
                if (sleepTime > 0)
                    Thread.sleep(sleepTime / 1000000);
                else Thread.sleep(period / 2000000);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            beginTime = System.nanoTime();
        }
    }
}
