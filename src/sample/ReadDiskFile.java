package sample;

import java.io.File;
import java.lang.reflect.Field;

public class ReadDiskFile {
    /*�����������ȡdiskfile�����ڽ�diskFile���ļ���Ⱦ��ǰ��*/




    /*����Ϊδ���̨���ϵİ汾�ĳ�Ա�Լ����췽��*/
    String openFilesInfo[][];

    ReadDiskFile(){
        openFilesInfo = new String[5][5];
        for (int i = 0 ; i < 5 ; i++ )
        openFilesInfo[i]= new String[]{"java.txt", "F:\\study\\JavaStudy\\OSKS\\src\\sample", "3"+i, "4", "ֻ��"};

    }
    /*����Ϊ���̨���Ϻ�İ汾�ĳ�Ա�Լ����췽��*/

    ReadDiskFile(File diskFile){

    }





}
