import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import sml.*;
public class Soaragent 
{
 
 	
 	//My own start here
	private static Kernel kernel;
 	private static Agent agent;
 	
 	private static Identifier inputLink;
	private static Identifier planetwarsWME;
 	private static StringElement isInit;

 	
 	private static Identifier planet;
 	private static Identifier fleet;
 	private static IntElement numships_us;
 	private static IntElement numships_opponent;
 	private static IntElement productions_us;
 	private static IntElement productions_opponent;
 	private static IntElement number_Fleets;
 	private static StringElement attack_mode;
 
 	private static PrintWriter csv;
 	//My own ends here
 	
 	

	public static void DoTurn(PlanetWars pw, Agent a) throws FileNotFoundException 
    	{
		int num_ships_us = pw.NumShips(1);
		int num_ships_opponent = pw.NumShips(2);
		int prod_us = pw.Production(1);
		int prod_opponent = pw.Production(2);
		int numFleets = 1;
		//String numF;
		boolean attackMode = false;
		int number_of_our_fleets_in_flight = pw.MyFleets().size();
		a.Update(numships_us, num_ships_us);
		a.Update(numships_opponent, num_ships_opponent);
		a.Update(productions_us, prod_us);
		a.Update(productions_opponent, prod_opponent);
		a.Update(number_Fleets, numFleets);
		a.Update(attack_mode, "false");
		String attackmode_1="false";
		a.Commit();
		a.RunSelfTilOutput();

	//	csv = new PrintWriter("output");
		
		csv.write("\nNumber commands"+a.GetNumberCommands());
		

		for (int i = 0; i < a.GetNumberCommands(); ++i) 
	    {
			Identifier commandWME = a.GetCommand(i);
			String commandName = commandWME.GetAttribute();
			if (commandName.equals("stop"))
			{
				csv.write("\nanant\n");
				attackmode_1=commandWME.GetParameterValue("attack_mode");
				//numFleets=Integer.parseInt(commandWME.GetParameterValue("number_fleets"));
				csv.write("\nattackmode_1"+attackmode_1+"\n");
				//csv.write("\nnumFleets"+numFleets+"\n");
				csv.write("\nanant\n");			
				break;
				//commandWME.AddStatusComplete();
			}
			if (commandName.equals("start"))
			{
				//attackmode_1=commandWME.GetParameterValue("attack_mode");
				//numFleets=Integer.parseInt(commandWME.GetParameterValue("number_fleets"));
				//csv.write("\nattackmode_1"+attackmode_1+"\n");
				//csv.write("\nnumFleets"+numFleets+"\n");
				//csv.write("\nanant\n");			
				break;
				//commandWME.AddStatusComplete();
			}
	   	}



		if(pw.NumShips(1) > pw.NumShips(2)) 
		{
			if (pw.Production(1) < pw.Production(2))
			numFleets = 3;
		} 
		else 
		{
		    if (pw.Production(1) > pw.Production(2)) 
		    {
			numFleets = 1;
		    } else 
		    {
			numFleets = 5;
		    }	    
		}
		if(attackmode_1=="true")
			attackMode=true;

		// (1) If we current have more tha numFleets fleets in flight, just do
		// nothing until at least one of the fleets arrives.
		if (pw.MyFleets().size() >= numFleets) 
		{
		    return;
		}
		// (2) Find my strongest planet.
		Planet source = null;
		double sourceScore = Double.MIN_VALUE;
	/*	for (Planet p : pw.MyPlanets()) 
		{
		    double score = (double)p.NumShips() / (1 + p.GrowthRate());
		    if (score > sourceScore) 
		    {
				sourceScore = score;
				source = p;
		    }
		}*/
		// (3) Find the weakest enem8y or neutral planet.
		Planet dest = null;
		double destScore = Double.MIN_VALUE;
		List<Planet> candidates = pw.NotMyPlanets();
		if (attackMode) 
		{
		    candidates = pw.EnemyPlanets();
		}
	/*	for (Planet p : candidates) 
		{
		    double score = (double)(1 + p.GrowthRate()) / p.NumShips();
		    if (score > destScore) 
		    {
				destScore = score;
				dest = p;
		    }
		}*/
		for (Planet p : candidates) 
		{
			for (Planet x : pw.MyPlanets()) 
			{
			    int dist=pw.Distance(p,x);
			    double score = (double)(1 + p.GrowthRate())*(x.NumShips()) / ((1 + x.GrowthRate())*(p.NumShips()*dist));
			    if (score > destScore) 
			    {
					destScore = score;
					source = x;
					dest = p;
			    }
			}

		}
		// (4) Send half the ships from my strongest planet to the weakest
		// planet that I do not own.
		if (source != null && dest != null) 
		{
		    int numShips = source.NumShips()/2;
		  //  for (int i = 0; i < a.GetNumberCommands(); ++i) 
		    //{
				//Identifier commandWME = a.GetCommand(i);
				//String commandName = commandWME.GetAttribute();
				//if (commandName.equals("stop")) 
				//{
					pw.IssueOrder(source, dest, numShips);
				//}
		//	}
		}	
}


    public static void main(String[] args) throws FileNotFoundException 
    {
    	csv = new PrintWriter("output");
		
    	kernel = kernel.CreateRemoteConnection(true,null,12121);
    	String version = kernel.GetSoarKernelVersion();
    	System.out.println("soar version : "+version);
    	agent = kernel.GetAgent("soar1");	//soar1 is the name of the agent in saor
    	System.out.println(agent.GetAgentName());
        if (kernel.HadError())
                throw new IllegalStateException("Error creating agent: " + kernel.GetLastErrorDescription()) ;
    	
        inputLink = agent.GetInputLink();



		isInit = agent.CreateStringWME(inputLink, "init","no");
		planetwarsWME = agent.CreateIdWME(inputLink, "planetwars");
		planet = agent.CreateIdWME(planetwarsWME, "planet");
		fleet = agent.CreateIdWME(planetwarsWME, "fleet");

		//My own WMEs start here

		numships_us=agent.CreateIntWME(planet,"numships_us",0);
		numships_opponent=agent.CreateIntWME(planet,"numships_opponent",0);
		productions_us=agent.CreateIntWME(planet,"productions_us",0);
		productions_opponent=agent.CreateIntWME(planet,"productions_opponent",0);
		number_Fleets=agent.CreateIntWME(fleet,"number_Fleets",0);
		attack_mode = agent.CreateStringWME(planetwarsWME, "attack_mode","false");


		//My own WMEs end here

        kernel.SetAutoCommit(false);
        
        
        String line = "";
		String message = "";
		int c;
		try 
		{
		    while ((c = System.in.read()) >= 0) 
		    {
  				switch (c) 
  				{
  					case '\n':
  					    if (line.equals("go")) 
  					    {
  							PlanetWars pw = new PlanetWars(message);
  							DoTurn(pw,agent);
  						    pw.FinishTurn();
  							message = "";
  					    } 
  					    else 
  					    {
  							message += line + "\n";
  						}
  					    line = "";
  					    break;
  					default:
  					    line += (char)c;
  					    break;
  				}
		    }
		} 
		catch (Exception e) 
		{
	    // Owned.
		}
		csv.close();
    }
}
