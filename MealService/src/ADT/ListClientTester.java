package ADT;

/*
 * Title : List Client Tester
 * Purpose : To test each methods in List Implementer
 */
import java.util.Iterator;
import java.util.*;

public class ListClientTester {

    public static void main(String[] args) {
        //testAppend();
        //testAdd();
        //testRemove();
        //testReplace();
        //testGet();
        //testClear();
        //testGetNumberOfEntries();
        //testIterator();
    }

    //Unit Test for each method
    public static void testAdd() {
        ListInterface ls = new LinkedList();
        ls.add("haha");
        ls.add("byebye");
        System.out.println("Add at the End of List : " + ls);
        ls.add("evening", 1);
        ls.add("night", 2);
        System.out.println("Add at Specified Position : " + ls);
    }

    public static void testGet() {
        ListInterface ls = new LinkedList();
        ls.add("haha");
        ls.add("byebye");
        ls.add("evening");
        System.out.println("List : " + ls);
        System.out.println("Get at Specified Position(2) : " + ls.get(2));
    }

    public static void testRemove() {
        ListInterface ls = new LinkedList();
        ls.add("haha");
        ls.add("gg");
        System.out.println("Before Removing : " + ls);
        //ls.remove(1);
        //System.out.println(ls.remove(3));
        //System.out.println(ls.remove(1));
        ls.add("lol", 1);
        ls.add("a", 2);
        System.out.println("After Removing : " + ls);
    }

    public static void testReplace() {
        ListInterface ls = new LinkedList();
        ls.add("haha");
        ls.add("byebye", 1);
        ls.add("Evening");
        System.out.println("Before Replacing : " + ls);
        ls.replace("Morning", 1);
        System.out.println(ls.replace("Morning", 3));
        System.out.println(ls.replace("Morning", 4));
        System.out.println("After Replacing : " + ls);
    }

    public static void testClear() {
        ListInterface ls = new LinkedList();
        ls.add("haha");
        ls.add("byebye");
        ls.add("evening");
        System.out.println("List : " + ls);
        ls.clear();
        System.out.println("After Clear : " + ls);
    }

    public static void testGetNumberOfEntries() {
        ListInterface ls = new LinkedList();
        ls.add("haha");
        ls.add("byebye");
        ls.add("evening");
        System.out.println("List : " + ls);
        System.out.println("Number of Entries : " + ls.getNumberOfEntries());
    }

    public static void testIterator() {
        ListInterface ls = new LinkedList();
//        ls.add("haha");
        ls.add("byebye");
//        ls.add("evening", 3);

        Iterator iterateTester = ls.iterator();

        System.out.println(iterateTester.hasNext());

        System.out.println(iterateTester.next());

        System.out.println(iterateTester.hasNext());

        //System.out.println(iterateTester.next());
    }

    public static void testContains() {
        ListInterface ls = new LinkedList();

        ls.add(10);
        ls.add(100);
        ls.add(1);
        ls.add(0);
        if (ls.contains(100)) {
            System.out.println(ls);
        }
    }

    public static void testAppend() {
        ListInterface ls = new LinkedList();
        ListInterface ls2 = new LinkedList();

//        ls.add(10);
//        ls.add(100);
        ls2.add(1);
        ls2.add(0);

        ls.append(ls2);
        ls2.append(ls);
        System.out.println(ls);
    }
}
