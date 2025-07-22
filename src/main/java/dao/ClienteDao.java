package dao;

import factory.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import model.Cliente;
import model.Pessoa;
import util.*;
import exception.*;

public class ClienteDao {

    private EntityManager em;

    public ClienteDao(EntityManager em) {
        this.em = em;
    }

    public void salvarOuAtualizar(Cliente cliente) {
        try{
        JpaUtil.executarSemRetorno(em, em -> {
            if (cliente.getIdPessoa() == null || cliente.getIdPessoa().getId() == null) {
                throw new DadosInvalidosException("É necessário referenciar uma pessoa cadastrada");
            }

            // Reatacha a Pessoa ao contexto do EM
            Pessoa pessoaGerenciada = em.find(Pessoa.class, cliente.getIdPessoa().getId());
            cliente.setIdPessoa(pessoaGerenciada);

            if (cliente.getId() == null) {
                em.persist(cliente);
                System.out.println("Cliente cadastrado com sucesso.");
            } else {
                em.merge(cliente);
                System.out.println("Cliente atualizado com sucesso.");
            }
        });
}catch(AgendaException ex){
MensagemUtil.tratar(ex);
    }catch(Exception ex){
MensagemUtil.tratar(ex);
}
}

    public void deletar(Cliente cliente) {
        try{
        JpaUtil.executarSemRetorno(em, em -> {
            if (cliente == null || cliente.getId() == null) {
                throw new EntidadeNaoEncontradaException("Cliente não está cadastrado.");
            }

            Cliente c = em.find(Cliente.class, cliente.getId());
            if (c != null) {
                em.remove(c);
                System.out.println("Cliente removido com sucesso");
            } else {
                throw new EntidadeNaoEncontradaException("Cliente não encontrado para exclusão.");
            }
        });
        }catch(AgendaException ex){
            MensagemUtil.tratar(ex);
        }catch(Exception ex){
            MensagemUtil.tratar(ex);
        }
}
    

    public Cliente buscarPorId(int id) {
        return JpaUtil.executar(em, em -> em.find(Cliente.class, id));
    }

    public Cliente buscarPorNome(String nome) {
        return JpaUtil.executar(em, em -> {
            try {
                String jpql = "SELECT c FROM Cliente c WHERE LOWER(TRIM(c.idPessoa.nome)) = LOWER(TRIM(:nome))";
                return em.createQuery(jpql, Cliente.class)
                         .setParameter("nome", nome)
                         .getSingleResult();
            } catch (NoResultException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
        });
    }

    public List<Cliente> listar() {
        return JpaUtil.executar(em, em -> em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList());
    }
}