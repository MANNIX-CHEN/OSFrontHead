package FrontHead.content.component;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public abstract class FilePaneCom extends StackPane {
    protected Button img;   //显示图标
    protected Label title;  //显示名称
    protected int clickNum;
    protected int callTimes;
    protected boolean flag;


    public abstract void setImg();
    public abstract void mouseClickedTiwce();


    public FilePaneCom(String name){
        img = new Button();
        title = new Label();

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
            clickNum ++;
            Timer timer = new Timer();
            if (clickNum==2){
                //System.out.println("click twice");
                mouseClickedTiwce();
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

}
