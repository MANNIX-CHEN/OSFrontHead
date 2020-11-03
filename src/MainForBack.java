import BackGround.Server;
import FrontHead.content.Catalogue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MainForBack {



    public static void main(String[] args) throws IOException {
        Server testServer = new Server();
        Catalogue test = new Catalogue("ggg",testServer);
        testServer.showMeFat();
        //bytes.set(0,new Byte[]{});


    }
}
