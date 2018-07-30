/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.dimyadi.classes.times.posAlgo;

public class EarthHeading {

    private double mHeading;
    private long mMetres;

    EarthHeading(double heading, long metres) {
        mHeading = heading;
        mMetres = metres;
    }

    public double getHeading() {
        return mHeading;
    }

    public long getKiloMetres() {
        return mMetres / 1000;
    }
}
