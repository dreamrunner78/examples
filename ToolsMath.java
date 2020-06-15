package com.bas.tools;

import com.tdunning.math.stats.ArrayDigest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ToolsMath {

/*    public static double computePercentiles(PriorityQueue<Double> values, double percentile) {
        int index = (int) Math.ceil((percentile / 100) * values.size());
        Iterator value = values.iterator();
        int blocker = 0;
        Double retvalue = 0.0;
        while (value.hasNext() && blocker <= (index - 1)) {
            //System.out.println(value.next());
            blocker++;
            retvalue = (Double) value.next();
        }
        return retvalue;
    }

    public static int getClosestPercentilePriorityQueue(PriorityQueue<Double> values, Double amount) {
        List<Double> percentiles = new ArrayList<>();
        percentiles.add(computePercentiles(values, 25));
        percentiles.add(computePercentiles(values, 50));
        percentiles.add(computePercentiles(values, 75));
        percentiles.add(computePercentiles(values, 100));

        Double distance = Math.abs(percentiles.get(0) - amount);
        int idx = 0;
        for (int c = 1; c < percentiles.size(); c++) {
            Double cdistance = Math.abs(percentiles.get(c) - amount);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        Double theClosest = percentiles.get(idx);
        int percentile = 0;
        if (idx == 0)
            percentile = 25;
        else if (idx == 1)
            percentile = 50;
        else if (idx == 2)
            percentile = 75;
        else if (idx == 3)
            percentile = 100;

        return percentile;

    }

  */

    public static Double divide(Double a, Double b) {
        if (b == 0)
            return a;
        else
            return a / b;
    }

    public static int getClosestPercentileTDigest(ArrayDigest mainDigest, Double amount) {
        List<Double> percentiles = new ArrayList<>();
        percentiles.add(mainDigest.quantile(0.25));
        percentiles.add(mainDigest.quantile(0.5));
        percentiles.add(mainDigest.quantile(0.75));
        percentiles.add(mainDigest.quantile(1));

        Double distance = Math.abs(percentiles.get(0) - amount);
        int idx = 0;
        for (int c = 1; c < percentiles.size(); c++) {
            Double cdistance = Math.abs(percentiles.get(c) - amount);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        Double theClosest = percentiles.get(idx);
        int percentile = 0;
        if (idx == 0)
            percentile = 25;
        else if (idx == 1)
            percentile = 50;
        else if (idx == 2)
            percentile = 75;
        else if (idx == 3)
            percentile = 100;

        return percentile;

    }

    public static int getClosestPercentiles(List<Double> percentiles, Double amount) {

        Double distance = Math.abs(percentiles.get(0) - amount);
        int idx = 0;
        for (int c = 1; c < percentiles.size(); c++) {
            Double cdistance = Math.abs(percentiles.get(c) - amount);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }

        int percentile = 0;
        if (idx == 0)
            percentile = 25;
        else if (idx == 1)
            percentile = 50;
        else if (idx == 2)
            percentile = 75;
        else if (idx == 3)
            percentile = 100;

        return percentile;

    }

    public static Boolean isDouble(String input) {
        String DOUBLE_PATTERN = "[0-9]+(\\.){0,1}[0-9]*";
        input = input.replace(",",".");
        return Pattern.matches(DOUBLE_PATTERN, input);
    }
}
