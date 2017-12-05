/*    */
package com.payfort.sdk.android.dependancies.utils;
/*    */ 
/*    */

import java.util.regex.Pattern;

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Validate
/*    */ {
    /*    */
    public static void notNull(Object arg, String name)
/*    */ {
/* 18 */
        if (arg == null) {
/* 19 */
            throw new NullPointerException("Argument '" + name + "' cannot be null");
/*    */
        }
/*    */
    }

    /*    */
/*    */ 
/*    */ 
/*    */ 
/*    */
    public static boolean luhnValidation(String number)
/*    */ {
/* 28 */
        int s1 = 0;
        int s2 = 0;
/* 29 */
        String reverse = new StringBuffer(number).reverse().toString();
/* 30 */
        for (int i = 0; i < reverse.length(); i++) {
/* 31 */
            int digit = Character.digit(reverse.charAt(i), 10);
/* 32 */
            if (i % 2 == 0)
/*    */ {
/* 34 */
                s1 += digit;
/*    */
            } else {
/* 36 */
                s2 += 2 * digit;
/* 37 */
                if (digit >= 5) {
/* 38 */
                    s2 -= 9;
/*    */
                }
/*    */
            }
/*    */
        }
/*    */     
/* 43 */
        if ((s1 + s2) % 10 == 0) {
/* 44 */
            return true;
/*    */
        }
/*    */     
/* 47 */
        return false;
/*    */
    }

    /*    */
/*    */ 
/*    */ 
/*    */
    public static String getPaymentMethodOptionName(String cardBin)
/*    */ {
/*    */
        try
/*    */ {
/* 56 */
            Pattern VISA_PATTERN = Pattern.compile("^4");
/* 57 */
            Pattern VISA_ELECTRON_PATTERN = Pattern.compile("^4(026|17500|405|508|844|91[37])");
/* 58 */
            Pattern MASTERCARD_PATTERN = Pattern.compile("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)");
/* 59 */
            Pattern AMEX_PATTERN = Pattern.compile("^3[47]");
/*    */       
/* 61 */
            if (cardBin == null) {
/* 62 */
                return null;
/*    */
            }
/*    */       
/* 65 */
            if (VISA_PATTERN.matcher(cardBin).find())
/* 66 */ return "VISA";
/* 67 */
            if (VISA_ELECTRON_PATTERN.matcher(cardBin).find())
/* 68 */ return "VISA";
/* 69 */
            if (MASTERCARD_PATTERN.matcher(cardBin).find())
/* 70 */ return "MASTERCARD";
/* 71 */
            if (AMEX_PATTERN.matcher(cardBin).find()) {
/* 72 */
                return "AMEX";
/*    */
            }
/*    */
        } catch (Exception localException) {
        }
/* 75 */
        return null;
/*    */
    }
/*    */
}
