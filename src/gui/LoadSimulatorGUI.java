
package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import loadSimulador.LoadSimulatorModel;
import loadSimulador.ProcessingProcess;
import loadSimulador.ReadDiskProcess;
import loadSimulador.TaskProcessingProcess;
import loadSimulador.TimeProcessingProcess;
import loadSimulador.WriteDiskProcess;
import loadSimulador.WriteReadDiskProcess;

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

public class LoadSimulatorGUI extends javax.swing.JDialog {
    private LoadSimulatorModel loadSimulatorModel;
    private ArrayList <TaskProcessingProcess> taskProcessingProcesses;
    private ArrayList <TimeProcessingProcess> timeProcessingProcesses;
    private ArrayList <WriteReadDiskProcess> WRProcesses;
    private ArrayList <ReadDiskProcess> RProcesses;
    private ArrayList <WriteDiskProcess> WProcesses;

    /** Creates new form LoadSimulatorGUI */
    public LoadSimulatorGUI(java.awt.Frame parent, boolean modal, boolean usePool, boolean singleThread, int threadNumber, boolean beginProcessExecutionAtTheSameTime) {
        super(parent, modal);
        //public LoadSimulatorModel(boolean usePool, boolean singleThread, int poolSize);
        initComponents();
        this.centrarVentana();
        
        if(!usePool)
            loadSimulatorModel = new LoadSimulatorModel(false, false, 0, false);
        else
            if(usePool && singleThread)
                loadSimulatorModel = new LoadSimulatorModel(true, true, 0, beginProcessExecutionAtTheSameTime);
            else
                loadSimulatorModel = new LoadSimulatorModel(true, false, threadNumber, beginProcessExecutionAtTheSameTime);
        this.initVisualComponents();        
    }

    public void initVisualComponents(){
        /*
         * Read
         */
        selectOptionForRTaskProcess();
        selectOptionForRTimeProcess();
        /*
         * Write
         */
        selectOptionForWProcess();
        /*
         * Read/Write
         */
        selectOptionForRWProcess();
        selectOptionForRWTimeProcess();
        /*
         * Processing
         */
        loadProcessingProcesses();
        /*
         * Read
         */
        loadRDiskProcesses();
        /*
         * Write
         */
        loadWDiskProcesses();
        /*
         * Read/Write
         */
        loadWRDiskProcesses();

        int groupOfNProcesses = 0;
        if(this.loadSimulatorModel.getPoolSize()>0)
            groupOfNProcesses = this.loadSimulatorModel.getPoolSize();
        else
            groupOfNProcesses = 1;

        this.spnNumberEveryNProcesses.setValue(groupOfNProcesses);

        if(!this.loadSimulatorModel.isBeginProcessExecutionAtTheSameTime())
        {            
            this.jbExecutionAtTheSameTimeBegin.setEnabled(false);
            this.jbExecutionAtTheSameTimeClean.setEnabled(false);
        }
        if(!this.loadSimulatorModel.isBeginProcessExecutionAtTheSameTime() || this.loadSimulatorModel.isSingleThread())
        {
            this.cbEveryNProcesses.setEnabled(false);
            this.spnNumberEveryNProcesses.setEnabled(false);
        }
    }

    public void loadProcessingProcesses(){

        try {
            modelProcessingProcesses = new DefaultTableModel();
            modelProcessingProcesses.addColumn("Process ID");
            modelProcessingProcesses.addColumn("Process type");
            modelProcessingProcesses.addColumn("Status");
            modelProcessingProcesses.addColumn("Time Execution (ms)");
            modelProcessingProcesses.addColumn("Aditional Information");
            
            // task processing process
            taskProcessingProcesses = this.loadSimulatorModel.getTastProcessingProcesses();
            long id;
            String processType;
            String status;
            long processRealDuration;
            for (TaskProcessingProcess tpp : taskProcessingProcesses){
                id = tpp.getIdProcess();
                processType = ProcessingProcess.TASK_PROCESSING_PROCESS;
                if(tpp.isAlive())
                {
                    status = "Running";
                    processRealDuration = tpp.getProcessRealDuration();
                }
                else
                {
                    status = "Finished";
                    processRealDuration = tpp.getProcessRealDuration();
                }
                modelProcessingProcesses.addRow(new Object[]{id, processType, status, processRealDuration, tpp.getSpecificInformation()});
            }

            //Time processing process
            timeProcessingProcesses = this.loadSimulatorModel.getTimeProcessingProcesses();
            for (TimeProcessingProcess tpp : timeProcessingProcesses){
                id = tpp.getIdProcess();
                processType = ProcessingProcess.TIME_PROCESSING_PROCESS;
                if(tpp.isAlive())
                {
                    status = "Running";
                    processRealDuration = tpp.getProcessRealDuration();
                }
                else
                {
                    status = "Finished";
                    processRealDuration = tpp.getProcessRealDuration();
                }
                modelProcessingProcesses.addRow(new Object[]{id, processType, status, processRealDuration, tpp.getSpecificInformation()});
            }

            this.tableTaskProcessingProcess.setModel(modelProcessingProcesses);
            } catch (Exception e) {
                e.printStackTrace();
                WindowOptionPane.printError("Error loading the task processing processes\n"+e.getMessage(), "Error");
            }
    }

    public void loadWRDiskProcesses(){

        try {
            modelRWDiskProcesesess = new DefaultTableModel();
            WRProcesses = this.loadSimulatorModel.getWRProcesses();
            modelRWDiskProcesesess.addColumn("Process ID");
            modelRWDiskProcesesess.addColumn("Read write type");
            modelRWDiskProcesesess.addColumn("Status");
            modelRWDiskProcesesess.addColumn("Time Execution (ms)");
            modelRWDiskProcesesess.addColumn("Aditional Information");
            long id;
            String readWriteType;
            String status;
            long processRealDuration;
            for (WriteReadDiskProcess wrdp : WRProcesses){
                id = wrdp.getIdProcess();
                readWriteType = wrdp.getReadWriteType();
                if(wrdp.isAlive())
                {
                    status = "Running";
                    processRealDuration = wrdp.getProcessRealDuration();
                }
                else
                {
                    status = "Finished";
                    processRealDuration = wrdp.getProcessRealDuration();
                }

                modelRWDiskProcesesess.addRow(new Object[]{id, readWriteType, status, processRealDuration, wrdp.getSpecificInformation()});
            }
            tableRWProcesses.setModel(modelRWDiskProcesesess);
            } catch (Exception e) {
                e.printStackTrace();
                WindowOptionPane.printError("Error loading the RW processes\n"+e.getMessage(), "Error");
        }
    }

