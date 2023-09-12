package com.hluther.gui.textEditor;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
/**
 *
 * @author helmuth
 */
public class AutoComplete {
    
    private DefaultCompletionProvider provider;
    
    public AutoComplete(){
        provider = new DefaultCompletionProvider();
    }
    
    public CompletionProvider getCompletionProvider() {
        provider.addCompletion(new BasicCompletion(provider, "entero"));
        provider.addCompletion(new BasicCompletion(provider, "doble"));
        provider.addCompletion(new BasicCompletion(provider, "boolean"));
        provider.addCompletion(new BasicCompletion(provider, "caracter"));
        provider.addCompletion(new BasicCompletion(provider, "cadena"));
        provider.addCompletion(new BasicCompletion(provider, "keep"));
        provider.addCompletion(new BasicCompletion(provider, "var"));
        provider.addCompletion(new BasicCompletion(provider, "pista"));
        provider.addCompletion(new BasicCompletion(provider, "extiende"));
        provider.addCompletion(new BasicCompletion(provider, "si"));
        provider.addCompletion(new BasicCompletion(provider, "sino"));
        provider.addCompletion(new BasicCompletion(provider, "switch"));
        provider.addCompletion(new BasicCompletion(provider, "caso"));
        provider.addCompletion(new BasicCompletion(provider, "default"));
        provider.addCompletion(new BasicCompletion(provider, "salir"));
        provider.addCompletion(new BasicCompletion(provider, "para"));
        provider.addCompletion(new BasicCompletion(provider, "mientras"));
        provider.addCompletion(new BasicCompletion(provider, "hacer"));
        provider.addCompletion(new BasicCompletion(provider, "continuar"));
        provider.addCompletion(new BasicCompletion(provider, "retorna"));
        provider.addCompletion(new BasicCompletion(provider, "arreglo"));
        provider.addCompletion(new BasicCompletion(provider, "Reproducir"));
        provider.addCompletion(new BasicCompletion(provider, "Esperar"));
        provider.addCompletion(new BasicCompletion(provider, "Sumarizar"));
        provider.addCompletion(new BasicCompletion(provider, "Longitud"));
        provider.addCompletion(new BasicCompletion(provider, "Mensaje"));
        provider.addCompletion(new BasicCompletion(provider, "Principal"));
        provider.addCompletion(new BasicCompletion(provider, "do"));
        provider.addCompletion(new BasicCompletion(provider, "re"));
        provider.addCompletion(new BasicCompletion(provider, "mi"));
        provider.addCompletion(new BasicCompletion(provider, "fa"));
        provider.addCompletion(new BasicCompletion(provider, "sol"));
        provider.addCompletion(new BasicCompletion(provider, "la"));
        provider.addCompletion(new BasicCompletion(provider, "do#"));
        provider.addCompletion(new BasicCompletion(provider, "re#"));
        provider.addCompletion(new BasicCompletion(provider, "fa#"));
        provider.addCompletion(new BasicCompletion(provider, "sol#"));
        provider.addCompletion(new BasicCompletion(provider, "la#"));
        provider.addCompletion(new BasicCompletion(provider, "verdadero"));
        provider.addCompletion(new BasicCompletion(provider, "falso"));
        provider.addCompletion(new BasicCompletion(provider, "true"));
        provider.addCompletion(new BasicCompletion(provider, "false"));
        //provider.addCompletion(new ShorthandCompletion(provider, "<-", "<-\n->"));
        return provider;
   }
}