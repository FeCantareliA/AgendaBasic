
package connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;



/**
 *
 * @author Fecan
 */
public class ConnectionFactory {
    private static final EntityManagerFactory emf =Persistence.createEntityManagerFactory("agendaBasic");
    public EntityManager getConnection(){
        return emf.createEntityManager();
    }
}
