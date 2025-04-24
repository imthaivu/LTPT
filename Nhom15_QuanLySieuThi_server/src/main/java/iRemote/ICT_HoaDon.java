package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.CT_HoaDon;
import entity.SanPham;

public interface ICT_HoaDon extends Remote{
	public List<CT_HoaDon> getAllCTHoaDon() throws RemoteException;
	public CT_HoaDon getCTHoaDon(String maHD) throws RemoteException;
	public List<CT_HoaDon> findCTHDByMaHD(String maHD) throws RemoteException;
	public boolean addCTHD(CT_HoaDon ctHD) throws RemoteException;
	public boolean updateCTHD(CT_HoaDon ctHD) throws RemoteException;
	public boolean deleteCTHD(String maCTHD) throws RemoteException;
	public void deleteSPHoaDon(String maHD, String maSP) throws RemoteException;
	public boolean checkSPHoaDon(String maHD, String maSP) throws RemoteException;
	public List<CT_HoaDon> getCTHDByMaHD(String maHD) throws RemoteException;
	public List<CT_HoaDon> getCTHDForReport(String maHD) throws RemoteException;
}
