package com.hluther.interpreter.lexer;

import java.io.*;   
import javax.swing.text.Segment;   
   import org.fife.ui.rsyntaxtextarea.*;   
   
%%   
   
%public   
%class TokenMaker   
%extends AbstractJFlexCTokenMaker   
%unicode   
%type org.fife.ui.rsyntaxtextarea.Token   
      
%{     
   public TokenMaker() { }   
   
    private void addHyperlinkToken(int start, int end, int tokenType) {   
        int so = start + offsetShift;   
        addToken(zzBuffer, start,end, tokenType, so, true);   
    }   
   
    private void addToken(int tokenType) {   
       addToken(zzStartRead, zzMarkedPos-1, tokenType);   
    }   
   
    private void addToken(int start, int end, int tokenType) {   
       int so = start + offsetShift;   
       addToken(zzBuffer, start,end, tokenType, so, false);   
    }   
   
    public void addToken(char[] array, int start, int end, int tokenType,   
        int startOffset, boolean hyperlink) {   
        super.addToken(array, start,end, tokenType, startOffset, hyperlink);   
        zzStartRead = zzMarkedPos;   
    }   
   
    public String[] getLineCommentStartAndEnd() {   
        return new String[] { "//", null };   
    }   
   
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {   
        resetTokenList();   
        this.offsetShift = -text.offset + startOffset;  
        // Start off in the proper state.   
        int state = Token.NULL;   
        switch (initialTokenType) {   
                    case Token.COMMENT_MULTILINE:   
              state = MLC;   
              start = text.offset;   
              break;   

           /* No documentation comments */   
           default:   
              state = Token.NULL;   
        }   
        s = text;   
        try {   
           yyreset(zzReader);   
           yybegin(state);   
           return yylex();   
        } catch (IOException ioe) {   
           ioe.printStackTrace();   
           return new TokenImpl();   
        }   
    }   
   
    private boolean zzRefill() {   
       return zzCurrentPos>=s.offset+s.count;   
    }   
   
    public final void yyreset(Reader reader) {   
       zzBuffer = s.array;  
       zzStartRead = s.offset;   
       zzEndRead = zzStartRead + s.count - 1;   
       zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;   
       zzLexicalState = YYINITIAL;   
       zzReader = reader;   
       zzAtBOL  = true;   
       zzAtEOF  = false;   
    }   
  
%}   
   
