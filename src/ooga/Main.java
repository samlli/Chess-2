package ooga;


import javafx.application.Application;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.view.View;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * A method to test (and a joke :).
     */
    public double getVersion () {
        return 0.001;
    }

    @Override
    public void start(Stage stage) {
        new Controller();
    }
}
