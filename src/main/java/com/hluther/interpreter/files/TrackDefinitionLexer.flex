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
    "Verdadero"                                                 { return createToken("TRUE", TrackSymbol.TRUE, yytext()); }
    "verdadero"                                                 { return createToken("TRUE", TrackSymbol.TRUE, yytext()); }
    "Falso"                                                     { return createToken("FALSE", TrackSymbol.FALSE, yytext()); }
    "falso"                                                     { return createToken("FALSE", TrackSymbol.FALSE, yytext()); }
    "True"                                                      { return createToken("TRUE", TrackSymbol.TRUE, yytext()); }
    "true"                                                      { return createToken("TRUE", TrackSymbol.TRUE, yytext()); }
    "False"                                                     { return createToken("FALSE", TrackSymbol.FALSE, yytext()); }
    "false"                                                     { return createToken("FALSE", TrackSymbol.FALSE, yytext()); }
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
    "Pista"         { return createToken("TRACK", TrackSymbol.TRACK); }
    "pista"         { return createToken("TRACK", TrackSymbol.TRACK); }
    "Extiende"      { return createToken("EXTENDS", TrackSymbol.EXTENDS); }
    "extiende"      { return createToken("EXTENDS", TrackSymbol.EXTENDS); }
    "Keep"          { return createToken("KEEP", TrackSymbol.KEEP); }
    "keep"          { return createToken("KEEP", TrackSymbol.KEEP); }
    "Var"           { return createToken("VAR", TrackSymbol.VAR); }
    "var"           { return createToken("VAR", TrackSymbol.VAR); }
    "Entero"        { return createToken("INTEGERTYPE", TrackSymbol.INTEGERTYPE); }
    "entero"        { return createToken("INTEGERTYPE", TrackSymbol.INTEGERTYPE); }
    "Doble"         { return createToken("DOUBLETYPE", TrackSymbol.DOUBLETYPE); }
    "doble"         { return createToken("DOUBLETYPE", TrackSymbol.DOUBLETYPE); }
    "Boolean"       { return createToken("BOOLEANTYPE", TrackSymbol.BOOLEANTYPE); }
    "boolean"       { return createToken("BOOLEANTYPE", TrackSymbol.BOOLEANTYPE); }
    "Cadena"        { return createToken("STRINGTYPE", TrackSymbol.STRINGTYPE); }
    "cadena"        { return createToken("STRINGTYPE", TrackSymbol.STRINGTYPE); }
    "Caracter"      { return createToken("CHARACTERTYPE", TrackSymbol.CHARACTERTYPE); }
    "caracter"      { return createToken("CHARACTERTYPE", TrackSymbol.CHARACTERTYPE); }
    "Arreglo"       { return createToken("ARRAY", TrackSymbol.ARRAY); }
    "arreglo"       { return createToken("ARRAY", TrackSymbol.ARRAY); }
    "Si"            { return createToken("IF", TrackSymbol.IF); }
    "si"            { return createToken("IF", TrackSymbol.IF); }
    "Sino"          { return createToken("ELSE", TrackSymbol.ELSE); }
    "sino"          { return createToken("ELSE", TrackSymbol.ELSE); }
    "Switch"        { return createToken("SWITCH", TrackSymbol.SWITCH); }
    "switch"        { return createToken("SWITCH", TrackSymbol.SWITCH); }
    "Caso"          { return createToken("CASE", TrackSymbol.CASE); }
    "caso"          { return createToken("CASE", TrackSymbol.CASE); }
    "Salir"         { return createToken("BREAK", TrackSymbol.BREAK); }
    "salir"         { return createToken("BREAK", TrackSymbol.BREAK); }
    "Default"       { return createToken("DEFAULT", TrackSymbol.DEFAULT); }
    "default"       { return createToken("DEFAULT", TrackSymbol.DEFAULT); }
    "Para"          { return createToken("FOR", TrackSymbol.FOR); }
    "para"          { return createToken("FOR", TrackSymbol.FOR); }
    "Mientras"      { return createToken("WHILE", TrackSymbol.WHILE); }
    "mientras"      { return createToken("WHILE", TrackSymbol.WHILE); }
    "Hacer"         { return createToken("DO", TrackSymbol.DO); }
    "hacer"         { return createToken("DO", TrackSymbol.DO); }
    "Continuar"     { return createToken("CONTINUE", TrackSymbol.CONTINUE); }
    "continuar"     { return createToken("CONTINUE", TrackSymbol.CONTINUE); }
    "Retorna"       { return createToken("RETURN", TrackSymbol.RETURN); }
    "retorna"       { return createToken("RETURN", TrackSymbol.RETURN); }
    "Reproducir"    { return createToken("PLAY", TrackSymbol.PLAY); }
    "reproducir"    { return createToken("PLAY", TrackSymbol.PLAY); }
    "Esperar"       { return createToken("WAIT", TrackSymbol.WAIT); }
    "esperar"       { return createToken("WAIT", TrackSymbol.WAIT); }
    "Ordenar"       { return createToken("ORDER", TrackSymbol.ORDER); }
    "ordenar"       { return createToken("ORDER", TrackSymbol.ORDER); }
    "Ascendente"    { return createToken("ASCENDENT", TrackSymbol.ASCENDENT); }
    "ascendente"    { return createToken("ASCENDENT", TrackSymbol.ASCENDENT); }
    "Descendente"   { return createToken("DESCENDENT", TrackSymbol.DESCENDENT); }
    "descendente"   { return createToken("DESCENDENT", TrackSymbol.DESCENDENT); }
    "Pares"         { return createToken("EVEN", TrackSymbol.EVEN); }
    "pares"         { return createToken("EVEN", TrackSymbol.EVEN); }
    "Impares"       { return createToken("ODD", TrackSymbol.ODD); }
    "impares"       { return createToken("ODD", TrackSymbol.ODD); }
    "Primos"        { return createToken("PRIME", TrackSymbol.PRIME); }
    "primos"        { return createToken("PRIME", TrackSymbol.PRIME); }
    "Sumarizar"     { return createToken("SUMMARIZE", TrackSymbol.SUMMARIZE); }
    "sumarizar"     { return createToken("SUMMARIZE", TrackSymbol.SUMMARIZE); }
    "Longitud"      { return createToken("LENGTH", TrackSymbol.LENGTH); }
    "longitud"      { return createToken("LENGTH", TrackSymbol.LENGTH); }
    "Mensaje"       { return createToken("MESSAGE", TrackSymbol.MESSAGE); }
    "mensaje"       { return createToken("MESSAGE", TrackSymbol.MESSAGE); }
    "Principal"     { return createToken("MAIN", TrackSymbol.MAIN); }
    "principal"     { return createToken("MAIN", TrackSymbol.MAIN); }
    "Do"            { return createToken("C", TrackSymbol.C); }
    "do"            { return createToken("C", TrackSymbol.C); }
    "Do#"           { return createToken("C8", TrackSymbol.C8); }
    "do#"           { return createToken("C8", TrackSymbol.C8); }
    "Re"            { return createToken("D", TrackSymbol.D); }
    "re"            { return createToken("D", TrackSymbol.D); }
    "Re#"           { return createToken("D8", TrackSymbol.D8); }
    "re#"           { return createToken("D8", TrackSymbol.D8); }
    "Mi"            { return createToken("E", TrackSymbol.E); }
    "mi"            { return createToken("E", TrackSymbol.E); }
    "Fa"            { return createToken("F", TrackSymbol.F); }
    "fa"            { return createToken("F", TrackSymbol.F); }
    "Fa#"           { return createToken("F8", TrackSymbol.F8); }
    "fa#"           { return createToken("F8", TrackSymbol.F8); }
    "Sol"           { return createToken("G", TrackSymbol.G); }
    "sol"           { return createToken("G", TrackSymbol.G); }
    "Sol#"          { return createToken("G8", TrackSymbol.G8); }
    "sol#"          { return createToken("G8", TrackSymbol.G8); }
    "La"            { return createToken("A", TrackSymbol.A); }
    "la"            { return createToken("A", TrackSymbol.A); }
    "La#"           { return createToken("A8", TrackSymbol.A8); }
    "la#"           { return createToken("A8", TrackSymbol.A8); }

    //COMENTARIOS
    {Comment}       { verifyLexicalError(); }

    //OTROS
    "{"                                             { return createToken("CURLYBRACKETO", TrackSymbol.CURLYBRACKETO); }
    "}"                                             { return createToken("CURLYBRACKETC", TrackSymbol.CURLYBRACKETO); }
    "["                                             { return createToken("SQUAREBRACKETO", TrackSymbol.SQUAREBRACKETO); }
    "]"                                             { return createToken("SQUAREBRACKETC", TrackSymbol.SQUAREBRACKETC); }
    "("                                             { return createToken("PARENTHESISO", TrackSymbol.PARENTHESISO); }
    ")"                                             { return createToken("PARENTHESISC", TrackSymbol.PARENTHESISC); }
    "="                                             { return createToken("EQUAL", TrackSymbol.EQUAL); }
    ";"                                             { return createToken("SEMICOLON", TrackSymbol.SEMICOLON); }
    ","                                             { return createToken("COMMA", TrackSymbol.COMMA); }
    ":"                                             { return createToken("COLON", TrackSymbol.COLON); }
    ([:letter:]|"_")([:letter:]|[:number:]|"_")*    { return createToken("ID", TrackSymbol.ID, yytext()); } 
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