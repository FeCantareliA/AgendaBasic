/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connection.ConnectionFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import model.Cliente;

/**
 *
 * @author Fecan
 */
public class ClienteDao {

    private static EntityManager em;

    public static void Salvar(Cliente cliente) {
        em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (cliente.getId() == null && cliente.getIdPessoa() != null) {
                em.persist(cliente);
                System.out.println("Cliente cadastrado com sucesso.");
            } else if (cliente.getId() == null && cliente.getIdPessoa() == null) {
                System.out.println("Cliente deve referenciar uma pessoa.");
            } else {
                System.out.println("Pessoa já cadastrada.");
            }
            em.getTransaction().commit();

        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    public static void Deletar(Cliente cliente) {
        em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (cliente.getId() == null) {
                System.out.println("Cliente não cadastrado.");
            } else {
                Cliente c = em.find(Cliente.class, cliente.getId());
                if (c.getId() != null) {
                    em.remove(c);
                    System.out.println("Cliente removido com sucesso.");
                } else {
                    System.out.println("Cliente não encontrado para remoção.");
                }
            }
            em.getTransaction().commit();

        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }

    }

    public static void Atualizar(Cliente cliente) {
        em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (cliente.getId() == null || cliente.getIdPessoa() == null
                    || cliente.getIdPessoa().getId() == null) {
                System.out.println("Cliente ou Pessoa não cadastrado. Atualização inválida.");
                return;
            }
            em.merge(cliente);
            System.out.println("Cliente Atualizado.");

            em.getTransaction().commit();

        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    public static Cliente BuscarPorId(int id) {
        em = new ConnectionFactory().getConnection();
        try {
            Cliente c = em.find(Cliente.class, id);
            return c;
        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            em.close();
        }

    }

    public static Cliente BuscarPorNome(String nome) {
        em = new ConnectionFactory().getConnection();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE c.idPessoa.nome = :nome";
            return em.createQuery(jpql, Cliente.class).setParameter("nome", nome).getSingleResult();

        } catch (jakarta.persistence.NoResultException ex) {
            System.out.println("Nenhum cliente encontrado com esse nome.");
            return null;
        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            em.close();
        }
    }
    public static List<Cliente> Listar(){
        em = new ConnectionFactory().getConnection();
        try{
            String jpql ="SELECT c FROM Cliente c";
            return em.createQuery(jpql, Cliente.class).getResultList();
        }catch(Exception ex){
            System.out.println("Erro: "+ex);
            return null;
        }finally{
            em.close();
        }
    }

}