Letter = [A-Za-z]   
Digit = [0-9]   
AnyCharacterButDoubleQuoteOrBackSlash = ([^\\\"\n])   
NonSeparator = ([^\t\f\r\n\ \(\)\{\}\[\]\;\,\.\=\>\<\!\~\?\:\+\-\*\/\&\|\^\%\"\']|"#"|"\\")   
IdentifierStart = ({Letter}|"_")   
IdentifierPart = ({IdentifierStart}|{Digit})   
WhiteSpace = ([ \t\f]+)   

UnclosedCharLiteral = ([\'][^\'\n]*)   
ErrorCharLiteral = ({UnclosedCharLiteral}[\'])   
StringLiteral = ([\"]({AnyCharacterButDoubleQuoteOrBackSlash})*[\"])   
UnclosedStringLiteral = ([\"]([\\].|[^\\\"])*[^\"]?)   
ErrorStringLiteral = ({UnclosedStringLiteral}[\"])   
  
MLCBegin = "<-"   
MLCEnd = "->"   
LineCommentBegin = ">>"   
   
IntegerLiteral = {Digit}+   
ErrorNumberFormat = ({IntegerLiteral}){NonSeparator}+   
   
Separator = ([\(\)\{\}\[\]])   
Separator2 = ([\;,.])   
   
Identifier = ({IdentifierStart}{IdentifierPart}*)   
   
%state MLC   
   
%%   
   
<YYINITIAL> {   
   
//---------------   PALABRAS RESERVADAS   ---------------//
    "entero"        
    | "doble"                   
    | "boolean"                       
    | "caracter"        
    | "cadena"                        
    | "keep"                                 
    | "var"                    
    | "pista"                             
    | "extiende"                           
    | "si"                          
    | "sino"                          
    | "switch"                          
    | "caso"                            
    | "default"                         
    | "salir"                              
    | "para"                               
    | "mientras"                         
    | "hacer"                       
    | "continuar"     
    | "retorna"          
    | "arreglo"   
    | "true"
    | "false"
    | "falso"
    | "verdadero"
    | "principal"     
    | "Entero"        
    | "Doble"                   
    | "Boolean"                       
    | "Caracter"        
    | "Cadena"                        
    | "Keep"                                 
    | "Var"                    
    | "Pista"                             
    | "Extiende"                           
    | "Si"                          
    | "Sino"                          
    | "Switch"                          
    | "Caso"                            
    | "Default"                         
    | "Salir"                              
    | "Para"                               
    | "Mientras"                         
    | "Hacer"                       
    | "Continuar"     
    | "Retorna"          
    | "Arreglo"
    | "True"
    | "False"
    | "Falso"
    | "Verdadero"
    | "Principal"                   
    | "Do"
    | "Do#"
    | "Re"
    | "Re#"
    | "Mi"
    | "Fa"
    | "Fa#"
    | "Sol"
    | "Sol#"
    | "La"
    | "La#"
    | "do"
    | "do#"
    | "re"
    | "re#"
    | "mi"
    | "fa"
    | "fa#"
    | "sol"
    | "sol#"
    | "la"
    | "la#"                          
    | "SI"  { addToken(Token.RESERVED_WORD); }    

//---------------   CHAR y STRING   ---------------//
    ("'") [^(#'\n\t\r )]{1} ("'")   { addToken(Token.LITERAL_CHAR); }                                
    ("'") ("##") ("'")              { addToken(Token.LITERAL_CHAR); }                                          
    ("'") ("#'") ("'")              { addToken(Token.LITERAL_CHAR); }  
    ("'") ("#t") ("'")              { addToken(Token.LITERAL_CHAR); }  
    ("'") ("#n") ("'")              { addToken(Token.LITERAL_CHAR); }  
    {StringLiteral}                 { addToken(Token.LITERAL_STRING_DOUBLE_QUOTE); }   
     
//---------------   IDENTIFICADOR   ---------------//
   {Identifier}                     { addToken(Token.IDENTIFIER); }   
   

//---------------   NUMEROS   ---------------//
   {IntegerLiteral}                                             { addToken(Token.LITERAL_NUMBER_DECIMAL_INT); } 
   0 "." {Digit}{1,6} | [1-9]{Digit}* "." {Digit}{1,6}          { addToken(Token.LITERAL_NUMBER_FLOAT); }



   {WhiteSpace}            { addToken(Token.WHITESPACE); }   
   
   /* String/Character literals. */   
   {UnclosedCharLiteral}      { addToken(Token.ERROR_CHAR); addNullToken(); return firstToken; }   
   {ErrorCharLiteral}         { addToken(Token.ERROR_CHAR); }   
   
   {UnclosedStringLiteral}      { addToken(Token.ERROR_STRING_DOUBLE); addNullToken(); return firstToken; }   
   {ErrorStringLiteral}      { addToken(Token.ERROR_STRING_DOUBLE); }   
   
   /* Comment literals. */   
   {MLCBegin}               { start = zzMarkedPos-2; yybegin(MLC); }   
   {LineCommentBegin}.*      { addToken(Token.COMMENT_EOL); addNullToken(); return firstToken; }   
   
   /* Separators. */   
   {Separator}               { addToken(Token.SEPARATOR); }   
   {Separator2}            { addToken(Token.IDENTIFIER); }   
   
   /* Operators. */   
   "!" | "%" | "%=" | "&" | "&&" | "*" | "*=" | "+" | "++" | "+=" | "," | "-" | "--" | "-=" |   
   "/" | "/=" | ":" | "<" | "<<" | "<<=" | "=" | "==" | ">" | ">>" | ">>=" | "?" | "^" | "|" |   
   "||" | "~"      { addToken(Token.OPERATOR); }   
   

   {ErrorNumberFormat}         { addToken(Token.ERROR_NUMBER_FORMAT); }   
   
   /* Ended with a line not in a string or comment. */   
   \n |   
   <<EOF>>                  { addNullToken(); return firstToken; }   
   
   /* Catch any other (unhandled) characters. */   
   .                     { addToken(Token.IDENTIFIER); }   
   
}   
   
<MLC> {   
   [^\n-]+            {}   
   {MLCEnd}         { yybegin(YYINITIAL); addToken(start,zzStartRead+2-1, Token.COMMENT_MULTILINE); }   
   "-"               {}   
   \n |   
   <<EOF>>            { addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); return firstToken; }   
}   