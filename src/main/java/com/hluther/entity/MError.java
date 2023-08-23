package com.hluther.entity;
/**
 *
 * @author helmuth
 */
public class MError {
    
    private String error;
    private String lexeme;
    private int line;
    private int column;

    public MError(String error, String lexeme, int line, int column) {
        this.error = error;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    
    
}
