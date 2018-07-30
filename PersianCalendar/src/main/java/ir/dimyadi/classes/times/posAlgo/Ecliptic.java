/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.dimyadi.classes.times.posAlgo;

public class Ecliptic {

    public double λ; //λ the ecliptic longitude
    public double β; //β the ecliptic latitude
    double Δ; //distance  in km

    Ecliptic() {
    }

    Ecliptic(double longitude, double latitude) {
        this.λ = longitude;
        this.β = latitude;
    }

    Ecliptic(double longitude, double latitude, double radius) {
        this.λ = longitude;
        this.β = latitude;
        this.Δ = radius;
    }
}