    public void loadRDiskProcesses(){

        try {
            modelRDiskProcesesess = new DefaultTableModel();
            RProcesses = this.loadSimulatorModel.getRProcesses();
            modelRDiskProcesesess.addColumn("Process ID");
            modelRDiskProcesesess.addColumn("Read type");
            modelRDiskProcesesess.addColumn("Status");
            modelRDiskProcesesess.addColumn("Time Execution (ms)");
            modelRDiskProcesesess.addColumn("Aditional Information");
            long id;
            String readType;
            String status;
            long processRealDuration;
            for (ReadDiskProcess rdp : RProcesses){
                id = rdp.getIdProcess();
                readType = rdp.getReadType();
                if(rdp.isAlive())
                {
                    status = "Running";
                    processRealDuration = rdp.getProcessRealDuration();
                }
                else
                {
                    status = "Finished";
                    processRealDuration = rdp.getProcessRealDuration();
                }

                modelRDiskProcesesess.addRow(new Object[]{id, readType, status, processRealDuration, rdp.getSpecificInformation()});
            }
            tableRProcesses.setModel(modelRDiskProcesesess);
            } catch (Exception e) {
                e.printStackTrace();
                WindowOptionPane.printError("Error loading the R processes\n"+e.getMessage(), "Error");
        }
    }

    public void loadWDiskProcesses(){

        try {
            modelWDiskProcesesess = new DefaultTableModel();
            WProcesses = this.loadSimulatorModel.getWProcesses();
            modelWDiskProcesesess.addColumn("Process ID");
            modelWDiskProcesesess.addColumn("Write type");
            modelWDiskProcesesess.addColumn("Status");
            modelWDiskProcesesess.addColumn("Time Execution (ms)");
            modelWDiskProcesesess.addColumn("Aditional Information");
            long id;
            String writeType;
            String status;
            long processRealDuration;
            for (WriteDiskProcess wdp : WProcesses){
                id = wdp.getIdProcess();
                writeType = wdp.getWriteType();
                if(wdp.isAlive())
                {
                    status = "Running";
                    processRealDuration = wdp.getProcessRealDuration();
                }
                else
                {
                    status = "Finished";
                    processRealDuration = wdp.getProcessRealDuration();
                }

                modelWDiskProcesesess.addRow(new Object[]{id, writeType, status, processRealDuration, wdp.getSpecificInformation()});
            }
            tableWProcesses.setModel(modelWDiskProcesesess);
            } catch (Exception e) {
                e.printStackTrace();
                WindowOptionPane.printError("Error loading the W processes\n"+e.getMessage(), "Error");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bgRWOption = new javax.swing.ButtonGroup();
        rbRTimeOption = new javax.swing.ButtonGroup();
        rbRTaskOption = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtfIterationNumber = new javax.swing.JTextField();
        jbCreateTasjProcess = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtfProcessingTimeSeconds = new javax.swing.JTextField();
        jbCreateTimeProcess = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableTaskProcessingProcess = new javax.swing.JTable();
        jbListProcessingProcess = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jbReadFileTask = new javax.swing.JButton();
        jtfRTaskProcess = new javax.swing.JTextField();
        jbSelectReadTaskFile = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        ckEraseFilesReadTaskGenerated = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jtfRTaskFileSize = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        rbRManualByTask = new javax.swing.JRadioButton();
        rbRAutomaticByTask = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        rbRManualByTime = new javax.swing.JRadioButton();
        rbRAutomaticByTime = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        jtfRTimeProcess = new javax.swing.JTextField();
        jbSelectReadTimeFile = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jtfRTime = new javax.swing.JTextField();
        ckEraseFilesReadTimeGenerated = new javax.swing.JCheckBox();
        jbReadFileTime = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jtfRTimeFileSize = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableRProcesses = new javax.swing.JTable();
        jbRListProcess = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        rbTaskAutomaticOptionWrite = new javax.swing.JRadioButton();
        rbTimeAutomaticOptionWrite = new javax.swing.JRadioButton();
        jLabel30 = new javax.swing.JLabel();
        jtfWTime = new javax.swing.JTextField();
        ckEraseFilesGeneratedW = new javax.swing.JCheckBox();
        jbReadFileTime2 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jtfWTimeFileSize = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableWProcesses = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableRWProcesses = new javax.swing.JTable();
        jbRWListProcess = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jbCompressFile = new javax.swing.JButton();
        jtfRWTaskProcess = new javax.swing.JTextField();
        btSelectTaskReadWriteFile = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ckEraseFilesGeneratedRWTask = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        rbTaskManualOptionReadWrite = new javax.swing.JRadioButton();
        rbTaskAutomaticOptionReadWrite = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jtfRWFileSize = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        rbTimeManualOptionReadWrite = new javax.swing.JRadioButton();
        rbTimeAutomaticOptionReadWrite = new javax.swing.JRadioButton();
        jLabel23 = new javax.swing.JLabel();
        jtfRWTimeProcess = new javax.swing.JTextField();
        btSelectTimeReadWriteFile = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jtfRWTime = new javax.swing.JTextField();
        ckEraseFilesGeneratedRWTime = new javax.swing.JCheckBox();
        jbReadFileTime1 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jtfRWTimeFileSize = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jtfTotalTime = new javax.swing.JTextField();
        jbExecutionAtTheSameTimeClean = new javax.swing.JButton();
        jbExecutionAtTheSameTimeBegin = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        spnNumberEveryNProcesses = new javax.swing.JSpinner();
        cbEveryNProcesses = new javax.swing.JCheckBox();

        jToolBar1.setRollover(true);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Simulator GUI");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText("LOAD SIMULATOR");

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("PROCESS BY TASK");

        jLabel7.setText("Iteration number:");

        jtfIterationNumber.setText("5000");

        jbCreateTasjProcess.setText("Create Process");
        jbCreateTasjProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCreateTasjProcessActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(jLabel6))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(jLabel7)
                        .addGap(26, 26, 26)
                        .addComponent(jtfIterationNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(jbCreateTasjProcess)))
                .addContainerGap(265, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtfIterationNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jbCreateTasjProcess)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setText("PROCESS BY TIME");

        jLabel9.setText("Time in seconds:");

        jtfProcessingTimeSeconds.setText("10");

