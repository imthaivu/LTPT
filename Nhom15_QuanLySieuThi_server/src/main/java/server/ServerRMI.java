package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dao.CT_HoaDonDao;
import dao.ChucVuDao;
import dao.HoaDonDao;
import dao.KhachHangDao;
import dao.LoaiSPDao;
import dao.NCCDao;
import dao.NhanVienDao;
import dao.SanPhamDao;
import dao.TaiKhoanDao;
import entity.HoaDon;
import iRemote.ICT_HoaDon;
import iRemote.IChucVu;
import iRemote.IHoaDon;
import iRemote.IKhachHang;
import iRemote.ILoaiSP;
import iRemote.INCC;
import iRemote.INhanVien;
import iRemote.ISanPham;
import iRemote.ITaiKhoan;



public class ServerRMI {
	public ServerRMI(String host) throws RemoteException, NamingException {
		SecurityManager securityManager= System.getSecurityManager();
		if(securityManager==null) {
			System.setProperty("java.security.policy", "myrmi/myrmi.policy");
			System.setSecurityManager(new SecurityManager());
		}

		IChucVu iChucVu = new ChucVuDao();
		ICT_HoaDon iCTHoaDon = new CT_HoaDonDao();
		IHoaDon iHoaDon = new HoaDonDao();
		IKhachHang iKhachHang = new KhachHangDao();
		ILoaiSP iLoaiSP = new LoaiSPDao();
		INCC iNCC = new NCCDao();
		INhanVien iNhanVien = new NhanVienDao();
		ISanPham iSanPham = new SanPhamDao();
		ITaiKhoan iTaiKhoan = new TaiKhoanDao();
		
		LocateRegistry.createRegistry(2910);
		Context context=new  InitialContext();
		context.bind("rmi://"+host+":2910/iChucVu", iChucVu);
		context.bind("rmi://"+host+":2910/iCTHoaDon", iCTHoaDon);
		context.bind("rmi://"+host+":2910/iHoaDon", iHoaDon);
		context.bind("rmi://"+host+":2910/iKhachHang", iKhachHang);
		context.bind("rmi://"+host+":2910/iLoaiSP", iLoaiSP);
		context.bind("rmi://"+host+":2910/iNCC",iNCC );
		context.bind("rmi://"+host+":2910/iNhanVien",iNhanVien );
		context.bind("rmi://"+host+":2910/iSanPham",iSanPham );
		context.bind("rmi://"+host+":2910/iTaiKhoan",iTaiKhoan );
		System.out.println(host);
		System.out.println("ready");
	}
	public static void main(String[] args) throws RemoteException, NamingException, MalformedURLException, NotBoundException {
		new ServerRMI("localhost");
//		shotDown("192.168.1.3");

//		shotDown();
	}
	public static void shotDown(String host) throws RemoteException, MalformedURLException, NotBoundException {
		Naming.unbind("rmi://"+host+":2910/iChucVu");
		Naming.unbind("rmi://"+host+":2910/iCTHoaDon");
		Naming.unbind("rmi://"+host+":2910/iHoaDon");
		Naming.unbind("rmi://"+host+":2910/iKhachHang");
		Naming.unbind("rmi://"+host+":2910/iLoaiSP");
		Naming.unbind("rmi://"+host+":2910/iNCC" );
		Naming.unbind("rmi://"+host+":2910/iNhanVien");
		Naming.unbind("rmi://"+host+":2910/iSanPham" );
		Naming.unbind("rmi://"+host+":2910/iTaiKhoan" );
//		UnicastRemoteObject.unexportObject(true);
		System.exit(1);
	}
}
