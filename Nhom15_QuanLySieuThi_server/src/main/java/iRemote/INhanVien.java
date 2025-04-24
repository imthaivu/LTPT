package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.NhanVien;

public interface INhanVien extends Remote{
	public List<NhanVien> getAllNV() throws RemoteException;
	public List<NhanVien> getNVByChucVu(String maCV) throws RemoteException;
	public List<NhanVien> getAllNVNghi() throws RemoteException;

	NhanVien getNV(String maNV) throws RemoteException;

	public boolean addNhanVien(NhanVien nhanVien) throws RemoteException;
	public boolean updateNhanVien(NhanVien nhanVien) throws RemoteException;
}
