
package com.hasan.uberclone.models;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserLocation {


    private String g;
    private List<Double> l = null;

    public UserLocation() {
    }

    public UserLocation(String g, List<Double> l) {

        this.g = g;
        this.l = l;
    }



    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public List<Double> getL() {
        return l;
    }

    public void setL(List<Double> l) {
        this.l = l;
    }

    @NotNull
    @Override
    public String toString() {
        return "UserLocation{" +
                ", g='" + g + '\'' +
                ", l=" + l +
                '}';
    }
}
