package ooga.view.ui.settingsUI;

import javafx.scene.control.ColorPicker;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import ooga.view.ViewController;
import ooga.view.ui.UIInterface;
import ooga.view.util.ViewUtility;

import java.util.List;

public class BoardStyleUI extends GridPane implements UIInterface {

    private final Color LICHESS_COLOR1 = Color.web("#f3dab0");
    private final Color LICHESS_COLOR2 = Color.web("#bb885b");
    private ViewController viewController;

    private ColorPicker colorPicker1;
    private ColorPicker colorPicker2;

    public BoardStyleUI(ViewController viewController) {
        this.viewController = viewController;
        this.getStyleClass().add("SettingsUI");
        createUI();
    }

    @Override
    public void createUI() {
        colorPicker1 = ViewUtility.makeColorPicker("board_color_1", LICHESS_COLOR1, e -> viewController.changeBoardColor(colorPicker1.getValue(), colorPicker2.getValue()));
        colorPicker2 = ViewUtility.makeColorPicker("board_color_2", LICHESS_COLOR2, e -> viewController.changeBoardColor(colorPicker1.getValue(), colorPicker2.getValue()));

        this.add(ViewUtility.makeLabel("board_color"), 0, 0);
        this.add(colorPicker1, 1, 0);
        this.add(colorPicker2, 1, 1);
        this.add(ViewUtility.makeLabel("piece_style_label"), 0, 3);
        this.add(ViewUtility.makeComboBox("piece_style", List.of("lichess"), e -> System.out.println(e)), 1, 3);
    }
}