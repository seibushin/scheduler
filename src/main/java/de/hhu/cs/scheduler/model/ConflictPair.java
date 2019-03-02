/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.model;

public class ConflictPair {
    private Operation op1;
    private Operation op2;

    public ConflictPair(Operation op1, Operation op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    public Operation getOp1() {
        return op1;
    }

    public void setOp1(Operation op1) {
        this.op1 = op1;
    }

    public Operation getOp2() {
        return op2;
    }

    public void setOp2(Operation op2) {
        this.op2 = op2;
    }
}
