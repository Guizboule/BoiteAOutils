package com.example.boiteaoutils.ui.analyseurDeSon;

public class FFT {

    // La taille du tableau doit être un multiple de deux
    // Algo de Cooley-Tukey
    public static Complex[] fft(Complex[] x) {
        int n = x.length;

        if (n == 1) return new Complex[]{x[0]};

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // FFT des éléments pairs
        Complex[] even = new Complex[n / 2];
        for (int k = 0; k < n / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // FFT des éléments impairs
        Complex[] odd = even;  // reuse the array
        for (int k = 0; k < n / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + n / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

}