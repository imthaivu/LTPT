package view.util;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class FrameUtil {
	public FrameUtil() {
		//TODO Auto-generated constructor stub
	}
	
	   // Phương thức thoát khỏi Frame
    public void exit(JFrame frame) {
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 16)));  // Đổi font thông báo
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 14));
        int lick = JOptionPane.showConfirmDialog(null, "Bạn Có Muốn Thoát Khỏi Chương Trình Hay Không?", "Thông Báo", 2);
        if (lick == JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            if (lick == JOptionPane.CANCEL_OPTION) {
                frame.setVisible(true);
            }
        }
    }
       
}
