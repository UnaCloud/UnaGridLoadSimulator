
package loadSimulador;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

public class LoadSimulatorModel {
    /*
     * ID of the process to execute
     */
    private int actualID;

    /*
     * Indicates if the execution process will be realized through a pool of threads
     */
    public boolean usePool;

     /*
     * Indicates if the execution process will be realized through a single thread pool
     */
    public boolean singleThread;

    /*
     * Concurrent thread number of the process execution pool
     */
    public int poolSize;

    /*
     * Indicates if the execution process will be started at the same time
     */
    public boolean beginProcessExecutionAtTheSameTime;

    /*
     * The process execution pool
     */
    private ExecutorService poolThreads;

    /*
     * Write-read processes
     */
    private ArrayList <WriteReadDiskProcess> WRProcesses;

    /*
     * Write processes
     */
    private ArrayList <WriteDiskProcess> WProcesses;
    
    /*
     * Read processes
     */
    private ArrayList <ReadDiskProcess> RProcesses;

    /*
     * Procesing processes by task
     */
    private ArrayList <TaskProcessingProcess> taskProcessingProcesses;

    /*
     * Procesing processes by time
     */
    private ArrayList <TimeProcessingProcess> timeProcessingProcesses;


    public LoadSimulatorModel(boolean usePool, boolean singleThread, int poolSize, boolean beginProcessExecutionAtTheSameTime){
        this.usePool= usePool;
        this.poolSize = poolSize;
        this.singleThread = singleThread;
        this.beginProcessExecutionAtTheSameTime = beginProcessExecutionAtTheSameTime;
        this.WRProcesses = new ArrayList <WriteReadDiskProcess> ();
        this.WProcesses = new ArrayList <WriteDiskProcess> ();
        this.RProcesses = new ArrayList <ReadDiskProcess> ();
        this.taskProcessingProcesses = new ArrayList <TaskProcessingProcess> ();
        this.timeProcessingProcesses = new ArrayList <TimeProcessingProcess> ();
        System.out.println("Use pool: "+this.usePool+" Use single thread: "+this.singleThread+" Thread number: "+this.poolSize+" Begin at the same time: "+this.beginProcessExecutionAtTheSameTime);
        this.establacerPoolThreads();
        this.actualID = 1;
    }

    public void establacerPoolThreads(){
        if(this.usePool && this.singleThread){
            this.poolThreads = Executors.newFixedThreadPool(1);
            System.out.println("\n\nExecuting ONE thread option");
        }
        else
            if(this.usePool){
                this.poolThreads = Executors.newFixedThreadPool(this.poolSize);
                System.out.println("\n\nExecuting MANY thread option");
            }
            else{
                System.out.println("\n\nExecuting without thread pool");
            }
    }
    
    /*
     * Add a task processing process
     */
    public void addTaskProcessingProcess(long iterationNumber){
        TaskProcessingProcess tpp = new TaskProcessingProcess(this.actualID, iterationNumber);
        this.taskProcessingProcesses.add(tpp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(tpp);
        }
        else
            tpp.start();
        this.actualID++;
    }

    /*
     * Add a time processing process
     */
    public void addTimeProcessingProcess(long scheduledExecutionTime){
        TimeProcessingProcess tpp = new TimeProcessingProcess(this.actualID, scheduledExecutionTime);
        this.timeProcessingProcesses.add(tpp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(tpp);
        }
        else
            tpp.start();
        this.actualID++;
    }

    /*
     * Add a manual read write by task
     */
    public void addTaskManualWriteReadDiskProcess(File pathManualFile, boolean eraseFiles){
        WriteReadDiskProcess wrdp = new WriteReadDiskProcess(this.actualID, pathManualFile, WriteReadDiskProcess.RW_MANUAL_BY_TASK, eraseFiles);
        this.WRProcesses.add(wrdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(wrdp);
        }
        else
            wrdp.start();
        this.actualID++;
    }
    
    /*
     * Add a manual read write by time
     */
    public void addTimeManualWriteReadDiskProcess(File pathManualFile, int time, boolean eraseFiles){
        WriteReadDiskProcess wrdp = new WriteReadDiskProcess(this.actualID, pathManualFile, WriteReadDiskProcess.RW_MANUAL_BY_TIME, time, eraseFiles);
        this.WRProcesses.add(wrdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(wrdp);
        }
        else
            wrdp.start();
        this.actualID++;
    }

    /*
     * Add a automatic read write by task
     */
    public void addTaskAutomaticWriteReadDiskProcess(int sizeKBytes, boolean eraseFiles){
        WriteReadDiskProcess wrdp = new WriteReadDiskProcess(this.actualID, WriteReadDiskProcess.RW_AUTOMATIC_BY_TASK, sizeKBytes, eraseFiles);
        this.WRProcesses.add(wrdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(wrdp);
        }
        else
            wrdp.start();
        this.actualID++;
    }

