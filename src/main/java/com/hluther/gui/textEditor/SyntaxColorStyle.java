package com.hluther.gui.textEditor;

import java.awt.Color;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;
/**
 *
 * @author helmuth
 */
public class SyntaxColorStyle {
    
    private SyntaxScheme syntaxScheme;
    
    public SyntaxColorStyle(RSyntaxTextArea rSyntaxTextArea){    
        syntaxScheme = rSyntaxTextArea.getSyntaxScheme();
        setSintaxStyleRules();
    }
    
    private void setSintaxStyleRules(){
        syntaxScheme.getStyle(Token.RESERVED_WORD).foreground = Color.BLUE;
        syntaxScheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.MAGENTA;
        syntaxScheme.getStyle(Token.LITERAL_NUMBER_FLOAT).foreground = Color.MAGENTA;
        syntaxScheme.getStyle(Token.IDENTIFIER).foreground = new Color(34, 139,34);    
        syntaxScheme.getStyle(Token.LITERAL_CHAR).foreground = new Color(244, 70, 17);
        syntaxScheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = new Color(244, 70, 17);
        syntaxScheme.getStyle(Token.COMMENT_EOL).foreground = Color.GRAY;
        syntaxScheme.getStyle(Token.COMMENT_MULTILINE).foreground = Color.GRAY;
        
        syntaxScheme.getStyle(Token.ANNOTATION).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.COMMENT_DOCUMENTATION).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.COMMENT_KEYWORD).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.COMMENT_MARKUP).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.DATA_TYPE).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.FUNCTION).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.LITERAL_BOOLEAN).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.LITERAL_NUMBER_HEXADECIMAL).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_CDATA).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_CDATA_DELIMITER).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_COMMENT).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_DTD).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_ENTITY_REFERENCE).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_PROCESSING_INSTRUCTION).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_TAG_ATTRIBUTE).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_TAG_ATTRIBUTE_VALUE).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_TAG_DELIMITER).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.MARKUP_TAG_NAME).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.NULL).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.OPERATOR).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.PREPROCESSOR).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.REGEX).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.RESERVED_WORD_2).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.SEPARATOR).foreground = Color.BLACK;
        syntaxScheme.getStyle(Token.VARIABLE).foreground = Color.BLACK;
    }
}
