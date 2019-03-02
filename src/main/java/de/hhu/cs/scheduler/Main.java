/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.cs.scheduler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
    private final static String FXML_PATH = "/fxml/main.fxml";

    private Scheduler scheduler = new Scheduler();

    @FXML
    TextField schedule;
    @FXML
    Label label_schedule;
    @FXML
    RadioButton status_csr;
    @FXML
    RadioButton status_vsr;
    @FXML
    RadioButton status_fsr;
    @FXML
    RadioButton status_2pl;
    @FXML
    Label label_csr;
    @FXML
    Label label_vsr;
    @FXML
    Label label_fsr;
    @FXML
    Label label_2pl;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("Scheduler");
            stage.setResizable(false);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
            // use this class as controller
            fxmlLoader.setController(this);

            Scene scene = new Scene(fxmlLoader.load());

            // show stage
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> close());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        status_2pl.selectedProperty().bind(scheduler.status_2plProperty());
        status_csr.selectedProperty().bind(scheduler.status_csrProperty());
        status_vsr.selectedProperty().bind(scheduler.status_vsrProperty());
        status_fsr.selectedProperty().bind(scheduler.status_fsrProperty());

        label_2pl.textProperty().bind(scheduler.label_2plProperty());
        label_csr.textProperty().bind(scheduler.label_csrProperty());
        label_vsr.textProperty().bind(scheduler.label_vsrProperty());
        label_fsr.textProperty().bind(scheduler.label_fsrProperty());

        label_schedule.textProperty().bind(scheduler.label_scheduleProperty());
    }

    @FXML
    private void check() {
        scheduler.check(schedule.getText());
    }

    @FXML
    private void showGraph() {
        scheduler.showGraph();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void close() {
        Platform.exit();
        // terminate process
        System.exit(0);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        close();
    }
}
