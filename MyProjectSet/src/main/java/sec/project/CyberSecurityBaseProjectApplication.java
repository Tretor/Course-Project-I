package sec.project;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.h2.tools.RunScript;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(CyberSecurityBaseProjectApplication.class);

        // Open connection to a database -- do not alter this code
        String databaseAddress = "jdbc:h2:file:./database";
        if (args.length > 0) {
            databaseAddress = args[0];
        }

        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        try {
            // If database has not yet been created, insert content
            RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/database-import.sql"));
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }
        String query = "SELECT * FROM pcpart";

        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String pcpartID = results.getString("ID");
                String pcpartName = results.getString("PCPART_NAME");
                String company = results.getString("COMPANY");
                String masterCode = results.getString("MASTER_CODE");
                
                System.out.println(pcpartID + ":" + pcpartName + ":" + company + ":" + masterCode);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
