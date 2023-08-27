package com.hluther.interpreter.lexer;

import java.util.LinkedList;
import com.hluther.entity.MError;
import com.hluther.interpreter.parser.TrackSymbol;
import java_cup.runtime.*;

%%
//----------------------------------- OPCIONES Y DECLARACIONES -----------------------------------//
%class TrackLexer
%cupsym TrackSymbol
%cup
%public
%unicode
%line
%column
%states STRING

//-------------------------------------------- MACROS --------------------------------------------//
LineTerminator =    \r|\n|\r\n
InputCharacter =    [^\r\n]
WhiteSpace =        {LineTerminator} | [ \t\f]
NoZeroNumber =      [1-9]
EndOfLineComment =  ">>" {InputCharacter}*
BlockComment =      ("<-" [^]* "->")
Comment =           {EndOfLineComment} | {BlockComment}
//------------------------------------------ CODIGO JAVA -----------------------------------------//
%{
    private LinkedList<MError> lexicalErrors = new LinkedList();
    private LinkedList<MError> semanticErrors = new LinkedList();
    private StringBuffer string = new StringBuffer();
    private StringBuffer errorLexeme = new StringBuffer();
    private int errorLine = 0;
    private int errorColumn = 0;
    private Symbol symbol;
    
    private Symbol createToken(String name, int type, Object lexeme){
        symbol = new Symbol(type, yyline+1, yycolumn, lexeme);
        printToken(name);
        verifyLexicalError();
        return symbol;
    }

    private Symbol createToken(String name, int type){
        symbol = new Symbol(type, yyline+1, yycolumn);
        printToken(name);
        verifyLexicalError();
        return symbol;
    }

    public LinkedList<MError> getLexicalErrors(){
        return lexicalErrors;
    }

    public LinkedList<MError> getSemanticErrors(){
        return semanticErrors;
    }
    
    private void printToken(String token){
        System.out.println(token);
    }

    private void addSemanticError(MError error){
        semanticErrors.add(error);
    }

    private void addLexicalError(MError error){
        lexicalErrors.add(error);
    }

    private void verifyLexicalError(){
        if(errorLexeme.length() != 0){
            addLexicalError(new MError("Lexema no reconocido.", errorLexeme.toString(), errorLine, errorColumn-errorLexeme.length()));
            errorLexeme.setLength(0);
        }
    }

%}

