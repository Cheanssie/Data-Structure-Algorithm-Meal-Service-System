package ADT;

import Entity.User;

public class SetClient {

    public static void main(String[] args) {
        SetInterface<User> userSet = new DoublyLinkedSet<>();

        SetInterface<User> userSet1 = new DoublyLinkedSet<>();
//        set.add("a");
//        System.out.println(set.add(1));
//        System.out.println(set.add(1));
        User u1 = new User("1", "aaa");
        User u2 = new User("55453", "aaa");
        User u3 = new User("2323", "aaa");
        User u4 = new User("ttt", "aaa");
        User u5 = new User("rrr", "aaa");

        userSet.add(u1);
        userSet.add(u2);
        userSet1.add(u3);
        userSet1.add(u4);
        userSet.add(u5);
        userSet.intersection(userSet1);
        System.out.println(userSet.intersection(userSet1));

    }
}
