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
public class TimeProcessingProcess extends ProcessingProcess {

    private long scheduledExecutionTimeSeconds;//segundos
   
    public TimeProcessingProcess(int id, long scheduledExecutionTime){
        super(id);
        this.scheduledExecutionTimeSeconds = scheduledExecutionTime;
    }
    
    @Override
    public void run(){
        Date begin = new Date();
        Date finish  = new Date();
        long stopTime = this.scheduledExecutionTimeSeconds*1000+begin.getTime();
        long tempTime = 0;
        while(tempTime<stopTime){
            finish = new Date();
            tempTime = finish.getTime();
        }
        this.processRealDuration = finish.getTime() - begin.getTime();
        System.out.println("Execution time thread "+this.idProcess+": "+this.processRealDuration+" ms");
    }

    public String getSpecificInformation(){
        return "Time (seconds): "+this.scheduledExecutionTimeSeconds;
    }

    public static void main(String [] args){
        TimeProcessingProcess tpp = new TimeProcessingProcess(1, 5);
        tpp.start();
    }
}
