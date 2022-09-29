package org.slbtty.yapyz;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class YapyzGUI extends Application {

    public static indexSettings indexSettings;

    // External "service" objects
    private static Clipboard clipboard;
    private static UrlHandler urlHandler;

    private Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {

        // external things init
        clipboard = Clipboard.getSystemClipboard();
        urlHandler = new UrlHandler();

        // jfx init

        mainStage = stage;

        indexSettings = new indexSettings();
        var indexer = new Indexer();


        mainStage.setTitle("Yapyz!");

        VBox mainFrame = new VBox(0);

        // MenuBar
        // Settings
        Menu settingsMenu = new Menu("Index Control");
        var indexSetting = new MenuItem("Index Settings");
        var rebuildIndex = new MenuItem("Rebuild Index");

        indexSetting.setOnAction(event -> {
            SettingPanel().show();
        });

        rebuildIndex.setOnAction(event -> {
            indexer.setPaths(indexSettings.indexPaths);
            try {
                indexer.rebuildIndex();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        settingsMenu.getItems().addAll(rebuildIndex, indexSetting);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");

        about.setOnAction(event -> AboutPanel().show());

        helpMenu.getItems().addAll(about);

        MenuBar mainMenuBar = new MenuBar();
        mainMenuBar.getMenus().addAll(settingsMenu, helpMenu);


        // MainTable
        TableView<Entry> table = new TableView<>();

        TableColumn<Entry, String> colName = new TableColumn<>("Path");
        TableColumn<Entry, Float> colDesc = new TableColumn<>("Score");
        colName.setPrefWidth(300);

        colName.setCellValueFactory(new PropertyValueFactory<>("path"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("score"));


        // TODO: code below is questionable since it create a content menu for every row creation?
        table.setRowFactory(param -> {
            final var row = new TableRow<Entry>();
            final var contextMenu = new ContextMenu();
            final var copy2clipboardMenuItem = new MenuItem("Copy path");

            copy2clipboardMenuItem.setOnAction(e -> {
                final var str_for_clipboard = new ClipboardContent();
                str_for_clipboard.putString(table.getSelectionModel().getSelectedItem().getPath()) ;
                clipboard.setContent(str_for_clipboard);
            });

            contextMenu.getItems().add(copy2clipboardMenuItem);

            // TODO: When row is empty, do nothing. Is there a better way to write this?
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu));


            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (! row.isEmpty())){
                    urlHandler.open(row.getItem().getPath());
                }
            });

            return row;
        });

        table.getColumns().add(colName);
        table.getColumns().add(colDesc);


        table.prefHeightProperty().bind(mainStage.heightProperty());

        // SearchBox and A btn
        
        var searchInput = new TextField();
        HBox.setHgrow(searchInput, Priority.ALWAYS);

        var searchButton = new Button("Search");

        EventHandler<ActionEvent> searchEvent =
                event -> table.setItems(SearchFiles.simpleTermSearch(searchInput.getText()));


        searchButton.setOnAction(searchEvent);
        // typically ENTER key pressed
        searchInput.setOnAction(searchEvent);


        var searchBar = new HBox(searchButton, searchInput);

        //

        mainFrame.getChildren()
                .addAll(mainMenuBar,
                        searchBar,
                        table);

        Scene mainFrameScene = new Scene(mainFrame, 800, 600);

        mainStage.setScene(mainFrameScene);

        mainStage.show();

    }

    private Stage AboutPanel() {
        var stage = new Stage();
        stage.setScene(new Scene(new VBox(new Text("https://github.com/shenlebantongying/Yapyz")), 400, 400));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.mainStage);
        return stage;
    }


    /**
     * TODO: move this into a class?
     */
    private Stage SettingPanel() {

        var dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select a path to be indexed");


        var stage = new Stage();
        var vBox = new VBox();

        var pathManipulateBar = new HBox();
        var addPathBtn = new Button("Add");
        var removePathBtn = new Button("Remove");
        pathManipulateBar.getChildren().addAll(addPathBtn, removePathBtn);

        addPathBtn.setOnAction(e -> {
            File selectedDir = dirChooser.showDialog(stage);

            if (selectedDir == null) {
                Logger.error("Unable to obtain a valid path");
            } else {
                indexSettings.addPath(selectedDir.getAbsolutePath());
            }
        });

        var confirmBtn = new Button("OK");
        confirmBtn.setMaxWidth(Integer.MAX_VALUE);
        confirmBtn.setOnAction(e -> {
            indexSettings.saveToDisk();
            stage.close();
        });

        var indexPathsView = new ListView<>(indexSettings.indexPaths);

        vBox.getChildren().addAll(
                new Label("Index Paths:"),
                pathManipulateBar,
                indexPathsView,
                confirmBtn);

        var scene = new Scene(vBox);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.mainStage);
        stage.setScene(scene);
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}