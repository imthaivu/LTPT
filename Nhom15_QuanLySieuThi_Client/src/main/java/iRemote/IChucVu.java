package iRemote;

import entity.ChucVu;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IChucVu extends Remote{
	public List<ChucVu> getAllCV() throws RemoteException;
	public ChucVu getCV(String maCV) throws RemoteException;
	public boolean addChucVu(ChucVu chucVu) throws RemoteException;
	public boolean updateChucVu(ChucVu chucVu) throws RemoteException;
	boolean deleteChucVu(ChucVu chucVu) throws RemoteException;
}
