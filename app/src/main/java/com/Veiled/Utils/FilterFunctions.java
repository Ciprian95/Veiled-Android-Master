package com.Veiled.Utils;

public class FilterFunctions {
    public static double CosineInterpolate(
            double y1,double y2,
            double mu) {
        double mu2;

        mu2 = (1 - Math.cos(mu * Math.PI)) / 2;
        return (y1 * (1 - mu2) + y2 * mu2);
    }
}
