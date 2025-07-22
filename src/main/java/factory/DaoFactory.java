package factory;

import dao.*;
import jakarta.persistence.EntityManager;

/**
 *
 * @author Fecan
 */
public class DaoFactory {
private EntityManager em;
private PessoaDao pessoaDao;
private ClienteDao clienteDao;
private ConsultaDao consultaDao;
    
public DaoFactory(EntityManager em){
    this.em=em;
}


public PessoaDao getPessoaDao(){
   if(pessoaDao == null){
       pessoaDao = new PessoaDao(em);
   }
   return pessoaDao; 
}
public ClienteDao getClienteDao(){
   if(clienteDao == null){
       clienteDao = new ClienteDao(em);
   }
   return clienteDao; 
}
public ConsultaDao getConsultaDao(){
    if(consultaDao == null){
       consultaDao = new ConsultaDao(em);
   }
   return consultaDao; 
}

}