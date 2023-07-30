package SubClient;

import ADT.*;
import Entity.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import SortingAlgo.OrderSort;

public class OrderManager {

    // CONSTANTS FOR INPUTS
    private static final int DEFAULT_CHOICE = -1;
    static Scanner input = new Scanner(System.in);

    // ADTS
    public static Order orderQueue = new Order();
    public static QueueInterface<OrderDetails> removedOrders = new CircularQueue();
    public static QueueInterface<OrderDetails> orderHistory = new CircularQueue();
    public static ListInterface<MealPackage> packageMeal = new LinkedList<>();
    int orderCount = 0;

    // FILES
    private static final String ORDER_FILENAME = "orderHistory.txt";
    private static final String PACKAGE_FILENAME = "packages.txt";
    public static File file = null;

    // CONSTANTS FOR COLORS
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESETCOLOR = "\u001B[0m";

    // CONSTANTS FOR ORDER STATUS
    public static final String PENDING = "Pending";
    public static final String REMOVED = "Removed";
    public static final String COMPLETED = "Completed";

    public OrderManager() {
        readData();
        if(!orderHistory.isEmpty()){
            orderCount = orderHistory.getRear().getOrderID();
            orderCount++;
            orderQueue.setOrderQueueID(orderCount);
        }else{
            orderQueue.setOrderQueueID(orderCount++);
        }
    }

    public void mainMethod() {
        int choice = DEFAULT_CHOICE;
        do {
            choice = orderMainMenu();
            switch (choice) {
                case 1:
                    addOrder();
                    break;
                case 2:
                    completeOrder();
                    break;
                case 3:
                    removeOrder();
                    break;
                case 4:
                    getFrontOrder();
                    break;
                case 5:
                    getOrderList();
                    break;
                case 6:
                    orderReport();
                    break;
            }
        } while (choice != 7);
    }

    public int orderMainMenu() {
        int choice = DEFAULT_CHOICE;
        boolean errorInput;
        boolean proceed = true;
        char confirm;
        int currentOrderID;
        System.out.printf("\n\n%s\n", "+ ========== + ==============================================+");
        System.out.printf("%s %35s\n", "| No.        |   Functions", "|");
        System.out.printf("%s\n", "+ ========== + ==============================================+");
        System.out.printf("%s %35s\n", "| 1_         |   Add Order", "|");
        System.out.printf("%s %30s\n", "| 2_         |   Complete Order", "|");
        System.out.printf("%s %32s\n", "| 3_         |   Remove Order", "|");
        System.out.printf("%s %27s\n", "| 4_         |   Get Current Order", "|");
        System.out.printf("%s %30s\n", "| 5_         |   Get Order List", "|");
        System.out.printf("%s %32s\n", "| 6_         |   Order Report", "|");
        System.out.printf("%s %38s\n", "| 7_         |   RETURN", "|");
        System.out.printf("%s\n", "+ ========== + ==============================================+");

        do {
            errorInput = false;
            System.out.printf("%-20s >>> ", "Enter Choice [1-7]");
            try {
                choice = input.nextInt();
                if (choice < 1 || choice > 7) {
                    errorInput = true;
                    System.out.println(RED + "Please enter a valid choice! [1-7]" + RESETCOLOR);
                }
            } catch (InputMismatchException ex) {
                errorInput = true;
                System.out.println(RED + "Please enter a valid choice! [1-7]" + RESETCOLOR);
            } finally {
                input.nextLine();
            }
        } while (errorInput);

        return choice;
    }

