package FrontHead.content.component;


import javafx.event.EventHandler;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
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
    protected boolean flag; //用于判断是否选中

    
    public abstract void setImg();
    public abstract void mouseClickedTiwce() throws Exception;



    public FilePaneCom(String name,Controller controller){
        img = new Button();
        title = new Label();
        setController(controller);

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
        	
        	
        	getStyleClass().add("test_deepblue_back");
        	img.setStyle("-fx-background-color:#1e90ff");
            flag=true; //用于判断已经选中的单位不会再有移入移出的变化
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
            },500);

        });
        
        //移入
        addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {        	
        	getStyleClass().remove("test_white_back");
        	if(!flag) {
        		getStyleClass().add("test_blue_back");
        		img.setStyle("-fx-background-color:#F0F8FF");
        	}       
        });
        
       //移出
        addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
        	if(!flag) {
        		getStyleClass().add("test_white_back");
        		img.setStyle("-fx-background-color:white");
        	}   
        });
       

    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    
}
