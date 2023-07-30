
package ADT;

import java.util.Iterator;

public class QueueToListConverter<T extends Comparable<T>> {
    
    public ListInterface<T> convertToList(QueueInterface<T> queue){
        ListInterface<T> listConvert = new ArrayList();
        Iterator iterQueue = queue.iterator();
        while(iterQueue.hasNext()){
            T item = (T) iterQueue.next();
            listConvert.add(item);
        }
        return (ListInterface<T>) listConvert;
    }
}
