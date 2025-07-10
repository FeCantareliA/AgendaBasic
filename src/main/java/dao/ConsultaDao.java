package dao;

import connection.ConnectionFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Cliente;
import model.Consulta;

public class ConsultaDao {

    private static EntityManager em;

   public static void Salvar(Consulta consulta) {
    em = new ConnectionFactory().getConnection();
    try {
        em.getTransaction().begin();
        Cliente clienteGen = em.find(Cliente.class, consulta.getIdCliente().getId());
        consulta.setIdCliente(clienteGen);
        if (consulta.getIdCliente() == null || consulta.getIdCliente().getId() == null) {
            System.out.println("Cliente não cadastrado.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return;
        }

        Consulta c = BuscarPorHorario(em,consulta.getHorario());
        if (c == null) {
            em.persist(consulta);
            System.out.println("Consulta cadastrada.");
        } else {
            System.out.println("Horário ocupado.");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return;
        }

        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }

    } catch (Exception ex) {
        System.out.println("Erro: " + ex);
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    } finally {
        em.close();
    }
}

    public static void Deletar(Consulta consulta) {
        em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (consulta.getId() == null) {
                System.out.println("Não há consulta cadastrada");
            } else {
                Consulta c = em.find(Consulta.class, consulta.getId());
                if (c == null || c.getId() == null) {
                    System.out.println("Consulta não encontrada para remoção.");
                } else {
                    em.remove(c);
                    System.out.println("Consulta removida com sucesso.");
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

    public static void Atualizar(Consulta consulta) {
        em = new ConnectionFactory().getConnection();
        try {
            em.getTransaction().begin();
            if (consulta.getId() == null || consulta.getIdCliente() == null
                    || consulta.getIdCliente().getId() == null) {
                System.out.println("Consulta ou Cliente não cadastrado.");
            } else {
                em.merge(consulta);
                System.out.println("Consulta atualizada");
            }
            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    //buscas
    public static Consulta BuscarPorId(int id) {
        em = new ConnectionFactory().getConnection();
        try {
            Consulta c = em.find(Consulta.class, id);
            return c;

        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            em.close();
        }
    }

    public static Consulta BuscarPorNome(String nome) {
        em = new ConnectionFactory().getConnection();
        try {
            String jpql = "SELECT c FROM Consulta c WHERE c.idCliente.idPessoa.nome = :nome";
            return em.createQuery(jpql, Consulta.class).setParameter("nome", nome).getSingleResult();

        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            em.close();
        }
    }

    public static Consulta BuscarPorHorario(String hora, String minuto) throws ParseException {
        em = new ConnectionFactory().getConnection();
        String hour = hora + ":" + minuto;
        Date horario = AdaptarHorario(hour);
        try {
            String jpql = "SELECT c FROM Consulta c WHERE c.horario = :horario";
            return em.createQuery(jpql, Consulta.class).setParameter("horario", horario).getSingleResult();
        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            em.close();
        }
    }

    public static Consulta BuscarPorHorario(String horas) throws ParseException {
        em = new ConnectionFactory().getConnection();
        Date horario = AdaptarHorario(horas);
        try {
            String jpql = "SELECT c FROM Consulta c WHERE c.horario = :horario";
            return em.createQuery(jpql, Consulta.class).setParameter("horario", horario).getSingleResult();
        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            em.close();
        }
    }
    
    public static Consulta BuscarPorHorario(Date horario) {
    em = new ConnectionFactory().getConnection();
    try {
        String jpql = "SELECT c FROM Consulta c WHERE c.horario = :horario";
        return em.createQuery(jpql, Consulta.class)
                 .setParameter("horario", horario)
                 .getSingleResult();
    } catch (jakarta.persistence.NoResultException e) {
        return null; // Nenhuma consulta no horário
    } catch (Exception ex) {
        System.out.println("Erro: " + ex);
        return null;
    }finally {
        em.close();
    }
}
    public static Consulta BuscarPorHorario(EntityManager em, Date horario) {
    try {
        String jpql = "SELECT c FROM Consulta c WHERE c.horario = :horario";
        List<Consulta> resultado= em.createQuery(jpql, Consulta.class)
                 .setParameter("horario", horario)
                 .getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    } catch (NoResultException e) {
        return null;
    } catch (Exception ex) {
        System.out.println("Erro: " + ex);
        return null;
    }
}

    //Listar
    public static List<Consulta> Listar() {
        em = new ConnectionFactory().getConnection();
        try {
            String jpql = "SELECT c FROM Consulta c";
            return em.createQuery(jpql, Consulta.class).getResultList();
        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
            return null;
        } finally {
            em.close();
        }
    }

    //metodos suportes
    private static Date AdaptarHorario(String horas) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date horario = sdf.parse(horas);
        return horario;
    }
}
