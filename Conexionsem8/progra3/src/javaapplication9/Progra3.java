/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package javaapplication9;
import java.sql.*;
import java.util.Scanner;

public class Progra3 {
    static final String CONTROLADOR = "org.mariadb.jdbc.Driver";
    static final String URL_BASEDATOS = "jdbc:mariadb://localhost:3306/progra3";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conexion = null;
        Statement instruccion = null;
        ResultSet conjuntoResultados = null;

        try {
            Class.forName(CONTROLADOR);
            conexion = DriverManager.getConnection(URL_BASEDATOS, "root", "666");
            instruccion = conexion.createStatement();

            System.out.println("Seleccione la tabla que desea ver (estudiante, materia): ");
            String tablaSeleccionada = scanner.nextLine().trim();

            String consulta = "";
            switch (tablaSeleccionada) {
                case "estudiante":
                    consulta = "SELECT * FROM estudiante";
                    break;
                case "materia":
                    consulta = "SELECT * FROM materia";
                    break;
                default:
                    System.out.println("Tabla no válida.");
                    return;
            }

            conjuntoResultados = instruccion.executeQuery(consulta);

            ResultSetMetaData metaDatos = conjuntoResultados.getMetaData();
            int numeroDeColumnas = metaDatos.getColumnCount();

            System.out.println("Registros de la tabla " + tablaSeleccionada + ":\n");

            for (int i = 1; i <= numeroDeColumnas; i++)
                System.out.printf("%-15s\t", metaDatos.getColumnName(i));
            System.out.println();

            while (conjuntoResultados.next()) {
                for (int i = 1; i <= numeroDeColumnas; i++)
                    System.out.printf("%-15s\t", conjuntoResultados.getObject(i));
                System.out.println();
            }

            if (tablaSeleccionada.equals("materia")) {
                System.out.println("\nIngrese el ID de la materia para ver los estudiantes inscritos:");
                int idMateria = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea después del entero

                PreparedStatement ps = conexion.prepareStatement("SELECT * FROM carga WHERE materia = ?");
                ps.setInt(1, idMateria);
                ResultSet rs = ps.executeQuery();

                System.out.println("\nEstudiantes inscritos en la materia seleccionada:\n");

                while (rs.next()) {
                    int idEstudiante = rs.getInt("estudiante");
                    // Obtener información del estudiante
                    PreparedStatement psEstudiante = conexion.prepareStatement("SELECT * FROM estudiante WHERE id = ?");
                    psEstudiante.setInt(1, idEstudiante);
                    ResultSet rsEstudiante = psEstudiante.executeQuery();
                    if (rsEstudiante.next()) {
                        System.out.printf("%-15s\t%-15s\t%-15s\n", rsEstudiante.getInt("id"),
                                rsEstudiante.getString("nombre"), rsEstudiante.getString("apellido"));
                    }
                }
            }

            if (tablaSeleccionada.equals("estudiante")) {
                System.out.println("\nIngrese el ID del estudiante para ver las materias que lleva:");
                int idEstudiante = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea después del entero

                PreparedStatement ps = conexion.prepareStatement("SELECT * FROM carga WHERE estudiante = ?");
                ps.setInt(1, idEstudiante);
                ResultSet rs = ps.executeQuery();

                System.out.println("\nMaterias que lleva el estudiante seleccionado:\n");

                while (rs.next()) {
                    int idMateria = rs.getInt("materia");
                    // Obtener información de la materia
                    PreparedStatement psMateria = conexion.prepareStatement("SELECT * FROM materia WHERE id = ?");
                    psMateria.setInt(1, idMateria);
                    ResultSet rsMateria = psMateria.executeQuery();
                    if (rsMateria.next()) {
                        System.out.printf("%-15s\t%-15s\t%-15s\t%-15s\t%-15s\n", rsMateria.getInt("id"),
                                rsMateria.getString("nombre"), rsMateria.getString("catedratico"),
                                rsMateria.getString("ciclo"), rsMateria.getInt("uv"));
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conjuntoResultados != null) conjuntoResultados.close();
                if (instruccion != null) instruccion.close();
                if (conexion != null) conexion.close();
                scanner.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

    /**
     * @param args the command line arguments


