
package dao;

import connection.ConnectionFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import model.Pessoa;


public class PessoaDao {
    private static EntityManager em;
    
    public static void Salvar(Pessoa user){
        em = new ConnectionFactory().getConnection();
        try{
           em.getTransaction().begin();
           
           if(user.getId() == null){
               em.persist(user);
               System.out.println("Pessoa cadastrada com sucesso.");
           }else{
               System.out.println("Pessoa já cadastrada.");
           }
           em.getTransaction().commit();
           
       }catch(Exception ex){
           if(em.getTransaction().isActive()){
               em.getTransaction().rollback();
           }
            System.out.println("Erro: "+ ex);
       }finally{
           em.close();
       }
    }
    public static void Atualizar(Pessoa user){
        em = new ConnectionFactory().getConnection();
        try{
           em.getTransaction().begin();
           
           if(user.getId() == null){
               System.out.println("Pessoa não cadastrada.");
           }else{
               em.merge(user);
               System.out.println("Pessoa atualizada com sucesso.");
           }
           em.getTransaction().commit();
           
       }catch(Exception ex){
           if(em.getTransaction().isActive()){
               em.getTransaction().rollback();
           }
            System.out.println("Erro: "+ ex);
       }finally{
           em.close();
       }
    }
    public static void Deletar(Pessoa user){
        em = new ConnectionFactory().getConnection();
        try{
           em.getTransaction().begin();
           
           
           if(user.getId() == null){
               System.out.println("Pessoa não cadastrada.");
           }else{
               Pessoa p = em.find(Pessoa.class, user.getId());
               if(p != null){
                   em.remove(p);
                   System.out.println("Pessoa removida com sucesso.");
               }else{
                   System.out.println("Pessoa não encontrada para remoção.");
               }
           }
           em.getTransaction().commit();
           
       }catch(Exception ex){
           if(em.getTransaction().isActive()){
               em.getTransaction().rollback();
           }
            System.out.println("Erro: "+ ex);
       }finally{
           em.close();
       }
    }
    
    //buscas
    public static Pessoa BuscarPorId(int id){
        em = new ConnectionFactory().getConnection();
        try{
           Pessoa p = em.find(Pessoa.class, id);
           return p;
       }catch(Exception ex){
            System.out.println("Erro: "+ ex);
            return null;
       }finally{
           em.close();
       }
    }
    public static Pessoa BuscarPorNome(String nome){
        em = new ConnectionFactory().getConnection();
        try{
             String jpql = "SELECT p FROM Pessoa p WHERE p.nome = :nome";
        return em.createQuery(jpql, Pessoa.class)
                 .setParameter("nome", nome)
                 .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
        System.out.println("Nenhuma pessoa encontrada com o nome: " + nome);
        return null;
        }catch(Exception ex){
            System.out.println("Erro: " + ex);
            return null;
        }finally{
            em.close();
        }
    }
    
    //listar
    public static List<Pessoa> Listar(){
        em = new ConnectionFactory().getConnection();
        try{
           String sql = "SELECT p FROM Pessoa p";
           return em.createQuery(sql,Pessoa.class).getResultList();
       }catch(Exception ex){
            System.out.println("Erro: "+ ex);
            return null;
       }finally{
           em.close();
       }
    }
    
}
