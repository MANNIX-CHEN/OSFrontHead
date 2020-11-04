package FrontHead.content.component;


import BackGround.Server;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import sample.Controller;

import java.util.Timer;
import java.util.TimerTask;


public abstract class FilePaneCom extends StackPane {
    protected Button img;   //显示图标
    protected Label title;  //显示名称
    protected int clickNum;
    protected int callTimes;
    protected Controller controller;
    protected boolean flag;


    public abstract void setImg();
    public abstract void mouseClickedTiwce() throws Exception;


    Server server;
    public FilePaneCom(String name,Controller controller){
        img = new Button();
        title = new Label();
        setController(controller);
        setServer(controller.getServer());

        //设置自己的样式
        getStyleClass().add("filePaneComPane");
        //设置图片
        setImg();
        setAlignment(Pos.CENTER);

        //设置文本
        title.setText(name);

        title.getStyleClass().add("filePaneComTitle");

        getChildren().add(img);
        getChildren().add(title);

        int x ;
        clickNum = 0;
        callTimes = 0;
        addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

            getStyleClass().add("test_red_back");


            clickNum ++;
            Timer timer = new Timer();
            if (clickNum==2){
                //System.out.println("click twice");
                try {
                    mouseClickedTiwce();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //双击事件
                    clickNum = 0;
                    this.cancel();
                    return;
                }
            },500,500);

        });



    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
