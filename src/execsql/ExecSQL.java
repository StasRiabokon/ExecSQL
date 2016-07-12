/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execsql;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.*;
import java.sql.Statement;

/**
 *
 * @author yasta
 */
public class ExecSQL {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws IOException, SQLException {
        try {
            Scanner in =new Scanner(System.in);
            try (Connection conn = getConnection()) {
                Statement stat = conn.createStatement();

                while (true) {
                    
                        System.out.println("Enter command or EXIT to exit:");
                    
                    if (!in.hasNextLine()) {
                        return;
                    }
                    String line = in.nextLine();
                    if (line.equalsIgnoreCase("EXIT")) {
                        return;
                    }
                    if (line.trim().endsWith(";")) {
                        line = line.trim();
                        line = line.substring(0, line.length() - 1);
                    }
                    try {
                        boolean isResult = stat.execute(line);
                        if (isResult) {
                            ResultSet rs = stat.getResultSet();
                            showResultSet(rs);
                        } else {
                            int updateCount = stat.getUpdateCount();
                            System.out.println(updateCount + " rows updated");
                        }
                    } catch (SQLException ex) {
                        for (Throwable e : ex) {
                            e.printStackTrace();
                        }
                    }

                }
            }

        } catch (SQLException ex) {
            for (Throwable e : ex) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:derby://localhost:1527/MyDataBase", "Stas", "12345");

    }

    public static void showResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (i > 1) {
                System.out.print(" ");
            }
            System.out.print(metaData.getColumnLabel(i));
        }
        System.out.println();

        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) {
                    System.out.print(", ");
                }
                System.out.print(resultSet.getString(i));
            }
            System.out.println();
        }

    }

}
