import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * THe purpose of this class is to read and process the input baggage detail messages and put
 * in the queue for processing.
 * @author Sameer.Kirange
 *
 */
public class Producer implements Runnable {

	protected BlockingQueue<StatusInputMsg> queue = null;

	public Producer(BlockingQueue<StatusInputMsg> queue) {
		this.queue = queue;
	}

	/**
	 * This run method of thread reads and parses the read message from the file and put in the Queue.
	 * THis method assumes that input format is already validated and is in the correct format
	 * Input validation is kept out of the scope of this method.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Reading current input status message");

			BufferedReader br = null;
			StatusInputMsg newMsg = new StatusInputMsg();
			try {

				String currentLine;
				int inputType = 0;

				br = new BufferedReader(new FileReader("C:\\BTCTestNew\\GraphDS\\src\\input.txt"));
				
				while ((currentLine = br.readLine()) != null) {
					System.out.println(currentLine);
					if ("# Section: Conveyor System".equals(currentLine )) {
						if(!newMsg.isEmpty()){
							newMsg.createFlightToGateMapping();
							StatusInputMsg pCopy = new StatusInputMsg();
							pCopy.copy(newMsg);
							queue.put(pCopy);
							newMsg.clear();
							
							//Following Sleep can be varied to see the effect of processing speed
							//on the input capacity planning. Basically we can vary the speed with which
							//input is coming.
							
							Thread.sleep(2000);
						}
						inputType = 1;
						continue;
					}
					if ("# Section: Departures".equals(currentLine )) {
						inputType = 2;
						continue;
					}
					if ("# Section: Bags".equals(currentLine )) {
						inputType = 3;
						continue;
					}

					switch(inputType){

					case 1:
						String[] line1 = currentLine.split(" ");
						Path path = new Path(line1[0], line1[1], Integer.parseInt(line1[2]));
						newMsg.getCurrEdge().add(path);
						break;
					case 2:
						String[] line2 = currentLine.split(" ");
						DepartureFlight dp = new DepartureFlight(line2[0], line2[1], line2[2], line2[3]);
						newMsg.getCurrFlightStatus().add(dp);
						break;
					case 3:
						String[] line3 = currentLine.split(" ");
						Baggage baggage = new Baggage(line3[0], line3[1], line3[2]);
						newMsg.getCurrBaggage().add(baggage);
						break;
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
					if(!newMsg.isEmpty()){
						newMsg.createFlightToGateMapping();
						StatusInputMsg pCopy = new StatusInputMsg();
						pCopy.copy(newMsg);
						queue.put(pCopy);
						newMsg.clear();
						Thread.sleep(2000);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}


		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
