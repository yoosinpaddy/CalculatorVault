package com.calculator.vault.gallery.locker.hide.data.smartkit.calcnew;

/**
 * Created by vahida on 13-03-2016.
 */

import android.util.Log;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;

import java.util.Iterator;

/**
 * A subclass of DoubleEvaluator that supports SQRT function.sinh⁻¹
 */
public class ExtendedDoubleEvaluator extends DoubleEvaluator {
    /**
     * Defines the new function (square root).
     */
    private static final Function SQRT = new Function("√", 1);
    private static final Function CBRT = new Function("\u00B3\u221A", 1);
    private static final Function ASIND = new Function("sin⁻¹", 1);
    private static final Function ACOSD = new Function("cos⁻¹", 1);
    private static final Function ATAND = new Function("tan⁻¹", 1);
    private static final Function ASINDH = new Function("sinh⁻¹(", 1);
    private static final Function LOG2 = new Function("log₂", 1);
    private static final Parameters PARAMS;

    static {
        // Gets the default DoubleEvaluator's parameters
        PARAMS = DoubleEvaluator.getDefaultParameters();
        // add the new sqrt function to these parameters
        PARAMS.add(SQRT);
        PARAMS.add(CBRT);
        PARAMS.add(ASIND);
        PARAMS.add(ACOSD);
        PARAMS.add(ATAND);
        PARAMS.add(LOG2);
        PARAMS.add(ASINDH);
    }

    //    Double.valueOf(Math.log2(((Double)arguments.next()).doubleValue()));
    public ExtendedDoubleEvaluator() {
        super(PARAMS);
    }

    @Override
    public Double evaluate(Function function, Iterator<Double> arguments, Object evaluationContext) {
        if (function == SQRT) {
            // Implements the new function
            return Math.sqrt(arguments.next());
        } else if (function == CBRT) {
            return Math.cbrt(arguments.next());
        } else if (function == ASINDH) {
            return Math.log(arguments.next() + Math.sqrt(((arguments.next() * arguments.next()) + 1)));
        } else if (function == ASIND) {
            return Math.toDegrees(Math.asin(arguments.next()));
        } else if (function == ACOSD) {
            return Math.toDegrees(Math.acos(arguments.next()));
        } else if (function == ATAND) {
            return Math.atan(arguments.next());
        } else if (function == LOG2) {
            Log.e("vaaa", "inside");
            return Double.valueOf(Math.log10(((Double) arguments.next()).doubleValue()) / (Math.log10(2)));
        } else {
            // If it's another function, pass it to DoubleEvaluator
            return super.evaluate(function, arguments, evaluationContext);
        }
    }
}
//return Double.valueOf(Math.log10(((Double)arguments.next()).doubleValue())/(Math.log10(2)));