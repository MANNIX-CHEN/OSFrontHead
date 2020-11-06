import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controller;

import java.net.URL;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        URL location = getClass().getResource("sample/sample.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());//����ʹ�þ�̬��ȡ����
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("�ļ�����ϵͳ");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Controller controller = fxmlLoader.getController();//��ȡControllerʵ������
        controller.Init();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
