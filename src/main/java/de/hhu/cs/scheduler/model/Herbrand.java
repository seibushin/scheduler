/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.model;

import java.util.List;

public class Herbrand {
    private String var;
    private int trans;
    private List<Herbrand> her;

    public Herbrand(int trans, String var, List<Herbrand> her) {
        this.var = var;
        this.trans = trans;
        this.her = her;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public int getTrans() {
        return trans;
    }

    public void setTrans(int trans) {
        this.trans = trans;
    }

    public List<Herbrand> getHer() {
        return her;
    }

    public void setHer(List<Herbrand> her) {
        this.her = her;
    }

    @Override
    public String toString() {
        String s = "f" + trans + "," + var + "(";
        boolean first = true;
        for (Herbrand h : her) {
            s += (first? "": ", ") + h;
        }
        s += ")";

        return s;
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
