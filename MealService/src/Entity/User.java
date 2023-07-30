package Entity;

import ADT.DoublyLinkedSet;
import ADT.SetInterface;
import java.io.Serializable;
import java.util.Iterator;

public class User implements Comparable<User>, Serializable {

    private static int userCount = 0;
    private int userId;
    private String userName;
    private String userTel;
    private SetInterface<Voucher> vouchers;

    public User(String userName, String userTel) {
        this.userId = userCount;
        this.userName = userName;
        this.userTel = userTel;
        this.vouchers = new DoublyLinkedSet<>();
        userCount++;
    }

    // Getter
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public static int getUserCount() {
        return userCount;
    }

    public Voucher get(String voucherCode) {
        Iterator iterator = vouchers.iterator();
        while (iterator.hasNext()) {
            Voucher currentVoucher = (Voucher) iterator.next();
            if (currentVoucher.getVoucherCode().equals(voucherCode)) {
                return currentVoucher;
            }
        }
        return null;
    }

    public SetInterface<Voucher> getVouchers() {
        return vouchers;
    }

    // Setter
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void SetUserName(String userName) {
        this.userName = userName;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public static void setUserCount(int userCount) {
        User.userCount = userCount;
    }

    // Method
    public boolean addVoucher(Voucher newVoucher) {
        return vouchers.add(newVoucher);
    }

    public void removeVoucher(Voucher aVoucher) {
        vouchers.remove(aVoucher);
    }

    public void unionVoucher(SetInterface<Voucher> newVoucherSet) {
        vouchers.union(newVoucherSet);
    }

    public void intersectVoucher(SetInterface<Voucher> newVoucherSet) {
        vouchers.intersection(newVoucherSet);
    }

    public void displayAllUser() {
        String str = "";
        str += String.format("| %2s | %-15s | %-12s |\n", userId, userName, userTel);
        System.out.print(str);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.userName.equals(userName)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "User ID : " + userId + ", User Name : " + userName + ", Phone Number : " + userTel + "\n";
    }

    @Override
    public int compareTo(User u) {
        if (this.userId > ((User) u).getUserId()) {
            return 1;
        }
        return 0;
    }
}
