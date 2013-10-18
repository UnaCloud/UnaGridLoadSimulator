
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

public class ReadDiskProcess extends DiskProcess {
    /*
     * Execute read by manual task
     */
    public static final String R_MANUAL_BY_TASK = "R MANUAL BY TASK";

    /*
     * Execute read by automatic task
     */
    public static final String R_AUTOMATIC_BY_TASK = "R AUTOMATIC BY TASK";

    /*
     * Execute read by manual time
     */
    public static final String R_MANUAL_BY_TIME = "R MANUAL BY TIME";

    /*
     * Execute read by automatic time
     */
    public static final String R_AUTOMATIC_BY_TIME = "R AUTOMATIC BY TIME";

    /*
     * Location of the automatic read file generated
     */
    private static String locationAutomaticReadFile = "./documents/automaticRead/automaticReadFile";

    /*
     * Size in KBytes of the read file
     */
    private int sizeKBytes = 0;

    /*
     * Indicates if the read file generated most be erased
     */
    private boolean eraseFilesGenerated;

    /*
     * Execution time programmed for a time task
     */
    private int scheduledTimeReadSeconds;

    /*
     * read file
     */
    protected File readFile;

    /*
     * type of read
     */
    protected String readType;

    /*
     * For manual read by task
     */
    public ReadDiskProcess(int id, File readFile, String readType){
        super(id);
        this.readFile = readFile;
        this.readType = readType;
    }

    /*
     * For manual read by time
     */
    public ReadDiskProcess(int id, File readFile, String readType, int secondTime){
        super(id);
        this.readFile = readFile;
        this.readType = readType;
        this.scheduledTimeReadSeconds = secondTime;
    }

    /*
     * For automatic read by task
     */
    public ReadDiskProcess(int id, String readType, int sizeKBytes, boolean eraseFilesGenerated){
        super(id);
        this.readType = readType;
        this.sizeKBytes = sizeKBytes;
        this.eraseFilesGenerated = eraseFilesGenerated;
    }

    /*
     * For automatic read by time
     */
    public ReadDiskProcess(int id, String typeRead, int sizeKBytes, boolean eraseFilesGenerated, int secondTime){
        super(id);
        this.readType = typeRead;
        this.sizeKBytes = sizeKBytes;
        this.eraseFilesGenerated = eraseFilesGenerated;
        this.scheduledTimeReadSeconds = secondTime;
    }

    @Override
    public void run(){
        if(this.readType.equals(ReadDiskProcess.R_MANUAL_BY_TASK))
            taskManualRead();
        else
            if(this.readType.equals(ReadDiskProcess.R_AUTOMATIC_BY_TASK))
                taskAutomaticRead();
            else
                if(this.readType.equals(ReadDiskProcess.R_MANUAL_BY_TIME))
                    timeManualRead();
                else
                    if(this.readType.equals(ReadDiskProcess.R_AUTOMATIC_BY_TIME))
                        timeAutomaticRead();
    }

    /*
     * For manual read by task
     */
    public void taskManualRead(){
        Date inicio = new Date();
        long readLineNumber = this.readFile(this.readFile);
        Date fin = new Date();
        this.processRealDuration = fin.getTime() - inicio.getTime();
        System.out.println("Number of line read: "+readLineNumber);
        System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
    }

    /*
     * For manual read by time
     */
    public void timeManualRead(){
        Date inicio = new Date();
        long readLineNumber = 0;
        Date fin;
        do{
            readLineNumber = this.readFile(this.readFile);
            fin = new Date();
            this.processRealDuration = fin.getTime() - inicio.getTime();
        }
        while(this.processRealDuration<this.scheduledTimeReadSeconds*1000);
        System.out.println("Number of line read: "+readLineNumber);
        System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
    }   

