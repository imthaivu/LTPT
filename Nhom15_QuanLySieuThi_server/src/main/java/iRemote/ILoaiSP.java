package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.LoaiSP;

public interface ILoaiSP extends Remote{
	public List<LoaiSP> getAllLoaiSP() throws RemoteException;
	public LoaiSP getLoaiSP(String maLoaiSP) throws RemoteException;
	public boolean addLoaiSP(LoaiSP loaiSP) throws RemoteException;
	public boolean updateLoaiSP(LoaiSP loaiSP) throws RemoteException;
	boolean deleteLoaiSP(String maLoaiSP) throws RemoteException;
}
