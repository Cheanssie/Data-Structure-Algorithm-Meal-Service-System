package Entity;

import java.io.Serializable;

public class Meal implements Comparable<Meal>, Cloneable, Serializable {

    //Uses to assign ID for each package automatically
    private static int mealCount = 0;

    //Objects attributes
    private int mealId;
    private String mealName;
    private String mealDesc;

    //Constructor
    public Meal(String mealName, String mealDesc) {
        this.mealId = mealCount;
        this.mealName = mealName;
        this.mealDesc = mealDesc;
        mealCount++;
    }

    /* Section : Mutators */
    //Getter
    public int getMealId() {
        return this.mealId;
    }

    public String getMealName() {
        return this.mealName;
    }

    public String getMealDesc() {
        return this.mealDesc;
    }

    public static int getMealCount() {
        return mealCount;
    }

    //Setter
    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setMealDesc(String mealDesc) {
        this.mealDesc = mealDesc;
    }

    public static void setMealCount(int mealCount) {
        Meal.mealCount = mealCount;
    }

    @Override
    public String toString() {
        return String.format("[ Meal ID: " + mealId + " | Meal Name: " + mealName + " | Meal Description: " + mealDesc + " ]\n");
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    @Override
    public int compareTo(Meal t) {
        return this.mealName.compareTo(((Meal) t).getMealName());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Meal)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        Meal targetObj = (Meal) obj;
        return targetObj.mealId == this.mealId;
    }
}
