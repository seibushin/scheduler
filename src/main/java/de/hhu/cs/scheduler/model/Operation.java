/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.model;

public class Operation {
    private int transaction;
    private OperationType type;
    private String variable;

    public Operation(OperationType type, int transaction, String variable) {
        this.transaction = transaction;
        this.type = type;
        this.variable = variable;
    }

    public Operation(String op) {
        parse(op);
    }

    private void parse(String op) {
        System.out.println(op);
        transaction = Integer.parseInt(op.replaceAll(".*?(\\d+).*", "$1"));
        type = OperationType.valueOf(op.replaceAll("(^.*?)[\\d+|\\(].*", "$1"));
        variable = (op.replaceAll("^.*?\\((.*)\\)*", "$1"));

        System.out.println(transaction);
        System.out.println(type);
        System.out.println(variable);
    }

    public int getTransaction() {
        return transaction;
    }

    public void setTransaction(int transaction) {
        this.transaction = transaction;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        String op = type.toString() + transaction;

        if (variable != null) {
            op += "(" + variable + ")";
        }

        return op;
    }
}
