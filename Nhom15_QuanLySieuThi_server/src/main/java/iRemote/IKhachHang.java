package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.KhachHang;

public interface IKhachHang extends Remote{
	public List<KhachHang> getAllKH() throws RemoteException;
	public List<KhachHang> findKH(String sql) throws RemoteException;
	public KhachHang getKhachHang(String maKH) throws RemoteException;
	public boolean addCustomer(KhachHang customer) throws RemoteException;
	public boolean updateCustomer(KhachHang customer) throws RemoteException;
}
