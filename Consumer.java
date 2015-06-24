import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * This will process the current input
 * THis will form the graph dynamically at each input
 * since the state of the graph in practical scenario can not remain constant every time.
 * For e.g Some connecting node may fail and then the baggage routing paths need to be adjusted accordingly using graphs.
 * So it is assumed that input status message will always contain the valid list of paths and weightage at that point.  
 * @author Sameer.Kirange
 *
 */
public class Consumer implements Runnable {

	protected BlockingQueue<StatusInputMsg> queue = null;

	public Consumer(BlockingQueue<StatusInputMsg> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {

			System.out.println("Processing current input status message");
			StatusInputMsg receivedNewMsg = queue.take();
			//-------------------------------------------------------------------------
			ArrayList<Path> currPathList = new ArrayList<Path>(receivedNewMsg.getCurrEdge()); 
			Map<String,Gate> currGateStatus = new HashMap<String,Gate>();
			Map<String,Gate> finalCurrGateStatus = new HashMap<String,Gate>();

			Map<Gate, ArrayList<Edge>> adjacencyStatus = getCurrentAdjacencyStatus(currPathList);

			for(Path path : currPathList){
				Gate tempGate = new Gate(path.getStartNode());
				currGateStatus.put(tempGate.toString(),tempGate);
			}

			// At this instant, form the graph and find the shorted path with the current edges
			for (Map.Entry<String,Gate> e1 : currGateStatus.entrySet()) {

				String key = e1.getKey();
				Gate gate = e1.getValue();

				ArrayList<Edge> a = adjacencyStatus.get(gate);
				Edge[] b = new Edge[a.size()];

				// Update the adjecencies with current input.
				for (int i = 0; i <a.size(); i++){
					Edge e = new Edge(gate,a.get(i).travelTime);
					b[i] = e;

				}
				gate.adjacencies = (Edge[])b.clone();
				finalCurrGateStatus.put(key, gate);
			}

			System.out.println("Succcessfully processed current Input Status");

			/*
			for(Map.Entry<String, Gate> entry : finalCurrGateStatus.entrySet()){
				System.out.println("key "+entry.getKey());
				System.out.println("value "+entry.getValue().shortestTime);

			}
			 */
			//-------------------------------------------------------------------------

			ArrayList<DepartureFlight> currDeptFlightList = new ArrayList<DepartureFlight>(receivedNewMsg.getCurrFlightStatus());
			Map<String,String> currFlightIdGateNameStatus = new HashMap<String,String>();

			for(DepartureFlight deptFlight : currDeptFlightList){

				currFlightIdGateNameStatus.put(deptFlight.getFlightId(),deptFlight.getFlightGate());
			}
			currFlightIdGateNameStatus.put("ARRIVAL", "BaggageClaim");

			System.out.println("Succcessfully processed current flight Status");

			//-------------------------------------------------------------------------

			ArrayList<Baggage>currBaggageList = new ArrayList<Baggage>(receivedNewMsg.getCurrBaggage());

			for(Baggage bg : currBaggageList){

				String currBagNumber = bg.getBagNumber();
				String currStartNode = bg.getEntryPoint();
				String currEndNode = currFlightIdGateNameStatus.get(bg.getFlightId());

				// This sleep can be varied to see the effect of processing time on the input buffer management. 
				sleep(1);
				
				Dijkstra.computeShortestPath(finalCurrGateStatus.get(currStartNode)); // run Dijkstra
				System.out.println("For bag "+ currBagNumber+ " Time to reach " + currEndNode + ": " + finalCurrGateStatus.get(currEndNode).shortestTime );

				List<Gate> path = Dijkstra.getShortestPathTo(finalCurrGateStatus.get(currEndNode));
				System.out.println("Path: " + path.toString());
				
				

			}

			System.out.println("Succcessfully generated shortest duration paths to each baggage in input message");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void sleep(int i) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This function will get the current adjacencyStatus from the latest paths.
	 * @param currPathList
	 * @return
	 */
	private Map<Gate, ArrayList<Edge>> getCurrentAdjacencyStatus(ArrayList<Path> currPathList) {
		Map<Gate, ArrayList<Edge>> adjacencyStatus = new HashMap<Gate, ArrayList<Edge>>();

		for(Path path : currPathList){
			Gate gate = new Gate(path.getStartNode());
			Edge edge = new Edge(new Gate(path.getEndNode()),path.getTravelTime());
			if(adjacencyStatus.containsKey(gate)){
				ArrayList<Edge> edgeList = adjacencyStatus.get(gate);
				edgeList.add(edge);
				adjacencyStatus.put(gate, edgeList);
			}else{
				ArrayList<Edge> edgeList = new ArrayList<Edge>();
				edgeList.add(edge);
				adjacencyStatus.put(gate, edgeList);
			}
		}

		/*
		for(Map.Entry<Gate, ArrayList<Edge>> entry : adjacencyStatus.entrySet()){
			System.out.println(" key "+entry.getKey());
			for(Edge edge : entry.getValue()){
				System.out.print(" value "+edge.destGate);
			}

		}
		 */

		return adjacencyStatus;
	}

}
