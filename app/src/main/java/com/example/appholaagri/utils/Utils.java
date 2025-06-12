package com.example.appholaagri.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Utils {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes());

            // Chuyển byte[] sang String
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String formatCurrency(String amount) {
        try {
            long value = Long.parseLong(amount);
            return String.format("%,d đ", value); // <-- giữ dấu phẩy
        } catch (NumberFormatException e) {
            return amount + " đ";
        }
    }
    public static String formatNumberWithCommas(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        decimalFormat.setGroupingSize(3);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat.format(number);
    }

    // Chuyển chuỗi định dạng xxx,xxx,xxx về số nguyên
    public static long parseNumberFromFormattedString(String formattedNumber) {
        try {
            return Long.parseLong(formattedNumber.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
