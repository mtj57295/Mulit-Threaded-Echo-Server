/*
    The Threadpool class is to handle the amount of threads be used on the server
    It has two inner classes "WorkQueue" and  "WorkerThread" that are used to start
    the threads. Uses ReentrantLock and condition to create safe threads in the
    WorkQueue Class.
 */
package reentrantLockPackage;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
    
    private final static int MAXTHREADS = 3;
    private Thread[] workingThreads;
    private WorkQueue workQueue;
    private ReentrantLock reentrant;
    private Condition condition;
    
    /**
     * Initializes the workingThread array to hold all the threads using the
     * variables MAXTHREADS to handle the max number of threads. This constructor
     * starts the threads after passing a runnable object from the WorkerThread
     * class.
     */
    public  ThreadPool() {       
        reentrant = new ReentrantLock();
        condition = reentrant.newCondition();
        workingThreads = new Thread[MAXTHREADS];
        workQueue = new WorkQueue();
        for(int i = 0; i < MAXTHREADS; i++) {
            workingThreads[i] = new Thread(new WorkerThread());
            workingThreads[i].start();  
        }
    }
    
    /**
     * Adds a new Connection object to the WorkQueue class.
     * @param connection the connection that is put into the queue.
     */
    public void addTaskToQueue(Connection connection) {
        workQueue.addTask(connection);
    }

    /**
     * This class handles all the connection by placing them in a linkedlist 
     * acting as a queue. Even though the program only allows for a max number 
     * of threads to be connected, if a thread is dropped from the array a new
     * one will be added if the queue holds any connection objects.
     */
    public class WorkQueue {
        
        private LinkedList<Connection> list;
        
        public WorkQueue() {
            list = new LinkedList<>();  
        }
        
        /**
         * Adds a new task to the linked list, does this by locking the thread
         * and unlocking it when the signalAll method is called. We have to 
         * lock up this method because a linked list is not thread safe.
         * @param connection The new connection object to had to the linked list
         */
        public void addTask(Connection connection) {           
            reentrant.lock();
            try {
                list.add(connection);  
                condition.signalAll();
            }catch(Exception e) {
                e.printStackTrace();
            }finally {
                reentrant.unlock();
            }
        }
        
        /**
         * Retrieves and removes a connection object from the linked list, we need
         * to lock and unlock in this method because the linked list is not thread
         * safe when accessing it. While the linked list is empty we need to wait
         * for a signal to be called from the addTask method.
         * @return a connection object at index 0 of the linked list.
         */
        public Connection getTask() {
            Connection connection = null;      
            reentrant.lock();
            try {
                while(list.isEmpty())
                    condition.await();
                connection = list.remove();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                reentrant.unlock();
                return connection;  
            }       
        }
    }
    
    /**
     * Creates a class that implements runnable which when declared runs the 
     * overrided method to get a connection object from the queue and start 
     * running it.
     */
    public class WorkerThread implements Runnable {
        @Override
        public void run() {
            while(true) {
                workQueue.getTask().run();
            }
        }
    }
}

