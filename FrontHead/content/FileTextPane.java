package FrontHead.content;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;



public class FileTextPane extends Application {

    File file;
    FlowPane root = new FlowPane();
    MenuBar MB;
    final int MENU_HEIGHT = 25;//这个是通过监听器测量得到。不是长久之计勉强用用
    int test = 0;
    public FileTextPane(File file){
        super();
        setFile(file);
    }

    @Override
    public void start(Stage fileStage) throws Exception {
        Scene scene = new Scene(root,600,600);
        fileStage.setScene(scene);
        fileStage.show();
    }

    @Override
    public void init() throws Exception {
        Stage fileStage = new Stage();
        root.getStylesheets().add("FrontHead/css/textPane.css");


        this.start(fileStage);
        initMenu();
        initTextArea();
    }

    private void initTextArea() {

        TextArea TA = new TextArea();
        TA.getStyleClass().add("textArea");
        TA.setPrefWidth(root.getScene().getWidth());
        TA.setPrefHeight(root.getScene().getHeight()-MENU_HEIGHT);
        root.getChildren().add(TA);
    }

    private void initMenu() {
        MB= new MenuBar();
        Menu fileMeau = new Menu("file");
        fileMeau.getItems().add(new MenuItem("save"));
        MB.getMenus().addAll(fileMeau);
        MB.setPrefWidth(root.getScene().getWidth());
        root.getChildren().add(MB);
    }


    public void setFile(File file) {
        this.file = file;
    }
}
