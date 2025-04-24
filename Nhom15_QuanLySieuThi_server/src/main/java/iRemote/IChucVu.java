package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.ChucVu;
import entity.KhachHang;

public interface IChucVu extends Remote{
	public List<ChucVu> getAllCV() throws RemoteException;
	public ChucVu getCV(String maCV) throws RemoteException;
	public boolean addChucVu(ChucVu chucVu) throws RemoteException;
	public boolean updateChucVu(ChucVu chucVu) throws RemoteException;
	boolean deleteChucVu(ChucVu chucVu) throws RemoteException;
}
