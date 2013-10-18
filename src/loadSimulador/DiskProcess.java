
package loadSimulador;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import utils.MyUtilies;

/**
 *
 * Ing. Mario Jose Villamizar Cano
 * Universidad de los Andes
 * Bogota - Colombia
 * Maestria en Ingenieria - Sistemas y Computacion
 * mjvc007@hotmail.com - mj.villamizar24@uniandes.edu.co
 * 2009
 * 
 */

public class DiskProcess extends Process {

    public DiskProcess(int id){
        super(id);
    }
    /*
     * Read a file f to the disk
     * return the line number read
     */
    public long readFile(File f){
        long readLineNumber = 0;
        String line = null;
        BufferedReader input = null;
        try {
             input =  new BufferedReader(new FileReader(f));
             while(( line = input.readLine()) != null){
                 readLineNumber++;
             }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return readLineNumber;
    }

    /*
     * Write a simple file f of sizeKBytes KBytes to the disk
     */
    public void writeSimpleFile(File f, int sizeKBytes){
        Writer output = null;

        String lineWritten = "abcdefghijklmnopqrstuvwxyz12345\n";

        try {
            output = new BufferedWriter(new FileWriter(f));
            for(int i=0;i<sizeKBytes*32;i++)
                output.write(lineWritten);
        }
        catch(IOException e){
            System.out.println("Error opening the file: "+e.toString());
        }
        finally {
            try {
                output.close();
            } catch (IOException ex) {
                System.out.println("Error closing the file: "+ex.toString());
            }
        }
    }

    /*
     * Write a complex file f of sizeKBytes KBytes to the disk
     */
    public void writeComplexFile(File f, int sizeKBytes){
        Writer output = null;
        long fileSize = 0;
        String lineWritten = "";
        long sizeBytes = sizeKBytes * 1024;
        try {
            output = new BufferedWriter(new FileWriter(f));
            while(fileSize<sizeBytes)
            {
                lineWritten = String.valueOf(MyUtilies.generateAleatoryDoubleNumber());
                output.write(lineWritten);
                fileSize += lineWritten.length();
            }
        }
        catch(IOException e){
            System.out.println("Error opening the file: "+e.toString());
        }
        finally {
            try {
                output.close();
            } catch (IOException ex) {
                System.out.println("Error closing the file: "+ex.toString());
            }
        }
    }

    /*
     * Delete a file f of the disk
     */
    public void deleteFile(File file){
        if(file.exists())
            file.delete();
    }

    /*
     * Compress a source file (sourceFile) to a destination file (targetFile)
     */
    public void compressFile(File sourceFile, File targetFile){
        try {
            String pathSourceFile = sourceFile.getAbsolutePath();
            String pathTargetFile = targetFile.getAbsolutePath();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pathTargetFile));
            FileInputStream fis = new FileInputStream(pathSourceFile);

            // put a new ZipEntry in the ZipOutputStream
            zos.putNextEntry(new ZipEntry(pathSourceFile));

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
    }
}
