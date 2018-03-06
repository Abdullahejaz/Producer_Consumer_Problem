import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;
import java.util.*;


public class prodConsProblem {
    static ArrayList<Integer> list = new ArrayList<Integer>();
    public static int numberToCreate = 0;
    public static int consumer_counter = 0;
    public static int i = 1;



    static class Producer implements Runnable{

        List<Integer> list;
        private String threadId1;

        //To keep count of the items produced by the a thread.
        private int producer_counter;

        public Producer(List<Integer> list) {
            this.list = list;

        }


        @Override
        public void run() {

            while(producer_counter <= 64) {

                synchronized(list) {

                    threadId1 = "Producer " + Thread.currentThread().getName();

                    //Buffer size is 8
                    while(list.size() >= 8) {
                        System.out.println( "----------------------------------------------------------------------------------");
                        System.out.println(threadId1+ " found the buffer is full & waiting for a Consumer to consume.");
                        System.out.println( "----------------------------------------------------------------------------------");
                        try {
                            //It causes the current thread to wait until another thread invokes the notify() method or the notifyAll() method for this object.
                            list.wait();
                            list.notifyAll();
                        }

                        catch (InterruptedException ie) {
                            // TODO Auto-generated catch block
                            ie.printStackTrace();
                        }
                    }

                    System.out.println(threadId1+ " produced item " + producer_counter);
                    //Adding the items produced to the list.
                    list.add(producer_counter);
                    producer_counter++;
                    //Invoking notify
                    list.notifyAll();

                }
            }
        }
    }


    static class Consumer implements Runnable{

        private String threadId;
        List<Integer> list;


        public Consumer (List<Integer> list) {
            this.list = list;

        }

        @Override
        public void run() {
            while(consumer_counter <= 64*numberToCreate) {
                synchronized(list) {
                    threadId = "Consumer " + Thread.currentThread().getName();

                    //Consumer does not run when the buffer is empty.
                    while(list.isEmpty()) {
                        System.out.println( "----------------------------------------------------------------------------------");
                        System.out.println( threadId+ " found the buffer is empty & waiting for the Producer to produce");
                        System.out.println( "----------------------------------------------------------------------------------");
                        try {
                            list.wait();
                            list.notifyAll();
                        }
                        catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }

                    //For consuming (removing) the items from the buffer
                    int j = list.remove(0);
                    consumer_counter++;
                    System.out.println(threadId+ " consumed item " + j);
                    list.notifyAll();

                }
            }
        }
    }


    @SuppressWarnings({ "resource"})
    public static void main (String [] args) {

        //Getting user's input for the number of Producers and consumer threads.
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the number of Producers and Consumers: ");

        while(!in.hasNextInt()){
            System.out.println("'" + in.nextLine() + "'" + " is not a valid entry. Please enter a number: ");
        }
        numberToCreate = in.nextInt();

        for(int i = 1; i <= numberToCreate; i++){

            //Producer Object
            Thread producerr = new Thread(new Producer(list));
            producerr.start();

            //Consumer Object
            Thread consumerr = new Thread(new Consumer(list));
            consumerr.start();
        }

    }


}