        jbCreateTimeProcess.setText("Create Process");
        jbCreateTimeProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCreateTimeProcessActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(jLabel8))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(jLabel9)
                        .addGap(26, 26, 26)
                        .addComponent(jtfProcessingTimeSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(jbCreateTimeProcess)))
                .addContainerGap(271, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtfProcessingTimeSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jbCreateTimeProcess)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel10.setText("Processing Process Status");

        tableTaskProcessingProcess.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tableTaskProcessingProcess);

        jbListProcessingProcess.setText("Update List");
        jbListProcessingProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbListProcessingProcessActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jLabel10))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(jbListProcessingProcess)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jbListProcessingProcess)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Processing Process", jPanel4);

        jPanel7.setBackground(new java.awt.Color(174, 199, 238));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbReadFileTask.setText("Read File");
        jbReadFileTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReadFileTaskActionPerformed(evt);
            }
        });

        jtfRTaskProcess.setEditable(false);

        jbSelectReadTaskFile.setText("Browse...");
        jbSelectReadTaskFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelectReadTaskFileActionPerformed(evt);
            }
        });

        jLabel11.setText("Path File: ");

        ckEraseFilesReadTaskGenerated.setBackground(new java.awt.Color(255, 255, 255));
        ckEraseFilesReadTaskGenerated.setSelected(true);
        ckEraseFilesReadTaskGenerated.setText("erase files generated");
        ckEraseFilesReadTaskGenerated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEraseFilesReadTaskGeneratedActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setText("READ BY TASK");

        jLabel13.setText("File size (KBytes):");

        jtfRTaskFileSize.setText("10240");

        jLabel15.setText("Process type:");

        rbRManualByTask.setBackground(new java.awt.Color(255, 255, 255));
        rbRTaskOption.add(rbRManualByTask);
        rbRManualByTask.setText("Manual");
        rbRManualByTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRManualByTaskActionPerformed(evt);
            }
        });

        rbRAutomaticByTask.setBackground(new java.awt.Color(255, 255, 255));
        rbRTaskOption.add(rbRAutomaticByTask);
        rbRAutomaticByTask.setSelected(true);
        rbRAutomaticByTask.setText("Automatic");
        rbRAutomaticByTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRAutomaticByTaskActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(233, 233, 233)
                        .addComponent(jLabel12))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(107, 107, 107)
                                        .addComponent(rbRManualByTask))
                                    .addComponent(jLabel15))
                                .addGap(18, 18, 18)
                                .addComponent(rbRAutomaticByTask))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfRTaskProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jbSelectReadTaskFile))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfRTaskFileSize, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(210, 210, 210)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(ckEraseFilesReadTaskGenerated)
                                    .addComponent(jbReadFileTask))))))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(rbRManualByTask)
                    .addComponent(rbRAutomaticByTask))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtfRTaskProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSelectReadTaskFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jtfRTaskFileSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckEraseFilesReadTaskGenerated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbReadFileTask)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel14.setText("Read Process Status");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel16.setText("READ BY TIME");

        jLabel17.setText("Process type:");

        rbRManualByTime.setBackground(new java.awt.Color(255, 255, 255));
        rbRTimeOption.add(rbRManualByTime);
        rbRManualByTime.setText("Manual");
        rbRManualByTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRManualByTimeActionPerformed(evt);
            }
        });

        rbRAutomaticByTime.setBackground(new java.awt.Color(255, 255, 255));
        rbRTimeOption.add(rbRAutomaticByTime);
        rbRAutomaticByTime.setText("Automatic");
        rbRAutomaticByTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRAutomaticByTimeActionPerformed(evt);
            }
        });

        jLabel18.setText("Path File:");

        jtfRTimeProcess.setEditable(false);

        jbSelectReadTimeFile.setText("Browse...");
        jbSelectReadTimeFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelectReadTimeFileActionPerformed(evt);
            }
        });

        jLabel19.setText("Time (Seconds):");

        jtfRTime.setText("10");

        ckEraseFilesReadTimeGenerated.setBackground(new java.awt.Color(255, 255, 255));
        ckEraseFilesReadTimeGenerated.setSelected(true);
        ckEraseFilesReadTimeGenerated.setText("erase files generated");

        jbReadFileTime.setText("Read File");
        jbReadFileTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReadFileTimeActionPerformed(evt);
            }
        });

        jLabel20.setText("File size (KBytes):");

        jtfRTimeFileSize.setText("10240");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addComponent(jLabel16))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(rbRManualByTime)
                                .addGap(35, 35, 35)
                                .addComponent(rbRAutomaticByTime))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jtfRTimeProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addGap(12, 12, 12)
                                        .addComponent(jtfRTime, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                .addComponent(ckEraseFilesReadTimeGenerated)
                                                .addComponent(jbReadFileTime))
                                            .addGroup(jPanel9Layout.createSequentialGroup()
                                                .addComponent(jLabel20)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jtfRTimeFileSize, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))))
                                .addGap(18, 18, 18)
                                .addComponent(jbSelectReadTimeFile)))))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(rbRManualByTime)
                    .addComponent(rbRAutomaticByTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jtfRTimeProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSelectReadTimeFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jtfRTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jtfRTimeFileSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckEraseFilesReadTimeGenerated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbReadFileTime)
                .addGap(42, 42, 42))
        );

        tableRProcesses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tableRProcesses);

        jbRListProcess.setText("Update List");
        jbRListProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRListProcessActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(jbRListProcess))
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel8, jPanel9, jScrollPane4});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbRListProcess)
                .addGap(68, 68, 68))
        );

        jTabbedPane1.addTab("Read Process", jPanel7);

        jPanel11.setBackground(new java.awt.Color(236, 234, 234));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel27.setText("AUTOMATIC WRITE");

        jLabel28.setText("Process type:");

        rbTaskAutomaticOptionWrite.setBackground(new java.awt.Color(255, 255, 255));
        rbRTimeOption.add(rbTaskAutomaticOptionWrite);
        rbTaskAutomaticOptionWrite.setSelected(true);
        rbTaskAutomaticOptionWrite.setText("By Task");
        rbTaskAutomaticOptionWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTaskAutomaticOptionWriteActionPerformed(evt);
            }
        });

        rbTimeAutomaticOptionWrite.setBackground(new java.awt.Color(255, 255, 255));
        rbRTimeOption.add(rbTimeAutomaticOptionWrite);
        rbTimeAutomaticOptionWrite.setText("By time");
        rbTimeAutomaticOptionWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTimeAutomaticOptionWriteActionPerformed(evt);
            }
        });

        jLabel30.setText("Time (Seconds):");

        jtfWTime.setText("10");

        ckEraseFilesGeneratedW.setBackground(new java.awt.Color(255, 255, 255));
        ckEraseFilesGeneratedW.setSelected(true);
        ckEraseFilesGeneratedW.setText("erase files generated");

        jbReadFileTime2.setText("Write File");
        jbReadFileTime2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReadFileTime2ActionPerformed(evt);
            }
        });

        jLabel31.setText("File size (KBytes):");

        jtfWTimeFileSize.setText("10240");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addComponent(rbTaskAutomaticOptionWrite)
                .addGap(35, 35, 35)
                .addComponent(rbTimeAutomaticOptionWrite)
                .addContainerGap(334, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jLabel27)
                .addContainerGap(249, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addGap(12, 12, 12)
                .addComponent(jtfWTime, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(ckEraseFilesGeneratedW)
                        .addContainerGap())
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfWTimeFileSize, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addGap(182, 182, 182))))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(241, 241, 241)
                .addComponent(jbReadFileTime2)
                .addContainerGap(269, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(rbTaskAutomaticOptionWrite)
                    .addComponent(rbTimeAutomaticOptionWrite))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jtfWTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(jtfWTimeFileSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(ckEraseFilesGeneratedW)
                .addGap(18, 18, 18)
                .addComponent(jbReadFileTime2)
                .addGap(47, 47, 47))
        );

        tableWProcesses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tableWProcesses);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel29.setText("Write Process Status");

        jButton1.setText("Update List");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addComponent(jLabel29))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(jButton1)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel11Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel12, jScrollPane5});

        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(61, 61, 61))
        );

        jTabbedPane1.addTab("Write Process", jPanel11);

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Read Write Process Status");

        tableRWProcesses.setModel(modelRWDiskProcesesess);
        jScrollPane2.setViewportView(tableRWProcesses);

        jbRWListProcess.setText("Update List");
        jbRWListProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRWListProcessActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbCompressFile.setText("Compress File");
        jbCompressFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCompressFileActionPerformed(evt);
            }
        });

        jtfRWTaskProcess.setEditable(false);

        btSelectTaskReadWriteFile.setText("Browse...");
        btSelectTaskReadWriteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectTaskReadWriteFileActionPerformed(evt);
            }
        });

        jLabel2.setText("Path File: ");

        ckEraseFilesGeneratedRWTask.setBackground(new java.awt.Color(255, 255, 255));
        ckEraseFilesGeneratedRWTask.setSelected(true);
        ckEraseFilesGeneratedRWTask.setText("erase files generated");
        ckEraseFilesGeneratedRWTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEraseFilesGeneratedRWTaskActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("READ/WRITE BY TASK");

        rbTaskManualOptionReadWrite.setBackground(new java.awt.Color(255, 255, 255));
        bgRWOption.add(rbTaskManualOptionReadWrite);
        rbTaskManualOptionReadWrite.setText("Manual");
        rbTaskManualOptionReadWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTaskManualOptionReadWriteActionPerformed(evt);
            }
        });

        rbTaskAutomaticOptionReadWrite.setBackground(new java.awt.Color(255, 255, 255));
        bgRWOption.add(rbTaskAutomaticOptionReadWrite);
        rbTaskAutomaticOptionReadWrite.setSelected(true);
        rbTaskAutomaticOptionReadWrite.setText("Automatic");
        rbTaskAutomaticOptionReadWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTaskAutomaticOptionReadWriteActionPerformed(evt);
            }
        });

        jLabel5.setText("Size automatic file (KBytes):");

        jtfRWFileSize.setText("10240");

        jLabel26.setText("Process type:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfRWFileSize, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(202, 202, 202)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jbCompressFile)
                            .addComponent(ckEraseFilesGeneratedRWTask)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel26))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfRWTaskProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btSelectTaskReadWriteFile))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(rbTaskManualOptionReadWrite)
                                .addGap(81, 81, 81)
                                .addComponent(rbTaskAutomaticOptionReadWrite)))))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(rbTaskManualOptionReadWrite)
                    .addComponent(rbTaskAutomaticOptionReadWrite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtfRWTaskProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSelectTaskReadWriteFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtfRWFileSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckEraseFilesGeneratedRWTask)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCompressFile)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel21.setText("READ/WRITE BY TIME");

        jLabel22.setText("Process type:");

        rbTimeManualOptionReadWrite.setBackground(new java.awt.Color(255, 255, 255));
        rbRTimeOption.add(rbTimeManualOptionReadWrite);
        rbTimeManualOptionReadWrite.setText("Manual");
        rbTimeManualOptionReadWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTimeManualOptionReadWriteActionPerformed(evt);
            }
        });

        rbTimeAutomaticOptionReadWrite.setBackground(new java.awt.Color(255, 255, 255));
        rbRTimeOption.add(rbTimeAutomaticOptionReadWrite);
        rbTimeAutomaticOptionReadWrite.setText("Automatic");
        rbTimeAutomaticOptionReadWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTimeAutomaticOptionReadWriteActionPerformed(evt);
            }
        });

        jLabel23.setText("Path File:");

        jtfRWTimeProcess.setEditable(false);

        btSelectTimeReadWriteFile.setText("Browse...");
        btSelectTimeReadWriteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectTimeReadWriteFileActionPerformed(evt);
            }
        });

        jLabel24.setText("Time (Seconds):");

        jtfRWTime.setText("10");

        ckEraseFilesGeneratedRWTime.setBackground(new java.awt.Color(255, 255, 255));
        ckEraseFilesGeneratedRWTime.setSelected(true);
        ckEraseFilesGeneratedRWTime.setText("erase files generated");

        jbReadFileTime1.setText("Compress File");
        jbReadFileTime1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReadFileTime1ActionPerformed(evt);
            }
        });

        jLabel25.setText("File size (KBytes):");

        jtfRWTimeFileSize.setText("10240");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(18, 18, 18)
                        .addComponent(rbTimeManualOptionReadWrite)
                        .addGap(35, 35, 35)
                        .addComponent(rbTimeAutomaticOptionReadWrite))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfRWTimeProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(12, 12, 12)
                                .addComponent(jtfRWTime, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ckEraseFilesGeneratedRWTime)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtfRWTimeFileSize, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))))
                        .addGap(18, 18, 18)
                        .addComponent(btSelectTimeReadWriteFile)))
                .addContainerGap(85, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(227, 227, 227)
                .addComponent(jbReadFileTime1)
                .addContainerGap(261, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jLabel21)
                .addContainerGap(236, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(rbTimeManualOptionReadWrite)
                    .addComponent(rbTimeAutomaticOptionReadWrite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jtfRWTimeProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSelectTimeReadWriteFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jtfRWTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jtfRWTimeFileSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ckEraseFilesGeneratedRWTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbReadFileTime1)
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jbRWListProcess)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel10, jPanel3, jScrollPane2});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbRWListProcess)
                .addGap(74, 74, 74))
        );

        jTabbedPane1.addTab("Write/Read Process", jPanel2);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Begin execution at the same time"));

        jLabel32.setText("Total execution time (ms):");

        jtfTotalTime.setEditable(false);
        jtfTotalTime.setText("0");

        jbExecutionAtTheSameTimeClean.setText("Clean Processes Created");
        jbExecutionAtTheSameTimeClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExecutionAtTheSameTimeCleanActionPerformed(evt);
            }
        });

        jbExecutionAtTheSameTimeBegin.setText("Begin Process Execution");
        jbExecutionAtTheSameTimeBegin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExecutionAtTheSameTimeBeginActionPerformed(evt);
            }
        });

        cbEveryNProcesses.setBackground(new java.awt.Color(255, 255, 255));
        cbEveryNProcesses.setSelected(true);
        cbEveryNProcesses.setText("Execute the processess by batch of n processes begining at the same time:");
        cbEveryNProcesses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEveryNProcessesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfTotalTime, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbExecutionAtTheSameTimeBegin)
                        .addGap(16, 16, 16)
                        .addComponent(jbExecutionAtTheSameTimeClean))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(cbEveryNProcesses)
                        .addGap(18, 18, 18)
                        .addComponent(spnNumberEveryNProcesses, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbEveryNProcesses)
                    .addComponent(spnNumberEveryNProcesses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel33)
                        .addGap(40, 40, 40))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(jtfTotalTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbExecutionAtTheSameTimeBegin)
                            .addComponent(jbExecutionAtTheSameTimeClean))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSelectTaskReadWriteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectTaskReadWriteFileActionPerformed
        jtfRWTaskProcess.setText(selectReadFile("./documents/manualReadWrite"));
}//GEN-LAST:event_btSelectTaskReadWriteFileActionPerformed

    private void jbCompressFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCompressFileActionPerformed
        createTaskRWDiscProcess();
}//GEN-LAST:event_jbCompressFileActionPerformed

    private void ckEraseFilesGeneratedRWTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEraseFilesGeneratedRWTaskActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ckEraseFilesGeneratedRWTaskActionPerformed

    private void jbRWListProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRWListProcessActionPerformed
        loadWRDiskProcesses();
}//GEN-LAST:event_jbRWListProcessActionPerformed

    private void rbTaskManualOptionReadWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTaskManualOptionReadWriteActionPerformed
        selectOptionForRWProcess();
}//GEN-LAST:event_rbTaskManualOptionReadWriteActionPerformed

    private void rbTaskAutomaticOptionReadWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTaskAutomaticOptionReadWriteActionPerformed
        selectOptionForRWProcess();
}//GEN-LAST:event_rbTaskAutomaticOptionReadWriteActionPerformed

    private void jbListProcessingProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbListProcessingProcessActionPerformed
        this.loadProcessingProcesses();
}//GEN-LAST:event_jbListProcessingProcessActionPerformed

    private void jbCreateTasjProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCreateTasjProcessActionPerformed
        createTaskProcessingProcess();
    }//GEN-LAST:event_jbCreateTasjProcessActionPerformed

    private void jbCreateTimeProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCreateTimeProcessActionPerformed
        createTimeProcessingProcess();
    }//GEN-LAST:event_jbCreateTimeProcessActionPerformed

    private void rbRAutomaticByTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRAutomaticByTaskActionPerformed
        selectOptionForRTaskProcess();
}//GEN-LAST:event_rbRAutomaticByTaskActionPerformed

    private void rbRManualByTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRManualByTaskActionPerformed
        selectOptionForRTaskProcess();
}//GEN-LAST:event_rbRManualByTaskActionPerformed

    private void ckEraseFilesReadTaskGeneratedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEraseFilesReadTaskGeneratedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ckEraseFilesReadTaskGeneratedActionPerformed

    private void jbSelectReadTaskFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelectReadTaskFileActionPerformed
        jtfRTaskProcess.setText(selectReadFile("./documents/manualRead"));
}//GEN-LAST:event_jbSelectReadTaskFileActionPerformed

    private void jbReadFileTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReadFileTaskActionPerformed
        createTaskRDiscProcess();
}//GEN-LAST:event_jbReadFileTaskActionPerformed

    public void createTaskRDiscProcess(){
        if(this.rbRManualByTask.isSelected()){
            if(this.jtfRTaskProcess.getText().equals(""))
            {
                WindowOptionPane.printError("A file most be selected for realizing the operation", "Error");
                return;
            }
            this.loadSimulatorModel.addTaskManualReadDiskProcess(new File(this.jtfRTaskProcess.getText()));
            this.loadRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");
        }else{
            if(this.rbRAutomaticByTask.getText().equals(""))
            {
                WindowOptionPane.printError("A file most be selected for realizing the operation", "Error");
                 this.rbRAutomaticByTask.requestFocus();
                return;
            }
            int fileSize = 0;
            try{
                fileSize = Integer.parseInt(this.jtfRTaskFileSize.getText().trim());
                if(fileSize<=0){
                    WindowOptionPane.printError("The file size most be greater than zero", "Error");
                    this.jtfRTaskFileSize.setText("10000");
                    this.jtfRTaskFileSize.requestFocus();
                    return;
                }
            }catch(Exception e){
                WindowOptionPane.printError("An error in the file size entered\n"+e.toString(), "Error");
                this.jtfRTaskFileSize.setText("10000");
                this.jtfRTaskFileSize.requestFocus();
                return;
            }
            this.loadSimulatorModel.addTaskAutomaticReadDiskProcess(fileSize, this.ckEraseFilesReadTaskGenerated.isSelected());
            this.loadRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");            
        }
    }

    public void createTimeRWDiscProcess(){
        //Time validation
        int seconds = 0;
        try{
            if(this.jtfRWTime.getText().trim().equals(""))
            {
                WindowOptionPane.printError("A time most be entered", "Error");
                this.jtfRWTime.setText("10");
                this.jtfRWTime.requestFocus();
                return;
            }
            seconds = Integer.parseInt(this.jtfRWTime.getText().trim());
            if(seconds<=0){
                WindowOptionPane.printError("The time most be greater than zero", "Error");
                this.jtfRWTime.setText("10");
                this.jtfRWTime.requestFocus();
                return;
            }
        }catch(Exception e){
            WindowOptionPane.printError("An error in the time entered\n"+e.toString(), "Error");
            this.jtfRWTime.setText("10");
            this.jtfRWTime.requestFocus();
            return;
        }

        //Manual option
        if(this.rbTimeManualOptionReadWrite.isSelected()){
            if(this.jtfRWTimeProcess.getText().trim().equals(""))
            {
                WindowOptionPane.printError("A file most be selected for realizing the operation", "Error");
                return;
            }

            this.loadSimulatorModel.addTimeManualWriteReadDiskProcess(new File(this.jtfRWTimeProcess.getText()), seconds, this.ckEraseFilesGeneratedRWTime.isSelected());
            this.loadWRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");
        }else{
            int fileSize = 0;
            try{
                if(this.jtfRWTimeFileSize.getText().trim().equals(""))
                {
                    WindowOptionPane.printError("A file size most be entered", "Error");
                    this.jtfRWTimeFileSize.setText("10000");
                    this.jtfRWTimeFileSize.requestFocus();
                    return;
                }
                fileSize = Integer.parseInt(this.jtfRWTimeFileSize.getText().trim());
                if(fileSize<=0){
                    WindowOptionPane.printError("The file size most be greater than zero", "Error");
                    this.jtfRWTimeFileSize.setText("10000");
                    this.jtfRWTimeFileSize.requestFocus();
                    return;
                }
            }catch(Exception e){
                WindowOptionPane.printError("An error in the file size entered\n"+e.toString(), "Error");
                this.jtfRWTimeFileSize.setText("10000");
                this.jtfRWTimeFileSize.requestFocus();
                return;
            }
            this.loadSimulatorModel.addTimeAutomaticWriteReadDiskProcess(fileSize, seconds, this.ckEraseFilesGeneratedRWTime.isSelected());
            this.loadWRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");
        }
    }

    public void createTimeRDiscProcess(){
        //Time validation
        int seconds = 0;
        try{
            if(this.jtfRTime.getText().trim().equals(""))
            {
                WindowOptionPane.printError("A time most be entered", "Error");
                this.jtfRTime.setText("10");
                this.jtfRTime.requestFocus();
                return;
            }
            seconds = Integer.parseInt(this.jtfRTime.getText().trim());
            if(seconds<=0){
                WindowOptionPane.printError("The time most be greater than zero", "Error");
                this.jtfRTime.setText("10");
                this.jtfRTime.requestFocus();
                return;
            }
        }catch(Exception e){
            WindowOptionPane.printError("An error in the time entered\n"+e.toString(), "Error");
            this.jtfRTime.setText("10");
            this.jtfRTime.requestFocus();
            return;
        }

        //Manual option
        if(this.rbRManualByTime.isSelected()){
            if(this.jtfRTimeProcess.getText().trim().equals(""))
            {
                WindowOptionPane.printError("A file most be selected for realizing the operation", "Error");
                return;
            }
            this.loadSimulatorModel.addTimeManualReadDiskProcess(new File(this.jtfRTimeProcess.getText()), seconds);
            this.loadRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");
        }else{
            if(this.rbRAutomaticByTime.getText().equals(""))
            {
                WindowOptionPane.printError("A file most be selected for realizing the operation", "Error");
                this.rbRAutomaticByTime.requestFocus();
                return;
            }
            int fileSize = 0;
            try{
                if(this.jtfRTimeFileSize.getText().trim().equals(""))
                {
                    WindowOptionPane.printError("A file size most be entered", "Error");
                    this.jtfRTimeFileSize.setText("10000");
                    this.jtfRTimeFileSize.requestFocus();
                    return;
                }
                fileSize = Integer.parseInt(this.jtfRTimeFileSize.getText().trim());
                if(fileSize<=0){
                    WindowOptionPane.printError("The file size most be greater than zero", "Error");
                    this.jtfRTimeFileSize.setText("10000");
                    this.jtfRTimeFileSize.requestFocus();
                    return;
                }
            }catch(Exception e){
                WindowOptionPane.printError("An error in the file size entered\n"+e.toString(), "Error");
                this.jtfRTimeFileSize.setText("10000");
                this.jtfRTimeFileSize.requestFocus();
                return;
            }
            this.loadSimulatorModel.addTimeAutomaticReadDiskProcess(fileSize, this.ckEraseFilesReadTaskGenerated.isSelected(), seconds);
            this.loadRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");
        }
    }

    private void rbRManualByTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRManualByTimeActionPerformed
        selectOptionForRTimeProcess();
    }//GEN-LAST:event_rbRManualByTimeActionPerformed

    private void rbRAutomaticByTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRAutomaticByTimeActionPerformed
        selectOptionForRTimeProcess();
    }//GEN-LAST:event_rbRAutomaticByTimeActionPerformed

    private void jbRListProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRListProcessActionPerformed
        this.loadRDiskProcesses();
    }//GEN-LAST:event_jbRListProcessActionPerformed

    private void jbSelectReadTimeFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelectReadTimeFileActionPerformed
        jtfRTimeProcess.setText(selectReadFile("./documents/manualRead"));
    }//GEN-LAST:event_jbSelectReadTimeFileActionPerformed

    private void jbReadFileTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReadFileTimeActionPerformed
        createTimeRDiscProcess();
    }//GEN-LAST:event_jbReadFileTimeActionPerformed

    private void rbTimeManualOptionReadWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTimeManualOptionReadWriteActionPerformed
        selectOptionForRWTimeProcess();
}//GEN-LAST:event_rbTimeManualOptionReadWriteActionPerformed

    private void rbTimeAutomaticOptionReadWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTimeAutomaticOptionReadWriteActionPerformed
        selectOptionForRWTimeProcess();
}//GEN-LAST:event_rbTimeAutomaticOptionReadWriteActionPerformed

    private void btSelectTimeReadWriteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectTimeReadWriteFileActionPerformed
        jtfRWTimeProcess.setText(selectReadFile("./documents/manualReadWrite"));
}//GEN-LAST:event_btSelectTimeReadWriteFileActionPerformed

    private void jbReadFileTime1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReadFileTime1ActionPerformed
        createTimeRWDiscProcess();
    }//GEN-LAST:event_jbReadFileTime1ActionPerformed

    private void rbTaskAutomaticOptionWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTaskAutomaticOptionWriteActionPerformed
        selectOptionForWProcess();
}//GEN-LAST:event_rbTaskAutomaticOptionWriteActionPerformed

    private void rbTimeAutomaticOptionWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTimeAutomaticOptionWriteActionPerformed
        selectOptionForWProcess();
}//GEN-LAST:event_rbTimeAutomaticOptionWriteActionPerformed

    private void jbReadFileTime2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReadFileTime2ActionPerformed
        createWDiscProcess();
    }//GEN-LAST:event_jbReadFileTime2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.loadWDiskProcesses();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbExecutionAtTheSameTimeBeginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExecutionAtTheSameTimeBeginActionPerformed
        beginProcessExecutionAtTheSameTime();
    }//GEN-LAST:event_jbExecutionAtTheSameTimeBeginActionPerformed

    private void jbExecutionAtTheSameTimeCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExecutionAtTheSameTimeCleanActionPerformed
        cleanProcessBegunAtTheSameTime();
    }//GEN-LAST:event_jbExecutionAtTheSameTimeCleanActionPerformed

    private void cbEveryNProcessesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEveryNProcessesActionPerformed
        controlBeginingProcessExecution();
    }//GEN-LAST:event_cbEveryNProcessesActionPerformed

    public void controlBeginingProcessExecution(){
        if(this.cbEveryNProcesses.isSelected())
            this.spnNumberEveryNProcesses.setEnabled(true);
        else
            this.spnNumberEveryNProcesses.setEnabled(false);
    }

    public void cleanProcessBegunAtTheSameTime(){
        this.loadSimulatorModel.cleanProcessExecution();
        this.jtfTotalTime.setText("0");
        /*
         * Processing
         */
        loadProcessingProcesses();
        /*
         * Read
         */
        loadRDiskProcesses();
        /*
         * Write
         */
        loadWDiskProcesses();
        /*
         * Read/Write
         */
        loadWRDiskProcesses();
        this.jbExecutionAtTheSameTimeBegin.setEnabled(true);
        WindowOptionPane.printInformation("All the processes has been deleted", "Processes deleted");
        
    }

    public void beginProcessExecutionAtTheSameTime(){
        if(this.loadSimulatorModel.getActualID()==1)
        {
            WindowOptionPane.printInformation("Thera are not processes for being executed", "Information");
            return;
        }
        this.jbExecutionAtTheSameTimeBegin.setEnabled(false);
        WindowOptionPane.printInformation("Press Aceptar for begining the process execution", "Begining the process execution");
        long totalExecutionTime = 0;
        int beginEveryNProcesses = Integer.parseInt(String.valueOf(this.spnNumberEveryNProcesses.getValue()));
        if(beginEveryNProcesses<=0){
            WindowOptionPane.printError("A file size most be entered", "Error");
            this.spnNumberEveryNProcesses.setValue(2);
            this.spnNumberEveryNProcesses.requestFocus();
            return;
        }
        if(!this.cbEveryNProcesses.isSelected())
            totalExecutionTime = this.loadSimulatorModel.beginProcessExecutionAtTheSameTime();
        else
            totalExecutionTime = this.loadSimulatorModel.beginProcessExecutionAtTheSameTime(beginEveryNProcesses);
        this.jtfTotalTime.setText(String.valueOf(totalExecutionTime));
        /*
         * Processing
         */
        loadProcessingProcesses();
        /*
         * Read
         */
        loadRDiskProcesses();
        /*
         * Write
         */
        loadWDiskProcesses();
        /*
         * Read/Write
         */
        loadWRDiskProcesses();
        WindowOptionPane.printInformation("The process execution has finished", "Process execution finished");        
    }

    public void createWDiscProcess(){
        //File size validation
        int fileSize = 0;
        try{
            if(this.jtfWTimeFileSize.getText().trim().equals(""))
            {
                WindowOptionPane.printError("A file size most be entered", "Error");
                this.jtfWTimeFileSize.setText("10000");
                this.jtfWTimeFileSize.requestFocus();
                return;
            }
            fileSize = Integer.parseInt(this.jtfWTimeFileSize.getText().trim());
            if(fileSize<=0){
                WindowOptionPane.printError("The file size most be greater than zero", "Error");
                this.jtfWTimeFileSize.setText("10000");
                this.jtfWTimeFileSize.requestFocus();
                return;
            }
        }catch(Exception e){
            WindowOptionPane.printError("An error in the file size entered\n"+e.toString(), "Error");
            this.jtfWTimeFileSize.setText("10000");
            this.jtfWTimeFileSize.requestFocus();
            return;
        }

        //Manual option
        if(this.rbTaskAutomaticOptionWrite.isSelected()){
            this.loadSimulatorModel.addTaskAutomaticWriteDiskProcess(fileSize, ckEraseFilesGeneratedW.isSelected());
            this.loadWDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");
        }else{
            if(this.rbTimeAutomaticOptionWrite.isSelected()){
                //Time validation
                int seconds = 0;
                try{
                    if(this.jtfWTime.getText().trim().equals(""))
                    {
                        WindowOptionPane.printError("A time most be entered", "Error");
                        this.jtfWTime.setText("10");
                        this.jtfWTime.requestFocus();
                        return;
                    }
                    seconds = Integer.parseInt(this.jtfWTime.getText().trim());
                    if(seconds<=0){
                        WindowOptionPane.printError("The time most be greater than zero", "Error");
                        this.jtfWTime.setText("10");
                        this.jtfWTime.requestFocus();
                        return;
                    }
                }catch(Exception e){
                    WindowOptionPane.printError("An error in the time entered\n"+e.toString(), "Error");
                    this.jtfWTime.setText("10");
                    this.jtfWTime.requestFocus();
                    return;
                }
                this.loadSimulatorModel.addTimeAutomaticWriteDiskProcess(fileSize, ckEraseFilesGeneratedW.isSelected(), seconds);
                this.loadWDiskProcesses();
                WindowOptionPane.printInformation("The process was sent", "Process");
            }
        }
    }

    public void selectOptionForWProcess(){
        if(this.rbTaskAutomaticOptionWrite.isSelected()){
            jtfWTime.setEnabled(false);
            jtfWTimeFileSize.setEnabled(true);
        }
        else
            if(this.rbTimeAutomaticOptionWrite.isSelected()){
                jtfWTime.setEnabled(true);
                jtfWTimeFileSize.setEnabled(true);
            }
    }

    public void selectOptionForRWTimeProcess(){
        if(this.rbTimeManualOptionReadWrite.isSelected()){
            btSelectTimeReadWriteFile.setEnabled(true);
            jtfRWTime.setEnabled(true);
            ckEraseFilesGeneratedRWTime.setEnabled(true);
            jtfRWTimeFileSize.setEnabled(false);
        }
        else
            if(this.rbTimeAutomaticOptionReadWrite.isSelected()){
                btSelectTimeReadWriteFile.setEnabled(false);
                jtfRWTime.setEnabled(true);
                ckEraseFilesGeneratedRWTime.setEnabled(true);
                jtfRWTimeFileSize.setEnabled(true);
            }
    }

    public void selectOptionForRTimeProcess(){
        if(this.rbRManualByTime.isSelected()){
            jbSelectReadTimeFile.setEnabled(true);
            jtfRTimeFileSize.setEnabled(false);
            ckEraseFilesReadTimeGenerated.setEnabled(false);
        }
        else
            if(this.rbRAutomaticByTime.isSelected()){
                jbSelectReadTimeFile.setEnabled(false);
                jtfRTimeFileSize.setEnabled(true);
                ckEraseFilesReadTimeGenerated.setEnabled(true);
            }
    }

    public void selectOptionForRTaskProcess(){
        if(this.rbRManualByTask.isSelected()){
            jtfRTaskFileSize.setEnabled(false);
            jbSelectReadTaskFile.setEnabled(true);
            ckEraseFilesReadTaskGenerated.setEnabled(false);
        }
        else
            if(this.rbRAutomaticByTask.isSelected()){
                jtfRTaskFileSize.setEnabled(true);
                jbSelectReadTaskFile.setEnabled(false);
                ckEraseFilesReadTaskGenerated.setEnabled(true);
            }
    }

    public void createTaskProcessingProcess (){
        if(this.jtfIterationNumber.getText().trim().equals(""))
        {
            WindowOptionPane.printError("The iteration number most be entered for realizing the operation", "Error");
            this.jtfIterationNumber.setText("50000");
            this.jtfIterationNumber.requestFocus();
            return;
        }
        long iterationNumber = 0;
        try{
            iterationNumber = Long.parseLong(this.jtfIterationNumber.getText().trim());
            if(iterationNumber<=0){
                WindowOptionPane.printError("The iteration number most be greater than zero", "Error");
                this.jtfIterationNumber.setText("50000");
                this.jtfIterationNumber.requestFocus();
                return;
            }
        }catch(Exception e){
            WindowOptionPane.printError("An error in the iteration number entered\n"+e.toString(), "Error");
            this.jtfIterationNumber.setText("50000");
            this.jtfIterationNumber.requestFocus();
            return;
        }
        this.loadSimulatorModel.addTaskProcessingProcess(iterationNumber);
        this.loadProcessingProcesses();
        WindowOptionPane.printInformation("The process was sent", "Process");
    }

    public void createTimeProcessingProcess (){
        if(this.jtfProcessingTimeSeconds.getText().trim().equals(""))
        {
            WindowOptionPane.printError("The second time most be entered for realizing the operation", "Error");
            this.jtfProcessingTimeSeconds.setText("10");
            this.jtfProcessingTimeSeconds.requestFocus();
            return;
        }
        long timeSeconds = 0;
        try{
            timeSeconds = Long.parseLong(this.jtfProcessingTimeSeconds.getText().trim());
            if(timeSeconds<=0){
                WindowOptionPane.printError("The time most be greater than zero", "Error");
                this.jtfProcessingTimeSeconds.setText("10");
                this.jtfProcessingTimeSeconds.requestFocus();
                return;
            }
        }catch(Exception e){
            WindowOptionPane.printError("An error in the time entered\n"+e.toString(), "Error");
            this.jtfIterationNumber.setText("10");
            this.jtfIterationNumber.requestFocus();
            return;
        }
        this.loadSimulatorModel.addTimeProcessingProcess(timeSeconds);
        this.loadProcessingProcesses();
        WindowOptionPane.printInformation("The process was sent", "Process");
    }

    public void selectOptionForRWProcess(){
        if(this.rbTaskManualOptionReadWrite.isSelected()){
            jtfRWFileSize.setEnabled(false);
            btSelectTaskReadWriteFile.setEnabled(true);
        }
        else
            if(this.rbTaskAutomaticOptionReadWrite.isSelected()){
                jtfRWFileSize.setEnabled(true);
                btSelectTaskReadWriteFile.setEnabled(false);
            }
    }
    

    public void createTaskRWDiscProcess(){
        if(this.rbTaskManualOptionReadWrite.isSelected()){
            if(this.jtfRWTaskProcess.getText().equals(""))
            {
                WindowOptionPane.printError("A file most be selected for realizing the operation", "Error");
                return;
            }
            this.loadSimulatorModel.addTaskManualWriteReadDiskProcess(new File(this.jtfRWTaskProcess.getText()), this.ckEraseFilesGeneratedRWTask.isSelected());
            this.loadWRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");
        }else{
            if(this.jtfRWFileSize.getText().equals(""))
            {
                WindowOptionPane.printError("A size most be entered for realizing the operation", "Error");
                this.jtfRWFileSize.requestFocus();
                return;
            }
            int fileSize = 0;
            try{
                fileSize = Integer.parseInt(this.jtfRWFileSize.getText());
                if(fileSize<=0){
                    WindowOptionPane.printError("The file size most be greater than zero", "Error");
                    this.jtfRWFileSize.setText("10000");
                    this.jtfRWFileSize.requestFocus();
                    return;
                }
            }catch(Exception e){
                WindowOptionPane.printError("An error in the file size entered+\n"+e.toString(), "Error");
                this.jtfRWFileSize.setText("10000");
                this.jtfRWFileSize.requestFocus();
                return;
            }
            this.loadSimulatorModel.addTaskAutomaticWriteReadDiskProcess(fileSize, this.ckEraseFilesGeneratedRWTask.isSelected());
            this.loadWRDiskProcesses();
            WindowOptionPane.printInformation("The process was sent", "Process");            
        }
    }


    public String selectReadFile(String initialPath){
        JFileChooser jfc = new JFileChooser(initialPath);
        jfc.removeChoosableFileFilter(jfc.getFileFilter());
        jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isFile()||f.isDirectory())
                    return true;
                return false;
            }

            @Override
            public String getDescription() {
                return "*";
            }
        });
        int resultado = jfc.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return jfc.getSelectedFile().getAbsolutePath();
        }
        else
            return "";
    }
    /**
    * @param args the command line arguments
    */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoadSimulatorGUI().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgRWOption;
    private javax.swing.JButton btSelectTaskReadWriteFile;
    private javax.swing.JButton btSelectTimeReadWriteFile;
    private javax.swing.JCheckBox cbEveryNProcesses;
    private javax.swing.JCheckBox ckEraseFilesGeneratedRWTask;
    private javax.swing.JCheckBox ckEraseFilesGeneratedRWTime;
    private javax.swing.JCheckBox ckEraseFilesGeneratedW;
    private javax.swing.JCheckBox ckEraseFilesReadTaskGenerated;
    private javax.swing.JCheckBox ckEraseFilesReadTimeGenerated;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbCompressFile;
    private javax.swing.JButton jbCreateTasjProcess;
    private javax.swing.JButton jbCreateTimeProcess;
    private javax.swing.JButton jbExecutionAtTheSameTimeBegin;
    private javax.swing.JButton jbExecutionAtTheSameTimeClean;
    private javax.swing.JButton jbListProcessingProcess;
    private javax.swing.JButton jbRListProcess;
    private javax.swing.JButton jbRWListProcess;
    private javax.swing.JButton jbReadFileTask;
    private javax.swing.JButton jbReadFileTime;
    private javax.swing.JButton jbReadFileTime1;
    private javax.swing.JButton jbReadFileTime2;
    private javax.swing.JButton jbSelectReadTaskFile;
    private javax.swing.JButton jbSelectReadTimeFile;
    private javax.swing.JTextField jtfIterationNumber;
    private javax.swing.JTextField jtfProcessingTimeSeconds;
    private javax.swing.JTextField jtfRTaskFileSize;
    private javax.swing.JTextField jtfRTaskProcess;
    private javax.swing.JTextField jtfRTime;
    private javax.swing.JTextField jtfRTimeFileSize;
    private javax.swing.JTextField jtfRTimeProcess;
    private javax.swing.JTextField jtfRWFileSize;
    private javax.swing.JTextField jtfRWTaskProcess;
    private javax.swing.JTextField jtfRWTime;
    private javax.swing.JTextField jtfRWTimeFileSize;
    private javax.swing.JTextField jtfRWTimeProcess;
    private javax.swing.JTextField jtfTotalTime;
    private javax.swing.JTextField jtfWTime;
    private javax.swing.JTextField jtfWTimeFileSize;
    private javax.swing.JRadioButton rbRAutomaticByTask;
    private javax.swing.JRadioButton rbRAutomaticByTime;
    private javax.swing.JRadioButton rbRManualByTask;
    private javax.swing.JRadioButton rbRManualByTime;
    private javax.swing.ButtonGroup rbRTaskOption;
    private javax.swing.ButtonGroup rbRTimeOption;
    private javax.swing.JRadioButton rbTaskAutomaticOptionReadWrite;
    private javax.swing.JRadioButton rbTaskAutomaticOptionWrite;
    private javax.swing.JRadioButton rbTaskManualOptionReadWrite;
    private javax.swing.JRadioButton rbTimeAutomaticOptionReadWrite;
    private javax.swing.JRadioButton rbTimeAutomaticOptionWrite;
    private javax.swing.JRadioButton rbTimeManualOptionReadWrite;
    private javax.swing.JSpinner spnNumberEveryNProcesses;
    private javax.swing.JTable tableRProcesses;
    private javax.swing.JTable tableRWProcesses;
    private javax.swing.JTable tableTaskProcessingProcess;
    private javax.swing.JTable tableWProcesses;
    // End of variables declaration//GEN-END:variables
    
    DefaultTableModel modelRWDiskProcesesess = new DefaultTableModel();
    DefaultTableModel modelRDiskProcesesess = new DefaultTableModel();
    DefaultTableModel modelWDiskProcesesess = new DefaultTableModel();
    DefaultTableModel modelProcessingProcesses = new DefaultTableModel();    

    public void centrarVentana() {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > pantalla.height)
        {
                frameSize.height = pantalla.height;
            }
            if (frameSize.width > pantalla.width)
        {
                frameSize.width = pantalla.width;
        }
        this.setLocation((pantalla.width - frameSize.width) / 2, (pantalla.height - frameSize.height) / 2);
    }
}
