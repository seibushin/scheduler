/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler;

import de.hhu.cs.scheduler.model.ConflictDirection;
import de.hhu.cs.scheduler.model.Coordinate;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ConflictGraph {
    @FXML
    Canvas canvas;
    GraphicsContext gc;

    private int tCount;
    List<ConflictDirection> direction;

    public ConflictGraph(int tCount, List direction) {
        this.tCount = tCount;
        this.direction = direction;
    }

    public void initialize() {
        gc = canvas.getGraphicsContext2D();

        draw();
    }

    private void draw() {
        int size = tCount;
        // set config values
        double margin = 50;
        double angle = 360 / size;
        double radiusOuter = (canvas.getWidth() - 2 * margin) / 2;
        double radius = 40;

        // calculate coordinates and draw the transaction
        List<Coordinate> coords = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // position at the correct circle position
            double x = Math.cos(Math.toRadians(angle * i)) * radiusOuter + radiusOuter - radius * 0.5 + margin;
            double y = Math.sin(Math.toRadians(angle * i)) * radiusOuter + radiusOuter - radius * 0.5 + margin;

            // add coordinate
            coords.add(new Coordinate(x, y));

            gc.strokeOval(x, y, radius, radius);
            gc.fillText("T" + (i + 1), x + radius * 0.4, y + radius * 0.6);
        }

        // draw the direction between the transactions
        for (ConflictDirection dir : direction) {
            int start = dir.getDir1() - 1;
            int end = dir.getDir2() - 1;

            // we start at the center
            double x1 = coords.get(start).getX() + radius * 0.5;
            double y1 = coords.get(start).getY() + radius * 0.5;
            double x2 = coords.get(end).getX() + radius * 0.5;
            double y2 = coords.get(end).getY() + radius * 0.5;

            // calculate circular offset
            double a = Math.abs(y1 - y2);
            double b = Math.abs(x1 - x2);
            double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

            double wa = Math.asin(a / c);

            if (x1 < x2) {
                x1 += Math.cos(wa) * (radius * 0.5);
                x2 -= Math.cos(wa) * (radius * 0.5);
            } else {
                x1 -= Math.cos(wa) * (radius * 0.5);
                x2 += Math.cos(wa) * (radius * 0.5);
            }

            if (y1 < y2) {
                y1 += Math.sin(wa) * (radius * 0.5);
                y2 -= Math.sin(wa) * (radius * 0.5);
            } else {
                y1 -= Math.sin(wa) * (radius * 0.5);
                y2 += Math.sin(wa) * (radius * 0.5);
            }

            // draw the line
            gc.strokeLine(x1, y1, x2, y2);

            // add red dots to the line ends
            gc.setFill(Color.RED);
            gc.fillOval(x2 - 2, y2 - 2, 4, 4);
        }
    }
}
