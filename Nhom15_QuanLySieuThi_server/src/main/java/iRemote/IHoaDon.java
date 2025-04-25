package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import entity.HoaDon;


public interface IHoaDon extends Remote{
	public List<HoaDon> getAllHD() throws RemoteException;
	public List<HoaDon> findHD(String sql) throws RemoteException;
	public boolean addHoaDon(HoaDon hoaDon) throws RemoteException;
	public boolean updateHoaDon(HoaDon hoaDon) throws RemoteException;
	boolean deleteSanPham(String maHoaDon) throws RemoteException;
	boolean deleteHoaDon(String maHoaDon) throws RemoteException;
	HoaDon getHD(String maHD) throws RemoteException;
}
