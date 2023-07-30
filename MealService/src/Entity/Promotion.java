package Entity;

import ADT.*;
import java.io.Serializable;
import java.util.Iterator;

public class Promotion implements Comparable<Promotion>, Serializable {

    private static int promotionCount = 0;

    private SetInterface<Voucher> vouchers;
    private int promotionId;
    private String promotionName;
    private String promotionDesc;

    public Promotion(String promotionName, String promotionDesc) {
        this.promotionId = promotionCount;
        this.promotionName = promotionName;
        this.promotionDesc = promotionDesc;
        this.vouchers = new DoublyLinkedSet<>();
        promotionCount++;
    }

    // Getter
    public int getPromotionId() {
        return promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public String getPromotionDesc() {
        return promotionDesc;
    }

    public static int getPromotionCount() {
        return Promotion.promotionCount;
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
    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public void setPromotionDesc(String promotionDesc) {
        this.promotionDesc = promotionDesc;
    }

    public static void setPromotionCount(int promotionCount) {
        Promotion.promotionCount = promotionCount;
    }

    // Method
    public void addVoucher(Voucher newVoucher) {
        vouchers.add(newVoucher);
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
        final Promotion other = (Promotion) obj;
        if (this.promotionName.toUpperCase().equals(other.promotionName.toUpperCase())) {
            return true;
        }
        return false;
    }

    public String toString() {
        String str = "";
        if (!vouchers.isEmpty()) {
            str += vouchers.toString();
        }
        return String.format("Promotion ID : " + promotionId + "\nPromotion Name : " + promotionName + "\nPromotion Description : "
                + promotionDesc + "\n" + str);
    }

    public void displayAllVoucher() {
        String str = "";
        int count = 0;
        Iterator iterator = vouchers.iterator();
        str += "\nPromotion ID : " + promotionId + "\nPromotion Name : " + promotionName + "\nPromotion Description : "
                + promotionDesc + "\n";
        if (!vouchers.isEmpty()) {
            str += String.format("+----------------------------------------------------------+\n");
            str += String.format("| No. | Voucher Code | Discount | Expiry Date | Min. Spend |\n");
            str += String.format("+----------------------------------------------------------+\n");
            while (iterator.hasNext()) {
                Voucher currentVoucher = (Voucher) iterator.next();
                double discount = currentVoucher.getDiscountRate();
                discount *= 100.0;
                count++;
                str += String.format("| %3s | %-12s | %7.2s%% | %-11s | RM%-8.2s |\n", count, currentVoucher.getVoucherCode(), discount, currentVoucher.getExpiryDate(), currentVoucher.getMinSpend());
            }
            str += String.format("+----------------------------------------------------------+");
        }
        System.out.println(str);
    }

    @Override
    public int compareTo(Promotion p) {
        return this.promotionName.compareTo(((Promotion) p).getPromotionName());// if equal
    }

}
