
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

public class WriteReadDiskProcess extends DiskProcess  {
    /*
     * Execute read by manual task
     */
    public static final String RW_MANUAL_BY_TASK = "RW MANUAL BY TASK";

    /*
     * Execute read by automatic task
     */
    public static final String RW_AUTOMATIC_BY_TASK = "RW AUTOMATIC BY TASK";

    /*
     * Execute read by manual time
     */
    public static final String RW_MANUAL_BY_TIME = "RW MANUAL BY TIME";

    /*
     * Execute read by automatic time
     */
    public static final String RW_AUTOMATIC_BY_TIME = "RW AUTOMATIC BY TIME";

    /*
     * read file
     */
    protected File readFile;

    /*
     * write file
     */
    protected File writeFile;

    /*
     * type of read
     */
    protected String readWriteType;

    /*
     * Location of the automatic read file generated
     */
    private static String locationAutomaticReadFile = "./documents/automaticReadWrite/automaticReadFile";

    /*
     * Location of the automatic write file generated
     */
    private static String locationAutomaticWriteFile = "./documents/automaticReadWrite/automaticWriteFile";
    /*
     * Location of the manual write file generated
     */
    private static String locationManualWriteFile = "./documents/manualReadWrite/manualWriteFile";

    /*
     * Size in KBytes of the read/write file
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
     * For manual read write by task
     */
    public WriteReadDiskProcess(int id, File readFile, String readWriteType, boolean eraseFilesGenerated){
        super(id);
        this.readFile = readFile;
        this.readWriteType = readWriteType;
        this.eraseFilesGenerated = eraseFilesGenerated;
    }

    /*
     * For automatic read write by task
     */
    public WriteReadDiskProcess(int id, String readWriteType, int sizeKBytes, boolean eraseFilesGenerated){
        super(id);
        this.readWriteType = readWriteType;
        this.sizeKBytes = sizeKBytes;
        this.eraseFilesGenerated = eraseFilesGenerated;
    }

    /*
     * For manual read write by time
     */
    public WriteReadDiskProcess(int id, File readFile, String readWriteType, int secondTime, boolean eraseFilesGenerated){
        super(id);
        this.readFile = readFile;
        this.readWriteType = readWriteType;
        this.scheduledTimeReadSeconds = secondTime;
        this.eraseFilesGenerated = eraseFilesGenerated;
    }

    /*
     * For automatic read write by time
     */
    public WriteReadDiskProcess(int id, String readWriteType, int sizeKBytes, boolean eraseFilesGenerated, int secondTime){
        super(id);
        this.readWriteType = readWriteType;
        this.sizeKBytes = sizeKBytes;
        this.eraseFilesGenerated = eraseFilesGenerated;
        this.scheduledTimeReadSeconds = secondTime;
    }

    @Override
    public void run(){
        if(this.readWriteType.equals(WriteReadDiskProcess.RW_MANUAL_BY_TASK))
            taskManualReadWrite();
        else
            if(this.readWriteType.equals(WriteReadDiskProcess.RW_MANUAL_BY_TIME))
                timeManualReadWrite();
            else
                if(this.readWriteType.equals(WriteReadDiskProcess.RW_AUTOMATIC_BY_TASK))
                    taskAutomaticReadWrite();
                else
                    if(this.readWriteType.equals(WriteReadDiskProcess.RW_AUTOMATIC_BY_TIME))
                        timeAutomaticReadWrite();
    }

     /*
     * For manual read write by task
     */
    public void taskManualReadWrite(){
        Date inicio = new Date();
        this.writeFile = new File(locationManualWriteFile+MyUtilies.generateAleatoryDoubleNumber()+".zip");
        //Delete the old automatic file
        deleteFile(this.writeFile);
        //Crate the new automatic file
        try {
            this.writeFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error creating the file: "+ex.toString());
        }

        //Compress the file
        compressFile(this.readFile, this.writeFile);
        Date fin = new Date();
        this.processRealDuration = fin.getTime() - inicio.getTime();
        System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
        if(this.eraseFilesGenerated)
            this.writeFile.delete();
    }

    /*
     * For manual read write by time
     */
    public void timeManualReadWrite(){
        Date inicio = new Date();
        Date fin;
        this.writeFile = new File(locationManualWriteFile+MyUtilies.generateAleatoryDoubleNumber()+".zip");
        
        do{
            //Delete the old automatic file
            deleteFile(this.writeFile);
            //Crate the new automatic file
            try {
                this.writeFile.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error creating the file: "+ex.toString());
            }
            compressFile(this.readFile, this.writeFile);
            fin = new Date();
            this.processRealDuration = fin.getTime() - inicio.getTime();
        }
        while(this.processRealDuration<this.scheduledTimeReadSeconds*1000);
        if(this.eraseFilesGenerated)
            deleteFile(this.writeFile);
        System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
    }

