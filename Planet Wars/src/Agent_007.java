import sml.*;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class Agent_007  {
	private static Kernel kernel;
	private static Agent agent;
	
	public Agent_007()
	{

		kernel = Kernel.CreateKernelInNewThread();
		if (kernel.HadError())
			throw new IllegalStateException("Error initializing kernel: " + kernel.GetLastErrorDescription()) ;
		
		String version = kernel.GetSoarKernelVersion() ;
		System.out.println("Soar version " + version) ;
		
		agent = kernel.CreateAgent("mario") ;
		if (kernel.HadError())
			throw new IllegalStateException("Error creating agent: " + kernel.GetLastErrorDescription()) ;
		

		//boolean load = agent.LoadProductions(productions);
		//if (!load || agent.HadError())
		//	throw new IllegalStateException("Error loading productions: " + agent.GetLastErrorDescription()) ;
		
		kernel.SetAutoCommit(false);
		
		agent.Commit();
	}
	public static void main(String[] args)
	{
		new Agent_007();
	}

}
