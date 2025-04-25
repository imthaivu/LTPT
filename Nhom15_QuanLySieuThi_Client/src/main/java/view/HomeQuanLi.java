
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import entity.TaiKhoan;
import iRemote.ITaiKhoan;
import view.util.FrameUtil;
import view.util.RMIUrl;
//Frame homeQuanLi
public class HomeQuanLi extends javax.swing.JFrame implements Runnable{

    private Thread thread;
    public Process processed;
	private ITaiKhoan taiKhoanDao;
	private FrameUtil frameUtil = new FrameUtil();
	private TaiKhoan account;
	private String rmiUrl = new RMIUrl().RMIUrl();
    //private TaiKhoan account;
    public HomeQuanLi(TaiKhoan tk) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        lblSoftwareName.setForeground(Color.GREEN);
        lblRun.setForeground(Color.GREEN);
        try {
			taiKhoanDao = (ITaiKhoan) Naming.lookup("rmi://"+ rmiUrl +":2910/iTaiKhoan");
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
        //processed = new Controller.Process(); // Gọi đến đối tượng Process liên kết với csdl
        lblNhanVien.setText("(" + tk.getNhanVien().getMaNV() + ") " + tk.getNhanVien().getHoTen());
        Start();
    }

    
    // Lấy thời gian
    private void Start(){
        if(thread==null){
            thread= new Thread(this);
            thread.start();
        }
    }
    
    // Giao diện thông tin shop chạy
    
    private void Update(){
        lblRun.setForeground(Color.GREEN);
        lblRun.setLocation(lblRun.getX()-1, lblRun.getY());
        if(lblRun.getX()+lblRun.getWidth()<0){
            lblRun.setLocation(this.getWidth(), lblRun.getY());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel(){
            ImageIcon icon = new ImageIcon(getClass().getResource("/IconImage/Background2.png"));
            public void paintComponent(Graphics g){

                Dimension d = getSize();
                g.drawImage(icon.getImage(), 0, 0, d.width, d.height, this);
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        lblSoftwareName = new javax.swing.JLabel();
        updateEmpoyeesBtn = new javax.swing.JButton();
        updateAccountBtn = new javax.swing.JButton();
        revenuesBtn = new javax.swing.JButton();
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

        lblSoftwareName.setFont(new java.awt.Font("Times New Roman", 0, 46)); // NOI18N
        lblSoftwareName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoftwareName.setText("Phần Mềm Quản Lý Siêu Thị  ACE MART");

        updateEmpoyeesBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        updateEmpoyeesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Account.png"))); // NOI18N
        updateEmpoyeesBtn.setText("Quản Lý Nhân Viên");
        updateEmpoyeesBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        updateEmpoyeesBtn.setPreferredSize(new java.awt.Dimension(134, 127));
        updateEmpoyeesBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        updateEmpoyeesBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        updateEmpoyeesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateEmpoyeesBtnActionPerformed(evt);
            }
        });

        updateAccountBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        updateAccountBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Change PasssWord.png"))); // NOI18N
        updateAccountBtn.setText("Cập Nhật Tài Khoản");
        updateAccountBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        updateAccountBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        updateAccountBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        updateAccountBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAccountBtnActionPerformed(evt);
            }
        });

        revenuesBtn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        revenuesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconImage/Payroll.png"))); // NOI18N
        revenuesBtn.setText("Thống Kê Doanh Thu");
        revenuesBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        revenuesBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        revenuesBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        revenuesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					revenuesBtnActionPerformed(evt);
				} catch (MalformedURLException | RemoteException | NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

        lblRun.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        lblRun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRun.setText("Cửa hàng thời trang IUH: 12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, TPHCM Điện thoại: 0333333333; Email: IUH@gmail.com.vn");

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
                    .addComponent(lblRun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblSoftwareName, javax.swing.GroupLayout.PREFERRED_SIZE, 1296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(updateEmpoyeesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addComponent(updateAccountBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(76, 76, 76)
                                .addComponent(revenuesBtn)
                                .addGap(73, 73, 73)
                                .addComponent(signOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoftwareName)
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(lblNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateEmpoyeesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateAccountBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(revenuesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signOutBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(119, 119, 119)
                .addComponent(lblRun)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       frameUtil.exit(this);
    }//GEN-LAST:event_formWindowClosing

    private void updateEmpoyeesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateEmpoyeesBtnActionPerformed
        EmployeesFrame employees=new EmployeesFrame(account);
        this.setVisible(false);
        employees.setVisible(true);
    }//GEN-LAST:event_updateEmpoyeesBtnActionPerformed

    private void updateAccountBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateAccountBtnActionPerformed
        Accounts accounts = new Accounts(account);
        this.setVisible(false);
        accounts.setVisible(true);
    }//GEN-LAST:event_updateAccountBtnActionPerformed

    private void revenuesBtnActionPerformed(java.awt.event.ActionEvent evt) throws MalformedURLException, RemoteException, NotBoundException {//GEN-FIRST:event_revenuesBtnActionPerformed
        Revenues revenue = new Revenues(account);
        this.setVisible(false);
        revenue.setVisible(true);
    }//GEN-LAST:event_revenuesBtnActionPerformed

    private void signOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signOutBtnActionPerformed
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(  
          "Arial", Font.BOLD, 18))); 
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 16));
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất khỏi tài khoản hay không?", "Thông Báo",2); //thông báo khi bấm nút đăng xuất
        if(Click ==JOptionPane.YES_OPTION){
            Login login = new Login();
            this.setVisible(false);
            login.setVisible(true);
        }
    }//GEN-LAST:event_signOutBtnActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        frameUtil.exit(this);
    }//GEN-LAST:event_exitBtnActionPerformed

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaiKhoan account = new TaiKhoan();
                new HomeQuanLi(account).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblRun;
    private javax.swing.JLabel lblSoftwareName;
    private javax.swing.JButton revenuesBtn;
    private javax.swing.JButton signOutBtn;
    private javax.swing.JButton updateAccountBtn;
    private javax.swing.JButton updateEmpoyeesBtn;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        long FPS=80;
        long period=1000*1000000/FPS;
        long beginTime,sleepTime;
        
        beginTime=System.nanoTime();
        while(true){
            
            Update();
            
            long deltaTime=System.nanoTime()-beginTime;
            sleepTime=period-deltaTime;
            try{
                if(sleepTime>0)
                    Thread.sleep(sleepTime/1000000);
                else    Thread.sleep(period/2000000);
                
            }catch(Exception ex){
                ex.printStackTrace();
            }
            beginTime=System.nanoTime();
        }
    }
}
