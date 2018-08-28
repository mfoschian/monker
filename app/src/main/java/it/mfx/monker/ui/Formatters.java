package it.mfx.monker.ui;

import java.text.DecimalFormat;
import java.text.ParseException;

public class Formatters {

    protected static DecimalFormat df = new DecimalFormat("#.##");

    public static String renderAmount(float amount) {
        return df.format(amount);
    }

    public static float parseAmount(String amount) {
        try {
            Number res = df.parse(amount);
            return res.floatValue();
        }
        catch( ParseException pe ) {
            pe.printStackTrace();
            return 0.0f;
        }
    }
}
