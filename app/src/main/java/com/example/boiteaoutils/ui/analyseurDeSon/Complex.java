package com.example.boiteaoutils.ui.analyseurDeSon;

import java.util.Objects;

public class Complex {
    private final double re;   // Partie réelle
    private final double im;   // Partie imaginaire

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }


    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im < 0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // retourne le module
    public double abs() {
        return Math.hypot(re, im);
    }

    // retourne l'argument du nombre complexe
    public double phase() {
        return Math.atan2(im, re);
    }

    // Additionne deux nombres
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    // Version statique de plus
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }


    // Soustrait deux nombres
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    // Multiplie deux nombres
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    // Retourne un nombre de valeur this * alpha
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // Retourne le conjugué du nombre
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    public Complex reciprocal() {
        double scale = re * re + im * im;
        return new Complex(re / scale, -im / scale);
    }

    // Partie réelle
    public double re() {
        return re;
    }

    // Partie imaginaire
    public double im() {
        return im;
    }

    // Retourne this / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // Indique si deux complexe sont égaux
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

}