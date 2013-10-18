package gui;
import javax.swing.JOptionPane;

/**
 * @author Mario José Villamizar Cano
 * Ingeniero de Sistemas - CISCO CCNA
 * Estudiante de Maestría en Ingeniería - Sistemas y Computación 
 * Universidad de los Andes - Colombia
 * mj.villamizar24@uniandes.edu.co
 */
public class WindowOptionPane
{
    //Type of messages
    //0. X roja JOptionPane.ERROR_MESSAGE
    //1. I violeta JOptionPane.INFORMATION_MESSAGE
    //2. ! Amarillo JOptionPane.WARNING_MESSAGE
    //3. ? Verde JOptionPane.QUESTION_MESSAGE

	public static void printError(String message, String titulo)
	{
        JOptionPane.showMessageDialog(null, message,titulo,JOptionPane.ERROR_MESSAGE);
	}

    public static void printInformation(String message, String titulo)
	{
        JOptionPane.showMessageDialog(null, message,titulo,JOptionPane.INFORMATION_MESSAGE);
	}

    public static void printWarning(String message, String titulo)
	{
        JOptionPane.showMessageDialog(null, message,titulo,JOptionPane.WARNING_MESSAGE);
	}

    public static void printQuestion(String message, String titulo)
	{
        JOptionPane.showMessageDialog(null, message,titulo,JOptionPane.QUESTION_MESSAGE);
	}
	
	public static void print(int msg1, String msg2)
	{
        JOptionPane.showMessageDialog(null, Integer.toString(msg1),msg2,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void print(double msg1, String msg2)
	{
        JOptionPane.showMessageDialog(null, Double.toString(msg1),msg2,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void print(byte msg1, String msg2)
	{
        JOptionPane.showMessageDialog(null, Byte.toString(msg1),msg2,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void print(short msg1, String msg2)
	{
        JOptionPane.showMessageDialog(null, Short.toString(msg1),msg2,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void print(float msg1, String msg2)
	{
        JOptionPane.showMessageDialog(null, Float.toString(msg1),msg2,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void print(long msg1, String msg2)
	{
        JOptionPane.showMessageDialog(null, Long.toString(msg1),msg2,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static boolean readOption(String m)
   	{
        int opc=JOptionPane.showOptionDialog(null,m,"OPCION",0,JOptionPane.QUESTION_MESSAGE,null,null,null);
        if(opc==0)
            return true;
        return false;
	}
        
    public static String readCadena(String mensaje, String titulo)
	{
		return(JOptionPane.showInputDialog(null,mensaje,titulo,JOptionPane.QUESTION_MESSAGE));
	}
}