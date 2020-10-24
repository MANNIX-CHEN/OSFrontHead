package sample;

import java.io.File;
import java.lang.reflect.Field;

public class ReadDiskFile {
    /*这个类用来读取diskfile，用于将diskFile的文件渲染到前端*/




    /*以下为未与后台整合的版本的成员以及构造方法*/
    String openFilesInfo[][];

    ReadDiskFile(){
        openFilesInfo = new String[5][5];
        for (int i = 0 ; i < 5 ; i++ )
        openFilesInfo[i]= new String[]{"java.txt", "F:\\study\\JavaStudy\\OSKS\\src\\sample", "3"+i, "4", "只读"};

    }
    /*以下为与后台整合后的版本的成员以及构造方法*/

    ReadDiskFile(File diskFile){

    }





}
