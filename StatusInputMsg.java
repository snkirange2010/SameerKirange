import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represent the status input message.
 * @author Sameer.Kirange
 *
 */
public class StatusInputMsg {

	ArrayList<Path> currEdge; //corresponds to section 1 of the input.
	ArrayList<DepartureFlight> currFlightStatus; // corresponds to section 2 of the input.
	ArrayList<Baggage> currBaggage; // corresponds to section 3 of the input.

	Map<String, String> flightToGateMap;
	
	public boolean isEmpty(){
		if(currEdge.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public StatusInputMsg() {
		super();
		this.currEdge = new ArrayList<Path>();
		this.currFlightStatus = new ArrayList<DepartureFlight>();
		this.currBaggage = new ArrayList<Baggage>();
		flightToGateMap = new HashMap<String,String>();
	}

	public ArrayList<Path> getCurrEdge() {
		return currEdge;
	}

	public ArrayList<DepartureFlight> getCurrFlightStatus() {
		return currFlightStatus;
	}

	public ArrayList<Baggage> getCurrBaggage() {
		return currBaggage;
	}

	public Map<String, String> getFlightToGateMap(){
		return flightToGateMap;
	}
	
	public StatusInputMsg getCurrentStatus(){
		StatusInputMsg currStatus = new StatusInputMsg();
		
		return currStatus;
	}

	public void copy(StatusInputMsg newMsg) {
		currEdge = new ArrayList<Path>(newMsg.getCurrEdge());
		currFlightStatus = new ArrayList<DepartureFlight>(newMsg.getCurrFlightStatus());
		currBaggage = new ArrayList<Baggage>(newMsg.getCurrBaggage());
		flightToGateMap.putAll(newMsg.getFlightToGateMap());
	}
	

	public void clear(){
		currEdge.clear(); //corresponds to section 1 of the input.
		currFlightStatus.clear(); // corresponds to section 2 of the input.
		currBaggage.clear(); // corresponds to section 3 of the input.
	}
	
	public void createFlightToGateMapping() {
		for(DepartureFlight dp : currFlightStatus){
			flightToGateMap.put(dp.getFlightId(), dp.getFlightGate());
		}
	}
	
	public String getGateForFlight(String flightId){
		return flightToGateMap.get(flightId);
	}

}

/**
 * Represent Path class
 * @author Sameer.Kirange
 *
 */
class Path {
	
	String startNode;
	String endNote;
	int travelTime;
	
	public Path(String startNode, String endNote, int travelTime) {
		super();
		this.startNode = startNode;
		this.endNote = endNote;
		this.travelTime = travelTime;
	}
	
	public String getStartNode() {
		return startNode;
	}

	public String getEndNode() {
		return endNote;
	}

	public double getTravelTime() {
		return (double)travelTime;
	}
	
}

class DepartureFlight {
	
	String flightId;
	String flightGate;
	String destinationGate;
	String flightTime;
	
	public DepartureFlight(String flightId, String flightGate,
			String destinationGate, String flightTime) {
		super();
		this.flightId = flightId;
		this.flightGate = flightGate;
		this.destinationGate = destinationGate;
		this.flightTime = flightTime;
	}

	public String getFlightId() {
		return flightId;
	}

	public String getFlightGate() {
		return flightGate;
	}

	public String getDestinationGate() {
		return destinationGate;
	}

	public String getFlightTime() {
		return flightTime;
	}

}

/**
 * Represent baggage class
 * @author Sameer.Kirange
 *
 */
class Baggage {
	
	String bagNumber;
	String entryPoint;
	String flightId;
	
	public Baggage(String bagNumber, String entryPoint, String flightId) {
		super();
		this.bagNumber = bagNumber;
		this.entryPoint = entryPoint;
		this.flightId = flightId;
	}

	public String getBagNumber() {
		return bagNumber;
	}

	public String getEntryPoint() {
		return entryPoint;
	}

	public String getFlightId() {
		return flightId;
	}
	
}
