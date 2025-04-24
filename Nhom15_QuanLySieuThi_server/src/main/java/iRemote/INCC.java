package iRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entity.NCC;

public interface INCC extends Remote{
	public List<NCC> getAllNCC() throws RemoteException;
	public NCC getNCC(String maNCC) throws RemoteException;
	public boolean addNCC(NCC ncc) throws RemoteException;
	public boolean updateNCC(NCC ncc) throws RemoteException;
	boolean deleteNCC(String maNcc) throws RemoteException;
}
