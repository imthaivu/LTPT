package iRemote;

import entity.CT_HoaDon;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ICT_HoaDon extends Remote{
	public List<CT_HoaDon> getAllCTHoaDon() throws RemoteException;
	public CT_HoaDon getCTHoaDon(String maCTHD) throws RemoteException;
	public List<CT_HoaDon> findCTHD(String sql) throws RemoteException;
	
	public boolean addCTHD(CT_HoaDon ctHD) throws RemoteException;
	public boolean updateCTHD(CT_HoaDon ctHD) throws RemoteException;
	boolean deleteCTHD(String maCTHD) throws RemoteException;
	public void deleteSPHoaDon(String maHD, String maSP) throws RemoteException;
	boolean checkSPHoaDon(String maHD, String maSP) throws RemoteException;
}
