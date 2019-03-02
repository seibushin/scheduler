/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.serializable;

import de.hhu.cs.scheduler.model.Operation;
import de.hhu.cs.scheduler.model.OperationType;

import java.util.HashMap;

public class ZPL extends SR {
    @Override
    public boolean check(String schedule) {
        reset();

        // parse schedule
        parse(schedule);

        // clean
        clean();

        // init status
        HashMap<Integer, OperationType> status = new HashMap<>();
        for(int t : tSet) {
            status.put(t, OperationType.wl);
        }

        boolean isS2pl = true;
        boolean contains = false;

        // check if after the first unlock in a tranaction only unlocks follow
        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);

            if (op.getType() == OperationType.wl) {
                contains = true;
                if (status.get(op.getTransaction()) == OperationType.u) {
                    isS2pl = false;
                    break;
                }
                status.put(op.getTransaction(), op.getType());
            } else if (op.getType() == OperationType.u) {
                status.put(op.getTransaction(), op.getType());
            }
        }

        // check if schedule uses lock
        boolean s2pl = false;
        if (contains) {
            s2pl = isS2pl;
        }

        return s2pl;
    }
}
