package SubClient;

import ADT.*;
import Entity.*;
import SortingAlgo.MealPackageSort;
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

public class PackageManager {

    // Constant for Colors
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESETCOLOR = "\u001B[0m";

    // Constant File Name
    private static final String PACKAGE_FILE = "packages.txt";
    private static final String MEAL_FILE = "meals.txt";

    // Default Setting
    public static final int DEFAULT_CHOICE = -1;
    public static Scanner input = new Scanner(System.in);
    public static boolean errorInput = false; // Uses to determine correctness of input

    private ListInterface<MealPackage> packages = new LinkedList<>();
    private ListInterface<Meal> meals = new LinkedList<>();

    public PackageManager() {
        initializeData();

//        meals.add(new Meal("Ikan Siakap Tiga Rasa", "Hot & Spicy"));
//        meals.add(new Meal("Ayam Paprik", "Sweet & Spicy"));
//        meals.add(new Meal("Creamie Chicken", "Creamy & Delicious "));
//        meals.add(new Meal("Lemon Chicken", "Sweet & Sour"));
//        meals.add(new Meal("Longan Dessert", "Cool & Refreshing"));
//        meals.add(new Meal("Ayam Rendang", "Hot & Spicy"));
//        meals.add(new Meal("Kambing Bakar", "Marinated with Secret Recipe"));
//        meals.add(new Meal("Sup Daging", "Nutritious"));
//        meals.add(new Meal("Mixed Fruit Plate", "Nutritious & Mellifluous"));
    }

    public void mainMethod() {
        // Variables Declaration
        int choice = DEFAULT_CHOICE; // Uses to accept user's selection, default is -1

        do {
            choice = packageMainMenu();

            switch (choice) {
                case 1: {
                    // Manage meals inside the package
                    managePackageDetails();
                }
                break;
                case 2: {
                    addPackage();
                }
                break;
                case 3: {
                    removePackage();
                }
                break;
                case 4: {
                    editPackage();
                }
                break;
                case 5: {
                    sortPackage();
                }
                break;
                case 6: {
                    searchPackage();
                }
                break;
            }
        } while (choice != 7);

        saveData();

    }

