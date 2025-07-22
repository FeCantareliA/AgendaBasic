package dao;

import exception.*;
import factory.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.time.LocalTime;
import java.util.List;
import model.Cliente;
import model.Consulta;
import util.MensagemUtil;

public class ConsultaDao {

    private EntityManager em;

    public ConsultaDao(EntityManager em) {
        this.em = em;
    }

    public void salvarOuAtualizar(Consulta consulta) {
        try{
    JpaUtil.executarSemRetorno(em, em -> {
        if (consulta.getIdCliente() == null || consulta.getIdCliente().getId() == null) {
            throw new AgendaException("Consulta deve ter um Cliente cadastrado.");
            
        }

        // Reatacha cliente (se necessário)
        Cliente clienteGerenciado = em.find(Cliente.class, consulta.getIdCliente().getId());
        consulta.setIdCliente(clienteGerenciado);

        if (consulta.getId() == null) {
            Consulta existente = buscarPorHorario(consulta.getHorario());
            if (existente != null) {
                throw new HorarioOcupadoException("O horário já está ocupado");
            }
            em.persist(consulta);
            System.out.println("Consulta cadastrada.");
        } else {
            em.merge(consulta);
            System.out.println("Consulta atualizada.");
        }
    });
        }catch(AgendaException ex){
            MensagemUtil.tratar(ex);
        }catch(Exception ex){
            MensagemUtil.tratar(ex);
        }
        
}

    public void deletar(Consulta consulta) {
        try{
            
        JpaUtil.executarSemRetorno(em, em -> {
            if (consulta.getId() == null) {
                throw new EntidadeNaoEncontradaException("Não há consulta cadastrada");
            } else {
                Consulta c = em.find(Consulta.class, consulta.getId());
                if (c == null || c.getId() == null) {
                    throw new EntidadeNaoEncontradaException("Consulta não encontrada para remoção.");
                } else {
                    em.remove(c);
                    System.out.println("Consulta removida com sucesso.");
                }
            }

        });
        }catch(AgendaException ex){
            MensagemUtil.tratar(ex);
        }catch(Exception ex){
            MensagemUtil.tratar(ex);
        }
    }

    //buscas
    public Consulta buscarPorId(int id) {
        return JpaUtil.executar(em, em -> {
            return em.find(Consulta.class, id);
        });
    }

    public Consulta buscarPorNome(String nome) {
        return JpaUtil.executar(em, em -> {
            try {
                String jpql = "SELECT c FROM Consulta c WHERE c.idCliente.idPessoa.nome = :nome";
                return em.createQuery(jpql, Consulta.class)
                        .setParameter("nome", nome)
                        .getSingleResult();

            } catch (NoResultException ex) {
                throw new EntidadeNaoEncontradaException("Consulta com o nome '" + nome + "' não foi encontrada.");
            }
        });

    }

    public Consulta buscarPorHorario(LocalTime horario) {
        return JpaUtil.executar(em, em -> {
            try {

                String jpql = "SELECT c FROM Consulta c WHERE c.horario = :horario";
                return em.createQuery(jpql, Consulta.class)
                        .setParameter("horario", horario)
                        .getSingleResult();
            } catch (NoResultException ex) {
                return null; //retorna null para mostrar que não há consulta marcada
            }
        });

    }

    //Listar
    public List<Consulta> listar() {
        return JpaUtil.executar(em, em -> {
            String jpql = "SELECT c FROM Consulta c";
            return em.createQuery(jpql, Consulta.class).getResultList();

        });

    }

}
