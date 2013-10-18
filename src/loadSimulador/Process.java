
package loadSimulador;

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

public class Process extends Thread {
    /*
     * idProcess del proceso
     */
    protected int idProcess;
    /*
     * processRealDuration duracion de la ejecucion del proceso
     */
    protected long processRealDuration;

    public Process (int id){
        this.idProcess = id;
    }

    public int getIdProcess() {
        return idProcess;
    }

    public void setIdProcess(int idProcess) {
        this.idProcess = idProcess;
    }

    public long getProcessRealDuration() {
        return processRealDuration;
    }

    public void setProcessRealDuration(long processRealDuration) {
        this.processRealDuration = processRealDuration;
    }

    
}
