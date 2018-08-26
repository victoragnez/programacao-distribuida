import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Comparator;

import javafx.util.Pair;

@SuppressWarnings("serial")
public class Server extends UnicastRemoteObject implements Compute {
	protected Server() throws RemoteException { 
		super(); 
	}
	
	public Pair<Integer[], Long> sortArray(Integer sortType, Integer[] inputArray, Comparator<Integer> Comp) throws RemoteException {
		System.out.println("Executando a tarefa no servidor...");
		Sort ordena;
		switch(sortType) {
		case 0:
			ordena = new SelectionSort(inputArray, Comp);
			break;
		case 1:
			ordena = new InsertionSort(inputArray, Comp);
			break;
		case 2:
			ordena = new MergeSort(inputArray, Comp);
			break;
		default:
			throw new RuntimeException("Invalid sortType");
		}
		Long startTime = System.nanoTime();
		Integer[] array = ordena.sort();
		return new Pair<Integer[], Long>(array, System.nanoTime()-startTime);
	}
	
	public Pair<Integer[], Long> sortArray(Integer sortType, Integer[] inputArray) throws RemoteException {
		return sortArray(sortType, inputArray, Comparator.<Integer>naturalOrder());
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(12346);
		Server compute = new Server();
		
		Naming.rebind("rmi://localhost:12346/compute", compute);
		System.out.println("Servidor registrado no RMI Registry.");
	}
}
