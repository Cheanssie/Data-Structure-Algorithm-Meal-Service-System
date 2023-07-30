package Entity;

import java.io.Serializable;
import java.util.Comparator;

public class OrderDetails implements Comparable<OrderDetails>, Serializable {

    //private static int allOrderID = 0;
    private int orderID;
    private int totalOrders = 0;
    private int packageQuantity;
    private float packagePrice;
    private String packageName;
    private String orderStatus;

    public OrderDetails(){
        
    }
    
    public OrderDetails(int mealQuantity, float mealPrice, String mealName, int orderID) {
        //this.orderID = ++allOrderID;
        this.orderID = orderID;
        this.packageQuantity = mealQuantity;
        this.packagePrice = (mealPrice * mealQuantity);
        this.packageName = mealName;
        this.orderStatus = "Pending";
        totalOrders++;
    }

    public OrderDetails(int mealQuantity, float mealPrice, String mealName, int orderID, boolean sameOrder) {
        this.orderID = orderID;
        this.packageQuantity = mealQuantity;
        this.packagePrice = (mealPrice * mealQuantity);
        this.packageName = mealName;
        this.orderStatus = "Pending";
    }

    public void setPackageQuantity(int mealQuantity) {
        this.packageQuantity = mealQuantity;
    }

    public void setPackagePrice(float mealPrice) {
        this.packagePrice = mealPrice;
    }

    public void setPackageName(String mealName) {
        this.packageName = mealName;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    
    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }
    
//    public void setAllOrderID(int allOrderID){
//        OrderDetails.allOrderID = allOrderID;
//    }

    public int getPackageQuantity() {
        return packageQuantity;
    }

    public float getPackagePrice() {
        return packagePrice;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public int getOrderID() {
        return orderID;
    }
    
    public String getOrderStatus(){
        return orderStatus;
    }
    
//    public int getAllOrderID(){
//        return allOrderID;
//    }

    @Override
    public String toString() {
        return String.format("|%2s%-15s %-30s %-30s %-20.2f %-18s|\n", "", orderID, packageName, packageQuantity, packagePrice, orderStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        if(obj instanceof String){
            return obj.equals(this.getOrderStatus());
        }
        
        OrderDetails tempOrder = (OrderDetails) obj;

        if (tempOrder.getPackageName().equals(this.getPackageName())) {
            return true;
        }
        return false;
    }
    
    public static Comparator<OrderDetails> packNameComparatorASC = (OrderDetails o1, OrderDetails o2) -> {
        String packName1 = o1.getPackageName().toUpperCase();
        String packName2 = o2.getPackageName().toUpperCase();
        
        return packName1.compareTo(packName2);
    };
    
    public static Comparator<OrderDetails> packPriceComparatorASC = (OrderDetails o1, OrderDetails o2) -> {
        float packName1 = o1.getPackagePrice();
        float packName2 = o2.getPackagePrice();
        
        return Float.compare(packName1, packName2);
    };
    
    public static Comparator<OrderDetails> packQuantityComparatorASC = (OrderDetails o1, OrderDetails o2) -> {
        int packName1 = o1.getPackageQuantity();
        int packName2 = o2.getPackageQuantity();
        
        return packName1-packName2;
    };
    
    public static Comparator<OrderDetails> packNameComparatorDES = (OrderDetails o1, OrderDetails o2) -> {
        String packName1 = o1.getPackageName().toUpperCase();
        String packName2 = o2.getPackageName().toUpperCase();
        
        return packName2.compareTo(packName1);
    };
    
    public static Comparator<OrderDetails> packPriceComparatorDES = (OrderDetails o1, OrderDetails o2) -> {
        float packName1 = o1.getPackagePrice();
        float packName2 = o2.getPackagePrice();
        
        return Float.compare(packName2, packName1);
    };
    
    public static Comparator<OrderDetails> packQuantityComparatorDES = (OrderDetails o1, OrderDetails o2) -> {
        int packName1 = o1.getPackageQuantity();
        int packName2 = o2.getPackageQuantity();
        
        return packName2-packName1;
    };

    @Override
    public int compareTo(OrderDetails o) {
        return this.packageName.compareTo(o.getPackageName());
    }
}
