import sml.*;
public class Gluetosoar {
	
	
	private static Identifier marioloc;
	private static Identifier inputLink;
	private static Agent agent;
	
	private static int prev_intArray0;
	
	private double Reward;
	private static FloatElement reward;
 
	private Identifier planetwarsWME;
	private static StringElement isInit;
	
	private static Identifier planet;
	private static FloatElement planet_x;
	private static FloatElement planet_y;
	private static IntElement growthrate;
	private static IntElement p_owner;
	private static IntElement planetid;
	private static IntElement planet_num_ship;
	private static IntElement xpos;
	private static StringElement type;
	
	
	private static Identifier fleet;
	private static IntElement f_owner;
	private static IntElement fleet_num_ship;
	private static IntElement source_planet;
	private static IntElement dest_planet;
	private static IntElement total_trip;
	private static IntElement turns_left;
	public Gluetosoar(Agent a)
	{
		//number_of_ships, planets we own, nearest planet, furthest planet, growthrate, color, x, y, planetid
		
		agent = a;
		inputLink = agent.GetInputLink();
		
		
		reward = agent.CreateFloatWME(inputLink, "reward", 0.0);
	
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
		
	}
}