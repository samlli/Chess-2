package ooga.view;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ooga.controller.LoginController;
import ooga.view.ui.loginUI.LoginUI;

import java.util.Objects;

public class LoginView {

    public static final String DEFAULT_RESOURCE_PACKAGE = View.class.getPackageName() + ".resources.";
    public static final String STYLE_PACKAGE = "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/");
    public static final String DEFAULT_STYLESHEET = STYLE_PACKAGE + "style.css";

    public static final int STAGE_WIDTH = 335;
    public static final int STAGE_HEIGHT = 220;

    private LoginController loginController;
    private Stage stage;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
        this.stage = new Stage();
    }

    private Scene setupDisplay() {
        GridPane root = new GridPane();
        root.add(new LoginUI(loginController), 0, 0);
        Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(DEFAULT_STYLESHEET)).toExternalForm());
        return scene;
    }

    public void initializeDisplay() {
        stage.setScene(setupDisplay());
        stage.show();
    }

    public void hideDisplay() {
        stage.hide();
    }
}
