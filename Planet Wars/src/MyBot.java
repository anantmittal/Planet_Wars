import java.util.*;
import sml.*;
public class MyBot 
{
	private static Kernel kernel;
 	private static Agent agent;
 	
 	private static Identifier inputLink;
 	private static Identifier planetwarsWME;
 	private static StringElement isInit;

 	
 	private static Identifier planet;
 	private static FloatElement planet_x;
 	private static FloatElement planet_y;
 	private static IntElement growthrate;
 	private static IntElement p_owner;
 	private static IntElement planetid;
 	private static IntElement planet_num_ship;
 		
 	private static Identifier fleet;
 	private static IntElement f_owner;
 	private static IntElement fleet_num_ship;
 	private static IntElement source_planet;
 	private static IntElement dest_planet;
 	private static IntElement total_trip;
 	private static IntElement turns_left;

	public static void DoTurn(PlanetWars pw) 
    {
		// (1) If we currently have a fleet in flight, just do nothing.
		if (pw.MyFleets().size() >= 1) 
		{
		    return;
		}
		// (2) Find my strongest planet.
		Planet source = null;
		double sourceScore = Double.MIN_VALUE;
		for (Planet p : pw.MyPlanets()) 
		{
		    double score = (double)p.NumShips();
		    if (score > sourceScore) 
		    {
				sourceScore = score;
				source = p;
		    }
		}
		// (3) Find the weakest enemy or neutral planet.
		Planet dest = null;
		double destScore = Double.MIN_VALUE;
		for (Planet p : pw.NotMyPlanets()) 
		{
		    double score = 1.0 / (1 + p.NumShips());
		    if (score > destScore) 
		    {
				destScore = score;
				dest = p;
		    }
		}
		// (4) Send half the ships from my strongest planet to the weakest
		// planet that I do not own.
		if (source != null && dest != null) 
		{
		    int numShips = source.NumShips()/2;
		    pw.IssueOrder(source, dest, numShips);
		}
    }


    public static void main(String[] args) 
    {
    	kernel = kernel.CreateRemoteConnection(true,null,12121);
    	String version = kernel.GetSoarKernelVersion();
    	System.out.println("soar version : "+version);
    	agent = kernel.CreateAgent("planetwars") ;
        if (kernel.HadError())
                throw new IllegalStateException("Error creating agent: " + kernel.GetLastErrorDescription()) ;
    	
        inputLink = agent.GetInputLink();
		
		
	
		isInit = agent.CreateStringWME(inputLink, "init","no");
		planetwarsWME = agent.CreateIdWME(inputLink, "planetwars");
		planet = agent.CreateIdWME(planetwarsWME, "planet");
		
		
		p_owner=agent.CreateIntWME(planet, "p_owner", 0);
		planetid=agent.CreateIntWME(planet, "planetid", 0);
		growthrate = agent.CreateIntWME(planet, "growthrate", 0);
		planet_x = agent.CreateFloatWME(planet, "x", 0.0);
		planet_y = agent.CreateFloatWME(planet, "y", 0.0);
		planet_num_ship=agent.CreateIntWME(planet,"planet_num_ship",0);
		
		
		fleet = agent.CreateIdWME(planetwarsWME, "fleet");
		fleet_num_ship = agent.CreateIntWME(fleet,"fleet_num_ships",0);
		f_owner = agent.CreateIntWME(fleet, "f_owner", 0);
		source_planet =agent.CreateIntWME(fleet, "source_planet", 0);
		dest_planet = agent.CreateIntWME(fleet, "dest_planet", 0);
		total_trip = agent.CreateIntWME(fleet, "total_trip", 0);
		turns_left = agent.CreateIntWME(fleet, "turns_left", 0);

        kernel.SetAutoCommit(true);
        agent.RunSelfTilOutput();
        
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
							DoTurn(pw);
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
    }
}
