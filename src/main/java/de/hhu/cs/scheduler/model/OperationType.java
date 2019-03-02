/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.model;

public enum OperationType {
    r("read"),
    w("write"),
    c("commit"),
    a("abort"),
    u("unlock"),
    wl("lock");

    OperationType(String type) {
        this.type = type;
    }

    private String type;

    public boolean conflictType() {
        return this == r || this == w;
    }
}