    /*
     * Add a automatic read write by time
     */
    public void addTimeAutomaticWriteReadDiskProcess(int sizeKBytes, int time, boolean eraseFiles){
        WriteReadDiskProcess wrdp = new WriteReadDiskProcess(this.actualID, WriteReadDiskProcess.RW_AUTOMATIC_BY_TIME, sizeKBytes, eraseFiles, time);
        this.WRProcesses.add(wrdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(wrdp);
        }
        else
            wrdp.start();
        this.actualID++;
    }

    /*
     * Add a manual read task
     */
    public void addTaskManualReadDiskProcess(File pathManualFile){
        ReadDiskProcess rdp = new ReadDiskProcess(this.actualID, pathManualFile, ReadDiskProcess.R_MANUAL_BY_TASK);
        this.RProcesses.add(rdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(rdp);
        }
        else
            rdp.start();
        this.actualID++;
    }

    /*
     * Add a manual read time
     */
    public void addTimeManualReadDiskProcess(File pathManualFile, int seconds){
        ReadDiskProcess rdp = new ReadDiskProcess(this.actualID, pathManualFile, ReadDiskProcess.R_MANUAL_BY_TIME, seconds);
        this.RProcesses.add(rdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(rdp);
        }
        else
            rdp.start();
        this.actualID++;
    }

    /*
     * Add a automatic read time
     */
    public void addTimeAutomaticReadDiskProcess(int fileSize, boolean erase, int seconds){
        ReadDiskProcess rdp = new ReadDiskProcess(this.actualID, ReadDiskProcess.R_AUTOMATIC_BY_TIME, fileSize, erase, seconds);
        this.RProcesses.add(rdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(rdp);
        }
        else
            rdp.start();
        this.actualID++;
    }

    /*
     * Add a automatic read task
     */
    public void addTaskAutomaticReadDiskProcess(int fileSize, boolean eraseFiles){
        ReadDiskProcess rdp = new ReadDiskProcess(this.actualID, ReadDiskProcess.R_AUTOMATIC_BY_TASK, fileSize, eraseFiles);
        this.RProcesses.add(rdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(rdp);
        }
        else
            rdp.start();
        this.actualID++;
    }

    /*
     * Add a automatic write task
     */
    public void addTaskAutomaticWriteDiskProcess(int fileSize, boolean erase){
        WriteDiskProcess wdp = new WriteDiskProcess(this.actualID, WriteDiskProcess.W_AUTOMATIC_BY_TASK, fileSize, erase);
        this.WProcesses.add(wdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(wdp);
        }
        else
            wdp.start();
        this.actualID++;
    }

    /*
     * Add a automatic write time
     */
    public void addTimeAutomaticWriteDiskProcess(int fileSize, boolean erase, int seconds){
        WriteDiskProcess wdp = new WriteDiskProcess(this.actualID, WriteDiskProcess.W_AUTOMATIC_BY_TIME, fileSize, erase, seconds);
        this.WProcesses.add(wdp);
        if(this.usePool)
        {
            if( !this.beginProcessExecutionAtTheSameTime )
                poolThreads.execute(wdp);
        }
        else
            wdp.start();
        this.actualID++;
    }

    public ArrayList<WriteReadDiskProcess> getWRProcesses() {
        return WRProcesses;
    }

    public void setWRProcesses(ArrayList<WriteReadDiskProcess> WRProcesses) {
        this.WRProcesses = WRProcesses;
    }

    public int getActualID() {
        return actualID;
    }

    public void setActualID(int actualID) {
        this.actualID = actualID;
    }

    public ArrayList<ReadDiskProcess> getRProcesses() {
        return RProcesses;
    }

    public void setRProcesses(ArrayList<ReadDiskProcess> RProcesses) {
        this.RProcesses = RProcesses;
    }

    public ArrayList<WriteDiskProcess> getWProcesses() {
        return WProcesses;
    }

    public void setWProcesses(ArrayList<WriteDiskProcess> WProcesses) {
        this.WProcesses = WProcesses;
    }

    public ExecutorService getPoolHilos() {
        return poolThreads;
    }

