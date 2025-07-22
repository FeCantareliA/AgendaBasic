package util;

import exception.AgendaException;

/**
 *
 * @author Fecan
 */
public class MensagemUtil {
public static void tratar(AgendaException ex) {
    System.out.println("[ERRO]: "+ex.getMessage());
    }

    public static void tratar(Exception ex) {
        System.out.println("[ERRO]: Ocorreu um erro inesperado. Contate o suporte.");
        ex.printStackTrace();
    }
}
