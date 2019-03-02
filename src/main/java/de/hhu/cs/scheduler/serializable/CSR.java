/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.serializable;

import de.hhu.cs.scheduler.ConflictGraph;
import de.hhu.cs.scheduler.model.ConflictDirection;
import de.hhu.cs.scheduler.model.ConflictPair;
import de.hhu.cs.scheduler.model.Operation;
import de.hhu.cs.scheduler.model.OperationType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CSR extends SR {
    private final static String FXML_PATH = "/fxml/conflictGraph.fxml";

    List<ConflictPair> conf = new ArrayList<>();
    List<ConflictDirection> direction = new ArrayList<>();

    @Override
    public boolean check(String schedule) {
        super.check(schedule);

        reset();
        // parse schedule
        parse(schedule);

        // remove aborted operations
        clean();

        // print result
        operations.forEach(operation -> System.out.print(operation));
        System.out.println();

        // check for conflicts
        // every operation is checked with every suffix
        System.out.print("conf(s) =  {");
        boolean first = true;
        for (int i = 0; i < operations.size() - 1; i++) {
            // op1
            Operation op1 = operations.get(i);
            for (int j = i + 1; j < operations.size(); j++) {
                // op2
                Operation op2 = operations.get(j);

                // check for conflict
                if (conflict(op1, op2)) {
                    // add conflict pair
                    conf.add(new ConflictPair(op1, op2));

                    // print conf
                    System.out.print((!first ? ", " : "") + "(" + op1 + ", " + op2 + ")");
                    first = false;

                    // separate list for every conflictDirection
                    // used as set
                    // provides better debugging
                    ConflictDirection dir = new ConflictDirection(op1.getTransaction(), op2.getTransaction());
                    if (!direction.contains(dir)) {
                        direction.add(dir);
                    }
                }
            }
        }
        System.out.println("}");

        // check for cycles
        boolean csr = !checkCycle();

        if (csr) {
            createSerializable();
        }

        return csr;
    }

    /**
     * Reset to initial state
     */
    protected void reset() {
        super.reset();
        conf.clear();
        direction.clear();
    }

    /**
     * Draw the conflict Graph
     */
    public void drawConflictGraph() {
        if (tSet.size() > 0) {
            ConflictGraph conflictGraph = new ConflictGraph(tSet.size(), direction);

            try {
                Stage stage = new Stage();
                stage.setTitle("Scheduler");
                stage.setResizable(false);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
                // use this class as controller
                fxmlLoader.setController(conflictGraph);

                Scene scene = new Scene(fxmlLoader.load());

                // show stage
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create Serialization for the schedule
     */
    private void createSerializable() {
        // user every direction as starting point
        List<Integer> ts = new ArrayList<>();
        for (int i = 0; i < direction.size(); i++) {
            // rest list
            ts.clear();

            ConflictDirection conDir = direction.get(i);
            String ser = "T" + conDir.getDir1() + " " + "T" + conDir.getDir2();

            // add T index
            ts.add(conDir.getDir1());
            ts.add(conDir.getDir2());

            // save last transaction
            int last = conDir.getDir2();

            // create working copy of the directions
            List<ConflictDirection> con = new ArrayList<>();
            con.addAll(direction);
            con.remove(conDir);

            // while he have directions and found a new T to add continue
            boolean found = true;
            while (con.size() > 0 && found) {
                // set found to false at the beginning
                found = false;
                // iterate over every direction
                for (int j = 0; j < con.size(); j++) {
                    ConflictDirection conDir2 = con.get(j);
                    // check if the direction can be used
                    // meaning must start with previously added
                    if (conDir2.getDir1() == last) {
                        // add to serialization
                        ser += " T" + conDir2.getDir2();
                        // add to list
                        ts.add(conDir2.getDir2());
                        // new last
                        last = conDir.getDir2();
                        // remove current
                        con.remove(conDir2);
                        found = true;
                        break;
                    }
                }
            }

            // if found is true at the end -> we found serialization
            if (found) {
                // stop here
                serialized = ser;
                break;
            }
        }

        if (ts.size() < tSet.size()) {
            for (int i = 0; i < tSet.size(); i++) {
                if (!ts.contains(i)) {
                    serialized += " T" + (i + 1);
                }
            }
        }

        System.out.println(serialized);
    }

    /**
     * Check for cylces
     *
     * @return
     */
    private boolean checkCycle() {
        boolean cycle = false;
        List<ConflictDirection> cycles = new ArrayList<>();

        // check for every conflictDirection if reverse is in list as well
        for (ConflictDirection dir : direction) {
            // only check one direction not the reverse if already checked
            if (!cycles.contains(dir)) {
                // contains reverse?
                ConflictDirection reverse = new ConflictDirection(dir.getDir2(), dir.getDir1());
                if (direction.contains(reverse)) {
                    System.out.println("Cycle " + dir.getDir1() + " -> " + dir.getDir2());

                    cycles.add(reverse);

                    // we have a cycle
                    cycle = true;
                }
            }
        }

        return cycle;
    }

    /**
     * Check for conflict
     *
     * @param op1
     * @param op2
     * @return
     */
    private boolean conflict(Operation op1, Operation op2) {
        // different transactions on same var
        if (op1.getTransaction() != op2.getTransaction() && op1.getVariable() != null && op1.getVariable().equals(op2.getVariable())) {
            // conflict type (r, w)
            if (op1.getType().conflictType() && op2.getType().conflictType()) {
                // conflict for every combination except r,r
                if (!(op1.getType() == OperationType.r && op2.getType() == OperationType.r)) {
                    return true;
                }
            }
        }
        return false;
    }
}