    public void setPoolHilos(ExecutorService poolHilos) {
        this.poolThreads = poolHilos;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public ArrayList<TaskProcessingProcess> getTastProcessingProcesses() {
        return taskProcessingProcesses;
    }

    public void setTastProcessingProcesses(ArrayList<TaskProcessingProcess> tastProcessingProcesses) {
        this.taskProcessingProcesses = tastProcessingProcesses;
    }

    public ArrayList<TimeProcessingProcess> getTimeProcessingProcesses() {
        return timeProcessingProcesses;
    }

    public void setTimeProcessingProcesses(ArrayList<TimeProcessingProcess> timeProcessingProcesses) {
        this.timeProcessingProcesses = timeProcessingProcesses;
    }

    public long beginProcessExecutionAtTheSameTime(){

        Date inicio = new Date();
        for(WriteReadDiskProcess wrdp:WRProcesses)
            this.poolThreads.execute(wrdp);
        for(WriteDiskProcess wdp:WProcesses)
            this.poolThreads.execute(wdp);
        for(ReadDiskProcess rdp:RProcesses)
            this.poolThreads.execute(rdp);
        for(TaskProcessingProcess tpp:taskProcessingProcesses)
            this.poolThreads.execute(tpp);
        for(TimeProcessingProcess tpp:timeProcessingProcesses)
            this.poolThreads.execute(tpp);

        this.poolThreads.shutdown();

        boolean terminoBien = false;
        try {
            terminoBien = this.poolThreads.awaitTermination(500, TimeUnit.HOURS);
        } catch (InterruptedException ex) {
            System.out.println("Error while the method awaitTermination is invoked: \n"+ex.toString());
        }
        
        Date fin = new Date();
        if(terminoBien)
            return fin.getTime() - inicio.getTime();
        else
            return -1;
    }

    public long beginProcessExecutionAtTheSameTime(int processControlNumber){

        Date inicio = new Date();
        ArrayList <Process> processes = new ArrayList <Process>();
        for(WriteReadDiskProcess wrdp:WRProcesses)
            processes.add(wrdp);
        for(WriteDiskProcess wdp:WProcesses)
            processes.add(wdp);
        for(ReadDiskProcess rdp:RProcesses)
            processes.add(rdp);
        for(TaskProcessingProcess tpp:taskProcessingProcesses)
            processes.add(tpp);
        for(TimeProcessingProcess tpp:timeProcessingProcesses)
            processes.add(tpp);
        int executionNumber = 1;
        int processNumber = 1;
        int maximumNumberProcessAtTheSameTime = processControlNumber;
        boolean terminoBien = false;
        this.establacerPoolThreads();
        for(int i=0; i<processes.size();i++){
            this.poolThreads.execute(processes.get(i));            
            if(processNumber%maximumNumberProcessAtTheSameTime == 0 || processes.size()==(processNumber)){
                try {
                    System.out.println("A wait time was begun in the process number: "+processNumber);
                    this.poolThreads.shutdown();
                    terminoBien = this.poolThreads.awaitTermination(500, TimeUnit.HOURS);
                    System.out.println("The following execution has finalized: "+executionNumber++);
                    this.establacerPoolThreads();
                } catch (InterruptedException ex) {
                    System.out.println("Error while the method awaitTermination is invoked: \n"+ex.toString());
                }
            }
            processNumber++;
        }       

        Date fin = new Date();
        if(terminoBien)
            return fin.getTime() - inicio.getTime();
        else
            return -1;
    }

    public void cleanProcessExecution(){
        this.WRProcesses = new ArrayList <WriteReadDiskProcess> ();
        this.WProcesses = new ArrayList <WriteDiskProcess> ();
        this.RProcesses = new ArrayList <ReadDiskProcess> ();
        this.taskProcessingProcesses = new ArrayList <TaskProcessingProcess> ();
        this.timeProcessingProcesses = new ArrayList <TimeProcessingProcess> ();
        System.out.println("Use pool: "+this.usePool+" Use single thread: "+this.singleThread+" Thread number: "+this.poolSize+" Begin at the same time: "+this.beginProcessExecutionAtTheSameTime);
        if(this.usePool && this.singleThread){
            this.poolThreads = Executors.newFixedThreadPool(1);
            System.out.println("Executing ONE thread option");
        }
        else
            if(this.usePool){
                this.poolThreads = Executors.newFixedThreadPool(this.poolSize);
                System.out.println("Executing MANY thread option");
            }
            else{
                System.out.println("Executing without thread pool");
            }
        this.actualID = 1;
    }

    public boolean isBeginProcessExecutionAtTheSameTime() {
        return beginProcessExecutionAtTheSameTime;
    }

    public void setBeginProcessExecutionAtTheSameTime(boolean beginProcessExecutionAtTheSameTime) {
        this.beginProcessExecutionAtTheSameTime = beginProcessExecutionAtTheSameTime;
    }

    public boolean isSingleThread() {
        return singleThread;
    }

    public void setSingleThread(boolean singleThread) {
        this.singleThread = singleThread;
    }

    public boolean isUsePool() {
        return usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

}