    /*
     * For automatic read write by task
     */
    public void taskAutomaticReadWrite(){
        Date inicio = new Date();
        //Establish the new file path
        this.readFile = new File(locationAutomaticReadFile+MyUtilies.generateAleatoryDoubleNumber()+".txt");
        this.writeFile = new File(locationAutomaticWriteFile+MyUtilies.generateAleatoryDoubleNumber()+".zip");
        //Delete the old automatic file
        deleteFile(this.readFile);
        deleteFile(this.writeFile);
        //Crate the new automatic file
        try {
            this.readFile.createNewFile();
            this.writeFile.createNewFile();
        } catch (IOException ex) {
            System.out.println("Error creating the file: "+ex.toString());
        }
        //Enter information to the new automatic file
       writeComplexFile(this.readFile, this.sizeKBytes);

       //Compress the file
       compressFile(this.readFile, this.writeFile);
       Date fin = new Date();
       this.processRealDuration = fin.getTime() - inicio.getTime();
       System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
       if(eraseFilesGenerated)
       {
           deleteFile(this.readFile);
           deleteFile(this.writeFile);
       }
    }

    /*
     * For automatic read write by time
     */
    public void timeAutomaticReadWrite(){
        Date inicio = new Date();
        Date fin;
        //Establish the new file path
        this.readFile = new File(locationAutomaticReadFile+MyUtilies.generateAleatoryDoubleNumber()+".txt");
        this.writeFile = new File(locationAutomaticWriteFile+MyUtilies.generateAleatoryDoubleNumber()+".zip");
        //Delete the old automatic file
        deleteFile(this.readFile);
        deleteFile(this.writeFile);
        //Crate the new automatic file
        try {
            this.readFile.createNewFile();            
        } catch (IOException ex) {
            System.out.println("Error creating the file: "+ex.toString());
        }
        //Enter information to the new automatic file
       writeComplexFile(this.readFile, this.sizeKBytes);
       //Compress the file
       do
       {
           try {
                this.writeFile.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error creating the file: "+ex.toString());
            }
           compressFile(this.readFile, this.writeFile);
           deleteFile(this.writeFile);
           fin = new Date();
           this.processRealDuration = fin.getTime() - inicio.getTime();
       }while(this.processRealDuration<this.scheduledTimeReadSeconds*1000);
       System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
       if(eraseFilesGenerated)
       {
           deleteFile(this.readFile);
           deleteFile(this.writeFile);
       }
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
        WriteReadDiskProcess.locationAutomaticReadFile = locationAutomaticReadFile;
    }

    public static String getLocationAutomaticWriteFile() {
        return locationAutomaticWriteFile;
    }

    public static void setLocationAutomaticWriteFile(String locationAutomaticWriteFile) {
        WriteReadDiskProcess.locationAutomaticWriteFile = locationAutomaticWriteFile;
    }

    public static String getLocationManualWriteFile() {
        return locationManualWriteFile;
    }

    public static void setLocationManualWriteFile(String locationManualWriteFile) {
        WriteReadDiskProcess.locationManualWriteFile = locationManualWriteFile;
    }

    public File getReadFile() {
        return readFile;
    }

    public void setReadFile(File readFile) {
        this.readFile = readFile;
    }

    public String getReadWriteType() {
        return readWriteType;
    }

    public void setReadWriteType(String readWriteType) {
        this.readWriteType = readWriteType;
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

    public String getSpecificInformation(){
        String message="";
        if(this.readWriteType.equals(WriteReadDiskProcess.RW_AUTOMATIC_BY_TASK))
            message += "File size (KB): "+this.sizeKBytes;
        if(this.readWriteType.equals(WriteReadDiskProcess.RW_MANUAL_BY_TIME))
            message += "Time (seconds): "+this.scheduledTimeReadSeconds;
        if(this.readWriteType.equals(WriteReadDiskProcess.RW_AUTOMATIC_BY_TIME))
            message += "File size (KB): "+this.sizeKBytes+" Time (seconds): "+this.scheduledTimeReadSeconds;
        return message;
    }

    /*
     * A test for each case (there are 4 cases)
     */
    public static void main(String [] args){
        WriteReadDiskProcess wrdp1 = new WriteReadDiskProcess(1, new File ("./documents/manualReadWrite/ejecutable.exe"), WriteReadDiskProcess.RW_MANUAL_BY_TASK, true);
        wrdp1.start();
        //WriteReadDiskProcess wrdp2 = new WriteReadDiskProcess(1, new File ("./documents/manualReadWrite/ejecutable.exe"), WriteReadDiskProcess.RW_MANUAL_BY_TIME, 20, true);
        //wrdp2.start();
        //WriteReadDiskProcess wrdp3 = new WriteReadDiskProcess(1, WriteReadDiskProcess.RW_AUTOMATIC_BY_TASK, 20000, true);
        //wrdp3.start();
        //WriteReadDiskProcess wrdp4 = new WriteReadDiskProcess(1, WriteReadDiskProcess.RW_AUTOMATIC_BY_TIME, 20000, false, 30);
        //wrdp4.start();
    }
}
