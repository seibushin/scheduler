/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler;

import de.hhu.cs.scheduler.serializable.CSR;
import de.hhu.cs.scheduler.serializable.FSR;
import de.hhu.cs.scheduler.serializable.VSR;
import de.hhu.cs.scheduler.serializable.ZPL;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private CSR csr = new CSR();
    private FSR fsr = new FSR();
    private VSR vsr = new VSR();
    private ZPL zpl = new ZPL();

    private BooleanProperty status_csr = new SimpleBooleanProperty(false);
    private BooleanProperty status_vsr = new SimpleBooleanProperty(false);
    private BooleanProperty status_fsr = new SimpleBooleanProperty(false);
    private BooleanProperty status_2pl = new SimpleBooleanProperty(false);

    private StringProperty label_csr = new SimpleStringProperty("");
    private StringProperty label_vsr = new SimpleStringProperty("");
    private StringProperty label_fsr = new SimpleStringProperty("");
    private StringProperty label_2pl = new SimpleStringProperty("");

    private StringProperty label_schedule = new SimpleStringProperty("");

    public BooleanProperty status_csrProperty() {
        return status_csr;
    }

    public BooleanProperty status_vsrProperty() {
        return status_vsr;
    }
    public BooleanProperty status_fsrProperty() {
        return status_fsr;
    }

    public BooleanProperty status_2plProperty() {
        return status_2pl;
    }

    public StringProperty label_csrProperty() {
        return label_csr;
    }

    public StringProperty label_vsrProperty() {
        return label_vsr;
    }

    public StringProperty label_fsrProperty() {
        return label_fsr;
    }

    public StringProperty label_2plProperty() {
        return label_2pl;
    }

    public StringProperty label_scheduleProperty() {
        return label_schedule;
    }

    public Scheduler() {
    }

    public void check(String schedule) {
        label_schedule.set(schedule);

        List<String> schedules = new ArrayList<>();
        schedules.add("r1(a)r2(a)r1(c)r2(b)w1(d)w1(c)w1(a)a1r2(d)w2(d)c2");
        schedules.add("r2(x)r1(y)r2(z)w1(y)w2(z)r1(z)w1(z)w2(y)w1(x)c1c2");
        schedules.add("r2(y)w2(y)r2(x)r1(y)r1(x)w1(x)w1(z)r3(z)r3(x)w3(z)c1 c2 c3");
        schedules.add("r1(x)r2(z)w3(y)r1(y)r2(x)r3(y)w1(x)w2(z)r3(z)w1(z)w3(x)c1c2c3");
        schedules.add("r2(z)r1(x)w2(x)r4(x)r1(y)r4(y)w3(y)r4(z)w4(y)c1 c2 c3 c4");

        status_csr.set(csr.check(schedule));
        label_csr.set(csr.getSchedule());

        status_vsr.set(vsr.check(schedule));
        label_vsr.set(vsr.getSchedule());

        status_fsr.set(fsr.check(schedule));
        label_fsr.set(fsr.getSchedule());

        status_2pl.set(zpl.check(schedule));
        label_2pl.set(zpl.getSchedule());
    }

    public void showGraph() {
        csr.drawConflictGraph();
    }
}
