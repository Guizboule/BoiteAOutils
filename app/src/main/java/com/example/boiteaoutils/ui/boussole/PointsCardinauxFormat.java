package com.example.boiteaoutils.ui.boussole;
import android.content.Context;

import com.example.boiteaoutils.R;

public class PointsCardinauxFormat {
    private static final int[] sides = {0, 45, 90, 135, 180, 225, 270, 315, 360}; //9
    private static String[] names = null;

    public PointsCardinauxFormat(Context context) {
        initNomsPointsCardinaux(context);
    }

    public String format(float azimuth) {
        int iAzimuth = (int)azimuth;
        int index = findClosestIndex(iAzimuth);
        return iAzimuth + "° " + names[index];
    }

    private void initNomsPointsCardinaux(Context context) {
        // N est 2 fois pour 0 et 360°

        if (names == null) {
            names = new String[]{context.getString(R.string.north),
                    context.getString(R.string.northeast),
                    context.getString(R.string.east),
                    context.getString(R.string.southeast),
                    context.getString(R.string.south),
                    context.getString(R.string.southwest),
                    context.getString(R.string.west),
                    context.getString(R.string.northwest),
                    context.getString(R.string.north)
            };
        }
    }

    private static int findClosestIndex(int target) {

        int i = 0, j = sides.length, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (target < sides[mid]) {

                if (mid > 0 && target > sides[mid - 1]) {
                    return getClosest(mid - 1, mid, target);
                }


                j = mid;
            } else {
                if (mid < sides.length-1 && target < sides[mid + 1]) {
                    return getClosest(mid, mid + 1, target);
                }
                i = mid + 1;
            }
        }

        return mid;
    }

    //Méthode pour comparer la valeur la plus proche de la target
    //On trouve la plus proche en faisant la différence entre les 2 valeurs

    private static int getClosest(int index1, int index2, int target) {
        if (target - sides[index1] >= sides[index2] - target) {
            return index2;
        }
        return index1;
    }
}