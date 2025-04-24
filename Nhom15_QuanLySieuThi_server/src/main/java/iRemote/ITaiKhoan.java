package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.TaiKhoan;

public interface ITaiKhoan extends Remote{
	public TaiKhoan getTaiKhoan(String taiKhoan) throws RemoteException;
	public List<TaiKhoan> getAllTaiKhoan() throws RemoteException;
	public TaiKhoan getTaiKhoanByUserNamePassword(String taiKhoan, String matKhau) throws RemoteException;
	public void changePassWord(String userName, String oldPassWord, String newPassword) throws RemoteException;
	public boolean themTaiKhoan(TaiKhoan taiKhoan) throws RemoteException;
	public boolean suaTaiKhoan(TaiKhoan taiKhoan) throws RemoteException;
	public boolean xoaTaiKhoan(String tenTaiKhoan) throws RemoteException;


	public boolean addTaiKhoan(TaiKhoan taiKhoan) throws RemoteException;

	public boolean updateTaiKhoan(TaiKhoan taiKhoan) throws RemoteException;

	public boolean deleteTaiKhoan(String maTK) throws RemoteException;
}
