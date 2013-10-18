/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package loadSimulador;

import java.util.Date;



/**
 *
 * @author mj.villamizar24
 */
public class TaskProcessingProcess extends ProcessingProcess {

    private long iterationNumber;
    private long timesByIteration;
   
    public TaskProcessingProcess(int id, long iterationNumber){
        super(id);
        this.iterationNumber = iterationNumber;
        this.timesByIteration = 100000;
    }
    
    @Override
    public void run(){
        Date inicio = new Date();
        for(long i=0;i<this.iterationNumber;i++){
            for(long j=0;j<this.timesByIteration;j++){
            }
        }
        Date fin = new Date();
        this.processRealDuration = fin.getTime() - inicio.getTime();
        System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
    }

    public String getSpecificInformation(){
        return "Task: "+this.iterationNumber;
    }

    public static void main(String [] args){
        TaskProcessingProcess tpp = new TaskProcessingProcess(6, 1000);
        tpp.start();
    }
}
