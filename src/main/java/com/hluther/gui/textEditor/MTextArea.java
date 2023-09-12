package com.hluther.gui.textEditor;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.text.BadLocationException;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/**
 *
 * @author helmuth
 */
public class MTextArea extends RSyntaxTextArea{
    
    private RSyntaxTextArea syntaxTextArea; 
    private SyntaxEditingStyle syntaxEditingStyle;
    private SyntaxColorStyle syntaxColorStyle;
    private LineNumber lineNumber;
    private JScrollPane scrollPane;
    private AutoComplete autocomplete;
    private AutoCompletion autoCompletion;
    private JPanel panel;
    private JLabel positionLabel;
    
    public MTextArea(JPanel panel, JLabel positionLabel){
        syntaxTextArea = new RSyntaxTextArea();
        this.panel = panel;
        this.positionLabel = positionLabel;
        initializeInstances();
        initializeComponent();
    }
    
    public MTextArea(JPanel panel, String text){
        syntaxTextArea = new RSyntaxTextArea(text);
        this.panel = panel;
        initializeInstances();
        initializeComponent();
    }
    
    private void initializeInstances(){
        autocomplete = new AutoComplete();
        lineNumber = new LineNumber(syntaxTextArea);
        scrollPane = new JScrollPane(syntaxTextArea);
        autoCompletion = new AutoCompletion(autocomplete.getCompletionProvider());
        syntaxEditingStyle = new SyntaxEditingStyle(syntaxTextArea);
        syntaxColorStyle = new SyntaxColorStyle(syntaxTextArea);
    }
    
    private void initializeComponent(){
        //Add LineNumnber and JScrollPane
        scrollPane.setRowHeaderView(lineNumber);
        panel.setLayout(new GridLayout());
        panel.add(scrollPane);
        syntaxTextArea.setCodeFoldingEnabled(true);
        
        //Add Autocompletion
        autoCompletion.install(syntaxTextArea);   
        
        //Add Event
        syntaxTextArea.addCaretListener((CaretEvent e) -> {
            try {
                int position = syntaxTextArea.getCaretPosition();
                int line = syntaxTextArea.getLineOfOffset(position);
                int column = position - syntaxTextArea.getLineStartOffset(line);
                line += 1;
                positionLabel.setText("Linea: " + line + "  Columna: " + column);
            }
            catch(BadLocationException ex){
                System.out.println("Error al capturar el evento");
            }
        }); 
    }
    
    @Override
    public String getText(){
        return syntaxTextArea.getText();
    }
    
    @Override
    public void setText(String data){
        syntaxTextArea.setText(data);
    }
}
