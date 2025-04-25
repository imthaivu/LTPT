package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.KhachHang;
import entity.SanPham;

public interface ISanPham extends Remote{
	public List<SanPham> getAllSanPham() throws RemoteException;
	public SanPham getSP(String maSP) throws RemoteException;
	public List<SanPham> findSP(String sql) throws RemoteException;
	public boolean addSanPham(SanPham sanPham) throws RemoteException;
	public boolean updateSanPham(SanPham sanPham) throws RemoteException;
	boolean deleteSanPham(String maSanPham) throws RemoteException;
}
