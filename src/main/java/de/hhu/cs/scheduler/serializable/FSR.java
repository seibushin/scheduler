/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.serializable;

import de.hhu.cs.scheduler.model.Herbrand;
import de.hhu.cs.scheduler.model.Operation;
import de.hhu.cs.scheduler.model.OperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FSR extends SR {
    HashMap<String, Herbrand> h = new HashMap<>();

    @Override
    public boolean check(String schedule) {
        super.check(schedule);

        reset();

        // parse
        parse(schedule);

        // remove aborted operations
        clean();

        // create serializations
        HashMap<String, List<Operation>> serOps = createSerializations(operations);

        // add zero and inf trans
        addPreTrans(operations);
        // create RF set
        h = h(operations);

        boolean fsr = false;
        for (String ser : serOps.keySet()) {
            List<Operation> ops = serOps.get(ser);
            // add zero and inf trans
            addPreTrans(ops);

            // create RF(s) set
            HashMap<String, Herbrand> herbrand = h(ops);

            System.out.println("EQUAL?");
            // check if schedule is serializable with given serialization
            boolean equal = true;
            for (String var : vars) {
                System.out.println(h.get(var));
                System.out.println(herbrand.get(var));
                if (!h.get(var).equals(herbrand.get(var))) {
                    equal = false;
                }
            }

            // if equal to ser schedule set vsr to true and stop loop
            if (equal) {
                fsr = equal;
                serialized = ser;
                System.out.println(ser);
                break;
            }
        }

        return fsr;
    }

    /**
     * Create Herbrand semantic
     *
     * @param operations
     * @return
     */
    private HashMap<String, Herbrand> h(List<Operation> operations) {
        // save last read of var in transaction
        HashMap<Integer, HashMap<String, Herbrand>> lastReads = new HashMap<>();
        for (int t : tSet) {
            lastReads.put(t, new HashMap<>());
        }

        // save last write of var
        HashMap<String, Herbrand> lastWrite = new HashMap<>();

        // iterate over all operations
        for (int i = 0; i < operations.size(); i++) {
            // get current operation
            Operation op = operations.get(i);

            /*for (Integer k : lastReads.keySet()) {
                for (String s : lastReads.get(k).keySet()) {
                    System.out.println("T" + k + " " + s + " " + lastReads.get(k).get(s));
                }
            }*/

            if (op.getType() == OperationType.w) {
                // get last read in trans
                List<Herbrand> herbs = new ArrayList<>();
                if (lastReads.get(op.getTransaction()) != null && lastReads.get(op.getTransaction()).values() != null) {
                    herbs.addAll(lastReads.get(op.getTransaction()).values());
                }
                // create new Herbrand
                Herbrand newHer = new Herbrand(op.getTransaction(), op.getVariable(), herbs);
                // add to last write of the variable
                lastWrite.put(op.getVariable(), newHer);
            } else if (op.getType() == OperationType.r) {
                // add read to reads of the transaction
                HashMap<String, Herbrand> cur = lastReads.get(op.getTransaction());
                cur.put(op.getVariable(), lastWrite.get(op.getVariable()));
                lastReads.put(op.getTransaction(), cur);
            }
        }

        // print Herbrand for every variable
        for (String s : lastWrite.keySet()) {
            System.out.println("H(s)(" + s + ") = " + lastWrite.get(s));
        }

        // return the lastWrites
        return lastWrite;
    }

    /**
     * Add the zero and inf transaction to the given operation list
     *
     * @param operations
     */
    private void addPreTrans(List<Operation> operations) {
        for (String var : vars) {
            // add zero transaction
            operations.add(0, new Operation(OperationType.w, 0, var));
        }
        operations.add(vars.size(), new Operation(OperationType.c, 0, null));

        // print result
        operations.forEach(operation -> System.out.print(operation));
        System.out.println();
    }
}
