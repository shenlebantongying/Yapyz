package org.slbtty.yapyz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class YapyzGUI extends Application {
    @Override
    public void start(Stage stage){
        stage.setTitle("Yapyz!");

        VBox mainFrame = new VBox(0);

        // About page

        Stage stage2 = new Stage();
        stage2.setScene(new Scene(new VBox(new Text("https://github.com/shenlebantongying/Yapyz")),400,400));
        stage2.initModality(Modality.WINDOW_MODAL);

        // MenuBar

        Menu mainMenu= new Menu("Help");
        MenuItem about = new MenuItem("About");

        about.setOnAction(event -> stage2.show());

        mainMenu.getItems().add(about);

        MenuBar mainMenuBar = new MenuBar();
        mainMenuBar.getMenus().add(mainMenu);

        // MainTable
        TableView<Entry> table = new TableView<>();

        TableColumn<Entry,String> colName = new TableColumn<>("Name");
        TableColumn<Entry,String> colDesc = new TableColumn<>("Description");

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        table.getColumns().add(colName);
        table.getColumns().add(colDesc);

        table.getItems().addAll(
                new Entry("one", "good"),
                new Entry("two","nice")
        );

        table.prefHeightProperty().bind(stage.heightProperty());

        //

        mainFrame.getChildren().addAll(mainMenuBar,table);

        Scene mainFrameScene = new Scene(mainFrame,800,600);

        stage.setScene(mainFrameScene);

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}