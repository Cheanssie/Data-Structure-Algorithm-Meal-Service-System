package SubClient;

import ADT.DoublyLinkedSet;
import ADT.SetInterface;
import Entity.Promotion;
import Entity.User;
import Entity.Voucher;
import Validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class UserManager {

    // Constant for Colors
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESETCOLOR = "\u001B[0m";

    // Constant File Name
    private static final String USER_FILE = "user.txt";
    private static final String VOUCHER_FILE = "voucher.txt";
    private static final String PROMOTION_FILE = "promotion.txt";

    private SetInterface<Promotion> promotionSet = new DoublyLinkedSet<>();
    private SetInterface<Voucher> voucherSet = new DoublyLinkedSet<>();
    private SetInterface<User> userSet = new DoublyLinkedSet<>();

    private boolean errorInput;
    private int choice;
    private Scanner scanner = new Scanner(System.in);

    public UserManager() {
        initializeData();
    }

    public void mainMethod() {
        do {
            System.out.println();
            System.out.printf("%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %38s\n", "|  No.       |   Modules", "|");
            System.out.printf("%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %32s\n", "|  1_        |   Display Users", "|");
            System.out.printf("%s %32s\n", "|  2_        |   Add New Users", "|");
            System.out.printf("%s %33s\n", "|  3_        |   Remove Users", "|");
            System.out.printf("%s %28s\n", "|  4_        |   Edit User Voucher", "|");
            System.out.printf("%s %41s\n", "|  5_        |   Exit", "|");
            System.out.printf("%s\n", "+ ========== + ============================================== +");

            do {
                errorInput = false;
                try {
                    System.out.printf("%-20s >>> ", "Enter Choice [1-5]");
                    choice = scanner.nextInt();
                    if (choice < 1 || choice > 5) {
                        errorInput = true;
                        System.out.println(RED + "Please enter a valid choice! [1-5]" + RESETCOLOR);
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter an integer only! [1-5]" + RESETCOLOR);
                } finally {
                    scanner.nextLine();
                }
            } while (errorInput);

            switch (choice) {
                case 1: {
                    System.out.println();
                    displayUserSet();
                }
                break;
                case 2: {
                    System.out.println();
                    addNewUser();
                }
                break;
                case 3: {
                    System.out.println();
                    removeuser();
                }
                break;
                case 4: {
                    System.out.println();
                    editUserVoucher();
                }
                break;
                default:
            }
        } while (choice != 5);
        saveData();
    }

    public void displayUserSet() {
        System.out.println("+-------------------------------------+");
        System.out.println("| ID |    User Name    | Phone Number |");
        System.out.println("+-------------------------------------+");
        Iterator iterator = userSet.iterator();
        while (iterator.hasNext()) {
            User currentUser = (User) iterator.next();
            currentUser.displayAllUser();
        }
        System.out.println("+-------------------------------------+");
    }

    public void addNewUser() {
        String userTel = null;
        System.out.print("Enter User Name (-1 to Exit) : ");
        String userName = scanner.nextLine();
        if (!userName.equals("-1")) {
            do {
                errorInput = false;
                System.out.print("Enter Phone Number e.g:012-3456789 : ");
                userTel = scanner.nextLine();
                if (!userTel.equals("-1")) {
                    for (int i = 0; i < userTel.length(); i++) {
                        if (i == 3) {
                            continue;
                        }
                        if (!Character.isDigit(userTel.charAt(i))) {
                            errorInput = true;
                            System.out.println(RED + "Please enter digit in Phone Number!" + RESETCOLOR);
                            break;
                        }
                    }
                    if (Validator.countWord(userTel) > 1) {
                        errorInput = true;
                        System.out
                                .println(RED + "Please do not include space in Phone Number!" + RESETCOLOR);
                    } else if (userTel.length() < 11 || userTel.length() > 12) {

                        errorInput = true;
                        System.out.println(RED + "Invalid length!" + RESETCOLOR);
                    } else if (userTel.charAt(3) != '-') {
                        errorInput = true;
                        System.out.println(RED + "Please include '-' !" + RESETCOLOR);
                    }
                }
            } while (errorInput);
            if (userSet.add(new User(userName, userTel))) {
                System.out.println(GREEN + "Added Successfully! " + RESETCOLOR);
            } else {
                System.out.println(RED + "User Existed! " + RESETCOLOR);
            }
        } else {
            System.out.println(RED + "Exited! " + RESETCOLOR);
        }

    }

    public void removeuser() {
        if (!userSet.isEmpty()) {
            displayUserSet();
            System.out.print("Enter User ID to remove (-1 to Exit) : ");
            int userId = scanner.nextInt();
            if (userId != -1) {
                if (userSet.remove(getUser(userId)) != null) {
                    System.out.println(GREEN + "Removed Successfully !" + RESETCOLOR);
                } else {
                    System.out.println(RED + "User not Exist! " + RESETCOLOR);
                }
            } else {
                System.out.println(RED + "Exited! " + RESETCOLOR);
            }
        } else {
            System.out.print(RED + "No User Found! Unable to Remove!" + RESETCOLOR);
        }
    }

    public void editUserVoucher() {
        String str = "";
        Iterator userSetIterator = userSet.iterator();
        str += String.format("+----------------------------+\n");
        str += String.format("| ID. | %-20s |\n", "      User Name ");
        str += String.format("+----------------------------+\n");
        while (userSetIterator.hasNext()) {
            User currentUser = (User) userSetIterator.next();
            str += String.format("| %3s | %-20s |\n", currentUser.getUserId(), currentUser.getUserName());
        }
        str += String.format("+----------------------------+\n");
        System.out.println(str);

        int promotionId = -1;
        User targetedUser = null;
        do {
            try {
                System.out.printf("%-20s >>> ", "Enter ID (-1 to Exit)");
                promotionId = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println(RED + "Please enter an integer only!" + RESETCOLOR);
            } finally {
                scanner.nextLine();
            }

            if (promotionId != -1) {
                targetedUser = getUser(promotionId);
                if (targetedUser != null) {
                    do {
                        System.out.println();
                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %38s\n", "|  No.       |   Modules", "|");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %30s\n", "|  1_        |   Display Voucher", "|");
                        System.out.printf("%s %30s\n", "|  2_        |   Add New Voucher", "|");
                        System.out.printf("%s %31s\n", "|  3_        |   Remove Voucher", "|");
                        System.out.printf("%s %41s\n", "|  4_        |   Exit", "|");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");

                        do {
                            errorInput = false;
                            try {
                                System.out.printf("%-20s >>> ", "Enter Choice [1-4]");
                                choice = scanner.nextInt();
                                if (choice < 1 || choice > 4) {
                                    errorInput = true;
                                    System.out.println(RED + "Please enter a valid choice! [1-4]" + RESETCOLOR);
                                }
                            } catch (InputMismatchException ex) {
                                errorInput = true;
                                System.out.println(RED + "Please enter an integer only! [1-4]" + RESETCOLOR);
                            } finally {
                                scanner.nextLine();
                            }
                        } while (errorInput);

                        switch (choice) {
                            case 1: {//display voucher in user
                                if (!targetedUser.getVouchers().isEmpty()) {
                                    System.out.println();
                                    displayVoucherInUser(targetedUser);
                                } else {
                                    System.out.println(RED + targetedUser.getUserName() + " Has No Voucher! " + RESETCOLOR);
                                }
                            }
                            break;
                            case 2: {//assign voucher to user
                                System.out.println();
                                if (!targetedUser.getVouchers().isEmpty()) {
                                    displayVoucherInUser(targetedUser);
                                } else {
                                    System.out.println(RED + targetedUser.getUserName() + " Has No Voucher! " + RESETCOLOR);
                                }
                                //get vouchers from promotion and display
                                if (!promotionSet.isEmpty()) {
                                    String str1 = "";
                                    Iterator iterator = promotionSet.iterator();
                                    str1 += String.format("+----------------------------+\n");
                                    str1 += String.format("| ID. | %-20s |\n", "     Promotions");
                                    str1 += String.format("+----------------------------+\n");
                                    while (iterator.hasNext()) {
                                        Promotion currentPromotion = (Promotion) iterator.next();
                                        str1 += String.format("| %3s | %-20s |\n", currentPromotion.getPromotionId(),
                                                currentPromotion.getPromotionName());
                                    }
                                    str1 += String.format("+----------------------------+\n");
                                    System.out.println(str1);

                                    Promotion targetedPromotion = null;
                                    do {
                                        try {
                                            System.out.printf("%-20s >>> ", "Enter ID (-1 to Exit)");
                                            promotionId = scanner.nextInt();
                                        } catch (InputMismatchException ex) {
                                            System.out.println(RED + "Please enter an integer only!" + RESETCOLOR);
                                        } finally {
                                            scanner.nextLine();
                                        }
                                        if (promotionId != -1) {
                                            targetedPromotion = getPromotion(promotionId);
                                            if (targetedPromotion != null) {
                                                targetedPromotion.displayAllVoucher();//display vouchers in currentPromotion
                                                Iterator assignVoucherIterate = targetedPromotion.getVouchers().iterator();
                                                System.out.printf("%-20s >>> ", "Enter Voucher Code (all to assign all vouchers)");
                                                String voucherCode = scanner.next();
                                                while (assignVoucherIterate.hasNext()) {
                                                    Voucher currentVoucher = (Voucher) assignVoucherIterate.next();
                                                    if (currentVoucher.getVoucherCode().equals(voucherCode)) {
                                                        targetedUser.addVoucher(targetedPromotion.get(voucherCode));
                                                        System.out.println(GREEN + "Added Successfully! " + RESETCOLOR);
                                                        choice = -1;
                                                    }
                                                }
                                                if (voucherCode.equals("all")) {
                                                    targetedUser.getVouchers().union(targetedPromotion.getVouchers());
                                                    System.out.println(GREEN + "Added Successfully! " + RESETCOLOR);
                                                    choice = -1;
                                                }
                                            } else {
                                                System.out.println(RED + "Promotion not Exist! " + RESETCOLOR);
                                            }
                                        } else {
                                            System.out.println(RED + "Exited! " + RESETCOLOR);
                                        }
                                    } while (choice != -1);
                                } else {
                                    System.out.println(RED + "No Promotion Found! " + RESETCOLOR);
                                }
                            }
                            break;
                            case 3: {//remove voucher from user
                                if (!targetedUser.getVouchers().isEmpty()) {
                                    do {
                                        System.out.println();
                                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                                        System.out.printf("%s %38s\n", "|  No.       |   Modules", "|");
                                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                                        System.out.printf("%s %26s\n", "|  1_        |   Remove by Promotion", "|");
                                        System.out.printf("%s %23s\n", "|  2_        |   Remove by Voucher Code", "|");
                                        System.out.printf("%s %41s\n", "|  3_        |   Exit", "|");
                                        System.out.printf("%s\n", "+ ========== + ============================================== +");

                                        do {
                                            errorInput = false;
                                            try {
                                                System.out.printf("%-20s >>> ", "Enter Choice [1-3]");
                                                choice = scanner.nextInt();
                                                if (choice < 1 || choice > 3) {
                                                    errorInput = true;
                                                    System.out.println(RED + "Please enter a valid choice! [1-3]" + RESETCOLOR);
                                                }
                                            } catch (InputMismatchException ex) {
                                                errorInput = true;
                                                System.out.println(RED + "Please enter an integer only! [1-3]" + RESETCOLOR);
                                            } finally {
                                                scanner.nextLine();
                                            }
                                        } while (errorInput);

                                        switch (choice) {
                                            case 1: {
                                                if (!promotionSet.isEmpty()) {
                                                    String str1 = "";
                                                    Iterator iterator = promotionSet.iterator();
                                                    str1 += String.format("+---------------------------+\n");
                                                    str1 += String.format("| ID | %-20s |\n", "     Promotions");
                                                    str1 += String.format("+---------------------------+\n");
                                                    while (iterator.hasNext()) {
                                                        Promotion currentPromotion = (Promotion) iterator.next();
                                                        str1 += String.format("| %2s | %-20s |\n", currentPromotion.getPromotionId(),
                                                                currentPromotion.getPromotionName());
                                                    }
                                                    str1 += String.format("+---------------------------+\n");
                                                    System.out.println(str1);

                                                    Promotion targetedPromotion = null;
                                                    do {
                                                        try {
                                                            System.out.printf("%-20s >>> ", "Enter ID (-1 to Exit)");
                                                            promotionId = scanner.nextInt();
                                                        } catch (InputMismatchException ex) {
                                                            System.out.println(RED + "Please enter an integer only!" + RESETCOLOR);
                                                        } finally {
                                                            scanner.nextLine();
                                                        }
                                                        if (promotionId != -1) {
                                                            targetedPromotion = getPromotion(promotionId);
                                                            if (targetedPromotion != null) {
                                                                do {
                                                                    errorInput = false;
                                                                    System.out.printf("%-45s >>> ", "Confirm to Remove? (Y/y = Yes, N/n = No)");
                                                                    switch (scanner.next().charAt(0)) {
                                                                        case 'Y':
                                                                        case 'y':
                                                                            SetInterface<Voucher> targetedNewSet = null;
                                                                            targetedNewSet = targetedUser.getVouchers().intersection(targetedPromotion.getVouchers());
                                                                            Iterator targetVoucher = targetedNewSet.iterator();
                                                                            while (targetVoucher.hasNext()) {
                                                                                targetedUser.removeVoucher((Voucher) targetVoucher.next());
                                                                            }
                                                                            System.out.println(GREEN + "Removed!" + RESETCOLOR);
                                                                            break;
                                                                        case 'N':
                                                                        case 'n':
                                                                            break;
                                                                        default:
                                                                            errorInput = true;
                                                                            System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                                                                    }
                                                                    scanner.nextLine();
                                                                } while (errorInput);
                                                                choice = -1;
                                                            }
                                                        } else {
                                                            System.out.println(RED + "Exited! " + RESETCOLOR);
                                                        }
                                                    } while (choice != -1);
                                                } else {
                                                    System.out.println(RED + "No Voucher Found! Unable to Remove! " + RESETCOLOR);
                                                }
                                            }
                                            break;
                                            case 2: {
                                                if (!targetedUser.getVouchers().isEmpty()) {
                                                    System.out.println();
                                                    displayVoucherInUser(targetedUser);
                                                    Iterator assignVoucherIterate = targetedUser.getVouchers().iterator();
                                                    System.out.printf("%-20s >>> ", "Enter Voucher Code");
                                                    String voucherCode = scanner.next();
                                                    if (voucherCode != null) {
                                                        while (assignVoucherIterate.hasNext()) {
                                                            Voucher currentVoucher = (Voucher) assignVoucherIterate.next();
                                                            if (currentVoucher.getVoucherCode().equals(voucherCode)) {
                                                                targetedUser.removeVoucher(targetedUser.get(voucherCode));
                                                                System.out.println(GREEN + "Removed Successfully! " + RESETCOLOR);
                                                            }
                                                        }
                                                    } else {
                                                        System.out.println(RED + "Voucher Code not Exist! " + RESETCOLOR);
                                                    }
                                                } else {
                                                    System.out.println(RED + "No Voucher Found! Unable to Remove! " + RESETCOLOR);
                                                }
                                            }
                                            break;
                                        }
                                    } while (choice != 3);
                                }
                            }
                            break;
                        }
                    } while (choice != 4);
                    promotionId = -1;
                } else {
                    System.out.println(RED + "User ID not Existed! " + RESETCOLOR);
                }
            } else {
                System.out.println(RED + "Exited! " + RESETCOLOR);
            }
        } while (promotionId != -1);
    }

    public void displayVoucherInUser(User currentUserVoucher) {
        String result = "";
        int count = 0;
        Iterator userVoucheriterator = currentUserVoucher.getVouchers().iterator();
        result += String.format("+----------------------------------------------------------+\n");
        result += String.format("| No. | Voucher Code | Discount | Expiry Date | Min. Spend |\n");
        result += String.format("+----------------------------------------------------------+\n");
        while (userVoucheriterator.hasNext()) {
            Voucher currentVoucher = (Voucher) userVoucheriterator.next();
            double discount = currentVoucher.getDiscountRate();
            discount *= 100.0;
            count++;
            result += String.format("| %3s | %-12s | %7.2s%% | %-11s | RM%-8.2s |\n", count, currentVoucher.getVoucherCode(), discount, currentVoucher.getExpiryDate(), currentVoucher.getMinSpend());
        }
        result += String.format("+----------------------------------------------------------+");
        System.out.println(result);
    }

    public Voucher getVoucher(String voucherCode) {
        Iterator userIterator = voucherSet.iterator();
        while (userIterator.hasNext()) {
            Voucher currentVoucher = (Voucher) userIterator.next();
            if (currentVoucher.getVoucherCode().equals(voucherCode)) {
                return currentVoucher;
            }
        }
        return null;
    }

    public Promotion getPromotion(int promotionId) {
        Iterator promotionIterator = promotionSet.iterator();
        while (promotionIterator.hasNext()) {
            Promotion currentPromotion = (Promotion) promotionIterator.next();
            if (currentPromotion.getPromotionId() == promotionId) {
                return currentPromotion;
            }
        }
        return null;
    }

    public User getUser(int userId) {
        Iterator userIterator = userSet.iterator();
        while (userIterator.hasNext()) {
            User currentUser = (User) userIterator.next();
            if (currentUser.getUserId() == userId) {
                return currentUser;
            }
        }
        return null;
    }

    public void initializeData() {
        File file;

        try {
            file = new File(USER_FILE);
            if (file.isFile()) {
                ObjectInputStream ooStream = new ObjectInputStream(new FileInputStream(file));
                userSet = (SetInterface<User>) ooStream.readObject();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (ClassNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }

        try {
            file = new File(VOUCHER_FILE);
            if (file.isFile()) {
                ObjectInputStream ooStream = new ObjectInputStream(new FileInputStream(file));
                voucherSet = (SetInterface<Voucher>) ooStream.readObject();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (ClassNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }

        try {
            file = new File(PROMOTION_FILE);
            if (file.isFile()) {
                ObjectInputStream ooStream = new ObjectInputStream(new FileInputStream(file));
                promotionSet = (SetInterface<Promotion>) ooStream.readObject();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (ClassNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
    }

    public void saveData() {
        File file;

        try {
            file = new File(USER_FILE);
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(userSet);
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }

        try {
            file = new File(VOUCHER_FILE);
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(voucherSet);
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
        try {
            file = new File(PROMOTION_FILE);
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(promotionSet);
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
    }
}
