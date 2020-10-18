package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        URL location = getClass().getResource("sample.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());//不能使用静态读取方法
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("文件管理系统");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Controller controller = fxmlLoader.getController();//获取Controller实例对象
        controller.Init();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