%%
//------------------------------------------ REGLAS LEXICAS -----------------------------------------//
<YYINITIAL>{

    //TIPOS DE DATOS
    0 | {NoZeroNumber}[:digit:]*                                {   if(yytext().length() > 9){
                                                                        addSemanticError(new MError("El tamaño del ENTERO execede el limite permitido (9 cifras).", yytext(), yyline+1, yycolumn));
                                                                        return createToken("INTEGER", TrackSymbol.INTEGER, Integer.parseInt(yytext().substring(0,8)));
                                                                    } else{
                                                                        return createToken("INTEGER", TrackSymbol.INTEGER, Integer.parseInt(yytext())); 
                                                                    }
                                                                }
    0 "." [:digit:]+ | {NoZeroNumber}[:digit:]* "." [:digit:]+  {   if(yytext().split("\\.")[1].length() > 6){
                                                                        addSemanticError(new MError("El tamaño del DOBLE execede el limite permitido (6 cifras despues del punto).", yytext(), yyline+1, yycolumn));
                                                                        return createToken("DOUBLE", TrackSymbol.DOUBLE, Double.valueOf(yytext().substring(0, yytext().indexOf('.')+6)));
                                                                    } else{
                                                                        return createToken("DOUBLE", TrackSymbol.DOUBLE, Double.valueOf(yytext()));
                                                                    }
                                                                }
    "Verdadero"                                                 { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "verdadero"                                                 { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "Falso"                                                     { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "falso"                                                     { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "True"                                                      { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "true"                                                      { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "False"                                                     { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "false"                                                     { return createToken("BOOLEAN", TrackSymbol.BOOLEAN, yytext()); }
    "'" [^(#'\n\t\r )]{1} "'"                                   { return createToken("CHARACTER", TrackSymbol.CHARACTER, (Character)yytext().charAt(1)); } 
    ("'") ("##") ("'")                                          { return createToken("CHARACTER", TrackSymbol.CHARACTER, (Character)'#'); }         
    ("'") ("#'") ("'")                                          { return createToken("CHARACTER", TrackSymbol.CHARACTER, (Character)'\''); }       
    ("'") ("#t") ("'")                                          { return createToken("CHARACTER", TrackSymbol.CHARACTER, (Character)'\t'); }        
    ("'") ("#n") ("'")                                          { return createToken("CHARACTER", TrackSymbol.CHARACTER, (Character)'\n'); }   
    \"                                                          { string.setLength(0); yybegin(STRING); }

    //OPERADORES RELACIONALES
    "=="                        { return createToken("ISEQUAL", TrackSymbol.ISEQUAL); }                      
    "!="                        { return createToken("NOTEQUAL", TrackSymbol.NOTEQUAL); }                    
    "<"                         { return createToken("LESSTHAN", TrackSymbol.LESSTHAN); }                     
    ">"                         { return createToken("GREATERTHAN", TrackSymbol.GREATERTHAN); }                     
    "<="                        { return createToken("LESSTHANOREQUALTO", TrackSymbol.LESSTHANOREQUALTO); }                     
    ">="                        { return createToken("GREATERTHANOREQUALTO", TrackSymbol.GREATERTHANOREQUALTO); }                     
    "!!"                        { return createToken("ISNULL", TrackSymbol.ISNULL); }     
    
    //OPERADORES LOGICOS
    "&&"                       { return createToken("AND", TrackSymbol.AND); }                       
    "!&&"                      { return createToken("NAND", TrackSymbol.NAND); }                       
    "||"                       { return createToken("OR", TrackSymbol.OR); }                      
    "!||"                      { return createToken("NOR", TrackSymbol.NOR); }                       
    "&|"                       { return createToken("XOR", TrackSymbol.XOR); }                     
    "!"                        { return createToken("NOT", TrackSymbol.NOT); } 

    //OPERADORES ARITMETICOS
    "+"                       { return createToken("PLUS", TrackSymbol.PLUS); }                         
    "-"                       { return createToken("MINUS", TrackSymbol.MINUS); }                         
    "*"                       { return createToken("ASTERISK", TrackSymbol.ASTERISK); }                            
    "/"                       { return createToken("SLASH", TrackSymbol.SLASH); }                         
    "%"                       { return createToken("MOD", TrackSymbol.MOD); }                         
    "^"                       { return createToken("POWER", TrackSymbol.POWER); }      

    //OPERADORES ESPECIALES
    "++"                      { return createToken("INCREMENT", TrackSymbol.INCREMENT); }                         
    "--"                      { return createToken("DECREMENT", TrackSymbol.DECREMENT); }                        
    "+="                      { return createToken("INCREMENTANDASSIGMENT", TrackSymbol.INCREMENTANDASSIGMENT); } 

    //PALABRAS RESERVADAS
    "Pista"         { return createToken("TRACK", TrackSymbol.TRACK, yytext()); }
    "pista"         { return createToken("TRACK", TrackSymbol.TRACK, yytext()); }
    "Extiende"      { return createToken("EXTENDS", TrackSymbol.EXTENDS, yytext()); }
    "extiende"      { return createToken("EXTENDS", TrackSymbol.EXTENDS, yytext()); }
    "Keep"          { return createToken("KEEP", TrackSymbol.KEEP, yytext()); }
    "keep"          { return createToken("KEEP", TrackSymbol.KEEP, yytext()); }
    "Var"           { return createToken("VAR", TrackSymbol.VAR, yytext()); }
    "var"           { return createToken("VAR", TrackSymbol.VAR, yytext()); }
    "Entero"        { return createToken("INTEGERTYPE", TrackSymbol.INTEGERTYPE, yytext()); }
    "entero"        { return createToken("INTEGERTYPE", TrackSymbol.INTEGERTYPE, yytext()); }
    "Doble"         { return createToken("DOUBLETYPE", TrackSymbol.DOUBLETYPE, yytext()); }
    "doble"         { return createToken("DOUBLETYPE", TrackSymbol.DOUBLETYPE, yytext()); }
    "Boolean"       { return createToken("BOOLEANTYPE", TrackSymbol.BOOLEANTYPE, yytext()); }
    "boolean"       { return createToken("BOOLEANTYPE", TrackSymbol.BOOLEANTYPE, yytext()); }
    "Cadena"        { return createToken("STRINGTYPE", TrackSymbol.STRINGTYPE, yytext()); }
    "cadena"        { return createToken("STRINGTYPE", TrackSymbol.STRINGTYPE, yytext()); }
    "Caracter"      { return createToken("CHARACTERTYPE", TrackSymbol.CHARACTERTYPE, yytext()); }
    "caracter"      { return createToken("CHARACTERTYPE", TrackSymbol.CHARACTERTYPE, yytext()); }
    "Arreglo"       { return createToken("ARRAY", TrackSymbol.ARRAY, yytext()); }
    "arreglo"       { return createToken("ARRAY", TrackSymbol.ARRAY, yytext()); }
    "Si"            { return createToken("IF", TrackSymbol.IF, yytext()); }
    "si"            { return createToken("IF", TrackSymbol.IF, yytext()); }
    "Sino"          { return createToken("ELSE", TrackSymbol.ELSE, yytext()); }
    "sino"          { return createToken("ELSE", TrackSymbol.ELSE, yytext()); }
    "Switch"        { return createToken("SWITCH", TrackSymbol.SWITCH, yytext()); }
    "switch"        { return createToken("SWITCH", TrackSymbol.SWITCH, yytext()); }
    "Caso"          { return createToken("CASE", TrackSymbol.CASE, yytext()); }
    "caso"          { return createToken("CASE", TrackSymbol.CASE, yytext()); }
    "Salir"         { return createToken("BREAK", TrackSymbol.BREAK, yytext()); }
    "salir"         { return createToken("BREAK", TrackSymbol.BREAK, yytext()); }
    "Default"       { return createToken("DEFAULT", TrackSymbol.DEFAULT, yytext()); }
    "default"       { return createToken("DEFAULT", TrackSymbol.DEFAULT, yytext()); }
    "Para"          { return createToken("FOR", TrackSymbol.FOR, yytext()); }
    "para"          { return createToken("FOR", TrackSymbol.FOR, yytext()); }
    "Mientras"      { return createToken("WHILE", TrackSymbol.WHILE, yytext()); }
    "mientras"      { return createToken("WHILE", TrackSymbol.WHILE, yytext()); }
    "Hacer"         { return createToken("DO", TrackSymbol.DO, yytext()); }
    "hacer"         { return createToken("DO", TrackSymbol.DO, yytext()); }
    "Continuar"     { return createToken("CONTINUE", TrackSymbol.CONTINUE, yytext()); }
    "continuar"     { return createToken("CONTINUE", TrackSymbol.CONTINUE, yytext()); }
    "Retorna"       { return createToken("RETURN", TrackSymbol.RETURN, yytext()); }
    "retorna"       { return createToken("RETURN", TrackSymbol.RETURN, yytext()); }
    "Reproducir"    { return createToken("PLAY", TrackSymbol.PLAY, yytext()); }
    "reproducir"    { return createToken("PLAY", TrackSymbol.PLAY, yytext()); }
    "Esperar"       { return createToken("WAIT", TrackSymbol.WAIT, yytext()); }
    "esperar"       { return createToken("WAIT", TrackSymbol.WAIT, yytext()); }
    "Ordenar"       { return createToken("ORDER", TrackSymbol.ORDER, yytext()); }
    "ordenar"       { return createToken("ORDER", TrackSymbol.ORDER, yytext()); }
    "Ascendente"    { return createToken("ASCENDENT", TrackSymbol.ASCENDENT, yytext()); }
    "ascendente"    { return createToken("ASCENDENT", TrackSymbol.ASCENDENT, yytext()); }
    "Descendente"   { return createToken("DESCENDENT", TrackSymbol.DESCENDENT, yytext()); }
    "descendente"   { return createToken("DESCENDENT", TrackSymbol.DESCENDENT, yytext()); }
    "Pares"         { return createToken("EVEN", TrackSymbol.EVEN, yytext()); }
    "pares"         { return createToken("EVEN", TrackSymbol.EVEN, yytext()); }
    "Impares"       { return createToken("ODD", TrackSymbol.ODD, yytext()); }
    "impares"       { return createToken("ODD", TrackSymbol.ODD, yytext()); }
    "Primos"        { return createToken("PRIME", TrackSymbol.PRIME, yytext()); }
    "primos"        { return createToken("PRIME", TrackSymbol.PRIME, yytext()); }
    "Sumarizar"     { return createToken("SUMMARIZE", TrackSymbol.SUMMARIZE, yytext()); }
    "sumarizar"     { return createToken("SUMMARIZE", TrackSymbol.SUMMARIZE, yytext()); }
    "Longitud"      { return createToken("LENGTH", TrackSymbol.LENGTH, yytext()); }
    "longitud"      { return createToken("LENGTH", TrackSymbol.LENGTH, yytext()); }
    "Mensaje"       { return createToken("MESSAGE", TrackSymbol.MESSAGE, yytext()); }
    "mensaje"       { return createToken("MESSAGE", TrackSymbol.MESSAGE, yytext()); }
    "Principal"     { return createToken("MAIN", TrackSymbol.MAIN, yytext()); }
    "principal"     { return createToken("MAIN", TrackSymbol.MAIN, yytext()); }
    "Do"            { return createToken("C", TrackSymbol.C, yytext()); }
    "do"            { return createToken("C", TrackSymbol.C, yytext()); }
    "Do#"           { return createToken("C8", TrackSymbol.C8, yytext()); }
    "do#"           { return createToken("C8", TrackSymbol.C8, yytext()); }
    "Re"            { return createToken("D", TrackSymbol.D, yytext()); }
    "re"            { return createToken("D", TrackSymbol.D, yytext()); }
    "Re#"           { return createToken("D8", TrackSymbol.D8, yytext()); }
    "re#"           { return createToken("D8", TrackSymbol.D8, yytext()); }
    "Mi"            { return createToken("E", TrackSymbol.E, yytext()); }
    "mi"            { return createToken("E", TrackSymbol.E, yytext()); }
    "Fa"            { return createToken("F", TrackSymbol.F, yytext()); }
    "fa"            { return createToken("F", TrackSymbol.F, yytext()); }
    "Fa#"           { return createToken("F8", TrackSymbol.F8, yytext()); }
    "fa#"           { return createToken("F8", TrackSymbol.F8, yytext()); }
    "Sol"           { return createToken("G", TrackSymbol.G, yytext()); }
    "sol"           { return createToken("G", TrackSymbol.G, yytext()); }
    "Sol#"          { return createToken("G8", TrackSymbol.G8, yytext()); }
    "sol#"          { return createToken("G8", TrackSymbol.G8, yytext()); }
    "La"            { return createToken("A", TrackSymbol.A, yytext()); }
    "la"            { return createToken("A", TrackSymbol.A, yytext()); }
    "La#"           { return createToken("A8", TrackSymbol.A8, yytext()); }
    "la#"           { return createToken("A8", TrackSymbol.A8, yytext()); }

    //COMENTARIOS
    {Comment}       { verifyLexicalError(); }

    //OTROS
    "{"                                             { return createToken("CURLYBRACKETO", TrackSymbol.CURLYBRACKETO); }
    "}"                                             { return createToken("CURLYBRACKETC", TrackSymbol.CURLYBRACKETC); }
    "["                                             { return createToken("SQUAREBRACKETO", TrackSymbol.SQUAREBRACKETO); }
    "]"                                             { return createToken("SQUAREBRACKETC", TrackSymbol.SQUAREBRACKETC); }
    "("                                             { return createToken("PARENTHESISO", TrackSymbol.PARENTHESISO); }
    ")"                                             { return createToken("PARENTHESISC", TrackSymbol.PARENTHESISC); }
    "="                                             { return createToken("EQUAL", TrackSymbol.EQUAL); }
    ";"                                             { return createToken("SEMICOLON", TrackSymbol.SEMICOLON); }
    ","                                             { return createToken("COMMA", TrackSymbol.COMMA); }
    ":"                                             { return createToken("COLON", TrackSymbol.COLON); }
    ([:letter:]|"_")([:letter:]|[:digit:]|"_")*     { return createToken("ID", TrackSymbol.ID, yytext()); } 
    {WhiteSpace}                                    { verifyLexicalError(); }
    [^]                                             { errorLexeme.append(yytext()); errorLine = yyline+1; errorColumn = yycolumn+1; }
    <<EOF>>                                         { verifyLexicalError(); return new Symbol(TrackSymbol.EOF); }
                                                       
}

<STRING> {
    \"              { yybegin(YYINITIAL); return createToken("STRING", TrackSymbol.STRING, string.toString()); }
    [^\n\r#\"]+     { string.append( yytext() ); }
    #t              { string.append('\t'); }
    #n              { string.append('\n'); }
    #r              { string.append('\r'); }
    #\"             { string.append('\"'); }
    ##              { string.append('#'); } 
    [\n]+           { addLexicalError(new MError("No se permiten saltos de linea dento de una CADENA.", string.toString(), yyline+1, yycolumn)); }
    [\r\n]+         { addLexicalError(new MError("No se permiten saltos de linea dento de una CADENA.", string.toString(), yyline+1, yycolumn)); }
    <<EOF>>         { yybegin(YYINITIAL); addLexicalError(new MError("Se alcanzo el final del archivo mientras se leia una CADENA.", string.toString(), yyline+1, yycolumn)); }
}