package young.get.archive;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.zip.GZIPInputStream;
/**
 * UncompressGZ
 * 
 * @author young
 * 
 */
public class UncompressGZ {
    public static void ungz(String filename) throws Exception {

        String fileName = filename;
        GZIPInputStream gzi = new GZIPInputStream(new FileInputStream(fileName));
        int to = fileName.lastIndexOf('.');
        String toFileName = fileName.substring(0, to);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(toFileName));
        int b;
        byte[] d = new byte[1024];
        try {

            while ((b = gzi.read(d)) > 0) {
                bos.write(d, 0, b);
            }

        } catch (Exception err) {
        }
        gzi.close();
        bos.close();
    }
    /*delete the gz files*/
    public static void delgz(String filename) throws Exception {
        String fileName = filename;
        File file = new File(fileName);
        file.delete();
    }
}
