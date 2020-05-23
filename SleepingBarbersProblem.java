
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concurrent Programming
 * Assignment-1 --> Java Threads - Sleeping Barbers Problem
 */
public class SleepingBarbersProblem extends Thread {
    
    public static final int WaitingChairs = 5; // Numbers of waiting chairs in the Barber Shop
    public static final int CostPerHaircut = 10; //Cost Per Haircut
    public static final int BarbersCount = 2; //Assumed the number of barbers in the barbershop
    public static final int CustomersCount = 20; //Assumed the number of customers visiting in a day for haircut
    public static final int CustomerArrivalOffset = 1000; // For random customer generation
    public static final int CustomerArrivalIntervalRange = 800; // For random customer generation
    public static int counter = 0; // Integer variable counter for calculating money collected
    public static int counter1; //Integer variable to count the haircut    
    /**
     * Created an integer variable as free seat to decide whether customer will sit
     * or leave the shop if no free seats are available
     */
    
    public static int FreeSeats = WaitingChairs;
    
    /**
     * Created a customer queue for the customers who are waiting in for their haircut
     */
    public static CustomersQueue customers = new CustomersQueue(WaitingChairs);
    /**
     * Reentrant locks for barbers for synchronization and concurrency 
     */
    public static Lock barber = new ReentrantLock();
    public static Condition BarberAvailable = barber.newCondition();
    /**
     * Reentrant lock for chairs synchronization safely
     */
    public static Lock accesschairs = new ReentrantLock();
    /**
     * Customer Class Thread
     */
    class Customers extends Thread {
        int CustomerId;
        boolean NoHairCut = true;
        /**
         * Creating constructor for the customers class
         */
        public Customers(int i){
            CustomerId = i;
        }
        public void run() {
            while(NoHairCut){ // till the time customer has not got the haircut
                accesschairs.lock(); //Get access to the chairs
                if(FreeSeats > 0) {
                    System.out.println("Customer" + " " + this.CustomerId + " just sat down at" + " " + 
                            java.time.LocalTime.now() + " " + "and waiting for the barber to get the haircut.");
                   // counter++; //counter to count the customers attended
                    FreeSeats--; //Customer sits on the chair
                    customers.ReleaseCustomer(); //notify about the customer to the barber
                    accesschairs.unlock(); //unlock the chairs
                    barber.lock();
                    try {
                        BarberAvailable.await();
                    } catch (InterruptedException e) { 
                    } finally {
                        barber.unlock();
                    }
                    NoHairCut = false;
                    this.Haircut(); //Getting a haircut
                } else { //All the waiting chairs are occupied and there are no free chairs
                    counter++;
                    System.out.println("All the waiting chairs are occupied and customer " +
                            this.CustomerId + " has left the barbershop.");
                    accesschairs.unlock(); //release the locks on the chairs as the customer left the barbershop
                    NoHairCut = false;
                }
            }
        }
        /**
         * Method for customers getting the haircut
         */
        public void Haircut() {
            System.out.println("Customer " + this.CustomerId + " is getting the hair cut at " + java.time.LocalTime.now());
            try{
                sleep(5500);
                System.out.println("Customer " + this.CustomerId + " got the haircut and left the shop at " + java.time.LocalTime.now());
            } catch (InterruptedException ex) {
                
            }
        }
    }
    /**
     * Barber class thread for haircut
     */
    class Barbers extends Thread {
        int BarberId;
        /**
         * Creating constructor for Barber class
         */
        public Barbers(int i){
            BarberId = i;
        }
        public void run() {
            while(true) {
                counter1++;
                if(counter1 <= CustomersCount - counter) {
                    try {
                        customers.AttainCustomer(); //attain a customer for haircut and if no customer is available barber goes to sleep
                        accesschairs.lock(); //Barber wakes up for the customer's haircut
                        FreeSeats++; //a chair gets available at this time
                        barber.lock();
                        try {
                            BarberAvailable.signal(); //Barber is ready to give haircut
                        } finally {
                            barber.unlock();
                        }
                        accesschairs.unlock(); //Releases the lock on the chairs as we don't require to lock anymore
                        this.BarberOccupied();
                    } catch (InterruptedException ex) {               
                    }
                } else {
                    System.out.println("Total haircuts done by the barbers at the end of the day = " + (CustomersCount - counter));
                    System.out.println("Total customers left the barbershop without haircut = " + counter);
                    System.out.println("Total money earned by the barbers at the end of the day = " + (CustomersCount - counter) * 10 + " " + "Euros");
                    System.exit(0);
                }
            }
        }
        /**
         * Barber method for the haircut
         */
        public void BarberOccupied() {
            System.out.println("Barber " + this.BarberId + " is cutting the customer's hairs.");
            try {
                sleep(8000);
            } catch (InterruptedException ex){
            }
        }
    }
    /**
     * Main method for the SleepingBarbersProblem class
     */
    public static void main(String args[]) {
        SleepingBarbersProblem sbp = new SleepingBarbersProblem();
        sbp.start();
    }
    public void run(){
        for(int i = 1; i <= BarbersCount; i++ ){
            Barbers br = new Barbers(i);
            br.start();
        }
        for(int i = 1; i <= CustomersCount; i++){
            Customers c = new Customers(i);
            c.start();
            try{
                Thread.sleep(NextRandomCustomer()); //Random generation of customers
            } catch (InterruptedException ex){
            }
        }
    }
    /**
     * Method for random customers arrival at the barbershop
     */
    public long NextRandomCustomer() {
        return ThreadLocalRandom.current().nextInt(CustomerArrivalIntervalRange) + CustomerArrivalOffset;
    } 
}
