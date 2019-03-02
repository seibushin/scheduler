/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.model;

public class ReadFrom {
    private int trans;
    private String reads;
    private int from;

    public ReadFrom(int trans, String reads, int from) {
        this.trans = trans;
        this.reads = reads;
        this.from = from;
    }

    @Override
    public boolean equals(Object obj) {
        return trans == ((ReadFrom)obj).trans && reads == ((ReadFrom)obj).reads && from  == ((ReadFrom)obj).from;
    }

    @Override
    public String toString() {
        return "(T" + from + ", " + reads + ", " + "T" + trans + ")";
    }
}
