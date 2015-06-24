import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class AutomaticBaggageSystemSimulation {

	public static void main(String[] args) throws Exception {
		
		// Queue size can be varied to see the effect of average time between the flight arrival and
		// input capacity/ buffer planning requirement.
		
		BlockingQueue queue = new ArrayBlockingQueue(1024);

		//Producer will produce the input bag status messages,
		//For demo purpose it is read from a file :input.txt and put into the queue
		//Consumer will be blocked on the same queue and will consume the received messages.
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        //Create threads for producer and consumer
        Thread th1 = new Thread(producer);
        th1.start();
        
        //Join the thread to the main thread
        th1.join();
        Thread th2 = new Thread(consumer);
        th2.start();
        //Join the thread to the main thread
        th2.join();
        
        // This delay can be varied to see the effect of latency of baggage processing 
        Thread.sleep(4000);

	}

}