    /*
     * For automatic read by task
     */
    public void taskAutomaticRead(){
        //Establish the new file path
        this.readFile = new File(locationAutomaticReadFile+MyUtilies.generateAleatoryDoubleNumber()+".txt");
        //Delete the old automatic file
        deleteFile(this.readFile);
        //Crate the new automatic file
        try {
            this.readFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error creating the file: "+ex.toString());
        }
        //Enter information to the new automatic file
       writeSimpleFile(this.readFile, this.sizeKBytes);

       //Raed the file
       Date inicio = new Date();
       long readLineNumber = this.readFile(this.readFile);
       Date fin = new Date();
       this.processRealDuration = fin.getTime() - inicio.getTime();
       System.out.println("Number of line read: "+readLineNumber);
       System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");

       if(eraseFilesGenerated)
           deleteFile(this.readFile);
    }

    /*
     * For automatic read by time
     */
    public void timeAutomaticRead(){
        //Establish the new file path
        this.readFile = new File(locationAutomaticReadFile+MyUtilies.generateAleatoryDoubleNumber()+".txt");
        //Delete the old automatic file
        deleteFile(this.readFile);
        //Crate the new automatic file
        try {
            this.readFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error creating the file: "+ex.toString());
        }
        //Enter information to the new automatic file
       writeSimpleFile(this.readFile, this.sizeKBytes);

       //Raed the file
       Date inicio = new Date();
       long readLineNumber = 0;
       Date fin;
       do{
           readLineNumber = this.readFile(this.readFile);
           fin = new Date();
           this.processRealDuration = fin.getTime() - inicio.getTime();
       }
       while(this.processRealDuration<this.scheduledTimeReadSeconds*1000);
       System.out.println("Number of line read: "+readLineNumber);
       System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");

       if(eraseFilesGenerated)
           deleteFile(this.readFile);
    }

    public String getSpecificInformation(){
        String mensaje = "";
        if(this.readType.equals(ReadDiskProcess.R_AUTOMATIC_BY_TASK))
            mensaje += "File size (KBytes): "+this.sizeKBytes;
        if(this.readType.equals(ReadDiskProcess.R_MANUAL_BY_TIME))
            mensaje += "Time (seconds): "+this.scheduledTimeReadSeconds;
        if(this.readType.equals(ReadDiskProcess.R_AUTOMATIC_BY_TIME))
            mensaje += "File size (KBytes): "+this.sizeKBytes+" Time (seconds): "+this.scheduledTimeReadSeconds;
        return mensaje;
    }

    public boolean isEraseFilesGenerated() {
        return eraseFilesGenerated;
    }

    public void setEraseFilesGenerated(boolean eraseFilesGenerated) {
        this.eraseFilesGenerated = eraseFilesGenerated;
    }

    public static String getLocationAutomaticReadFile() {
        return locationAutomaticReadFile;
    }

    public static void setLocationAutomaticReadFile(String locationAutomaticReadFile) {
        ReadDiskProcess.locationAutomaticReadFile = locationAutomaticReadFile;
    }

    public File getReadFile() {
        return readFile;
    }

    public void setReadFile(File readFile) {
        this.readFile = readFile;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
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

    /*
     * A test for each case (there are 4 cases)
     */
    public static void main(String [] args){
        //ReadDiskProcess trdp1 = new ReadDiskProcess(1, new File ("./documents/manualRead/ejecutable.exe"), ReadDiskProcess.R_MANUAL_BY_TASK);
        //trdp1.start();
        //ReadDiskProcess trdp2 = new ReadDiskProcess(1, ReadDiskProcess.R_AUTOMATIC_BY_TASK, 20000, false);
        //trdp2.start();
        //ReadDiskProcess trdp3 = new ReadDiskProcess(1, new File ("./documents/manualRead/ejecutable.exe"), ReadDiskProcess.R_MANUAL_BY_TIME, 20);
        //trdp3.start();
        ReadDiskProcess trdp4 = new ReadDiskProcess(1, ReadDiskProcess.R_AUTOMATIC_BY_TIME, 200, true, 2);
        trdp4.start();
    }

}
