package ADT;

import java.io.Serializable;
import java.util.Iterator;


/*
 * @param <T>

==============
CIRCULAR QUEUE
==============
1) ENTRIES MOVES IN ACYCLIC MOTION
2) UPON REACHING THE REAR OF ARRAY/QUEUE,
3) ENTRIES WILL BE ADDED TO THE FRONT
4) CYCLE CONTINUES
5) ;HENCE, CIRCULAR QUEUE

 */
public class CircularQueue<T extends Comparable<T>> implements QueueInterface<T>, Serializable {

    private T[] queue;                           //QUEUE ARRAY/QUEUE.
    private T[] tempQueue;                       //TEMPORARY ARRAY/QUEUE.
    private int front, rear;                     //TO INDICATE POSITION OF THE QUEUE. FRONT = USED FOR DEQUEUE, REAR USED FOR ENQUEUE.
    private static int QUEUE_CAPACITY = 5;       //INITIAL CAPACITY CAPPED.
    private int numberOfEntries = 0;

    public CircularQueue() {
        queue = (T[]) new Comparable[QUEUE_CAPACITY];
        //TO INDICATE QUEUE IS EMPTY.
        front = -1;
        rear = -1;
    }

    @Override
    public boolean enqueue(T newEntry) {
        if (isFull()) {                   //IF QUEUE IS FULL, EXPAND SIZE BEFORE ADDING NEW ENTRIES.
            resize();
        } else if (isEmpty()) {
            front += 1;                   //IF QUEUE IS EMPTY SET FRONT AS THE FIRST INDEX (-1) + 1.
        }

        rear = (rear + 1) % queue.length; //NEXT POSITION.

        queue[rear] = newEntry;
        numberOfEntries++;

        return true;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T temp = queue[front];                  //ASSIGN QUEUE'S FRONT TO TEMPORARY VARIABLE TO RETURN.
        queue[front] = null;
        if (front == rear) {                    //CHECK IF ONE ENTRY LEFT IN QUEUE.
            front = -1;
            rear = -1;
        } else {
            front = (front + 1) % queue.length; //NEXT POSITION.
        }
        numberOfEntries--;
        return temp;
    }

    @Override
    public T peek() {
        //GET FRONT DATA OF THE QUEUE, ACTS AS A PEEK FOR QUEUE.
        if (!isEmpty()) {
            return queue[front];
        }
        return null;
    }

    @Override
    public T getRear() {
        if (!isEmpty()) {
            return queue[rear];
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return front == -1;
    }

    @Override
    public boolean isFull() {
        //IF NEXT ENQUEUE POSITION == FRONT ; HENCE, FULL.
        return (rear + 1) % queue.length == front;
    }

    @Override
    public void clear() {
        int start = front;
        int end = (rear + 1) % queue.length;
        //DO WHILE : PREVENT START == END CONDITION INSTANTLY.
        do {
            queue[start] = null;
            start = (start + 1) % queue.length;
        } while (start != end);
        //RESET ALL IDENTIFIERS.
        front = -1;
        rear = -1;
    }

    public boolean resize() {
        //TEMP QUEUE TO BE ASSIGNED AS NEW QUEUE WITH DOUBLED CAPACITY.
        if (!isEmpty()) {
            tempQueue = (T[]) new Comparable[QUEUE_CAPACITY * 2];
            int index = 0;                                //TEMP QUEUE'S FRONT POSITION.
            int start = front;                            //ORIGIN QUEUE'S FRONT POSITION.
            int end = (rear + 1) % queue.length;          //ORIGIN QUEUE'S NEXT POSITION OF REAR.

            //DO WHILE : PREVENT START == END CONDITION INSTANTLY.
            do {
                tempQueue[index] = queue[start];
                start = (start + 1) % queue.length;
                index++;
            } while (start != end);

            //FIFO NATURE, THEREFORE, FRONT THROUGH REAR INDEXES ARE NATURALLY SORTED FOR THE RESIZED QUEUE.
            QUEUE_CAPACITY *= 2;
            front = 0;
            rear = queue.length - 1;
            queue = tempQueue;

            return true;
        }
        return false;

    }

    @Override
    public int size() {
        return queue.length;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public Iterator iterator() {
        return new iterator();
    }

    private class iterator<T> implements Iterator<T> {

        private int current = 0;

        public iterator() {
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < numberOfEntries;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T result = (T) queue[(front + current) % queue.length];
                current += 1;
                return result;
            }
            return null;
        }

    }

    @Override
    public String toString() {
        String queueStr = "";

        if (!isEmpty()) {
            //IF LOOP (START) HAS NOT REACH ITS END, CONTINUE. OTHERWISE, RETURN APPENDED STRING.
            int start = front;
            int end = (rear + 1) % queue.length;
            //DO WHILE : PREVENT START == END CONDITION INSTANTLY.
            do {
                if (queue[start] != null) {
                    queueStr += queue[start];
                }
                start = (start + 1) % queue.length;
            } while (start != end);
        }
        return queueStr;

    }
}
