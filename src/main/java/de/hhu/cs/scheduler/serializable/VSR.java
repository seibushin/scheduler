/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.serializable;

import de.hhu.cs.scheduler.model.Operation;
import de.hhu.cs.scheduler.model.OperationType;
import de.hhu.cs.scheduler.model.ReadFrom;

import java.util.*;

public class VSR extends SR {
    List<ReadFrom> rf = new ArrayList<>();

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
        addPreAndSufTrans(operations);
        // create RF set
        rf = rf(operations);

        boolean vsr = false;
        for (String ser : serOps.keySet()) {
            List<Operation> ops = serOps.get(ser);
            // add zero and inf trans
            addPreAndSufTrans(ops);

            // create RF(s) set
            List<ReadFrom> readFrom = rf(ops);

            // check if schedule is serializable with given serialization
            boolean equal = true;
            for (ReadFrom rfrom : readFrom) {
                if (!rf.contains(rfrom)) {
                    equal = false;
                }
            }

            // if equal to ser schedule set vsr to true and stop loop
            if (equal) {
                vsr = equal;
                serialized = ser;
                break;
            }
        }

        return vsr;
    }

    /**
     * Create ReadFrom from given operations list
     *
     * @param operations
     * @return
     */
    private List<ReadFrom> rf(List<Operation> operations) {
        List<ReadFrom> readFrom = new ArrayList<>();
        System.out.print("RF(s) = {");
        boolean first = true;
        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);
            //System.out.println(op);

            if (op.getType() == OperationType.r) {
                // check last write
                for (int j = i; j >= 0; j--) {
                    Operation op2 = operations.get(j);

                    if (op2.getType() == OperationType.w && op.getVariable().equals(op2.getVariable())) {
                        ReadFrom rf_ = new ReadFrom(op.getTransaction(), op.getVariable(), op2.getTransaction());
                        readFrom.add(rf_);
                        System.out.print((!first ? ", " : "") + rf_);
                        first = false;
                        break;
                    }
                }
            }
        }
        System.out.println("}");

        return readFrom;
    }

    /**
     * Add the zero and inf transaction to the given operation list
     *
     * @param operations
     */
    private void addPreAndSufTrans(List<Operation> operations) {
        for (String var : vars) {
            // add zero transaction
            operations.add(0, new Operation(OperationType.w, 0, var));
            // add inf transaction
            operations.add(new Operation(OperationType.r, tSet.size() + 100, var));
        }
        operations.add(vars.size(), new Operation(OperationType.c, 0, null));
        operations.add(new Operation(OperationType.c, tSet.size() + 100, null));

        // print result
        operations.forEach(operation -> System.out.print(operation));
        System.out.println();
    }

    /**
     * Reset to initial state
     */
    @Override
    protected void reset() {
        super.reset();
        rf.clear();
    }
}