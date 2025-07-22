package factory;

import jakarta.persistence.EntityManager;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Fecan
 */
public class JpaUtil {
    public static void executarSemRetorno(EntityManager em, Consumer<EntityManager> acao) {
        boolean criador = !em.getTransaction().isActive();
        try {
            if (criador) em.getTransaction().begin();

            acao.accept(em);

            if (criador && em.getTransaction().isActive() && !em.getTransaction().getRollbackOnly()) {
                em.getTransaction().commit();
            } else if (criador && em.getTransaction().getRollbackOnly()) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (criador && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erro na transação: " + e.getMessage());
        }
    }

    public static <T> T executar(EntityManager em, Function<EntityManager, T> acao) {
        try {
            return acao.apply(em);
        } catch (Exception e) {
            System.out.println("Erro na transação: " + e.getMessage());
            return null;
        }
    }
}