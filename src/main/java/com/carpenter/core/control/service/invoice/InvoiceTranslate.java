package com.carpenter.core.control.service.invoice;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

import java.math.BigDecimal;

public class InvoiceTranslate {

    private static final String ZERO = "zero";
    private static String[] oneToNine = {
//            "one", "two", "three", "four", "five", "six", "seven", "height", "nine";
            "jeden", "dwa", "trzy", "cztery", "piec", "szesc", "siedem", "osiem", "dziewiec"
    };

    private static String[] tenToNinteen = {
//            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
            "dziesiec", "jedynascie", "dwanascie", "trzynascie", "czternascie", "pietnascie", "szesnascie", "siedemnascie", "osiemnascie", "dziewietnascie"
    };

    private static String[] dozens = {
            "dziesiec", "dwadziescia", "trzydziesci", "czterdziesci", "piedziesiat", "szesdziesiat", "siedemdiesiat", "osiemdziesiat", "dziewiedziesiat"
    };

    public static String solution(BigDecimal number) {
        if (number.equals(BigDecimal.ZERO)) {
            return ZERO;
        }

//        String[] splitedNumber = number.toPlainString().split("\\.");
//        String decimal = splitedNumber[0];
//        int decimalNumber = Integer.parseInt(decimal);
//        String decimalGenerated = generate(decimalNumber).trim();

        ULocale locale = new ULocale("Pl");

        NumberFormat format = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
        String result = format.format(number);

//        String afterPeriod = splitedNumber[1];

//        return decimalGenerated + " i " + afterPeriod + "/100";

        return result;
    }

    public static String generate(int number) {
        if (number >= 1000000000) {
            return generate(number / 1000000000) + " miliard " + generate(number % 1000000000);
        } else if (number >= 1000000) {
            return generate(number / 1000000) + " milion " + generate(number % 1000000);
        } else if (number >= 1000) {
            return generate(number / 1000) + " tysiecy " + generate(number % 1000);
        } else if (number >= 100) {
            return generate(number / 100) + " sto " + generate(number % 100);
        }

        return generate1To99(number);
    }

    private static String generate1To99(int number) {
        if (number == 0)
            return "";

        if (number <= 9)
            return oneToNine[number - 1];
        else if (number <= 19)
            return tenToNinteen[number % 10];
        else {
            return dozens[number / 10 - 1] + " " + generate1To99(number % 10);
        }
    }
}