    public void addOrder() {
        int currentOrderID = orderCount;
        boolean sameOrder = false;
        int choice = DEFAULT_CHOICE;
        boolean errorInput;
        boolean proceed = true;
        boolean cancelledOrder = false;
        char confirm;

        do {
            MealPackage indexMeal = null;
            proceed = true;
            errorInput = false;

            try {
                PackageManager packetManager = new PackageManager();
                System.out.println(packetManager.packagesString());
                do {
                    errorInput = false;
                    System.out.printf("%-45s >>> ", "Enter Meal Number   : [-1 to return] ");
                    int selectedMeal = input.nextInt();
                    indexMeal = packageMeal.get(selectedMeal);

                    if (selectedMeal == -1) {
                        proceed = false;
                        cancelledOrder = true;
                    } else {
                        if (selectedMeal < -1 || indexMeal == null) {
                            errorInput = true;
                            System.out.println(RED + "Meal does not exist, please try again." + RESETCOLOR);
                        }
                    }
                } while (errorInput);

                if (proceed) {
                    do {
                        try {
                            errorInput = false;
                            System.out.printf("%-45s >>> ", "Enter Meal Quantity : [-1 to return]");
                            int mealQuantity = input.nextInt();
                            if (mealQuantity != -1) {
                                if (mealQuantity < -1) {
                                    errorInput = true;
                                    System.out.println(RED + "Ordered meal must be more than 1!" + RESETCOLOR);
                                } else {
                                    do {
                                        errorInput = false;
                                        System.out.printf("%-45s >>> ", "Confirm ? (Y/y = Yes, N/n = No)");
                                        confirm = input.next().charAt(0);
                                        switch (confirm) {
                                            case 'Y':
                                            case 'y':
                                                OrderDetails orderedMeal;
                                                if (sameOrder) {
                                                    orderedMeal = new OrderDetails(mealQuantity, indexMeal.getPackagePrice(), indexMeal.getPackageName(), currentOrderID);
                                                } else {
                                                    orderQueue.setOrderQueueID(orderCount++);
                                                    currentOrderID = orderQueue.getOrderQueueID();
                                                    orderedMeal = new OrderDetails(mealQuantity, indexMeal.getPackagePrice(), indexMeal.getPackageName(), currentOrderID, sameOrder);
                                                }

                                                // CHECK IF MEAL EXISTS, IF EXISTS, ADD TO MEAL QUANTITY.
                                                // CHECK FOR ID TO RETRIEVE ORDERS
                                                boolean mealExisted = false;
                                                if (!orderQueue.isEmpty()) {
                                                    mealExisted = false;

                                                    Iterator iterOrder = orderQueue.iterator();
                                                    while (iterOrder.hasNext()) {
                                                        OrderDetails item = (OrderDetails) iterOrder.next();
                                                        if (orderedMeal.getOrderID() == item.getOrderID()) {
                                                            if (orderedMeal.equals(item)) {
                                                                int newQuantity = item.getPackageQuantity() + orderedMeal.getPackageQuantity();
                                                                item.setPackageQuantity(newQuantity);
                                                                mealExisted = true;
                                                            }
                                                        }

                                                    }
                                                } else {
                                                    mealExisted = false;
                                                }

                                                if (!mealExisted) {
                                                    orderQueue.addOrder(orderedMeal);
                                                }

                                                System.out.println(GREEN + "Order added." + RESETCOLOR);
                                                System.out.printf("%s\n|%2s%-15s %-30s %-30s %-20s %-18s|\n%s\n|%2s%-15s %-30s %-30s %-20.2f %-18s|\n%s\n",
                                                        "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +",
                                                        "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status",
                                                        "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +",
                                                        "", orderedMeal.getOrderID(), orderedMeal.getPackageName(), orderedMeal.getPackageQuantity(), orderedMeal.getPackagePrice(), orderedMeal.getOrderStatus(),
                                                        "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                                break;
                                            case 'N':
                                            case 'n':
                                                System.out.println(RED + "Order is not added" + RESETCOLOR);
                                                break;
                                            default:
                                                errorInput = true;
                                                System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                                        }
                                    } while (errorInput);
                                }
                            } else {
                                proceed = false;
                                cancelledOrder = true;
                            }
                        } catch (InputMismatchException ex) {
                            errorInput = true;
                            System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                        } finally {
                            input.nextLine();
                        }
                    } while (errorInput);
                }
                if (!cancelledOrder) {
                    do {
                        errorInput = false;
                        System.out.printf("%-45s >>> ", "Continue Ordering? (Y/y = Yes, N/n = No)");
                        confirm = input.next().charAt(0);
                        switch (confirm) {
                            case 'Y':
                            case 'y':
                                sameOrder = true;
                                proceed = true;
                                break;
                            case 'N':
                            case 'n':
                                sameOrder = false;
                                proceed = false;
                                break;
                            default:
                                errorInput = true;
                                System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                        }
                    } while (errorInput);
                }
            } catch (InputMismatchException ex) {
                errorInput = true;
                System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
            } finally {
                input.nextLine();
            }
        } while (errorInput || proceed);
    }

    public void completeOrder() {
        int currentOrderID = 0;
        char confirm;
        int choice = DEFAULT_CHOICE;
        boolean errorInput;
        boolean proceed = true;
        OrderDetails tempOrder;
        boolean hasPending = false;

        Iterator iterPending = orderQueue.iterator();
        if (!orderQueue.isEmpty()) {
            while (iterPending.hasNext()) {
                OrderDetails item = (OrderDetails) iterPending.next();
                if (item.getOrderStatus().equals(PENDING)) {
                    hasPending = true;
                    break;
                }
            }
            if (hasPending) {
                do {
                    errorInput = false;
                    boolean orderCompleted = false;

                    System.out.println("\nNext order to complete : \n");

                    System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                    System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                    System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");

                    Iterator iterGetCurrentID = orderQueue.iterator();
                    while (iterGetCurrentID.hasNext()) {
                        OrderDetails item = (OrderDetails) iterGetCurrentID.next();
                        if (!item.getOrderStatus().equals(REMOVED)) {
                            currentOrderID = item.getOrderID();
                            break;
                        }
                    }
                    Iterator iterOrder = orderQueue.iterator();
                    while (iterOrder.hasNext()) {
                        OrderDetails item = (OrderDetails) iterOrder.next();
                        if (item.getOrderID() == currentOrderID) {
                            if (!item.getOrderStatus().equals(REMOVED)) {
                                System.out.print(item);
                            }
                        }
                    }
                    System.out.println("+-----------------------------------------------------------------------------------------------------------------------+");
                    System.out.printf("%-45s >>> ", "Confirm to complete the order ? (Y/y = yes, N/n = no)");
                    confirm = input.next().charAt(0);
                    switch (confirm) {
                        case 'Y':
                        case 'y':
                            do {
                                if (orderQueue.getOrder().getOrderStatus().equals(REMOVED)) {
                                    orderQueue.completeOrder();
                                } else {
                                    tempOrder = orderQueue.completeOrder();
                                    tempOrder.setOrderStatus(COMPLETED);
                                    orderHistory.enqueue(tempOrder);
                                }
                            } while (orderQueue.getOrder() != null && orderQueue.getOrder().getOrderID() == currentOrderID);

                            orderCompleted = true;

                            System.out.println(GREEN + "The order has been completed." + RESETCOLOR);
                            break;
                        case 'N':
                        case 'n':
                            orderCompleted = false;
                            System.out.println(RED + "The order has not been completed" + RESETCOLOR);
                            break;
                        default:
                            errorInput = true;
                            System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                    }
                } while (errorInput);
            } else {
                System.out.println(RED + "There are currently no orders!" + RESETCOLOR + "\n");
            }
        } else {
            System.out.println(RED + "There are currently no orders!" + RESETCOLOR + "\n");
        }
        writeData();
    }

    public void removeOrder() {
        char confirm;
        int choice = DEFAULT_CHOICE;
        boolean errorInput;

        if (orderQueue.isEmpty()) {
            System.out.println(RED + "There are currently no orders!" + RESETCOLOR);
        } else {
            do {
                errorInput = false;
                System.out.printf("\n\n%s\n", "+ ========== + ==============================================+");
                System.out.printf("%s %29s\n", "| No.        |   REMOVE CRITERIA", "|");
                System.out.printf("%s\n", "+ ========== + ==============================================+");
                System.out.printf("%s %27s\n", "| 1_         |   Remove Order (ID)", "|");
                System.out.printf("%s %25s\n", "| 2_         |   Remove Package (ID)", "|");
                System.out.printf("%s %38s\n", "| 3_         |   Return", "|");
                System.out.printf("%s\n", "+ ========== + ==============================================+");

                do {
                    errorInput = false;
                    System.out.printf("%-20s >>> ", "Enter Choice [1-3]");
                    try {
                        choice = input.nextInt();
                        if (choice < 1 || choice > 3) {
                            errorInput = true;
                            System.out.println(RED + "Please enter a valid choice! [1-3]" + RESETCOLOR);
                        }
                    } catch (InputMismatchException ex) {
                        errorInput = true;
                        System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                    } finally {
                        input.nextLine();
                    }
                } while (errorInput);

                switch (choice) {
                    case 1:
                        int numberOfRemoveID = 0;
                        if (orderQueue.isEmpty()) {
                            System.out.println(RED + "There are currently no orders!" + RESETCOLOR);
                        } else {

                            int removeOrderID = 0;
                            System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                            System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                            System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                            Iterator iterChkRemoved = orderQueue.iterator();
                            boolean hasOrder = false;
                            while (iterChkRemoved.hasNext()) {
                                OrderDetails item = (OrderDetails) iterChkRemoved.next();
                                if (!item.getOrderStatus().equals(REMOVED)) {
                                    hasOrder = true;
                                    System.out.print(item);
                                }
                            }
                            if (!hasOrder) {
                                System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|\n", "", "???", "???", "???", "???", "???");
                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                System.out.println(RED + "There are currently no orders!" + RESETCOLOR);
                            }

                            if (hasOrder) {
                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                do {
                                    errorInput = false;
                                    try {
                                        System.out.printf("%-20s >>> ", "Select order ID to remove [Ex. 1]");
                                        removeOrderID = input.nextInt();
                                        if (removeOrderID < 1) {
                                            errorInput = true;
                                            System.out.println(RED + "Order does not exist, please try again." + RESETCOLOR);
                                        } else {
                                            numberOfRemoveID = 0;

                                            Iterator iterCheckRemoveID = orderQueue.iterator();
                                            while (iterCheckRemoveID.hasNext()) {
                                                OrderDetails item = (OrderDetails) iterCheckRemoveID.next();
                                                if (item.getOrderID() == removeOrderID) {
                                                    if (item.getOrderStatus().equals(REMOVED)) {
                                                        numberOfRemoveID++;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (numberOfRemoveID < 0) {
                                                errorInput = true;
                                                System.out.println(RED + "Order does not exist, please try again." + RESETCOLOR);
                                            }
                                        }
                                    } catch (InputMismatchException ex) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                                    } finally {
                                        input.nextLine();
                                    }
                                } while (errorInput);
                                System.out.println("\nORDER TO BE REMOVED : ");
                                System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                                System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");

                                Iterator iterOrder = orderQueue.iterator();
                                while (iterOrder.hasNext()) {
                                    OrderDetails item = (OrderDetails) iterOrder.next();
                                    if (item.getOrderID() == removeOrderID) {
                                        System.out.print(item);
                                    }
                                }

                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                do {
                                    errorInput = false;
                                    boolean orderRemoved = false;
                                    System.out.printf("%-45s >>> ", "Confirm to remove the order ? (Y/y = yes, N/n = no)");
                                    confirm = input.next().charAt(0);
                                    switch (confirm) {
                                        case 'Y':
                                        case 'y':

                                            Iterator iterRemove = orderQueue.iterator();
                                            while (iterRemove.hasNext()) {
                                                OrderDetails item = (OrderDetails) iterRemove.next();
                                                if (item.getOrderID() == removeOrderID) {
                                                    item.setOrderStatus(REMOVED);
                                                }
                                            }

                                            orderRemoved = true;
                                            System.out.println(GREEN + "The order has been removed." + RESETCOLOR);
                                            break;
                                        case 'N':
                                        case 'n':
                                            orderRemoved = false;
                                            System.out.println(RED + "The order has not been removed" + RESETCOLOR);
                                            break;
                                        default:
                                            errorInput = true;
                                            System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                                    }
                                } while (errorInput);
                            }
                        }
                        break;
                    case 2:
                        if (orderQueue.isEmpty()) {
                            System.out.println(RED + "There are currently no orders!" + RESETCOLOR);
                        } else {
                            int removeOrderID = 0;

                            System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                            System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                            System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                            Iterator iterOrder = orderQueue.iterator();
                            boolean hasOrder = false;
                            while (iterOrder.hasNext()) {
                                OrderDetails item = (OrderDetails) iterOrder.next();
                                if (!item.getOrderStatus().equals(REMOVED)) {
                                    hasOrder = true;
                                    System.out.print(item);
                                }
                            }
                            if (!hasOrder) {
                                System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|\n", "", "???", "???", "???", "???", "???");
                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                System.out.println(RED + "There are currently no orders!" + RESETCOLOR);
                            }
                            if (hasOrder) {

                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                do {
                                    errorInput = false;
                                    try {
                                        System.out.printf("%-20s >>> ", "Select order ID to remove [Ex. 1]");
                                        removeOrderID = input.nextInt();
                                        if (removeOrderID < 1) {
                                            errorInput = true;
                                            System.out.println(RED + "Order does not exist, please try again." + RESETCOLOR);
                                        } else {

                                            numberOfRemoveID = 0;

                                            Iterator iterCheckRemoveID = orderQueue.iterator();
                                            while (iterCheckRemoveID.hasNext()) {
                                                OrderDetails item = (OrderDetails) iterCheckRemoveID.next();
                                                if (item.getOrderID() == removeOrderID) {
                                                    if (!item.getOrderStatus().equals(REMOVED)) {
                                                        numberOfRemoveID++;
                                                    }
                                                }
                                            }
                                            if (numberOfRemoveID < 0) {
                                                errorInput = true;
                                                System.out.println(RED + "Order does not exist, please try again." + RESETCOLOR);
                                            }
                                        }
                                    } catch (InputMismatchException ex) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                                    } finally {
                                        input.nextLine();
                                    }
                                } while (errorInput);

                                int numberOfMeals = 0;
                                System.out.printf("\n%s\n", "+ -+-+- + + -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                System.out.printf("|  %-5s|%2s%-15s %-30s %-30s %-20s %-18s|", "No.", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                                System.out.printf("\n%s\n", "+ -+-+- + + -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                Iterator iterNumberMeals = orderQueue.iterator();
                                while (iterNumberMeals.hasNext()) {
                                    OrderDetails item = (OrderDetails) iterNumberMeals.next();
                                    if (item.getOrderID() == removeOrderID) {
                                        numberOfMeals++;
                                        System.out.printf("| %2d    ", numberOfMeals);
                                        System.out.print(item);
                                    }
                                }

                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                int removeMeal = 0;
                                do {
                                    errorInput = false;
                                    try {
                                        System.out.printf("%-20s >>> ", "Select meal number to remove [Ex. 1]");
                                        removeMeal = input.nextInt();
                                        if (removeMeal > numberOfMeals || removeMeal < 1) {
                                            errorInput = true;
                                            System.out.println(RED + "Meal does not exist, please try again." + RESETCOLOR);
                                        }
                                    } catch (InputMismatchException ex) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                                    } finally {
                                        input.nextLine();
                                    }
                                } while (errorInput);

                                int foundMeal = 0;
                                Iterator iterMealRemove = orderQueue.iterator();
                                while (iterMealRemove.hasNext()) {
                                    OrderDetails item = (OrderDetails) iterMealRemove.next();
                                    if (item.getOrderID() == removeOrderID) {
                                        foundMeal++;
                                        if (foundMeal == removeMeal) {
                                            item.setOrderStatus(REMOVED);
                                        }
                                    }
                                }

                                System.out.println(GREEN + "The order has been removed." + RESETCOLOR);
                            }
                        }
                        break;
                }
            } while (choice != 3);
        }
    }

    public void getFrontOrder() {
        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
        System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");

        if (orderQueue.isEmpty()) {
            System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|\n", "", "???", "???", "???", "???", "???");
        } else {
            Iterator iterFrontOrder = orderQueue.iterator();
            while (iterFrontOrder.hasNext()) {
                OrderDetails item = (OrderDetails) iterFrontOrder.next();
                if (!item.getOrderStatus().equals(REMOVED)) {
                    System.out.print(item);
                    break;
                }
            }
        }
        float totalPrice = (orderQueue.getOrder() == null ? 0 : getTotalPrice(orderQueue.getOrder().getOrderID()));
        System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
        System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Total Price : ", totalPrice);
        System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
    }

    public void getOrderList() {
        boolean ordersFound = false;
        boolean errorInput = false;
        int choice = DEFAULT_CHOICE;
        do {
            float totalPrice = 0;
            ordersFound = false;
            errorInput = false;
            choice = DEFAULT_CHOICE;
            System.out.printf("\n\n%s\n", "+ ========== + ==============================================+");
            System.out.printf("%s %34s\n", "| No.        |   Order List", "|");
            System.out.printf("%s\n", "+ ========== + ==============================================+");
            System.out.printf("%s %26s\n", "| 1_         |   Display All Orders", "|");
            System.out.printf("%s %24s\n", "| 2_         |   Display Only Pending", "|");
            System.out.printf("%s %24s\n", "| 3_         |   Display Only Removed", "|");
            System.out.printf("%s %38s\n", "| 4_         |   Return", "|");
            System.out.printf("%s\n", "+ ========== + ==============================================+");
            do {
                errorInput = false;
                System.out.printf("%-20s >>> ", "Enter Choice [1-4]");
                try {
                    choice = input.nextInt();
                    if (choice < 1 || choice > 4) {
                        errorInput = true;
                        System.out.println(RED + "Please enter a valid choice! [1-4]" + RESETCOLOR);
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                } finally {
                    input.nextLine();
                }
            } while (errorInput);
            System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
            System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
            System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
            if (!orderQueue.isEmpty()) {
                switch (choice) {
                    case 1:
                        ordersFound = true;
                        totalPrice = getTotalPrice();
                        System.out.print(orderQueue);
                        break;
                    case 2:
                        totalPrice = 0;
                        Iterator iterPendingOrder = orderQueue.iterator();
                        while (iterPendingOrder.hasNext()) {
                            OrderDetails item = (OrderDetails) iterPendingOrder.next();
                            if (item.getOrderStatus().equals(PENDING)) {
                                ordersFound = true;
                                totalPrice += (item.getPackagePrice() * item.getPackageQuantity());
                                System.out.print(item);
                            }
                        }
                        break;
                    case 3:
                        Iterator iterRemovedOrder = orderQueue.iterator();
                        while (iterRemovedOrder.hasNext()) {
                            OrderDetails item = (OrderDetails) iterRemovedOrder.next();
                            if (item.getOrderStatus().equals(REMOVED)) {
                                ordersFound = true;
                                totalPrice += (item.getPackagePrice() * item.getPackageQuantity());
                                System.out.print(item);
                            }
                        }
                        break;
                }
            }
            if (!ordersFound) {
                System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|\n", "", "???", "???", "???", "???", "???");
            }
            System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
            System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Total Price : ", totalPrice);
            System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
        } while (choice != 4);
    }

    public void orderReport() {
        //ORDER REPORT
        /*
                    1) RETRIEVE FROM FILE
                    2) CONTENTS ARE ALL DEQUEUED/COMPLETED ORDERS
                    3) ORDER ID, FOOD NAME, QUANITY, PRICE
         */
        boolean errorInput = false;
        int choice = DEFAULT_CHOICE;
        do {
            readData();
            errorInput = false;
            choice = DEFAULT_CHOICE;
            System.out.printf("\n\n%s\n", "+ ========== + ==============================================+");
            System.out.printf("%s %32s\n", "| No.        |   Order Report", "|");
            System.out.printf("%s\n", "+ ========== + ==============================================+");
            System.out.printf("%s %23s\n", "| 1_         |   Display Order History", "|");
            System.out.printf("%s %26s\n", "| 2_         |   Sort Order History", "|");
            System.out.printf("%s %38s\n", "| 3_         |   Return", "|");
            System.out.printf("%s\n", "+ ========== + ==============================================+");

            do {
                errorInput = false;
                System.out.printf("%-20s >>> ", "Enter Choice [1-3]");
                try {
                    choice = input.nextInt();
                    if (choice < 1 || choice > 3) {
                        errorInput = true;
                        System.out.println(RED + "Please enter a valid choice! [1-3]" + RESETCOLOR);
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                } finally {
                    input.nextLine();
                }
            } while (errorInput);

            switch (choice) {
                case 1:
                    //readData();
                    float totalPrice = 0;
                    if (!orderHistory.isEmpty()) {
                        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.print(orderHistory);

                        Iterator iterOrderHistory = orderHistory.iterator();
                        while (iterOrderHistory.hasNext()) {
                            OrderDetails item = (OrderDetails) iterOrderHistory.next();
                            if (item != null) {
                                totalPrice += item.getPackagePrice() * item.getPackageQuantity();
                            }
                        }

                        System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Total Price : ", totalPrice);
                        System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");

                    } else {
                        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|\n", "", "???", "???", "???", "???", "???");
                        System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Total Price : ", totalPrice);
                        System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                    }
                    break;
                case 2:
                    if (!orderHistory.isEmpty()) {
                        do {
                            choice = DEFAULT_CHOICE;
                            readData();
                            Iterator iterOrderHistory = orderHistory.iterator();
                            boolean found;
                            float totalPriceSold = 0;
                            int mostSold;
                            int mostSoldIndex;
                            totalPrice = 0;

                            if (!orderHistory.isEmpty()) {
                                QueueToListConverter converter = new QueueToListConverter();
                                ListInterface<OrderDetails> listHistory = (ListInterface<OrderDetails>) converter.convertToList(orderHistory);
                                ListInterface<OrderDetails> listOfPackages = new ArrayList();

                                choice = DEFAULT_CHOICE;
                                errorInput = false;
                                System.out.printf("\n\n%s\n", "+ ========== + ==============================================+");
                                System.out.printf("%s %31s\n", "| No.        |   Report Filter", "|");
                                System.out.printf("%s\n", "+ ========== + ==============================================+");
                                System.out.printf("%s %31s\n", "| 1_         |   Sort Packages", "|");
                                System.out.printf("%s %24s\n", "| 2_         |   Find By Best Sellers", "|");
                                System.out.printf("%s %24s\n", "| 3_         |   Find By Most Revenue", "|");
                                System.out.printf("%s %38s\n", "| 4_         |   Return", "|");
                                System.out.printf("%s\n", "+ ========== + ==============================================+");

                                do {
                                    errorInput = false;
                                    System.out.printf("%-20s >>> ", "Enter Choice [1-4]");
                                    try {
                                        choice = input.nextInt();
                                        if (choice < 1 || choice > 4) {
                                            errorInput = true;
                                            System.out.println(RED + "Please enter a valid choice! [1-4]" + RESETCOLOR);
                                        }
                                    } catch (InputMismatchException ex) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                                    } finally {
                                        input.nextLine();
                                    }
                                } while (errorInput);

                                if (choice != 4 && choice != 1) {
                                    System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                    System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                                    System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                }
                                switch (choice) {
                                    case 1:
                                        OrderDetails[] arraySort = new OrderDetails[listHistory.getNumberOfEntries()];
                                        for (int i = 0; i < listHistory.getNumberOfEntries(); i++) {
                                            arraySort[i] = listHistory.get(i);
                                        }
                                        do {
                                            totalPrice = 0;
                                            System.out.printf("\n\n%s\n", "+ ========== + ==============================================+");
                                            System.out.printf("%s %31s\n", "| No.        |   Report Filter", "|");
                                            System.out.printf("%s\n", "+ ========== + ==============================================+");
                                            System.out.printf("%s %21s\n", "| 1_         |   Sort Package Name (DES)", "|");
                                            System.out.printf("%s %20s\n", "| 2_         |   Sort Package Price (DES)", "|");
                                            System.out.printf("%s %17s\n", "| 3_         |   Find Package Quantity (DES)", "|");
                                            System.out.printf("%s %21s\n", "| 4_         |   Sort Package Name (ASC)", "|");
                                            System.out.printf("%s %20s\n", "| 5_         |   Sort Package Price (ASC)", "|");
                                            System.out.printf("%s %17s\n", "| 6_         |   Find Package Quantity (ASC)", "|");
                                            System.out.printf("%s %38s\n", "| 7_         |   Return", "|");
                                            System.out.printf("%s\n", "+ ========== + ==============================================+");
                                            do {
                                                errorInput = false;
                                                System.out.printf("%-20s >>> ", "Enter Choice [1-4]");
                                                try {
                                                    choice = input.nextInt();
                                                    if (choice < 1 || choice > 7) {
                                                        errorInput = true;
                                                        System.out.println(RED + "Please enter a valid choice! [1-4]" + RESETCOLOR);
                                                    }
                                                } catch (InputMismatchException ex) {
                                                    errorInput = true;
                                                    System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                                                } finally {
                                                    input.nextLine();
                                                }
                                            } while (errorInput);

                                            switch (choice) {
                                                case 1:
                                                    OrderSort.sort(arraySort, OrderDetails.packNameComparatorDES);
                                                    break;
                                                case 2:
                                                    OrderSort.sort(arraySort, OrderDetails.packPriceComparatorDES);
                                                    break;
                                                case 3:
                                                    OrderSort.sort(arraySort, OrderDetails.packQuantityComparatorDES);
                                                    break;
                                                case 4:
                                                    OrderSort.sort(arraySort, OrderDetails.packNameComparatorASC);
                                                    break;
                                                case 5:
                                                    OrderSort.sort(arraySort, OrderDetails.packPriceComparatorASC);
                                                    break;
                                                case 6:
                                                    OrderSort.sort(arraySort, OrderDetails.packQuantityComparatorASC);
                                                    break;
                                            }
                                            if (choice != 7) {
                                                System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                                System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                                                System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                                for (OrderDetails arrItem : arraySort) {
                                                    totalPrice += arrItem.getPackagePrice();
                                                    System.out.print(arrItem);
                                                }

                                                System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                                System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Total Price : ", totalPrice);
                                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                            }
                                            
                                        } while (choice != 7);
                                            break;
                                    case 2:
                                        while (iterOrderHistory.hasNext()) {
                                            found = false;
                                            OrderDetails foundPackage = (OrderDetails) iterOrderHistory.next();
                                            for (int j = 0; j < listOfPackages.getNumberOfEntries(); j++) {
                                                if (listOfPackages.get(j).equals(foundPackage)) {
                                                    float bfrPackPrice = listOfPackages.get(j).getPackagePrice();
                                                    float afterPackPrice = bfrPackPrice + foundPackage.getPackagePrice();
                                                    listOfPackages.get(j).setPackagePrice(afterPackPrice);
                                                    int bfrPackQty = listOfPackages.get(j).getPackageQuantity();
                                                    int afterPackQty = bfrPackQty + foundPackage.getPackageQuantity();
                                                    listOfPackages.get(j).setPackageQuantity(afterPackQty);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                listOfPackages.add(foundPackage);
                                            }
                                        }

                                        mostSold = 0;
                                        mostSoldIndex = 0;
                                        totalPriceSold = 0;
                                        for (int i = 0; i < listOfPackages.getNumberOfEntries(); i++) {
                                            if (listOfPackages.get(i).getPackageQuantity() >= mostSold) {
                                                float itemPrice = listOfPackages.get(i).getPackagePrice();
                                                if (itemPrice > totalPriceSold) {
                                                    totalPriceSold = itemPrice;
                                                    mostSoldIndex = i;
                                                    mostSold = listOfPackages.get(i).getPackageQuantity();
                                                }
                                            }
                                        }
                                        for (int i = 0; i < listOfPackages.getNumberOfEntries(); i++) {
                                            if (listOfPackages.get(i).getPackageQuantity() == mostSold) {
                                                System.out.print(listOfPackages.get(i));
                                            }
                                        }

                                        System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                        System.out.printf("|%2s%81s%-36s%-25s\n", "", "|    Most Sold Package : ", listOfPackages.get(mostSoldIndex).getPackageName(), "|");
                                        System.out.printf("|%2s%81s%-11.2f%26s\n", "", "|    Total Price Sold  : ", totalPriceSold, "|");
                                        System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                        listOfPackages = null;
                                        break;
                                    case 3:
                                        while (iterOrderHistory.hasNext()) {
                                            OrderDetails foundPackage = (OrderDetails) iterOrderHistory.next();
                                            found = false;
                                            for (int j = 0; j < listOfPackages.getNumberOfEntries(); j++) {
                                                if (listOfPackages.get(j).equals(foundPackage)) {
                                                    float bfrPackPrice = listOfPackages.get(j).getPackagePrice();
                                                    float afterPackPrice = bfrPackPrice + foundPackage.getPackagePrice();
                                                    listOfPackages.get(j).setPackagePrice(afterPackPrice);
                                                    int bfrPackQty = listOfPackages.get(j).getPackageQuantity();
                                                    int afterPackQty = bfrPackQty + foundPackage.getPackageQuantity();
                                                    listOfPackages.get(j).setPackageQuantity(afterPackQty);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                listOfPackages.add(foundPackage);
                                            }
                                        }

                                        totalPriceSold = 0;
                                        for (int i = 0; i < listOfPackages.getNumberOfEntries(); i++) {
                                            float priceSold = listOfPackages.get(i).getPackagePrice();
                                            if (priceSold > totalPriceSold) {
                                                totalPriceSold = priceSold;
                                            }
                                        }
                                        for (int i = 0; i < listOfPackages.getNumberOfEntries(); i++) {
                                            float priceSold = listOfPackages.get(i).getPackagePrice();
                                            if (priceSold == totalPriceSold) {
                                                System.out.print(listOfPackages.get(i));
                                            }
                                        }
                                        System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                        System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Highest Revenue  : ", totalPriceSold);
                                        System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                                        listOfPackages = null;
                                        break;
                                }
                            } else {
                                System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                                System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|\n", "", "???", "???", "???", "???", "???");
                                System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                                System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Total Price : ", totalPrice);
                                System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                            }
                        } while (choice != 4);
                    } else {
                        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|", "", "ID", "Package Name", "Package Quantity", "Package Price($)", "Order Status");
                        System.out.printf("\n%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%-15s %-30s %-30s %-20s %-18s|\n", "", "???", "???", "???", "???", "???");
                        System.out.printf("%s\n", "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +");
                        System.out.printf("|%2s%106s%-11.2f|\n", "", "|    Total Price : ", 0.0);
                        System.out.printf("%s\n", "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
                    }
                    break;
            }
        } while (choice != 3);
    }

    public float getTotalPrice(int orderID) {
        float totalPrice = 0;
        if (!orderQueue.isEmpty()) {
            Iterator iterPrice = orderQueue.iterator();
            while (iterPrice.hasNext()) {
                OrderDetails item = (OrderDetails) iterPrice.next();
                if (item.getOrderID() == orderID) {
                    totalPrice += (item.getPackagePrice() * item.getPackageQuantity());
                }
            }
        }
        return totalPrice;
    }

    public float getTotalPrice() {
        float totalPrice = 0;
        if (!orderQueue.isEmpty()) {
            Iterator iterPrice = orderQueue.iterator();
            while (iterPrice.hasNext()) {
                OrderDetails item = (OrderDetails) iterPrice.next();
                totalPrice += (item.getPackagePrice() * item.getPackageQuantity());
            }
        }
        return totalPrice;
    }

    public static void readData() {
        try {
            file = new File(ORDER_FILENAME);
            if (file.isFile()) {
                ObjectInputStream ooStream = new ObjectInputStream(new FileInputStream(file));
                orderHistory = (QueueInterface<OrderDetails>) ooStream.readObject();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (ClassNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }

        try {
            file = new File(PACKAGE_FILENAME);
            if (file.isFile()) {
                ObjectInputStream ooStream = new ObjectInputStream(new FileInputStream(file));
                packageMeal = (ListInterface<MealPackage>) ooStream.readObject();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (ClassNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
    }

    public static void writeData() {
        try {
            file = new File(ORDER_FILENAME);
            if (file.isFile()) {
                ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
                ooStream.writeObject(orderHistory);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
    }
}
