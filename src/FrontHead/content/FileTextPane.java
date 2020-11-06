package FrontHead.content;

import BackGround.Server;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Controller;

import java.io.IOException;
import java.util.Optional;


public class FileTextPane extends Application {

    VirtualFile file;
    FlowPane root = new FlowPane();
    Controller controller;
    TextArea TA;
    String startText;
    String latesText;
    MenuBar MB;
    final int MENU_HEIGHT = 25;//这个是通过监听器测量得到。不是长久之计勉强用用
    int test = 0;
    Server server;
    public FileTextPane(VirtualFile file , Controller controller ,Server server) throws IOException {
        super();
        setController(controller);
        setServer(controller.getServer());
        setFile(file);
    }

    @Override
    public void start(Stage fileStage) throws Exception {
        Scene scene = new Scene(root,600,600);
        fileStage.setScene(scene);
        fileStage.show();
        fileStage.setTitle(file.getName());
        fileStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
                windowEvent -> {
                    try {
                        closeTextPane(windowEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


    private void closeTextPane(WindowEvent windowEvent) throws IOException {


        if(!TA.getText().equals(file.getLatestText())){
        // (true){//距离latestText有改动
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.titleProperty().set("注意！");

            alert.headerTextProperty().set("是否保存改动");
            alert.getButtonTypes().setAll(
                    new ButtonType("是"),
                    new ButtonType("否")
//                    new ButtonType("取消")
                    );
            Optional<ButtonType> result = alert.showAndWait();

            switch (result.get().getText()){
                case "是" :{
                    saveFile();
                    break;
                }
                case "否" :{
                    break;
                }
//                case "取消" :{
//                    windowEvent.
//                    System.out.println("can");
//                    return;
//                }
            }

        }
        controller.closeFile(file);
    }

    private void saveFile() throws IOException {
        file.setLatestText(TA.getText());
        server.saveFile(file);
        controller.updateOpenFilesTable();
        //System.out.println("saveFile " + server.saveFile(file));
    }


    @Override
    public void init() throws Exception {
        Stage fileStage = new Stage();
        root.getStylesheets().add("FrontHead/css/textPane.css");

        this.start(fileStage);
        initMenu();
        initTextArea();
        saveFile();
    }

    private void initTextArea() {
        TA = new TextArea();
        TA.getStyleClass().add("textArea");
        TA.setPrefWidth(root.getScene().getWidth());
        TA.setPrefHeight(root.getScene().getHeight()-MENU_HEIGHT);
        TA.setText(file.getLatestText());
        startText = TA.getText();
        root.getChildren().add(TA);
    }

    private void initMenu() {
        MB= new MenuBar();
        Menu fileMeau = new Menu("file");

        MenuItem saveItem = new MenuItem("save");
        fileMeau.getItems().add(saveItem);
        saveItem.setOnAction(ActionEvent -> {
            try {
                saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MB.getMenus().addAll(fileMeau);
        MB.setPrefWidth(root.getScene().getWidth());
        root.getChildren().add(MB);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setFile(VirtualFile file) {
        this.file = file;
    }

    public void setServer(Server server) {
        this.server = server;
    }

}
