package Entity;

//Import ADT and Entity
import ADT.*;
import java.io.Serializable;
import java.util.Comparator;

public class MealPackage implements Comparable<MealPackage>, Cloneable, Serializable {

    //Uses to assign ID for each package automatically
    private static int packageCount;

    //Objects attributes
    private int packageId;
    private String packageName;
    private float packagePrice;
    private ListInterface<Meal> packageMeals;

    public MealPackage() {
        this.packageId = packageCount;
        packageCount++;
    }

    public MealPackage(String packageName, float packagePrice) {
        this.packageId = packageCount;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packageMeals = new LinkedList<Meal>();
        packageCount++;
    }

    /* Section : Mutators */
    //Setter
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setPackagePrice(float packagePrice) {
        this.packagePrice = packagePrice;
    }

    public static void setPackageCount(int packageCount) {
        MealPackage.packageCount = packageCount;
    }

    //Getter
    public int getPackageId() {
        return this.packageId;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public float getPackagePrice() {
        return this.packagePrice;
    }

    public static int getPackageCount() {
        return MealPackage.packageCount;
    }

    public ListInterface<Meal> getMealList() {
        return packageMeals;
    }

    //Methods
    public void addMeal(Meal newMeal) {
        packageMeals.add(newMeal);
    }

    public boolean addMeal(Meal newMeal, int newPosition) {
        return packageMeals.add(newMeal, newPosition);
    }

    public Meal getMeal(int givenPosition) {
        return packageMeals.get(givenPosition);
    }

    public Meal removeMeal(int givenPosition) {
        return packageMeals.remove(givenPosition);
    }

    public boolean editMeal(Meal newMeal, int givenPosition) {
        return packageMeals.replace(newMeal, givenPosition);
    }

    public boolean isEmpty() {
        return packageMeals.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("[ Package ID: " + packageId + " | Package Name: " + packageName + " | Package Price: " + packagePrice + " ]\n");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MealPackage)) {
            return false;
        }
        final MealPackage targetObj = (MealPackage) obj;

        if (targetObj.getPackageName().toUpperCase().equals(this.packageName.toUpperCase())) {
            return true;
        }

        return false;
    }

    public static Comparator<MealPackage> packageNameComparator = (MealPackage m1, MealPackage m2) -> {

        return m1.packageName.compareTo(m2.packageName);
    };

    public static Comparator<MealPackage> packagePriceComparator = (MealPackage m1, MealPackage m2) -> {

        return Float.compare(m1.packagePrice, m2.packagePrice);
    };

    @Override
    public int compareTo(MealPackage o) {
        return this.packageName.compareTo(o.packageName);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

}
