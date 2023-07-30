package Client;

import Entity.*;
import SubClient.OrderManager;
import SubClient.PackageManager;
import SubClient.PromotionManager;
import SubClient.UserManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainClient {

    //Class ID File
    public static final String CLASSID_FILE = "classId.txt";

    // Colors
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESETCOLOR = "\u001B[0m";

    // User
    public static User admin = new User("admin", "123");

    // Input Variables
    private static boolean errorInput;
    private static int choice;
    private static Scanner input = new Scanner(System.in);

    // Sub-clients Vairables
    //private static PromotionManager promotionSubClient = new PromotionManager();
    //private static OrderManager orderManager = new OrderManager();
    //private static UserClient userSubClient = new UserClient();
    public static void main(String[] args) {
        initializeClassesId();
        do {
            System.out.println();
            System.out.printf("%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %38s\n", "|  No.       |   Modules", "|");
            System.out.printf("%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %38s\n", "|  1_        |   Package", "|");
            System.out.printf("%s %38s\n", "|  2_        |   Voucher", "|");
            System.out.printf("%s %40s\n", "|  3_        |   Order", "|");
            System.out.printf("%s %41s\n", "|  4_        |   User", "|");
            System.out.printf("%s %41s\n", "|  5_        |   Exit", "|");
            System.out.printf("%s\n", "+ ========== + ============================================== +");

            do {
                errorInput = false;
                try {
                    System.out.printf("%-20s >>> ", "Enter Choice [1-5]");
                    choice = input.nextInt();
                    if (choice < 1 || choice > 5) {
                        errorInput = true;
                        System.out.println(RED + "Please enter a valid choice! [1-5]" + RESETCOLOR);
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter an integer only! [1-5]" + RESETCOLOR);
                } finally {
                    input.nextLine();
                }
            } while (errorInput);

            switch (choice) {
                case 1: {
                    PackageManager mealPackageSubClient = new PackageManager();
                    mealPackageSubClient.mainMethod();
                }
                break;
                case 2: {
                    PromotionManager promotionSubClient = new PromotionManager();
                    promotionSubClient.mainMethod();
                }
                break;
                case 3: {
                    OrderManager orderManager = new OrderManager();
                    orderManager.mainMethod();
                }
                break;
                case 4: {
                    UserManager userSubClient = new UserManager();
                    userSubClient.mainMethod();
                }
                break;
                default:
            }

        } while (choice != 5);
        saveClassesId();
    }

    public static void initializeClassesId() {
        File file;

        // Index 0 : MealPackage - packageCount
        // Index 1 : Meal - mealCount
        // Index 2 : Promotion - promotionCount
        // Index 3 : User - userCount
        int[] classesId;
        try {
            file = new File(CLASSID_FILE);
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
            classesId = (int[]) oiStream.readObject();

            //Initialization
            MealPackage.setPackageCount(classesId[0]);
            Meal.setMealCount(classesId[1]);
            Promotion.setPromotionCount(classesId[2]);
            User.setUserCount(classesId[3]);

        } catch (FileNotFoundException ex) {
            //System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            //System.out.println(RED + ex + RESETCOLOR);
        } catch (ClassNotFoundException ex) {
            //System.out.println(RED + ex + RESETCOLOR);
        }

    }

    public static void saveClassesId() {
        File file;

        // Index 0 : MealPackage - packageCount
        // Index 1 : Meal - mealCount
        // Index 2 : Promotion - promotionCount
        // Index 3 : User - userCount
        int[] classesId = {MealPackage.getPackageCount(), Meal.getMealCount(), Promotion.getPromotionCount(), User.getUserCount()};
        try {
            file = new File(CLASSID_FILE);
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(classesId);
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
    }

}
