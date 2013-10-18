
package grid;

import loadSimulador.TimeProcessingProcess;

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

public class GridExecution {

    public static void main(String [] args){
        if(args.length!=1){
            System.out.println("Error in the parameters, the applicaction require only one parameter - The ScheduledExecutionTime in seconds");
            return;
        }
        TimeProcessingProcess tpp = new TimeProcessingProcess(0, Long.parseLong(args[0]));
        tpp.start();
        System.out.println("ID Process: "+tpp.getIdProcess());        
    }

}
