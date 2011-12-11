/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lusano.enviaremail.util;

import java.util.ResourceBundle;

/**
 *
 * @author mmiranda1984
 */
public class Util {

    public static String obterValorPropriedade(String nomeCampo){
        ResourceBundle resource = ResourceBundle.getBundle("ConfiguracaoEmail");
        return resource.getString(nomeCampo);
    }

}
