package Entity;

import ADT.*;
import java.io.Serializable;
import java.util.Iterator;

public class Order implements Comparable<OrderDetails>, Serializable {

    private static int orderQueueID = 0;
    private QueueInterface<OrderDetails> queue;

    public Order() {
        this.queue = new CircularQueue<>();
    }
    
    public int getOrderQueueID(){
        return orderQueueID;
    }
    
    public void setOrderQueueID(int newOrderQueueID){
        Order.orderQueueID = newOrderQueueID;
    }

    public boolean addOrder(OrderDetails newOrder) {
        return queue.enqueue(newOrder);
    }

    public OrderDetails completeOrder() {
        return queue.dequeue();
    }

    public OrderDetails getOrder() {
        return queue.peek();
    }

    public void clearOrders() {
        queue.clear();
    }

    @Override
    public String toString() {
        return queue.toString();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public int getNumberOfEntries() {
        return queue.getNumberOfEntries();
    }
    
    public Iterator iterator(){
        return queue.iterator();
    }
    
    @Override
    public int compareTo(OrderDetails o) {
        return this.getOrder().getPackageName().compareTo(o.getPackageName());
    }

}
