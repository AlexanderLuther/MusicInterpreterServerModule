#!/bin/bash
jflex TextEditorLexer.flex
mv TokenMaker.java ../lexer/

#Despues de ejecutar se debe de eliminar los metodos generados por JFlex:
#   zzRefill()  
#   yyreset()
# Por ultimo se debe borrar la inicializacion del arreglo zzBuffer y dejarlo
# como no inicializado.