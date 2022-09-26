package org.slbtty.yapyz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class YapyzGUI extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Yapyz!");

        VBox mainFrame = new VBox(0);

        // MenuBar
        // Settings
        Menu settingsMenu = new Menu("Settings");
        MenuItem indexSetting = new MenuItem("Index Settings");

        indexSetting.setOnAction(event -> {
            SettingPanel().show();
        });

        settingsMenu.getItems().add(indexSetting);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");

        about.setOnAction(event -> AboutPanel().show());

        helpMenu.getItems().addAll(about);

        MenuBar mainMenuBar = new MenuBar();
        mainMenuBar.getMenus().addAll(settingsMenu,helpMenu);

        // SearchBox and A btn

        var searchInput = new TextField();
        HBox.setHgrow(searchInput, Priority.ALWAYS);

        var searchButton = new Button("Search");

        searchButton.setOnAction(event -> {
            System.out.println(event.toString());
        });


        var searchBar = new HBox(searchButton, searchInput);

        // MainTable
        TableView<Entry> table = new TableView<>();

        TableColumn<Entry, String> colName = new TableColumn<>("Name");
        TableColumn<Entry, String> colDesc = new TableColumn<>("Description");

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        table.getColumns().add(colName);
        table.getColumns().add(colDesc);

        table.getItems().addAll(
                new Entry("one", "good"),
                new Entry("two", "nice")
        );


        table.prefHeightProperty().bind(stage.heightProperty());

        //

        mainFrame.getChildren()
                .addAll(mainMenuBar,
                        searchBar,
                        table);

        Scene mainFrameScene = new Scene(mainFrame, 800, 600);

        stage.setScene(mainFrameScene);

        stage.show();

    }

    private Stage AboutPanel() {
        var stage = new Stage();
        stage.setScene(new Scene(new VBox(new Text("https://github.com/shenlebantongying/Yapyz")), 400, 400));
        stage.initModality(Modality.WINDOW_MODAL);
        return stage;
    }


    /**
     * TODO: move this into a class?
     *
     * @return
     */
    private Stage SettingPanel() {
        var stage = new Stage();
        var vBox = new VBox();

        vBox.getChildren().add(new Text("No idea yet"));


        var scene = new Scene(vBox);
        stage.setScene(scene);
        return stage;

    }


    public static void main(String[] args) {
        launch();
    }
}