# Scheduler
Transaktionsverwaltung SS2018

Sebastian Meyer

Use this tool to check if a schedule is serializable (conflict, view, finalstate) and check for the 2 phase locking protocol.
Additionally draw the conflict graph to see the conflicts. The end of a line is marked with a red dot.

![Image of Scheduler - GUI](https://github.com/seibushin/scheduler/blob/master/snap.png)
![Image of Scheduler - Conflict Graph](https://github.com/seibushin/scheduler/blob/master/conflict.png)

## Build
The project can be build using `gradlew shadowJar`. The created jar `scheduler-1.0.jar` consists of all used libaries and can simple be executed.

## Examples
### not CSR, VSR
r1(a)r2(c)w2(a)w1(b)r2(b)w2(c)c2r1(c)w1(a)c1

### VSR
r2(b)r1(b)r2(a)r2(c)w1(c)w2(c)w2(a)c2w1(a)w1(b)c1w3(c)c3

### not VSR
r2(x)r1(y)r2(z)w1(y)w2(z)r1(z)w1(z)w2(y)w1(x)c1c2

### CSR
r1(a)r2(a)r1(c)r2(b)w1(d)w1(c)w1(a)a1r2(d)w2(d)c2

### not CSR
r1(x)r2(z)w3(y)r1(y)r2(x)r3(y)w1(x)w2(z)r3(z)w1(z)w3(x)c1 c2 c3
