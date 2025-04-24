package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.HoaDon;


public interface IHoaDon extends Remote{
	public boolean deleteSanPham(String maHoaDon) throws RemoteException;
	public List<HoaDon> getAllHD() throws RemoteException;
	public HoaDon getHD(String maHD) throws RemoteException;
	public boolean addHoaDon(HoaDon hoaDon) throws RemoteException;
	public boolean updateHoaDon(HoaDon hoaDon) throws RemoteException;
	public boolean deleteHoaDon(String maHoaDon) throws RemoteException;
	public List<HoaDon> findHDByDateRange(Date startDate, Date endDate) throws RemoteException;
}
