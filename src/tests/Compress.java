/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author mj.villamizar24
 */
public class Compress {
    public static void main(String [] args){
        Date inicio = new Date();
        try {
        String source = "./Documentos/MVCS.vmdk";
        String target = "./Documentos/MVCS.zip";
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target));
		FileInputStream fis = new FileInputStream(source);

			// put a new ZipEntry in the ZipOutputStream
		zos.putNextEntry(new ZipEntry(source));

		int size = 0;
		byte[] buffer = new byte[1024];

			// read data to the end of the source file and write it to the zip
			// output stream.
		while ((size = fis.read(buffer, 0, buffer.length)) > 0) {
			zos.write(buffer, 0, size);
		}

		zos.closeEntry();
		fis.close();

			// Finish zip process
		zos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Date fin = new Date();
        long tiempo = fin.getTime() - inicio.getTime();
        System.out.println("Tiempo compresion: "+tiempo);

    }
}
