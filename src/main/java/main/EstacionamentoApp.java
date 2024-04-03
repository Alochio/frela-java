package main.java.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EstacionamentoApp extends JFrame {
    private CarroDAO carroDAO;

    public EstacionamentoApp() {
        try {
            Connection connection = ConexaoBD.getConnection();
            carroDAO = new CarroDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Controle de Estacionamento");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JTextField marcaField = new JTextField();
        JTextField placaField = new JTextField();
        JTextField corField = new JTextField();
        JTextField horaEntradaField = new JTextField();
        JTextField horaSaidaField = new JTextField();

        JButton addButton = new JButton("Adicionar Carro");
        addButton.addActionListener(e -> {
            // Obter os valores dos campos de texto
            String marca = marcaField.getText();
            String placa = placaField.getText();
            String cor = corField.getText();
            int horaEntrada = Integer.parseInt(horaEntradaField.getText());
            int horaSaida = horaSaidaField.getText().isEmpty() ? 0 : Integer.parseInt(horaSaidaField.getText());

            // Criar o objeto Carro
            Carro carro = new Carro(marca, placa, cor, horaEntrada, horaSaida);

            try {
                // Inserir o carro no banco de dados
                carroDAO.inserirCarro(carro);
                JOptionPane.showMessageDialog(this, "Carro adicionado com sucesso!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao adicionar carro: " + ex.getMessage());
            }
        });

        JButton showButton = new JButton("Exibir Carros cadastrados");
        showButton.addActionListener(e -> {
            try {
                List<Carro> carros = carroDAO.getTodosCarros();

                // Cria uma nova janela
                JFrame frame = new JFrame("Carros cadastrados");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Cria uma tabela para exibir os carros
                String[] columnNames = {"Marca", "Placa", "Cor", "Hora de Entrada", "Hora de Saída"};
                Object[][] data = new Object[carros.size()][5];
                for (int i = 0; i < carros.size(); i++) {
                    Carro carro = carros.get(i);

                    data[i][0] = carro.getMarca();
                    data[i][1] = carro.getPlaca();
                    data[i][2] = carro.getCor();
                    data[i][3] = carro.getHoraEntrada();
                    data[i][4] = carro.getHoraSaida();
                }
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                JTable table = new JTable(model);

                // Adiciona a tabela à janela
                JScrollPane scrollPane = new JScrollPane(table);
                frame.add(scrollPane, BorderLayout.CENTER);

                // Adiciona botões de editar e excluir abaixo da tabela
                JPanel buttonPanel = new JPanel();
                JButton editButton = new JButton("Editar Dados");
                JButton deleteButton = new JButton("Excluir Dados");
                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);
                frame.add(buttonPanel, BorderLayout.SOUTH);

                // Implementa ação para o botão "Editar Dados"
                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            // Obtém a placa do carro selecionado
                            String placa = (String) table.getValueAt(selectedRow, 1);
                            // Abre a janela de edição
                            new EditarCarroFrame(placa);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Selecione um carro para editar.");
                        }
                    }
                });

                // Implementa ação para o botão "Excluir Dados"
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            // Obtém a placa do carro selecionado
                            String placa = (String) table.getValueAt(selectedRow, 1);
                            // Exclui o carro do banco de dados
                            try {
                                carroDAO.deletarCarro(placa);
                                // Remove a linha da tabela
                                model.removeRow(selectedRow);
                                JOptionPane.showMessageDialog(frame, "Carro excluído com sucesso.");
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(frame, "Erro ao excluir carro: " + ex.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Selecione um carro para excluir.");
                        }
                    }
                });

                frame.pack();
                frame.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao exibir dados: " + ex.getMessage());
            }
        });

        panel.add(new JLabel("Marca:"));
        panel.add(marcaField);
        panel.add(new JLabel("Placa:"));
        panel.add(placaField);
        panel.add(new JLabel("Cor:"));
        panel.add(corField);
        panel.add(new JLabel("Hora de Entrada:"));
        panel.add(horaEntradaField);
        panel.add(new JLabel("Hora de Saída:"));
        panel.add(horaSaidaField);
        panel.add(addButton);
        panel.add(showButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EstacionamentoApp();
        });
    }
}
