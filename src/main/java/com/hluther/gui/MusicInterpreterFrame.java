package com.hluther.gui;

import com.hluther.controller.AnalysisController;
import com.hluther.controller.MelodyFileController;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.Melody;
import com.hluther.gui.errorsDialog.ErrorsDialog;
import com.hluther.gui.textEditor.MTextArea;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Track;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import org.jfugue.player.Player;
/**
 *
 * @author helmuth
 */
public class MusicInterpreterFrame extends javax.swing.JFrame {

    //Instancia para la tabla de melodias
     private DefaultTableModel tableModel;
     private LinkedList<Melody> melodies;
     private boolean  editingMelody;
     private String oldname;
    
    //Instancia para el editor de texto.
    private MTextArea mTextArea;
    
    //Instancias para el interprete
    AnalysisController analysisController;
    AnalysisError analysisError;
    ErrorsDialog errorsDialog;
    Track track;
    
    //Instancias para ejecucion de melodias
    private Player player;
    private Melody playing;
    
    //Instancias para manejo de melodias en archivo binario
    MelodyFileController melodyFileController;
    
    //Instancia para la grafica
    private RealTimeChart currentChart;

    public JTextPane getOutputArea() {
        return outputArea;
    }
    
    
    public MusicInterpreterFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.initializeInstances();
        this.addMelodies();
    }
    
    private void initializeInstances(){
        this.mTextArea = new MTextArea(textAreaPane, positionLabel);
        this.analysisController = new AnalysisController();
        this.errorsDialog = new ErrorsDialog(this, false);
        this.player = new Player();
        this.melodyFileController = new MelodyFileController(this);
        this.tableModel = (DefaultTableModel)melodiesTable.getModel();
    }
      
    private void saveMelody(Melody melody){
        LinkedList<Melody> melodies = melodyFileController.getMelodies();
        boolean canceled = false;
        for(Melody currentMelody : melodies){
            if(currentMelody.getId().equals(melody.getId()) && !editingMelody){
                switch (JOptionPane.showConfirmDialog(this, "<html><center>Ya existe una melodia con el nombre "+melody.getId()+", ¿Desea sobreescribirla?", "Mensaje", 1, 3)) {
                    case JOptionPane.CANCEL_OPTION, JOptionPane.CLOSED_OPTION, JOptionPane.NO_OPTION ->{ return;}
                    case JOptionPane.OK_OPTION ->{
                        melodies.remove(currentMelody);
                        JOptionPane.showMessageDialog(this, "<html><center>Melodia "+melody.getId()+" sobreescrita exitosamente<br>Puede reproducirla en el area de biblioteca.</center></html>", "Construccion Finalizada", 3);
                    }
                }
                break;
            }
            if(currentMelody.getId().equals(oldname) && editingMelody){    
             
                melodies.remove(currentMelody);
                JOptionPane.showMessageDialog(this, "<html><center>Melodia "+melody.getId()+" modificada exitosamente<br>Puede reproducirla en el area de biblioteca.</center></html>", "Construccion Finalizada", 3);
                editingMelody = false; 
                break;
            }

        }

  
        melodies.add(melody);
        melodyFileController.saveMelodies(melodies); 
        addMelodies();
        tabbedPane.setSelectedIndex(0);
       
    }
    
     private void addMelodies(){
        Thread thread = new Thread(){
        @Override 
        public  void run(){
            try {
               tableModel.setRowCount(0);
                melodies = melodyFileController.getMelodies();
                if(melodies == null){
                   JOptionPane.showMessageDialog(MusicInterpreterFrame.this, "No se pudieron recuperar las pistas desde el archivo Tracks.bin", "Error Grave", 0);
                } else{
                    melodies.forEach(melody -> {
                       tableModel.addRow(new Object[]{melody.getId(), melody.getDuration()+" seg"});
                   });
                }
            }    
            catch (NullPointerException ex) {
                System.out.println(ex.getMessage() );
            }
        }};
        thread.start();
    }
     
    private void stopPlaying(){
         if(playing != null){
            playing.setExit(true);
           // currentChart.setExit(true);
        }
    }

    private void play(){
        if(melodiesTable.getSelectedRow() != -1){
            if(playing != null){
                playing.setExit(true);
            }
            
            playing = melodies.get(melodiesTable.getSelectedRow());
            playing.setExit(false);
            playing.play(new Thread(), player);
            if(playing.getChannels().size() > 0){
                currentChart = new RealTimeChart ("Grafico De Frecuencias", "Grafico de Frecuencias", "Frecuencia", playing.getChannels().get(0));
                (new Thread(currentChart)).start(); 
                playerPanel.add(currentChart);
                playerPanel.repaint();
                playerPanel.revalidate();
            }
        }
    }
    
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator8 = new javax.swing.JSeparator();
        backPanel = new javax.swing.JPanel();
        topMargin = new javax.swing.JPanel();
        botomMargin = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        leftMargin = new javax.swing.JPanel();
        rightMargin = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        library = new javax.swing.JPanel();
        tracksArea = new javax.swing.JPanel();
        tablePanel = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        melodiesTable = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listsTable = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tracksButtonsPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        editButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        playTrackButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        tracksTittlePanel = new javax.swing.JPanel();
        tittleLabel = new javax.swing.JLabel();
        playerArea = new javax.swing.JPanel();
        playerPanel = new javax.swing.JPanel();
        playerButtonsPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        textAreaPane = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        compileButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextPane();
        positionLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Interprete de Musica");
        setMinimumSize(new java.awt.Dimension(1000, 195));
        setPreferredSize(new java.awt.Dimension(1000, 500));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        backPanel.setLayout(new java.awt.BorderLayout());

        topMargin.setMaximumSize(new java.awt.Dimension(32767, 10));
        topMargin.setMinimumSize(new java.awt.Dimension(100, 10));
        topMargin.setPreferredSize(new java.awt.Dimension(989, 10));

        javax.swing.GroupLayout topMarginLayout = new javax.swing.GroupLayout(topMargin);
        topMargin.setLayout(topMarginLayout);
        topMarginLayout.setHorizontalGroup(
            topMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 989, Short.MAX_VALUE)
        );
        topMarginLayout.setVerticalGroup(
            topMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        backPanel.add(topMargin, java.awt.BorderLayout.PAGE_START);

        botomMargin.setMaximumSize(new java.awt.Dimension(32767, 10));
        botomMargin.setMinimumSize(new java.awt.Dimension(0, 10));
        botomMargin.setPreferredSize(new java.awt.Dimension(989, 10));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout botomMarginLayout = new javax.swing.GroupLayout(botomMargin);
        botomMargin.setLayout(botomMarginLayout);
        botomMarginLayout.setHorizontalGroup(
            botomMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, botomMarginLayout.createSequentialGroup()
                .addContainerGap(536, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(353, 353, 353))
        );
        botomMarginLayout.setVerticalGroup(
            botomMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, botomMarginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        backPanel.add(botomMargin, java.awt.BorderLayout.PAGE_END);

        leftMargin.setMaximumSize(new java.awt.Dimension(10, 32767));
        leftMargin.setMinimumSize(new java.awt.Dimension(10, 100));
        leftMargin.setPreferredSize(new java.awt.Dimension(10, 619));

        javax.swing.GroupLayout leftMarginLayout = new javax.swing.GroupLayout(leftMargin);
        leftMargin.setLayout(leftMarginLayout);
        leftMarginLayout.setHorizontalGroup(
            leftMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        leftMarginLayout.setVerticalGroup(
            leftMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 631, Short.MAX_VALUE)
        );

        backPanel.add(leftMargin, java.awt.BorderLayout.LINE_START);

        rightMargin.setMaximumSize(new java.awt.Dimension(10, 32767));
        rightMargin.setMinimumSize(new java.awt.Dimension(10, 100));
        rightMargin.setPreferredSize(new java.awt.Dimension(10, 619));

        javax.swing.GroupLayout rightMarginLayout = new javax.swing.GroupLayout(rightMargin);
        rightMargin.setLayout(rightMarginLayout);
        rightMarginLayout.setHorizontalGroup(
            rightMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        rightMarginLayout.setVerticalGroup(
            rightMarginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 631, Short.MAX_VALUE)
        );

        backPanel.add(rightMargin, java.awt.BorderLayout.LINE_END);

        library.setLayout(new java.awt.GridLayout(1, 0));

        tracksArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tracksArea.setMaximumSize(new java.awt.Dimension(1500, 2147483647));
        tracksArea.setLayout(new java.awt.BorderLayout());

        tablePanel.setLayout(new java.awt.GridLayout(2, 0));

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        melodiesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Duracion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(melodiesTable);
        if (melodiesTable.getColumnModel().getColumnCount() > 0) {
            melodiesTable.getColumnModel().getColumn(0).setResizable(false);
            melodiesTable.getColumnModel().getColumn(1).setResizable(false);
        }

        jPanel12.add(jScrollPane1);

        tablePanel.add(jPanel12);

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        listsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(listsTable);
        if (listsTable.getColumnModel().getColumnCount() > 0) {
            listsTable.getColumnModel().getColumn(0).setResizable(false);
        }

        jPanel14.add(jScrollPane3);

        jPanel13.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel16.setMinimumSize(new java.awt.Dimension(100, 40));
        jPanel16.setPreferredSize(new java.awt.Dimension(480, 40));

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel1.setText("Listas de Reproduccion");
        jPanel16.add(jLabel1);

        jPanel13.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        tablePanel.add(jPanel13);

        tracksArea.add(tablePanel, java.awt.BorderLayout.CENTER);

        tracksButtonsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tracksButtonsPanel.setMaximumSize(new java.awt.Dimension(32767, 70));
        tracksButtonsPanel.setMinimumSize(new java.awt.Dimension(100, 70));
        tracksButtonsPanel.setPreferredSize(new java.awt.Dimension(484, 70));
        tracksButtonsPanel.setLayout(new java.awt.GridLayout(1, 0, 10, 10));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        createButton.setText("Crear");
        createButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createButton.setPreferredSize(new java.awt.Dimension(100, 30));
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });
        jPanel2.add(createButton, new java.awt.GridBagConstraints());

        tracksButtonsPanel.add(jPanel2);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        editButton.setText("Editar");
        editButton.setPreferredSize(new java.awt.Dimension(100, 30));
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        jPanel3.add(editButton, new java.awt.GridBagConstraints());

        tracksButtonsPanel.add(jPanel3);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        playTrackButton.setText("Reproducir");
        playTrackButton.setPreferredSize(new java.awt.Dimension(100, 30));
        playTrackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playTrackButtonActionPerformed(evt);
            }
        });
        jPanel4.add(playTrackButton, new java.awt.GridBagConstraints());

        tracksButtonsPanel.add(jPanel4);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        deleteButton.setText("Eliminar");
        deleteButton.setPreferredSize(new java.awt.Dimension(100, 30));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel5.add(deleteButton, new java.awt.GridBagConstraints());

        tracksButtonsPanel.add(jPanel5);

        tracksArea.add(tracksButtonsPanel, java.awt.BorderLayout.PAGE_END);

        tracksTittlePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tracksTittlePanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        tracksTittlePanel.setMinimumSize(new java.awt.Dimension(0, 40));
        tracksTittlePanel.setPreferredSize(new java.awt.Dimension(484, 40));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
        flowLayout1.setAlignOnBaseline(true);
        tracksTittlePanel.setLayout(flowLayout1);

        tittleLabel.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        tittleLabel.setText("Lista de Canciones");
        tittleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tittleLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tracksTittlePanel.add(tittleLabel);

        tracksArea.add(tracksTittlePanel, java.awt.BorderLayout.PAGE_START);

        library.add(tracksArea);

        playerArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        playerArea.setLayout(new java.awt.BorderLayout());

        playerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 2, true));

        javax.swing.GroupLayout playerPanelLayout = new javax.swing.GroupLayout(playerPanel);
        playerPanel.setLayout(playerPanelLayout);
        playerPanelLayout.setHorizontalGroup(
            playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 476, Short.MAX_VALUE)
        );
        playerPanelLayout.setVerticalGroup(
            playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 522, Short.MAX_VALUE)
        );

        playerArea.add(playerPanel, java.awt.BorderLayout.CENTER);

        playerButtonsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        playerButtonsPanel.setMaximumSize(new java.awt.Dimension(32767, 70));
        playerButtonsPanel.setMinimumSize(new java.awt.Dimension(100, 70));
        playerButtonsPanel.setPreferredSize(new java.awt.Dimension(480, 70));
        playerButtonsPanel.setRequestFocusEnabled(false);
        playerButtonsPanel.setLayout(new java.awt.GridLayout(1, 0, 30, 0));

        jPanel6.setPreferredSize(new java.awt.Dimension(30, 66));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 66, Short.MAX_VALUE)
        );

        playerButtonsPanel.add(jPanel6);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/boton-de-play.png"))); // NOI18N
        playButton.setBorder(null);
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        playButton.setMaximumSize(new java.awt.Dimension(100, 100));
        playButton.setMinimumSize(new java.awt.Dimension(100, 100));
        playButton.setPreferredSize(new java.awt.Dimension(100, 100));
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        playerButtonsPanel.add(playButton);

        pauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/detener.png"))); // NOI18N
        pauseButton.setBorder(null);
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setMaximumSize(new java.awt.Dimension(100, 100));
        pauseButton.setMinimumSize(new java.awt.Dimension(100, 100));
        pauseButton.setPreferredSize(new java.awt.Dimension(100, 100));
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });
        playerButtonsPanel.add(pauseButton);

        jPanel7.setPreferredSize(new java.awt.Dimension(30, 66));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 66, Short.MAX_VALUE)
        );

        playerButtonsPanel.add(jPanel7);

        playerArea.add(playerButtonsPanel, java.awt.BorderLayout.PAGE_END);

        library.add(playerArea);

        tabbedPane.addTab("Biblioteca", library);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout textAreaPaneLayout = new javax.swing.GroupLayout(textAreaPane);
        textAreaPane.setLayout(textAreaPaneLayout);
        textAreaPaneLayout.setHorizontalGroup(
            textAreaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 969, Short.MAX_VALUE)
        );
        textAreaPaneLayout.setVerticalGroup(
            textAreaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );

        jPanel9.add(textAreaPane, java.awt.BorderLayout.CENTER);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel15.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanel15.setPreferredSize(new java.awt.Dimension(969, 30));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        compileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compile.png"))); // NOI18N
        compileButton.setBorderPainted(false);
        compileButton.setContentAreaFilled(false);
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileButtonActionPerformed(evt);
            }
        });
        jPanel15.add(compileButton);

        jPanel9.add(jPanel15, java.awt.BorderLayout.PAGE_START);

        jPanel8.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setMaximumSize(new java.awt.Dimension(32767, 200));
        jPanel10.setMinimumSize(new java.awt.Dimension(100, 200));
        jPanel10.setPreferredSize(new java.awt.Dimension(969, 150));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        outputArea.setEditable(false);
        jScrollPane2.setViewportView(outputArea);

        jPanel11.add(jScrollPane2);

        jTabbedPane1.addTab("Salida", jPanel11);

        jPanel10.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        positionLabel.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        positionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        positionLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 2, true));
        positionLabel.setMaximumSize(new java.awt.Dimension(53, 25));
        positionLabel.setMinimumSize(new java.awt.Dimension(53, 25));
        positionLabel.setOpaque(true);
        positionLabel.setPreferredSize(new java.awt.Dimension(53, 25));
        jPanel10.add(positionLabel, java.awt.BorderLayout.PAGE_END);

        jPanel8.add(jPanel10, java.awt.BorderLayout.PAGE_END);

        tabbedPane.addTab("Editor de Codigo", jPanel8);

        backPanel.add(tabbedPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(backPanel);

        jMenu1.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newFile.png"))); // NOI18N
        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open.png"))); // NOI18N
        jMenuItem2.setText("Abrir");
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saveAs.png"))); // NOI18N
        jMenuItem3.setText("Guardar");
        jMenu1.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saveAs.png"))); // NOI18N
        jMenuItem4.setText("Guardar Como");
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator3);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        jMenuItem5.setText("Salir");
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Mas");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/about.png"))); // NOI18N
        jMenuItem6.setText("Acerca De");
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void compileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileButtonActionPerformed
       Thread thread = new Thread(){
        @Override 
        public  void run(){
            try{
                 outputArea.setText("ANALISIS INICIADO");
                String text = mTextArea.getText();

                //Ejecutar analisis y obtener ast y errores resultantes
                analysisController.analyzeTrack(text);
                track = analysisController.getTrack();
                analysisError = analysisController.getAnalysisError();
                track.setConsole(outputArea);

                //Realizar analisis del ast
                try{
                    track.analyze(new TypeTable(), new SymbolTable(), new Stack<>(),  analysisError);
                } catch(NullPointerException e){
                    //Mostrar error en el log
                }

                //Si no existen errores realizar la ejecucion
                if(analysisError.getLexicalErrors().isEmpty() && analysisError.getSemanticErrors().isEmpty() && analysisError.getSintacticErrors().isEmpty()){
                   track.execute(new TypeTable(), new SymbolTable(), new Stack<>(), track);

                   //Si no existen errores obtener melodia, setearle su codigo y mostrar opcion de guardado
                   track.getMelody().setCode(text);
                   int answer = JOptionPane.showConfirmDialog(rootPane, "Construccion exitosa. ¿Desea guardar la cancion "+ track.getMelody().getId()+"?");
                    switch (answer) {
                        case JOptionPane.CANCEL_OPTION, JOptionPane.CLOSED_OPTION ->{}
                        case JOptionPane.OK_OPTION ->{
                            saveMelody(track.getMelody());
                        }
                    }
                } else{
                    errorsDialog.showErrors(analysisError);
                }
            } catch(ArithmeticException a){
               outputArea.setText("Error de ejecucion. Division entre 0");
            } catch(NegativeArraySizeException n){
                outputArea.setText("Error de ejecucion. Declaracion de arreglo con indices negativos");
            } catch(ArrayIndexOutOfBoundsException ar){
                outputArea.setText("Error de ejecucion. Acceso a arreglo fuera de los limites");
            } catch(NumberFormatException nu){
                 outputArea.setText("Error de ejecucion. Uso de valores negativos en funcion Reproducir|Esperar.");
            }
        }};
        thread.start();
    }//GEN-LAST:event_compileButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        stopPlaying();
         editingMelody = false;
        mTextArea.setText("");
        outputArea.setText("");
        tabbedPane.setSelectedIndex(1);
       
    }//GEN-LAST:event_createButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        stopPlaying(); 
        if(melodiesTable.getSelectedRow() != -1){
            outputArea.setText("");
            mTextArea.setText(melodies.get(melodiesTable.getSelectedRow()).getCode());
            oldname = melodies.get(melodiesTable.getSelectedRow()).getId();
            tabbedPane.setSelectedIndex(1);
            editingMelody = true;
        }
        
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if(melodiesTable.getSelectedRow() != -1){
            melodies.remove(melodiesTable.getSelectedRow());
            melodyFileController.saveMelodies(melodies);
            addMelodies();
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void playTrackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playTrackButtonActionPerformed
        play();
    }//GEN-LAST:event_playTrackButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        play();       
    }//GEN-LAST:event_playButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        if(playing != null){
            playing.setExit(true);
          //  currentChart.setExit(true);
        }
    }//GEN-LAST:event_pauseButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MusicInterpreterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MusicInterpreterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MusicInterpreterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MusicInterpreterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MusicInterpreterFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backPanel;
    private javax.swing.JPanel botomMargin;
    private javax.swing.JButton compileButton;
    private javax.swing.JButton createButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
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
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel leftMargin;
    private javax.swing.JPanel library;
    private javax.swing.JTable listsTable;
    private javax.swing.JTable melodiesTable;
    private javax.swing.JTextPane outputArea;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton playTrackButton;
    private javax.swing.JPanel playerArea;
    private javax.swing.JPanel playerButtonsPanel;
    private javax.swing.JPanel playerPanel;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JPanel rightMargin;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JPanel textAreaPane;
    private javax.swing.JLabel tittleLabel;
    private javax.swing.JPanel topMargin;
    private javax.swing.JPanel tracksArea;
    private javax.swing.JPanel tracksButtonsPanel;
    private javax.swing.JPanel tracksTittlePanel;
    // End of variables declaration//GEN-END:variables
}
