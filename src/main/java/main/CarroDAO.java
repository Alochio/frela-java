package main.java.main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarroDAO {
    private Connection connection;

    public CarroDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserirCarro(Carro carro) throws SQLException {
        String sql = "INSERT INTO Carros (Marca, Placa, Cor, HoraEntrada, HoraSaida) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, carro.getMarca());
            statement.setString(2, carro.getPlaca());
            statement.setString(3, carro.getCor());
            statement.setInt(4, carro.getHoraEntrada());
            statement.setInt(5, carro.getHoraSaida());
            statement.executeUpdate();
        }
    }

    public List<Carro> listarCarros() throws SQLException {
        List<Carro> carros = new ArrayList<>();
        String sql = "SELECT * FROM Carros";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Carro carro = new Carro();
                carro.setMarca(resultSet.getString("Marca"));
                carro.setPlaca(resultSet.getString("Placa"));
                carro.setCor(resultSet.getString("Cor"));
                carro.setHoraEntrada(resultSet.getInt("HoraEntrada"));
                carro.setHoraSaida(resultSet.getInt("HoraSaida"));
                carros.add(carro);
            }
        }
        return carros;
    }

    public List<Carro> getTodosCarros() throws SQLException {
        List<Carro> carros = new ArrayList<>();
        String sql = "SELECT * FROM Carros";
        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Carro carro = new Carro(
                rs.getString("marca"),
                rs.getString("placa"),
                rs.getString("cor"),
                rs.getInt("horaEntrada"),
                rs.getInt("horaSaida")
            );
            carros.add(carro);
        }
        rs.close();
        stmt.close();
        return carros;
    }

    public Carro getCarroByPlaca(String placa) throws SQLException {
        String sql = "SELECT * FROM Carros WHERE Placa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, placa);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Carro carro = new Carro();
                    carro.setMarca(resultSet.getString("Marca"));
                    carro.setPlaca(resultSet.getString("Placa"));
                    carro.setCor(resultSet.getString("Cor"));
                    carro.setHoraEntrada(resultSet.getInt("HoraEntrada"));
                    carro.setHoraSaida(resultSet.getInt("HoraSaida"));
                    return carro;
                }
            }
        }
        return null; // Retorna null se não encontrar nenhum carro com a placa fornecida
    }

    public void atualizarCarro(Carro carro) throws SQLException {
        String sql = "UPDATE Carros SET Marca = ?, Cor = ?, HoraEntrada = ?, HoraSaida = ? WHERE Placa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, carro.getMarca());
            statement.setString(2, carro.getCor());
            statement.setInt(3, carro.getHoraEntrada());
            statement.setInt(4, carro.getHoraSaida());
            statement.setString(5, carro.getPlaca());
            statement.executeUpdate();
        }
    }

    public void deletarCarro(String placa) throws SQLException {
        String sql = "DELETE FROM Carros WHERE Placa = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, placa);
            statement.executeUpdate();
        }
    }
    
    
}
