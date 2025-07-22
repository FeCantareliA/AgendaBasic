package dao;

import factory.JpaUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import model.Pessoa;

public class PessoaDao {

    private EntityManager em;

    public PessoaDao(EntityManager em) {
        this.em = em;
    }

    public void salvarOuAtualizar(Pessoa user) {
        JpaUtil.executarSemRetorno(em, em -> {
            if (user.getId() == null) {
                em.persist(user);
                System.out.println("Pessoa cadastrada com sucesso.");
            } else {
                em.merge(user);
                System.out.println("Pessoa atualizada com sucesso");
            }

        });
    }

    public void deletar(Pessoa user) {
        JpaUtil.executarSemRetorno(em, em -> {

            if (user.getId() == null) {
                System.out.println("Pessoa não cadastrada.");
            } else {
                Pessoa p = em.find(Pessoa.class, user.getId());
                if (p != null) {
                    em.remove(p);
                    System.out.println("Pessoa removida com sucesso.");
                } else {
                    System.out.println("Pessoa não encontrada para remoção.");
                }
            }
        });

    }

    //buscas
    public Pessoa buscarPorId(int id) {
        return JpaUtil.executar(em, em -> em.find(Pessoa.class, id));
          
            
    }

    public Pessoa buscarPorNome(String nome) {
        return JpaUtil.executar(em, em->{
            String jpql = "SELECT p FROM Pessoa p WHERE p.nome = :nome";
            return em.createQuery(jpql, Pessoa.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
            
        });
       
    }

    //listar
    public List<Pessoa> listar() {
        return JpaUtil.executar(em, em->{
            String sql = "SELECT p FROM Pessoa p";
            return em.createQuery(sql, Pessoa.class).getResultList();
            
        });
        
    }

}
