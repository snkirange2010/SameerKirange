import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Standard Dijkstra's shorted path algorithm.
 * Node/Vertex terminology changed to view analogy with baggage gates.
 * @author Sameer.Kirange
 *
 */
class Gate implements Comparable<Gate>
{
	public final String gateName;
	public Edge[] adjacencies;
	public double shortestTime = Double.POSITIVE_INFINITY;
	public Gate previous;
	public Gate(String argGateName) { gateName = argGateName; }
	public String toString() { return gateName; }
	public int compareTo(Gate other)
	{
		return Double.compare(shortestTime, other.shortestTime);
	}
	
	@Override
    public int hashCode() {
        
        return gateName.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Gate other = (Gate) obj;
        if(gateName.equals(other.gateName)){
        	return true;
        }
        return true;
    }

}


class Edge
{
	public final Gate destGate;
	public final double travelTime;
	public Edge(Gate argDestGate, double argTravelTime)
	{ destGate = argDestGate; travelTime = argTravelTime; }
}

public class Dijkstra
{
	public static void computeShortestPath(Gate sourceGate)
	{
		sourceGate.shortestTime = 0.;
		PriorityQueue<Gate> gateQueue = new PriorityQueue<Gate>();
		gateQueue.add(sourceGate);

		while (!gateQueue.isEmpty()) {
			Gate u = gateQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies)
			{
				Gate v = e.destGate;
				double travelTime = e.travelTime;
				double timeThroughU = u.shortestTime + travelTime;
				if (timeThroughU < v.shortestTime) {
					gateQueue.remove(v);

					v.shortestTime = timeThroughU ;
					v.previous = u;
					gateQueue.add(v);
				}
			}
		}
	}

	public static List<Gate> getShortestPathTo(Gate target)
	{
		List<Gate> path = new ArrayList<Gate>();
		for (Gate vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);

		Collections.reverse(path);
		return path;
	}

	
	/*Test function for algorithm*/
	public static void main(String[] args)
	{
		// mark all the vertices 
		Gate A = new Gate("A");
		Gate B = new Gate("B");
		Gate D = new Gate("D");
		Gate F = new Gate("F");
		Gate K = new Gate("K");
		Gate J = new Gate("J");
		Gate M = new Gate("M");
		Gate O = new Gate("O");
		Gate P = new Gate("P");
		Gate R = new Gate("R");
		Gate Z = new Gate("Z");

		// set the edges and weight
		
		// set the edges and weight
        A.adjacencies = new Edge[]{ new Edge(M, 8) };
        B.adjacencies = new Edge[]{ new Edge(D, 11) };
        D.adjacencies = new Edge[]{ new Edge(B, 11) };
        F.adjacencies = new Edge[]{ new Edge(K, 23) };
        K.adjacencies = new Edge[]{ new Edge(O, 40) };
        J.adjacencies = new Edge[]{ new Edge(K, 25) };
        M.adjacencies = new Edge[]{ new Edge(R, 8) };
        O.adjacencies = new Edge[]{ new Edge(K, 40) };
        P.adjacencies = new Edge[]{ new Edge(Z, 18) };
        R.adjacencies = new Edge[]{ new Edge(P, 15) };
        Z.adjacencies = new Edge[]{ new Edge(P, 18) };


        computeShortestPath(J); // run Dijkstra
        System.out.println("Distance to " + O + ": " + O.shortestTime);
        List<Gate> path = getShortestPathTo(O);
        System.out.println("Path: " + path);
	}
}
