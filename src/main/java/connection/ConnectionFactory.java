package connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Fecan
 */
public class ConnectionFactory {

    private static EntityManagerFactory emf;

    static {
        try {
            Properties prop = new Properties();
            InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("app.properties");
            if (input == null) {
                throw new RuntimeException("Arquivo app.properties não encontrado");
            }
            prop.load(input);

            String unidade = prop.getProperty("persistence.unit");

            if (unidade == null || unidade.isBlank()) {
                throw new RuntimeException("Unidade de persistência não definida em app.properties!");
            }

            emf = Persistence.createEntityManagerFactory(unidade, prop);
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao carregar app.properties: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao inicializar EntityManagerFactory: " + ex.getMessage(), ex);
        }
    }

    public EntityManager getConnection() {
        return emf.createEntityManager();
    }
}
