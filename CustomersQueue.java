
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concurrent Programming
 * Assignment-1 --> Java Threads - Sleeping Barbers Problem
 */
public class CustomersQueue {
   private final Lock lock = new ReentrantLock();
   private final Condition QueueAvailable = lock.newCondition();
   private int NumberOfCustomers; //integer variable for the customers in the queue
   private final int MaximumCustomers; //integer variable for maximum number of customers that can be held in the queue
   
   public CustomersQueue(int num_customers_queues) {
       this.MaximumCustomers = num_customers_queues;
       this.NumberOfCustomers = 0;
   }
   /**
    * Method to add a customer to the queue
    */
   public void AttainCustomer() throws InterruptedException {
       lock.lock();
       try {
           while(NumberOfCustomers <= 0)
               QueueAvailable.await();
               --NumberOfCustomers;
       } finally {
           lock.unlock();
       }  
   }
   /**
    * Method to free customers from the queue
    */
   public void ReleaseCustomer(){
    lock.lock();
    /**
     * Ensuring that release does not work before attaining customers in the queue
     */
    try {
        if(NumberOfCustomers >= MaximumCustomers)
            return;
        ++NumberOfCustomers;
        QueueAvailable.signal();
    } finally {
        lock.unlock();
    }
   }
}
