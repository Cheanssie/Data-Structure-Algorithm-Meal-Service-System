package SubClient;

import ADT.*;
import Entity.*;
import SortingAlgo.VoucherSort;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class PromotionManager {

    // Constant for Colors
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESETCOLOR = "\u001B[0m";

    // Constant File Name
    private static final String VOUCHER_FILE = "voucher.txt";
    private static final String PROMOTION_FILE = "promotion.txt";

    private SetInterface<Promotion> promotionSet = new DoublyLinkedSet<>();
    private SetInterface<Voucher> voucherSet = new DoublyLinkedSet<>();
    private SetInterface<User> userSet = new DoublyLinkedSet<>();

    private boolean errorInput;
    private int choice;
    private Scanner scanner = new Scanner(System.in);

    public PromotionManager() {
        initializeData();
    }

    public void mainMethod() {
        do {
            System.out.println();
            System.out.printf("%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %38s\n", "|  No.       |   Modules", "|");
            System.out.printf("%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %27s\n", "|  1_        |   Display Promotions", "|");
            System.out.printf("%s %28s\n", "|  2_        |   Add New Promotion", "|");
            System.out.printf("%s %29s\n", "|  3_        |   Remove Promotion", "|");
            System.out.printf("%s %30s\n", "|  4_        |   Enter Promotion", "|");
            System.out.printf("%s %28s\n", "|  5_        |   Combine Promotion", "|");
            System.out.printf("%s %41s\n", "|  6_        |   Exit", "|");
            System.out.printf("%s\n", "+ ========== + ============================================== +");

            do {
                errorInput = false;
                try {
                    System.out.printf("%-20s >>> ", "Enter Choice [1-6]");
                    choice = scanner.nextInt();
                    if (choice < 1 || choice > 6) {
                        errorInput = true;
                        System.out.println(RED + "Please enter a valid choice! [1-6]" + RESETCOLOR);
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter an integer only! [1-6]" + RESETCOLOR);
                } finally {
                    scanner.nextLine();
                }
            } while (errorInput);

            switch (choice) {
                case 1: {
                    promotionSetDisplay();
                }
                break;
                case 2: {
                    System.out.println();
                    addNewPromotion();
                }
                break;
                case 3: {
                    removePromotion();
                }
                break;
                case 4: {
                    promotionMenu();
                }
                break;
                case 5: {
                    combinePromotion();
                }
                break;
                default:
            }
        } while (choice != 6);
        saveData();
    }

    //case1
    public void promotionSetDisplay() {
        String str = "";
        Iterator iterator = promotionSet.iterator();
        str += String.format("\n+---------------------------+\n");
        str += String.format("| ID | %-20s |\n", "     Promotions");
        str += String.format("+---------------------------+\n");
        while (iterator.hasNext()) {
            Promotion currentPromotion = (Promotion) iterator.next();
            str += String.format("| %2s | %-20s |\n", currentPromotion.getPromotionId(),
                    currentPromotion.getPromotionName());
        }
        str += String.format("+---------------------------+\n");
        System.out.println(str);
    }

    //case2
    public void addNewPromotion() {// add promotion into promotionSet collection
        System.out.print("Enter Promotion Name (-1 to Exit) : ");
        String promotionName = scanner.nextLine();
        if (!promotionName.equals("-1")) {
            System.out.print("Enter Promotion Description : ");
            String promotionDesc = scanner.nextLine();
            Promotion newPromo = new Promotion(promotionName, promotionDesc);
            if (!promotionSet.contains(newPromo)) {
                promotionSet.add(newPromo);
                System.out.println(GREEN + "Added Successfully! " + RESETCOLOR);
            } else {
                System.out.println(RED + "Promotion Existed! " + RESETCOLOR);
            }
        } else {
            System.out.println(RED + "Exited! " + RESETCOLOR);
        }

    }

    //case3
    public void removePromotion() {// remove promotion from promotionSet collection
        if (!promotionSet.isEmpty()) {
            promotionSetDisplay();
            System.out.print("Enter Promotion ID to remove (-1 to Exit) : ");
            int promotionId = scanner.nextInt();
            if (promotionId != -1) {
                if (promotionSet.remove(getPromotion(promotionId)) != null) {
                    System.out.println(GREEN + "Removed Successfully !" + RESETCOLOR);
                } else {
                    System.out.println(RED + "Promotion not Exist! " + RESETCOLOR);
                }
            } else {
                System.out.println(RED + "Exited! " + RESETCOLOR);
            }
        } else {
            System.out.print(RED + "No Promotion Found! Unable to Remove!" + RESETCOLOR);
        }
    }

    //case4
    public void promotionMenu() {// enter promotion
        promotionSetDisplay();

        int promotionId = -1;
        Promotion targetedPromotion = null;
        do {
            errorInput = false;
            try {
                System.out.printf("%-20s >>> ", "Enter ID (-1 to Exit)");
                promotionId = scanner.nextInt();
            } catch (InputMismatchException ex) {
                errorInput = true;
                System.out.println(RED + "Please enter an integer only!" + RESETCOLOR);
            } finally {
                scanner.nextLine();
            }
            if (promotionId == -1) {
                System.out.println(RED + "Exited! " + RESETCOLOR);
            } else {
                targetedPromotion = getPromotion(promotionId);
                if (targetedPromotion != null) {
                    do {
                        System.out.println();
                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %38s\n", "|  No.       |   Modules", "|");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %30s\n", "|  1_        |   Display Voucher", "|");
                        System.out.printf("%s %30s\n", "|  2_        |   Add New Voucher", "|");
                        System.out.printf("%s %31s\n", "|  3_        |   Remove Voucher", "|");
                        System.out.printf("%s %33s\n", "|  4_        |   Sort Voucher", "|");
                        System.out.printf("%s %31s\n", "|  5_        |   Search Voucher", "|");
                        System.out.printf("%s %41s\n", "|  6_        |   Exit", "|");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");

                        do {
                            errorInput = false;
                            try {
                                System.out.printf("%-20s >>> ", "Enter Choice [1-6]");
                                choice = scanner.nextInt();
                                if (choice < 1 || choice > 6) {
                                    errorInput = true;
                                    System.out.println(RED + "Please enter a valid choice! [1-6]" + RESETCOLOR);
                                }
                            } catch (InputMismatchException ex) {
                                errorInput = true;
                                System.out.println(RED + "Please enter an integer only! [1-6]" + RESETCOLOR);
                            } finally {
                                scanner.nextLine();
                            }
                        } while (errorInput);

                        switch (choice) {
                            case 1: {
                                targetedPromotion.displayAllVoucher();
                            }
                            break;
                            case 2: {
                                System.out.println();
                                String voucherCode = null;
                                double discountRate = 0.0;
                                String expiryDate = null;
                                double minSpend = 0.0;
                                do {
                                    errorInput = false;
                                    System.out.print("Enter Voucher Code : ");
                                    voucherCode = scanner.nextLine();
                                    if (voucherCode.chars().count() > 12) {
                                        System.out.println(RED + "Please enter less than 12 characters! " + RESETCOLOR);
                                        errorInput = true;
                                    }
                                } while (errorInput);

                                do {
                                    errorInput = false;
                                    System.out.print("Enter Distance Rate (e.g. 20%=0.2) : ");
                                    discountRate = scanner.nextDouble();
                                    scanner.nextLine();
                                    if (discountRate < 0 || discountRate > 1.0) {
                                        System.out.println(RED + "Please enter within the range of 0.0 to 1.0 only! " + RESETCOLOR);
                                        errorInput = true;
                                    }
                                } while (errorInput);

                                do {
                                    errorInput = false;
                                    System.out.print("Enter Expiry Date (e.g. 11/02/2002) : ");
                                    expiryDate = scanner.nextLine();
                                    if (!expiryDate.trim().equals("")) {
                                        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                                        sdfrmt.setLenient(false);
                                        try {
                                            sdfrmt.parse(expiryDate);
                                        } /* Date format is invalid */ catch (ParseException e) {
                                            System.out.println(RED + expiryDate + " is Invalid Date format" + RESETCOLOR);
                                            errorInput = true;
                                        }
                                    }
                                } while (errorInput);

                                do {
                                    errorInput = false;
                                    System.out.print("Enter Minimum Spend : RM");
                                    minSpend = scanner.nextDouble();
                                    if (minSpend < 0) {
                                        System.out.println(RED + "Please enter a positive value! " + RESETCOLOR);
                                        errorInput = true;
                                    }
                                } while (errorInput);
                                if (targetedPromotion.get(voucherCode) == null) {
                                    targetedPromotion.addVoucher(new Voucher(voucherCode, discountRate, expiryDate, minSpend));
                                    System.out.println(GREEN + "Added Successfully! " + RESETCOLOR);
                                } else {
                                    System.out.println(RED + "Voucher Existed! " + RESETCOLOR);
                                }
                            }
                            break;
                            case 3: {// remove voucher
                                targetedPromotion.displayAllVoucher();
                                if (!targetedPromotion.getVouchers().isEmpty()) {
                                    System.out.print("Enter Voucher Code to remove : ");
                                    String voucherCode = scanner.nextLine();
                                    if (targetedPromotion.getVouchers().remove(targetedPromotion.get(voucherCode)) != null) {
                                        System.out.println(GREEN + "Removed Successfully !" + RESETCOLOR);
                                    } else {
                                        System.out.println(RED + "Promotion not Exist! " + RESETCOLOR);
                                    }
                                } else {
                                    System.out.print(RED + "No Promotion Found! Unable to Remove!" + RESETCOLOR);
                                }
                            }
                            break;
                            case 4: {
                                int choice = -1;
                                System.out.printf("\n%s\n", "+ ========== + ============================================== +");
                                System.out.printf("%s %38s\n", "|  No.       |   Sort By", " |");
                                System.out.printf("%s\n", "+ ========== + ============================================== +");
                                System.out.printf("%s %33s\n", "|  1_        |   Voucher Code", " |");
                                System.out.printf("%s %24s\n", "|  2_        |   Voucher Discount Rate", " |");
                                System.out.printf("%s %26s\n", "|  3_        |   Voucher Expiry Date", " |");
                                System.out.printf("%s %24s\n", "|  4_        |   Voucher Minimum Spend", " |");
                                System.out.printf("%s %41s\n", "|  5_        |   Exit", "|");
                                System.out.printf("%s\n", "+ ========== + ============================================== +");

                                do {
                                    errorInput = false;
                                    try {
                                        System.out.printf("%-20s >>> ", "Enter Aspect [1-5]");
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

                                Iterator iterator = targetedPromotion.getVouchers().iterator();

                                Voucher[] result = new Voucher[targetedPromotion.getVouchers().getSize()];
                                if (!targetedPromotion.getVouchers().isEmpty()) {
                                    int index = 0;
                                    while (iterator.hasNext()) {
                                        result[index] = (Voucher) iterator.next();
                                        index++;
                                    }

                                    int count = 0;
                                    String strOutput = String.format("\n+----------------------------------------------------------+\n");
                                    strOutput += String.format("| No. | Voucher Code | Discount | Expiry Date | Min. Spend |\n");
                                    strOutput += String.format("+----------------------------------------------------------+\n");

                                    switch (choice) {
                                        case 1: {
                                            VoucherSort.bubbleSort(result, result.length, Voucher.voucherCodeComparator);
                                        }
                                        break;
                                        case 2: {
                                            VoucherSort.bubbleSort(result, result.length, Voucher.voucherDiscountComparator);
                                        }
                                        break;
                                        case 3: {
                                            VoucherSort.bubbleSort(result, result.length, Voucher.voucherDateComparator);
                                        }
                                        break;
                                        case 4: {
                                            VoucherSort.bubbleSort(result, result.length, Voucher.voucherMinSpendComparator);
                                        }
                                        break;
                                    }

                                    for (int i = 0; i < result.length; i++) {
                                        Voucher currentVoucher = result[i];
                                        double discount = currentVoucher.getDiscountRate();
                                        discount *= 100.0;
                                        count++;
                                        strOutput += String.format("| %3s | %-12s | %7.2s%% | %-11s | RM%-8.2s |\n", count, currentVoucher.getVoucherCode(), discount, currentVoucher.getExpiryDate(), currentVoucher.getMinSpend());
                                    }
                                    strOutput += String.format("+----------------------------------------------------------+");
                                    System.out.println(strOutput);
                                    System.out.println("Press enter to return...");
                                    scanner.nextLine();
                                }
                            }
                            break;
                            case 5: {
                                String voucherCode = null;
                                String startExpiryDate = null;
                                String endExpiryDate = null;
                                double endDiscountRate = 0.0;
                                double startDiscountRate = 0.0;

                                System.out.printf("%-52s : ", "Voucher Code contains of (Enter to skip)");
                                voucherCode = scanner.nextLine();

                                do {
                                    errorInput = false;
                                    try {
                                        System.out.printf("%-52s : ", "Start Distance Rate (-1 to skip)");
                                        startDiscountRate = scanner.nextDouble();
                                        if (startDiscountRate != -1) {
                                            if (startDiscountRate < 0 || startDiscountRate > 1.0) {
                                                System.out.println(RED + "Please enter within the range of 0.0 to 1.0 only! " + RESETCOLOR);
                                                errorInput = true;
                                            }
                                        }
                                    } catch (InputMismatchException ex) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter an double only!" + RESETCOLOR);
                                    } finally {
                                        scanner.nextLine();
                                    }
                                } while (errorInput);

                                do {
                                    errorInput = false;
                                    try {
                                        System.out.printf("%-52s : ", "End Distance Rate (-1 to skip)");
                                        endDiscountRate = scanner.nextDouble();
                                        if (endDiscountRate != -1) {
                                            if (endDiscountRate < 0 || endDiscountRate > 1.0) {
                                                System.out.println(RED + "Please enter within the range of 0.0 to 1.0 only! " + RESETCOLOR);
                                                errorInput = true;
                                            }
                                        }
                                    } catch (InputMismatchException ex) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter an double only!" + RESETCOLOR);
                                    } finally {
                                        scanner.nextLine();
                                    }
                                } while (errorInput);

                                do {
                                    errorInput = false;
                                    System.out.printf("%-52s : ", "Start Expiry Date (e.g. 11/02/2002) (Enter to skip)");
                                    startExpiryDate = scanner.nextLine();
                                    if (!startExpiryDate.trim().equals("")) {
                                        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                                        sdfrmt.setLenient(false);
                                        try {
                                            sdfrmt.parse(startExpiryDate);
                                        } /* Date format is invalid */ catch (ParseException e) {
                                            System.out.println(RED + startExpiryDate + " is Invalid Date format" + RESETCOLOR);
                                            errorInput = true;
                                        }
                                    }
                                } while (errorInput);

                                do {
                                    errorInput = false;
                                    System.out.printf("%-52s : ", "End Expiry Date (e.g. 11/02/2002) (Enter to skip)");
                                    endExpiryDate = scanner.nextLine();
                                    if (!endExpiryDate.trim().equals("")) {
                                        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                                        sdfrmt.setLenient(false);
                                        try {
                                            sdfrmt.parse(endExpiryDate);
                                        } /* Date format is invalid */ catch (ParseException e) {
                                            System.out.println(RED + endExpiryDate + " is Invalid Date format" + RESETCOLOR);
                                            errorInput = true;
                                        }
                                    }
                                } while (errorInput);

                                System.out.println(GREEN + "\nResult:" + RESETCOLOR);

                                Iterator iterator = targetedPromotion.getVouchers().iterator();
                                while (iterator.hasNext()) {
                                    Voucher currentVoucher = (Voucher) iterator.next();
                                    SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
                                    sdfrmt.setLenient(false);
                                    Date startDate = null;
                                    Date endDate = null;
                                    Date currentDate = null;
                                    if (!startExpiryDate.equals("")) {
                                        try {
                                            startDate = sdfrmt.parse(startExpiryDate);
                                            currentDate = sdfrmt.parse(currentVoucher.getExpiryDate());
                                        } /* Date format is invalid */ catch (ParseException e) {
                                            System.out.println(e);
                                        }
                                    }

                                    if (!endExpiryDate.equals("")) {
                                        try {
                                            endDate = sdfrmt.parse(endExpiryDate);
                                            currentDate = sdfrmt.parse(currentVoucher.getExpiryDate());
                                        } /* Date format is invalid */ catch (ParseException e) {
                                            System.out.println(e);
                                        }
                                    }
                                    if (startDiscountRate == -1 && endDiscountRate == -1 && startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate == -1 && startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() >= startDiscountRate && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate == -1 && endDiscountRate != -1 && startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate != -1 && startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentVoucher.getDiscountRate() >= startDiscountRate && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate == -1 && endDiscountRate == -1 && !startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentDate.compareTo(startDate) >= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate == -1 && endDiscountRate == -1 && startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentDate.compareTo(endDate) <= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate == -1 && endDiscountRate == -1 && !startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentDate.compareTo(endDate) <= 0 && currentDate.compareTo(startDate) >= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate == -1 && !startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() >= startDiscountRate && currentDate.compareTo(startDate) >= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate == -1 && startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() >= startDiscountRate && currentDate.compareTo(endDate) <= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate == -1 && !startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() >= startDiscountRate && currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate == -1 && endDiscountRate != -1 && !startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentDate.compareTo(startDate) >= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate == -1 && endDiscountRate != -1 && startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentDate.compareTo(endDate) <= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate == -1 && endDiscountRate != -1 && !startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate != -1 && !startExpiryDate.equals("") && endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentVoucher.getDiscountRate() >= startDiscountRate && currentDate.compareTo(startDate) >= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate != -1 && startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentVoucher.getDiscountRate() >= startDiscountRate && currentDate.compareTo(endDate) <= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    } else if (startDiscountRate != -1 && endDiscountRate != -1 && !startExpiryDate.equals("") && !endExpiryDate.equals("")) {
                                        if (currentVoucher.getDiscountRate() <= endDiscountRate && currentVoucher.getDiscountRate() >= startDiscountRate && currentDate.compareTo(startDate) >= 0
                                                && currentDate.compareTo(endDate) <= 0 && currentVoucher.getVoucherCode().contains(voucherCode)) {
                                            System.out.print(currentVoucher.toString());
                                        }
                                    }

                                }
                            }
                            break;
                        }
                    } while (choice != 6);
                    promotionId = -1;
                } else {
                    System.out.println(RED + "Promotion ID not Exist! " + RESETCOLOR);
                }
            }
        } while (promotionId != -1);
    }

    //case5
    public void combinePromotion() {
        promotionSetDisplay();

        Promotion firstPromotion = null;
        do {
            errorInput = false;
            int firstPromotionId = -1;
            try {
                System.out.printf("%-20s >>> ", "Enter First ID ");
                firstPromotionId = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println(RED + "Please enter an integer only!" + RESETCOLOR);
                errorInput = true;
            }
            if (choice != -1) {
                firstPromotion = getPromotion(firstPromotionId);
                if (firstPromotion == null) {
                    errorInput = true;
                    System.out.println(RED + "Promotion not existed" + RESETCOLOR);
                }
            } else {
                errorInput = false;
            }
        } while (errorInput);

        Promotion secondPromotion = null;
        do {
            errorInput = false;
            int secondPromotionId = -1;
            try {
                System.out.printf("%-20s >>> ", "Enter Second ID ");
                secondPromotionId = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println(RED + "Please enter an integer only!" + RESETCOLOR);
                errorInput = true;
            }
            if (choice != -1) {
                secondPromotion = getPromotion(secondPromotionId);
                if (secondPromotion == null) {
                    errorInput = true;
                    System.out.println(RED + "Promotion not existed" + RESETCOLOR);
                } else if (secondPromotion.equals(firstPromotion)) {
                    errorInput = true;
                    System.out.println(RED + "Please select another promotion" + RESETCOLOR);
                }
            }
        } while (errorInput);

        do {
            errorInput = false;
            System.out.printf("%-20s >>> ", "Confirm to Combine? (Y/y = Yes, N/n = No) ");
            String confirm = scanner.next();
            scanner.nextLine();
            if (!confirm.equals("-1")) {
                switch (confirm) {
                    case "Y":
                    case "y":
                        firstPromotion.getVouchers().union(secondPromotion.getVouchers());
                        System.out.print("Rename Promotion Name : ");
                        String promotionName = scanner.nextLine();
                        firstPromotion.setPromotionName(promotionName);//name no output
                        System.out.print("Re-enter Promotion Description : ");
                        String promotionDesc = scanner.nextLine();
                        firstPromotion.setPromotionDesc(promotionDesc);
                        promotionSet.remove(secondPromotion);
                        System.out.println(GREEN + "Combine Successful! " + RESETCOLOR);
                        break;
                    case "N":
                    case "n":
                        System.out.println(RED + "Combine Canceled!" + RESETCOLOR);
                        break;
                    default:
                        errorInput = true;
                        System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                }
            }
        } while (errorInput);

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

    public void initializeData() {
        File file;

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
