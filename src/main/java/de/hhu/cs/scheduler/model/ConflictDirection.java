/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler.model;

public class ConflictDirection {
    private int dir1;
    private int dir2;

    public ConflictDirection(int dir1, int dir2) {
        this.dir1 = dir1;
        this.dir2 = dir2;
    }

    public int getDir1() {
        return dir1;
    }

    public void setDir1(int dir1) {
        this.dir1 = dir1;
    }

    public int getDir2() {
        return dir2;
    }

    public void setDir2(int dir2) {
        this.dir2 = dir2;
    }

    @Override
    public boolean equals(Object obj) {
        return dir1 == ((ConflictDirection) obj).getDir1() && dir2 == ((ConflictDirection) obj).getDir2();
    }
}
