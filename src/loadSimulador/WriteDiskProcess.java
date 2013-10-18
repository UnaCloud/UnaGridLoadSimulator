
package loadSimulador;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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

public class WriteDiskProcess extends DiskProcess {

    /*
     * Execute write by automatic task
     */
    public static final String W_AUTOMATIC_BY_TASK = "W AUTOMATIC BY TASK";

    /*
     * Execute write by automatic time
     */
    public static final String W_AUTOMATIC_BY_TIME = "W AUTOMATIC BY TIME";

    /*
     * read file
     */
    protected File writeFile;

    /*
     * type of read
     */
    protected String writeType;

    /*
     * Location of the automatic write file generated
     */
    private static String locationAutomaticWriteFile = "./documents/automaticWrite/automaticWriteFile";

    /*
     * Size in KBytes of the write file
     */
    private int sizeKBytes = 0;

    /*
     * Indicates if the write file generated most be erased
     */
    private boolean eraseFilesGenerated;

    /*
     * Execution time programmed for a time task
     */
    private int scheduledTimeReadSeconds;

    /*
     * For automatic write by task
     */
    public WriteDiskProcess(int id, String writeType, int sizeKBytes, boolean eraseFilesGenerated){
        super(id);
        this.writeType = writeType;
        this.sizeKBytes = sizeKBytes;
        this.eraseFilesGenerated = eraseFilesGenerated;
    }

    /*
     * For automatic write by time
     */
    public WriteDiskProcess(int id, String writeType, int sizeKBytes, boolean eraseFilesGenerated, int secondTime){
        super(id);
        this.writeType = writeType;
        this.sizeKBytes = sizeKBytes;
        this.eraseFilesGenerated = eraseFilesGenerated;
        this.scheduledTimeReadSeconds = secondTime;
    }

    @Override
    public void run(){
        if(this.writeType.equals(WriteDiskProcess.W_AUTOMATIC_BY_TASK))
            taskAutomaticWrite();
        else
            timeAutomaticWrite();
    }

    /*
     * For automatic write by task
     */
    public void taskAutomaticWrite(){
        //Establish the new file path
        this.writeFile = new File(locationAutomaticWriteFile+MyUtilies.generateAleatoryDoubleNumber()+".txt");
        //Delete the old automatic file
        deleteFile(this.writeFile);
        //Crate the new automatic file
        try {
            this.writeFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error creating the file: "+ex.toString());
        }
        //Enter information to the new automatic file
        Date inicio = new Date();
        writeSimpleFile(this.writeFile, this.sizeKBytes);
        Date fin = new Date();
        this.processRealDuration = fin.getTime() - inicio.getTime();
        System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");

       if(eraseFilesGenerated)
           deleteFile(this.writeFile);
    }

    /*
     * For automatic write by time
     */
    public void timeAutomaticWrite(){
        Date inicio = new Date();
        Date fin;
        this.writeFile = new File(locationAutomaticWriteFile+MyUtilies.generateAleatoryDoubleNumber()+".txt");
        //Establish the new file path        
        do{
            //Delete the old automatic file
            deleteFile(this.writeFile);
            //Crate the new automatic file
            try {
                this.writeFile.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error creating the file: "+ex.toString());
            }
            //Enter information to the new automatic file
            writeSimpleFile(this.writeFile, this.sizeKBytes);
            fin = new Date();
            this.processRealDuration = fin.getTime() - inicio.getTime();
            if(eraseFilesGenerated)
                deleteFile(this.writeFile);
       }
       while(this.processRealDuration<this.scheduledTimeReadSeconds*1000);
       System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
    }

    public String getSpecificInformation(){
        String message="";
        if(this.writeType.equals(WriteDiskProcess.W_AUTOMATIC_BY_TASK))
            message += "File size (KB): "+this.sizeKBytes;
        if(this.writeType.equals(WriteDiskProcess.W_AUTOMATIC_BY_TIME))
            message += "File size (KB): "+this.sizeKBytes+" Time (seconds): "+this.scheduledTimeReadSeconds;
        return message;
    }

    public boolean isEraseFilesGenerated() {
        return eraseFilesGenerated;
    }

    public void setEraseFilesGenerated(boolean eraseFilesGenerated) {
        this.eraseFilesGenerated = eraseFilesGenerated;
    }

    public static String getLocationAutomaticWriteFile() {
        return locationAutomaticWriteFile;
    }

    public static void setLocationAutomaticWriteFile(String locationAutomaticWriteFile) {
        WriteDiskProcess.locationAutomaticWriteFile = locationAutomaticWriteFile;
    }

    public int getScheduledTimeReadSeconds() {
        return scheduledTimeReadSeconds;
    }

    public void setScheduledTimeReadSeconds(int scheduledTimeReadSeconds) {
        this.scheduledTimeReadSeconds = scheduledTimeReadSeconds;
    }

    public int getSizeKBytes() {
        return sizeKBytes;
    }

    public void setSizeKBytes(int sizeKBytes) {
        this.sizeKBytes = sizeKBytes;
    }

    public File getWriteFile() {
        return writeFile;
    }

    public void setWriteFile(File writeFile) {
        this.writeFile = writeFile;
    }

    public String getWriteType() {
        return writeType;
    }

    public void setWriteType(String writeType) {
        this.writeType = writeType;
    }

    /*
     * A test for each case (there are 2 cases)
     */
    public static void main(String [] args){
        //WriteDiskProcess wdp1 = new WriteDiskProcess(1, WriteDiskProcess.W_AUTOMATIC_BY_TASK, 200000, false);
        //wdp1.start();
        WriteDiskProcess trdp4 = new WriteDiskProcess(1, WriteDiskProcess.W_AUTOMATIC_BY_TIME, 20000, true, 10);
        trdp4.start();
    }
}
