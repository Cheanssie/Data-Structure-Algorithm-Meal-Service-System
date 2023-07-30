package Entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Voucher implements Comparable<Voucher>, Serializable {

    private String voucherCode;
    private double discountRate;
    private String expiryDate;
    private double minSpend;

    public Voucher(String voucherCode, double discountRate, String expiryDate, double minSpend) {
        this.voucherCode = voucherCode;
        this.discountRate = discountRate;
        this.expiryDate = expiryDate;
        this.minSpend = minSpend;
    }

    // Getter
    public String getVoucherCode() {
        return voucherCode;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public double getMinSpend() {
        return minSpend;
    }

    // Setter
    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setMinSpend(float minSpend) {
        this.minSpend = minSpend;
    }

    // Method
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
        final Voucher other = (Voucher) obj;
        if (this.voucherCode != other.voucherCode) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "Voucher Code : " + voucherCode + ", Discount Rate : " + discountRate + ", Expiry Date : " + expiryDate
                + ", Minimum Spend : " + minSpend + "\n";
    }

    @Override
    public int compareTo(Voucher v) {
        return this.voucherCode.compareTo(((Voucher) v).getVoucherCode());
    }

    public static Comparator<Voucher> voucherCodeComparator = (Voucher v1, Voucher v2) -> {

        return v1.voucherCode.compareTo(v2.voucherCode);
    };

    public static Comparator<Voucher> voucherDiscountComparator = (Voucher v1, Voucher v2) -> {

        return Double.compare(v1.discountRate, v2.discountRate);
    };

    public static Comparator<Voucher> voucherDateComparator = (Voucher v1, Voucher v2) -> {
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        sdfrmt.setLenient(false);
        Date v1Date = null;
        Date v2Date = null;
        try {
            v1Date = sdfrmt.parse(v1.expiryDate);
            v2Date = sdfrmt.parse(v2.expiryDate);
        } /* Date format is invalid */ catch (ParseException e) {
            System.out.println(e);
        }
        return v1Date.compareTo(v2Date);
    };

    public static Comparator<Voucher> voucherMinSpendComparator = (Voucher v1, Voucher v2) -> {

        return Double.compare(v1.minSpend, v2.minSpend);
    };
}
