package views.button;

import dao.ConsultaDao;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import model.Consulta;
import views.CadastrarConsulta;
import views.Consultas;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private JButton button;
    private String label;
    private JTable table;
    private Consultas parentFrame;  // Referência para a JFrame Consultas
    private int row, col;

    public ButtonEditor(JTable table, Consultas parentFrame) {
        this.table = table;
        this.parentFrame = parentFrame;

        button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); // Para finalizar a edição da célula
                
                // Obtem o horário da linha atual
                String horarioStr = (String) table.getValueAt(row, 0);
                try {
                    // Adapta o horário para Date
                    Consulta c = ConsultaDao.BuscarPorHorario(horarioStr);

                    if (label.equals("Registrar")) {
                        if (c == null) {
                            CadastrarConsulta cdC = new CadastrarConsulta((Consultas) parentFrame,horarioStr);
                            cdC.setVisible(true);
                            // abrir janela de cadastro, passando horário...
                        } else {
                            System.out.println("Horário já ocupado!");
                        }
                    } else if (label.equals("Excluir")) {
                        if (c != null) {
                            ConsultaDao.Deletar(c);
                            System.out.println("Consulta excluída no horário: " + horarioStr);
                        } else {
                            System.out.println("Nenhuma consulta para excluir nesse horário.");
                        }
                    }

                    // Após ação, atualiza tabela
                    parentFrame.preencherTabelaHorario();

                } catch (ParseException ex) {
                    System.out.println("Erro ao converter horário: " + ex);
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.row = row;
        this.col = column;
        this.label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}
