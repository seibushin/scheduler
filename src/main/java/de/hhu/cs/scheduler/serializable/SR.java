/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.serializable;

import de.hhu.cs.scheduler.model.Operation;
import de.hhu.cs.scheduler.model.OperationType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SR implements SRInterface {
    protected List<Operation> operations = new ArrayList<>();
    protected String serialized = "";
    protected Set<Integer> tSet = new HashSet<>();
    protected Set<String> vars = new HashSet<>();
    protected String schedule;
    protected List<Integer> aborts = new ArrayList<>();

    @Override
    public boolean check(String schedule) {
        this.schedule = schedule;
        return false;
    }

    @Override
    public String getSchedule() {
        return serialized;
    }

    protected void reset() {
        serialized = "";
        operations.clear();
        tSet.clear();
        vars.clear();
        aborts.clear();
    }

    /**
     * Remove aborted transactions
     */
    protected void clean() {
        for (int j = 0; j < aborts.size(); j++) {
            int abort = aborts.get(j);
            for (int i = 0; i < operations.size(); i++) {
                Operation op = operations.get(i);
                if (op.getTransaction() == abort) {
                    operations.remove(i);
                }
            }
        }
    }

    /**
     * Parse the schedule
     *
     * @param schedule
     */
    protected void parse(String schedule) {
        Pattern p = Pattern.compile("(wl|r|w|c|a|u)(\\d*)(\\((.+?)\\))?");
        Matcher m = p.matcher(schedule);

        // iterate over matches
        while (m.find()) {
            // extract values
            OperationType type = OperationType.valueOf(m.group(1));
            int transaction = Integer.parseInt(m.group(2));
            String variable = m.group(4);

            // add var
            if (variable != null) {
                vars.add(variable);
            }

            // add abort
            if (type == OperationType.a) {
                aborts.add(transaction);
            }

            // count the number of transactions
            // we use a set
            tSet.add(transaction);

            // add operation
            operations.add(new Operation(type, transaction, variable));
        }

        // print result
        operations.forEach(operation -> System.out.print(operation));
        System.out.println();
    }

    /**
     * Permuate a simple list of integers
     *
     * @param list
     * @param k
     * @param perms
     */
    protected void permute(List<Integer> list, int k, List<List<Integer>> perms) {
        for (int i = k; i < list.size(); i++) {
            Collections.swap(list, i, k);
            permute(list, k + 1, perms);
            Collections.swap(list, k, i);
        }
        if (k == list.size() - 1) {
            perms.add(new ArrayList<>(list));
        }
    }

    /**
     * Create all possible serializations for the schedule
     */
    protected HashMap<String, List<Operation>> createSerializations(List<Operation> operations) {
        // create possible serializations
        List<Integer> perm = new ArrayList<>(tSet);
        List<List<Integer>> perms = new ArrayList<>();
        permute(perm, 0, perms);
        HashMap<String, List<Operation>> sers = new HashMap<>();

        // create serialized schedule
        for (int i = 0; i < perms.size(); i++) {
            List<Operation> schedule = new ArrayList<>();
            List<Integer> per = perms.get(i);

            String seri = "";
            boolean first = true;
            for (int p : per) {
                seri += (first ? "" : " ") + "T" + p;
                first = false;
            }

            for (int j = 0; j < per.size(); j++) {
                for (Operation op : operations) {
                    if (op.getTransaction() == per.get(j)) {
                        schedule.add(op);
                    }
                }
            }

            sers.put(seri, schedule);
        }

        return sers;
    }
}