    public String packagesString() {
        String result = "\n+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +\n";
        result += String.format("|%2s %-15s %-30s %-30s %-20s|\n", "", "Package No.", "Package ID", "Package Name",
                "Package Price($)");
        result += "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +\n";

        if (packages.isEmpty()) {
            result += String.format("|%2s %-15s %-30s %-30s %-20s|\n", "", "???", "???", "???", "???");
            result += String.format("%s\n",
                    "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
            result += String.format("|  %54s%45s|\n", "-- Package Is Empty --", "");
        } else {
            Iterator packageIterator = packages.iterator();
            int packageNo = 1;
            while (packageIterator.hasNext()) {
                MealPackage currentPackage = (MealPackage) packageIterator.next();
                result += String.format("|%2s %-15s %-30s %-30s %-20.2f|\n", "", (packageNo++) + "_",
                        currentPackage.getPackageId(), currentPackage.getPackageName(),
                        currentPackage.getPackagePrice());
            }

        }

        result += String.format("%s\n",
                "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");

        return result;
    }

    public int packageMainMenu() {

        // Variables Declaration
        int choice = DEFAULT_CHOICE; // Uses to accept user's selection, default is -1

        System.out.println(packagesString());
        System.out.printf("%s\n", "+ ========== + ============================================== +");
        System.out.printf("%s %36s\n", "|  No.       |   Functions", " |");
        System.out.printf("%s\n", "+ ========== + ============================================== +");
        System.out.printf("%s %23s\n", "|  1_        |   Manage Package Details", " |");
        System.out.printf("%s %34s\n", "|  2_        |   Add Package", " |");
        System.out.printf("%s %31s\n", "|  3_        |   Remove Package", " |");
        System.out.printf("%s %33s\n", "|  4_        |   Edit Package", " |");
        System.out.printf("%s %23s\n", "|  5_        |   Display Sorted Package", " |");
        System.out.printf("%s %31s\n", "|  6_        |   Search Package", " |");
        System.out.printf("%s %30s\n", "|  7_        |   Save and Return", " |");
        System.out.printf("%s\n", "+ ========== + ============================================== +");

        do {
            errorInput = false;
            try {
                System.out.printf("%-20s >>> ", "Enter Choice [1-7]");
                choice = input.nextInt();
                if (choice < 1 || choice > 7) {
                    errorInput = true;
                    System.out.println(RED + "Please enter a valid choice! [1-7]" + RESETCOLOR);
                }
            } catch (InputMismatchException ex) {
                errorInput = true;
                System.out.println(RED + "Please enter an integer only! [1-7]" + RESETCOLOR);
            } finally {
                input.nextLine();
            }
        } while (errorInput);

        return choice;
    }

    public void managePackageDetails() {
        int givenPosition = DEFAULT_CHOICE;
        int choice = DEFAULT_CHOICE;

        System.out.println(packagesString());

        do {
            errorInput = false;
            try {
                System.out.printf("%-45s >>> ", "Enter Package No.to Edit [-1 to Return]");
                givenPosition = input.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                errorInput = true;
            } finally {
                input.nextLine();
            }
            if (givenPosition != -1) {
                MealPackage targetedPackage = packages.get(givenPosition);
                if (targetedPackage == null) {
                    errorInput = true;
                    System.out.println(RED + "Package not existed! Please try again" + RESETCOLOR);
                } else {
                    do {
                        System.out.print("\nPackage: " + targetedPackage.getPackageName());
                        System.out.println(mealsString(targetedPackage.getMealList()));

                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %36s\n", "|  No.       |   Functions", " |");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %34s\n", "|  1_        |   Assign Meal", " |");
                        System.out.printf("%s %32s\n", "|  2_        |   Unassign Meal", " |");
                        System.out.printf("%s %30s\n", "|  3_        |   Save and Return", " |");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");

                        do {
                            errorInput = false;
                            try {
                                System.out.printf("%-20s >>> ", "Enter Choice [1-3]");
                                choice = input.nextInt();
                                if (choice < 1 || choice > 3) {
                                    errorInput = true;
                                    System.out.println(RED + "Please enter a valid choice! [1-3]" + RESETCOLOR);
                                }
                            } catch (InputMismatchException ex) {
                                errorInput = true;
                                System.out.println(RED + "Please enter an integer only! [1-4]" + RESETCOLOR);
                            } finally {
                                input.nextLine();
                            }
                        } while (errorInput);

                        switch (choice) {
                            case 1: {
                                System.out.print("\nAvailable Meals:");
                                System.out.println(mealsString(meals));
                                System.out.print("Package: " + targetedPackage.getPackageName());
                                System.out.println(mealsString(targetedPackage.getMealList()));
                                if (!meals.isEmpty()) {
                                    do {
                                        errorInput = false;
                                        try {
                                            System.out.printf("%-45s >>> ", "Enter Meal No.to Assign [-1 to Return]");
                                            givenPosition = input.nextInt();
                                            Meal targetedMeal = meals.get(givenPosition);
                                            if (givenPosition != -1) {
                                                if (targetedMeal == null) {
                                                    errorInput = true;
                                                    System.out.println(
                                                            RED + "Meal not existed! Please try again" + RESETCOLOR);
                                                } else {
                                                    if (!targetedPackage.getMealList().contains(targetedMeal)) {
                                                        targetedPackage.addMeal(targetedMeal);
                                                        System.out.println(GREEN + targetedMeal.toString() + GREEN
                                                                + "Has been assigned to "
                                                                + targetedPackage.getPackageName() + RESETCOLOR);
                                                    } else {
                                                        errorInput = true;
                                                        System.out.println(RED + targetedMeal.toString() + RED
                                                                + "Has existed in " + targetedPackage.getPackageName()
                                                                + RESETCOLOR);
                                                    }
                                                }
                                            }
                                        } catch (InputMismatchException ex) {
                                            errorInput = true;
                                            System.out.println(
                                                    RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                                        } finally {
                                            input.nextLine();
                                        }
                                    } while (errorInput);
                                } else {
                                    System.out.println(RED + "Menu is empty, there is nothing to assign!" + RESETCOLOR);
                                }
                            }
                            break;
                            case 2: {
                                System.out.print("\nPackage: " + targetedPackage.getPackageName());
                                System.out.println(mealsString(targetedPackage.getMealList()));
                                ListInterface<Meal> targetedPackageMeals = targetedPackage.getMealList();
                                if (!targetedPackageMeals.isEmpty()) {
                                    do {
                                        errorInput = false;
                                        try {
                                            System.out.printf("%-45s >>> ", "Enter Meal No.to Unassign [-1 to Return]");
                                            givenPosition = input.nextInt();
                                            Meal removedMeal = targetedPackageMeals.remove(givenPosition);
                                            if (givenPosition != -1) {
                                                if (removedMeal == null) {
                                                    errorInput = true;
                                                    System.out.println(
                                                            RED + "Meal not existed! Please try again" + RESETCOLOR);
                                                } else {
                                                    System.out.println(GREEN + removedMeal.toString() + GREEN
                                                            + "Has been removed from "
                                                            + targetedPackage.getPackageName() + RESETCOLOR);
                                                }
                                            }
                                        } catch (InputMismatchException ex) {
                                            errorInput = true;
                                            System.out.println(
                                                    RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                                        } finally {
                                            input.nextLine();
                                        }
                                    } while (errorInput);
                                } else {
                                    System.out.println(RED + "Menu is empty, there is nothing to assign!" + RESETCOLOR);
                                }
                            }
                            break;
                        }
                    } while (choice != 3);
                    saveData();
                }
            }
        } while (errorInput);

    }

    public String mealsString(ListInterface<Meal> targetedPackageDetail) {
        String result = "\n+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +\n";
        result += String.format("|%2s %-15s %-20s %-30s %-30s|\n", "", "Meal No.", "Meal ID", "Meal Name",
                "Meal Description");
        result += "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +\n";

        if (targetedPackageDetail.isEmpty()) {
            result += String.format("|%2s %-15s %-30s %-30s %-20s|\n", "", "???", "???", "???", "???");
            result += String.format("%s\n",
                    "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
            result += String.format("|  %54s%45s|\n", "-- Empty Meal List --", "");
        } else {
            Iterator mealsIterator = targetedPackageDetail.iterator();
            int mealNo = 1;
            while (mealsIterator.hasNext()) {
                Meal currentMeal = (Meal) mealsIterator.next();
                result += String.format("|%2s %-15s %-20s %-30s %-30s|\n", "", (mealNo++) + "_",
                        currentMeal.getMealId(), currentMeal.getMealName(), currentMeal.getMealDesc());
            }

        }

        result += String.format("%s\n",
                "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");

        return result;
    }

    public void addPackage() {
        int newPosition = DEFAULT_CHOICE;
        boolean continueInput = false;

        do {
            // Prompt and Read a new Package
            MealPackage newPackage = buildNewPackage();

            do {
                errorInput = false;
                try {
                    System.out.printf("%-45s >>> ", "Enter Position to Add (Default: -1)");
                    newPosition = input.nextInt();
                    if (!packages.contains(newPackage)) {
                        if (newPosition != -1) {
                            errorInput = !packages.add(newPackage, newPosition);
                            if (errorInput) {
                                System.out.println(RED + "Position not existed! Please try again" + RESETCOLOR);
                            } else {
                                System.out.println(
                                        GREEN + newPackage.toString() + "Has been successfully added!" + RESETCOLOR);
                            }
                        } else {
                            packages.add(newPackage);
                            System.out.println(
                                    GREEN + newPackage.toString() + "Has been successfully added!" + RESETCOLOR);
                        }
                    } else {
                        System.out.println(RED + "Package existed! Please try again" + RESETCOLOR);
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                } finally {
                    input.nextLine();
                }
            } while (errorInput);

            do {
                errorInput = false;
                System.out.printf("%-45s >>> ", "Continue to add? (Y/y = Yes, N/n = No)");
                switch (input.next().charAt(0)) {
                    case 'Y':
                    case 'y':
                        continueInput = true;
                        break;
                    case 'N':
                    case 'n':
                        continueInput = false;
                        break;
                    default:
                        errorInput = true;
                        System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                }
                input.nextLine();
            } while (errorInput);
        } while (continueInput);
    }

    public void removePackage() {
        // Implement Remove Meal Here
        int removePosition = -1;

        // Step1: Display menu
        // Step2: Prompt and Read for meal to remove
        if (!packages.isEmpty()) {
            do {
                errorInput = false;
                try {
                    System.out.printf("%-45s >>> ", "Enter Package No.to Remove [-1 to Return]");
                    removePosition = input.nextInt();
                    MealPackage removedPackage = packages.remove(removePosition);
                    // Step3: Remove and Return Message
                    if (removePosition != -1) {
                        if (removedPackage == null) {
                            errorInput = true;
                            System.out.println(RED + "Package not existed! Please try again" + RESETCOLOR);
                        } else {
                            System.out.println(GREEN + removedPackage.toString() + "Has been removed!" + RESETCOLOR);
                        }
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                } finally {
                    input.nextLine();
                }
            } while (errorInput);
        } else {
            System.out.println(RED + "Package list is empty, there is nothing to remove!" + RESETCOLOR);
        }
    }

    public void editPackage() {
        if (!packages.isEmpty()) {
            // Implement Edit here
            int givenPosition = -1;
            int choice = DEFAULT_CHOICE;
            boolean hasEdited = false;

            // Step2: Prompt and Read for new input
            do {
                errorInput = false;
                try {
                    System.out.println();
                    System.out.printf("%-45s >>> ", "Enter Package No.to Edit [-1 to Return]");
                    givenPosition = input.nextInt();
                } catch (InputMismatchException ex) {
                    System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                    errorInput = true;
                } finally {
                    input.nextLine();
                }
                if (givenPosition != -1) {
                    MealPackage targetedPackage = packages.get(givenPosition);
                    if (targetedPackage == null) {
                        errorInput = true;
                        System.out.println(RED + "Package not existed! Please try again" + RESETCOLOR);
                    } else {
                        // Step3: Prompt and Read for meal to edit
                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %38s\n", "|  No.       |   Aspects", " |");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");
                        System.out.printf("%s %33s\n", "|  1_        |   Package Name", " |");
                        System.out.printf("%s %32s\n", "|  2_        |   Package Price", " |");
                        System.out.printf("%s %26s\n", "|  3_        |   Package Replacement", " |");
                        System.out.printf("%s %39s\n", "|  4_        |   Return", "|");
                        System.out.printf("%s\n", "+ ========== + ============================================== +");

                        MealPackage newPackage = (MealPackage) targetedPackage.clone();

                        do {
                            errorInput = false;
                            try {
                                System.out.printf("%-20s >>> ", "Enter Aspect [1-4]");
                                choice = input.nextInt();
                                if (choice < 1 || choice > 4) {
                                    errorInput = true;
                                    System.out.println(RED + "Please enter a valid choice! [1-4]" + RESETCOLOR);
                                }
                            } catch (InputMismatchException ex) {
                                errorInput = true;
                                System.out.println(RED + "Please enter an integer only! [1-4]" + RESETCOLOR);
                            } finally {
                                input.nextLine();
                            }
                        } while (errorInput);

                        switch (choice) {
                            case 1: {
                                String packageName;
                                System.out.println();
                                do {
                                    errorInput = false;
                                    System.out.printf("%-45s >>> ", "Enter Package Name (Less than 4 words)");
                                    packageName = input.nextLine();
                                    if (Validator.countWord(packageName) > 3) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter less than 4 words!" + RESETCOLOR);
                                    }
                                } while (errorInput);
                                newPackage.setPackageName(packageName);
                                hasEdited = true;
                            }
                            break;
                            case 2: {
                                float packagePrice = 0;
                                do {
                                    try {
                                        errorInput = false;
                                        System.out.printf("%-45s >>> ", "Enter Package Price (EXP:XX.xx)");
                                        packagePrice = input.nextFloat();
                                        if (packagePrice < 0) {
                                            errorInput = true;
                                            System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                                        }
                                    } catch (InputMismatchException ex) {
                                        errorInput = true;
                                        System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                                    } finally {
                                        input.nextLine();
                                    }
                                } while (errorInput);
                                newPackage.setPackagePrice(packagePrice);
                                hasEdited = true;
                            }
                            break;
                            case 3: {
                                newPackage = buildNewPackage();
                                System.out.print(
                                        GREEN + newPackage.getPackageName() + "'s meal List has been cleared, please re-assign again!\n" + RESETCOLOR);
                                hasEdited = true;
                            }
                            break;
                        }

                        // Step4: Edit and Return Message
                        if (hasEdited) {
                            if (packages.replace(newPackage, givenPosition)) {
                                System.out.print(
                                        GREEN + "Package before edit : " + targetedPackage.toString() + RESETCOLOR);
                                System.out.print(GREEN + "Package after edit  : " + newPackage.toString() + RESETCOLOR);
                            } else {
                                System.out.println(RED + "Fail to edit!" + RESETCOLOR);
                            }
                        }
                        // Reset back choice to DEFAULT_CHOICE
                        choice = DEFAULT_CHOICE;
                    }
                }
            } while (errorInput);
        } else {
            System.out.println(RED + "Package list is empty, there is nothing to edit!" + RESETCOLOR);
        }
    }

    public void sortPackage() {
        int choice = DEFAULT_CHOICE;
        System.out.printf("\n%s\n", "+ ========== + ============================================== +");
        System.out.printf("%s %38s\n", "|  No.       |   Sort By", " |");
        System.out.printf("%s\n", "+ ========== + ============================================== +");
        System.out.printf("%s %28s\n", "|  1_        |   Package Name(ASC)", " |");
        System.out.printf("%s %27s\n", "|  2_        |   Package Price(ASC)", " |");
        System.out.printf("%s %28s\n", "|  3_        |   Package Name(DES)", " |");
        System.out.printf("%s %27s\n", "|  4_        |   Package Price(DES)", " |");
        System.out.printf("%s %39s\n", "|  5_        |   Return", "|");
        System.out.printf("%s\n", "+ ========== + ============================================== +");

        do {
            errorInput = false;
            try {
                System.out.printf("%-20s >>> ", "Enter Aspect [1-5]");
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

        Iterator iterator = packages.iterator();
        MealPackage[] result = new MealPackage[packages.getNumberOfEntries()];

        if (!packages.isEmpty()) {
            int index = 0;
            while (iterator.hasNext()) {
                result[index] = (MealPackage) iterator.next();
                index++;
            }
            String strOutput = "\n+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +\n";
            strOutput += String.format("|%2s %-15s %-30s %-30s %-20s|\n", "", "Package No.", "Package ID",
                    "Package Name", "Package Price($)");
            strOutput += "+ -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+- +\n";

            switch (choice) {
                case 1: {
                    MealPackageSort.quickSort(result, result.length, MealPackage.packageNameComparator, MealPackageSort.ASC);
                }
                break;
                case 2: {
                    MealPackageSort.quickSort(result, result.length, MealPackage.packagePriceComparator, MealPackageSort.ASC);
                }
                break;
                case 3: {
                    MealPackageSort.quickSort(result, result.length, MealPackage.packageNameComparator, MealPackageSort.DES);
                }
                break;
                case 4: {
                    MealPackageSort.quickSort(result, result.length, MealPackage.packagePriceComparator, MealPackageSort.DES);
                }
                break;
            }

            for (int i = 0; i < result.length; i++) {
                MealPackage currentMealPackage = result[i];
                strOutput += String.format("|%2s %-15s %-30s %-30s %-20.2f|\n", "", (i + 1) + "_",
                        currentMealPackage.getPackageId(), currentMealPackage.getPackageName(),
                        currentMealPackage.getPackagePrice());
            }
            strOutput += String.format("%s\n",
                    "+ _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ +");
            System.out.println(strOutput);
            System.out.print("Press enter to return...");
            input.nextLine();
        }
    }

    public void searchPackage() {
        boolean continueInput = false;
        int choice = DEFAULT_CHOICE;

        do {
            System.out.printf("\n%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %36s\n", "|  No.       |   Search By", " |");
            System.out.printf("%s\n", "+ ========== + ============================================== +");
            System.out.printf("%s %33s\n", "|  1_        |   Package Name", " |");
            System.out.printf("%s %32s\n", "|  2_        |   Package Price", " |");
            System.out.printf("%s %25s\n", "|  3_        |   Package Name & Price", " |");
            System.out.printf("%s %39s\n", "|  4_        |   Return", "|");
            System.out.printf("%s\n", "+ ========== + ============================================== +");

            do {
                errorInput = false;
                try {
                    System.out.printf("%-20s >>> ", "Enter Aspect [1-4]");
                    choice = input.nextInt();
                    if (choice < 1 || choice > 4) {
                        errorInput = true;
                        System.out.println(RED + "Please enter a valid choice! [1-4]" + RESETCOLOR);
                    }
                } catch (InputMismatchException ex) {
                    errorInput = true;
                    System.out.println(RED + "Please enter an integer only! [1-4]" + RESETCOLOR);
                } finally {
                    input.nextLine();
                }
            } while (errorInput);

            ListInterface<MealPackage> result = null;

            switch (choice) {
                case 1: {
                    String searchSubString;
                    System.out.printf("%-45s >>> ", "Contains of?");
                    searchSubString = input.nextLine();
                    result = searchPackage(searchSubString);
                }
                break;
                case 2: {
                    float[] priceRange = new float[2]; // [0] stores start price, [1] stores end price
                    do {
                        try {
                            errorInput = false;
                            System.out.printf("%-45s >>> ", "Enter Start Price (EXP:XX.xx)");
                            priceRange[0] = input.nextFloat();
                            if (priceRange[0] < 0) {
                                System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                                errorInput = true;
                            }
                        } catch (InputMismatchException ex) {
                            errorInput = true;
                            System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                        } finally {
                            input.nextLine();
                        }
                    } while (errorInput);

                    do {
                        try {
                            errorInput = false;
                            System.out.printf("%-45s >>> ", "Enter End Price (EXP:XX.xx)");
                            priceRange[1] = input.nextFloat();
                            if (priceRange[1] < 0) {
                                System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                                errorInput = true;
                            }
                        } catch (InputMismatchException ex) {
                            errorInput = true;
                            System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                        } finally {
                            input.nextLine();
                        }
                    } while (errorInput);

                    result = searchPackage(priceRange);
                }
                break;
                case 3: {
                    String searchSubString;
                    float[] priceRange = new float[2]; // [0] stores start price, [1] stores end price

                    System.out.printf("%-45s >>> ", "Contains of? (ENTER to Skip)");
                    searchSubString = input.nextLine();

                    do {
                        try {
                            errorInput = false;
                            System.out.printf("%-45s >>> ", "Enter Start Price (EXP:XX.xx | -1 Exit)");
                            priceRange[0] = input.nextFloat();
                            if (priceRange[0] < 0 && priceRange[0] != -1) {
                                errorInput = true;
                                System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                            }
                        } catch (InputMismatchException ex) {
                            errorInput = true;
                            System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                        } finally {
                            input.nextLine();
                        }
                    } while (errorInput);

                    do {
                        try {
                            errorInput = false;
                            System.out.printf("%-45s >>> ", "Enter End Price (EXP:XX.xx | -1 Exit)");
                            priceRange[1] = input.nextFloat();
                            if (priceRange[0] < 0 && priceRange[0] != -1) {
                                System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                                errorInput = true;
                            }
                        } catch (InputMismatchException ex) {
                            errorInput = true;
                            System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                        } finally {
                            input.nextLine();
                        }
                    } while (errorInput);

                    result = searchPackage(searchSubString, priceRange);
                }
                break;
            }

            System.out.println();
            if (result.isEmpty() || result == null) {
                System.out.println(RED + "No Package Found!" + RESETCOLOR);
            } else {
                System.out.println(GREEN + "Result :" + RESETCOLOR);
                System.out.print(result.toString());

            }

            do {
                errorInput = false;
                System.out.printf("%-45s >>> ", "Continue to Search? (Y/y = Yes, N/n = No)");
                switch (input.next().charAt(0)) {
                    case 'Y':
                    case 'y':
                        continueInput = true;
                        break;
                    case 'N':
                    case 'n':
                        continueInput = false;
                        break;
                    default:
                        errorInput = true;
                        System.out.println(RED + "Please enter a character! (Y/y = Yes, N/n = No)" + RESETCOLOR);
                }
                input.nextLine();
            } while (errorInput);
        } while (continueInput);
    }

    public ListInterface<MealPackage> searchPackage(String searchSubString) {
        Iterator iterator = packages.iterator();
        ListInterface<MealPackage> result = new LinkedList<>();
        while (iterator.hasNext()) {
            MealPackage currentPackage = (MealPackage) iterator.next();
            if (currentPackage.getPackageName().contains(searchSubString)) {
                result.add(currentPackage);
            }
        }
        return result;
    }

    public ListInterface<MealPackage> searchPackage(float[] price) {
        Iterator iterator = packages.iterator();
        ListInterface<MealPackage> result = new LinkedList<>();
        while (iterator.hasNext()) {
            MealPackage currentPackage = (MealPackage) iterator.next();
            if (currentPackage.getPackagePrice() >= price[0] && currentPackage.getPackagePrice() <= price[1]) {
                result.add(currentPackage);
            }
        }
        return result;
    }

    public ListInterface<MealPackage> searchPackage(String searchSubString, float[] price) {
        Iterator iterator = packages.iterator();
        ListInterface<MealPackage> result = new LinkedList<>();
        while (iterator.hasNext()) {
            MealPackage currentPackage = (MealPackage) iterator.next();
            if (searchSubString.equals("") && price[0] == -1 && price[1] == -1) {
                result.add(currentPackage);
            } else if (!searchSubString.equals("") && price[0] == -1 && price[1] == -1) {
                if (currentPackage.getPackageName().contains(searchSubString)) {
                    result.add(currentPackage);
                }
            } else if (searchSubString.equals("") && price[0] != -1 && price[1] == -1) {
                if (currentPackage.getPackagePrice() >= price[0]) {
                    result.add(currentPackage);
                }
            } else if (searchSubString.equals("") && price[0] == -1 && price[1] != -1) {
                if (currentPackage.getPackagePrice() <= price[1]) {
                    result.add(currentPackage);
                }
            } else if (searchSubString.equals("") && price[0] != -1 && price[1] != -1) {
                if (currentPackage.getPackagePrice() >= price[0] && currentPackage.getPackagePrice() <= price[1]) {
                    result.add(currentPackage);
                }
            } else if (!searchSubString.equals("") && price[0] != -1 && price[1] != -1) {
                if (currentPackage.getPackageName().contains(searchSubString)
                        && currentPackage.getPackagePrice() >= price[0]
                        && currentPackage.getPackagePrice() <= price[1]) {
                    result.add(currentPackage);
                }
            } else if (!searchSubString.equals("") && price[0] != -1 && price[1] == -1) {
                if (currentPackage.getPackageName().contains(searchSubString)
                        && currentPackage.getPackagePrice() >= price[0]) {
                    result.add(currentPackage);
                }
            } else if (!searchSubString.equals("") && price[0] == -1 && price[1] != -1) {
                if (currentPackage.getPackageName().contains(searchSubString)
                        && currentPackage.getPackagePrice() <= price[1]) {
                    result.add(currentPackage);
                }
            }

        }
        return result;
    }

    public static MealPackage buildNewPackage() {
        boolean errorInput = false;
        String packageName;
        float packagePrice = 0;
        System.out.println();
        do {
            errorInput = false;
            System.out.printf("%-45s >>> ", "Enter Package Name (Less than 4 words)");
            packageName = input.nextLine();
            if (Validator.countWord(packageName) > 3) {
                errorInput = true;
                System.out.println(RED + "Please enter less than 4 words!" + RESETCOLOR);
            }
        } while (errorInput);

        do {
            try {
                errorInput = false;
                System.out.printf("%-45s >>> ", "Enter Package Price (EXP:XX.xx)");
                packagePrice = input.nextFloat();
                if (packagePrice < 0) {
                    errorInput = true;
                    System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
                }
            } catch (InputMismatchException ex) {
                errorInput = true;
                System.out.println(RED + "Please enter a valid price!" + RESETCOLOR);
            } finally {
                input.nextLine();
            }
        } while (errorInput);

        return new MealPackage(packageName, packagePrice);
    }

    public void initializeData() {
        File file;

        try {
            file = new File(PACKAGE_FILE);
            if (file.isFile()) {
                ObjectInputStream ooStream = new ObjectInputStream(new FileInputStream(file));
                packages = (ListInterface<MealPackage>) ooStream.readObject();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (ClassNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }

        try {
            file = new File(MEAL_FILE);
            if (file.isFile()) {
                ObjectInputStream ooStream = new ObjectInputStream(new FileInputStream(file));
                meals = (ListInterface<Meal>) ooStream.readObject();
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
            file = new File(MEAL_FILE);
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(meals);
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
        try {
            file = new File(PACKAGE_FILE);
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(packages);
        } catch (FileNotFoundException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        } catch (IOException ex) {
            System.out.println(RED + ex + RESETCOLOR);
        }
    }

    public MealPackage getMealPackage() {

        MealPackage targetedPackage = null;
        System.out.println(packagesString());
        if (!packages.isEmpty()) {
            // Implement Edit here
            int givenPosition = -1;

            // Step2: Prompt and Read for new input
            do {
                errorInput = false;
                try {
                    System.out.printf("%-45s >>> ", "Enter Package No.to Edit [-1 to Return]");
                    givenPosition = input.nextInt();
                } catch (InputMismatchException ex) {
                    System.out.println(RED + "Please enter a valid input (Integer)!" + RESETCOLOR);
                    errorInput = true;
                } finally {
                    input.nextLine();
                }
                if (givenPosition != -1) {
                    targetedPackage = packages.get(givenPosition);
                }
            } while (errorInput);

        }
        return targetedPackage;

    }
}
