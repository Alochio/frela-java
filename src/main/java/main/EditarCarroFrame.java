package main.java.main;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class EditarCarroFrame extends JFrame {
    private JTextField marcaField;
    private JTextField corField;
    private JTextField horaEntradaField;
    private JTextField horaSaidaField;
    private CarroDAO carroDAO;

    public EditarCarroFrame(String Placa) {
        String placa = Placa;

        try {
            Connection connection = ConexaoBD.getConnection();
            carroDAO = new CarroDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage());
            dispose();
            return;
        }

        setTitle("Editar Carro");
        setSize(1280, 720);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        Carro carro = null;
        try {
            carro = carroDAO.getCarroByPlaca(placa);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao obter dados do carro: " + ex.getMessage());
            dispose();
            return;
        }

        marcaField = new JTextField(carro.getMarca());
        corField = new JTextField(carro.getCor());
        horaEntradaField = new JTextField(String.valueOf(carro.getHoraEntrada()));
        horaSaidaField = new JTextField(String.valueOf(carro.getHoraSaida()));

        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> {
            // Obter os novos valores dos campos de texto
            String novaMarca = marcaField.getText();
            String novaCor = corField.getText();
            int novaHoraEntrada = Integer.parseInt(horaEntradaField.getText());
            int novaHoraSaida = Integer.parseInt(horaSaidaField.getText());

            // Criar um novo objeto Carro com os novos valores
            Carro novoCarro = new Carro(novaMarca, placa, novaCor, novaHoraEntrada, novaHoraSaida);

            try {
                // Atualizar o carro no banco de dados
                carroDAO.atualizarCarro(novoCarro);
                JOptionPane.showMessageDialog(this, "Carro atualizado com sucesso!");
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao atualizar carro: " + ex.getMessage());
            }
        });

        panel.add(new JLabel("Marca:"));
        panel.add(marcaField);
        panel.add(new JLabel("Cor:"));
        panel.add(corField);
        panel.add(new JLabel("Hora de Entrada:"));
        panel.add(horaEntradaField);
        panel.add(new JLabel("Hora de Saída:"));
        panel.add(horaSaidaField);
        panel.add(new JLabel()); // Espaço em branco para alinhar o botão
        panel.add(salvarButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}
