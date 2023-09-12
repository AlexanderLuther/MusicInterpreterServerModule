package com.hluther.gui.textEditor;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
/**
 *
 * @author helmuth
 */
public class SyntaxEditingStyle {
    
    private AbstractTokenMakerFactory abstractTokenMakerFactory;
    private final String LEXER_PATH = "com.hluther.interpreter.lexer.TokenMaker";
    private final String STYLE_NAME = "text/MLanguage";
    
    public SyntaxEditingStyle(RSyntaxTextArea rSyntaxTextArea){
        abstractTokenMakerFactory = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
        abstractTokenMakerFactory.putMapping(STYLE_NAME, LEXER_PATH);
        rSyntaxTextArea.setSyntaxEditingStyle(STYLE_NAME);
    }
   
}
