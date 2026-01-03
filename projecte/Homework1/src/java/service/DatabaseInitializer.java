package service;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Inicializador automático de la base de datos.
 * 
 * Este Singleton se ejecuta automáticamente cuando se despliega la aplicación
 * gracias a la anotación @Startup. Comprueba si la base de datos ya tiene datos
 * y, si está vacía, inserta los datos iniciales.
 * 
 * Así no hace falta ejecutar manualmente install.jsp cada vez que se despliega.
 */
@Singleton
@Startup
public class DatabaseInitializer {

    private static final String DB_NAME = "sob_grup_4";
    private static final String SCHEMA = "ROOT";
    private static final String DB_URL = "jdbc:derby://localhost:1527/" + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @PostConstruct
    public void init() {
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement stmt = con.createStatement();
            
            // Comprobar si ya hay datos (miramos si hay providers)
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + SCHEMA + ".PROVIDER");
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            
            if (count > 0) {
                stmt.close();
                con.close();
                return;
            }
            
            
            // Datos a insertar (igual que install.jsp)
            String[] data = new String[]{
                // Providers
                "INSERT INTO " + SCHEMA + ".PROVIDER VALUES (NEXT VALUE FOR PROVIDER_GEN, 'OpenAI')",
                "INSERT INTO " + SCHEMA + ".PROVIDER VALUES (NEXT VALUE FOR PROVIDER_GEN, 'Mistral')",
                "INSERT INTO " + SCHEMA + ".PROVIDER VALUES (NEXT VALUE FOR PROVIDER_GEN, 'Anthropic')",
                // Licenses
                "INSERT INTO " + SCHEMA + ".LICENSE VALUES (NEXT VALUE FOR LICENSE_GEN, 'Proprietary')",
                "INSERT INTO " + SCHEMA + ".LICENSE VALUES (NEXT VALUE FOR LICENSE_GEN, 'Apache-2.0')",
                // Capabilities
                "INSERT INTO " + SCHEMA + ".CAPABILITY VALUES (NEXT VALUE FOR CAPABILITY_GEN, 'chat-completion')",
                "INSERT INTO " + SCHEMA + ".CAPABILITY VALUES (NEXT VALUE FOR CAPABILITY_GEN, 'code-generation')",
                "INSERT INTO " + SCHEMA + ".CAPABILITY VALUES (NEXT VALUE FOR CAPABILITY_GEN, 'text-to-image')",
                "INSERT INTO " + SCHEMA + ".CAPABILITY VALUES (NEXT VALUE FOR CAPABILITY_GEN, 'audio-generation')",
                // Models
                "INSERT INTO " + SCHEMA + ".MODEL (ID, NAME, PROVIDER_ID, SUMMARY, DESCRIPTION, LICENSE_ID, ISPRIVATE, TRAININGDATE, LASTUPDATEDATE, VERSION) VALUES (NEXT VALUE FOR MODEL_GEN, 'GPT-4', (SELECT ID FROM " + SCHEMA + ".PROVIDER WHERE NAME='OpenAI'), 'Powerful LLM', 'GPT-4 description', (SELECT ID FROM " + SCHEMA + ".LICENSE WHERE NAME='Proprietary'), 1, '2024-07-01', '2024-07-01', '4.0')",
                "INSERT INTO " + SCHEMA + ".MODEL (ID, NAME, PROVIDER_ID, SUMMARY, DESCRIPTION, LICENSE_ID, ISPRIVATE, TRAININGDATE, LASTUPDATEDATE, VERSION) VALUES (NEXT VALUE FOR MODEL_GEN, 'Mistral-Base', (SELECT ID FROM " + SCHEMA + ".PROVIDER WHERE NAME='Mistral'), 'Lightweight model', 'Mistral description', (SELECT ID FROM " + SCHEMA + ".LICENSE WHERE NAME='Apache-2.0'), 0, '2024-06-01', '2024-06-15', '1.0')",
                "INSERT INTO " + SCHEMA + ".MODEL (ID, NAME, PROVIDER_ID, SUMMARY, DESCRIPTION, LICENSE_ID, ISPRIVATE, TRAININGDATE, LASTUPDATEDATE, VERSION) VALUES (NEXT VALUE FOR MODEL_GEN, 'Claude', (SELECT ID FROM " + SCHEMA + ".PROVIDER WHERE NAME='Anthropic'), 'Assistant model', 'Claude description', (SELECT ID FROM " + SCHEMA + ".LICENSE WHERE NAME='Proprietary'), 0, '2024-05-10', '2024-05-20', '2.1')",
                // Model-capability relations
                "INSERT INTO " + SCHEMA + ".MODEL_CAPABILITY VALUES ((SELECT ID FROM " + SCHEMA + ".MODEL WHERE NAME='GPT-4'), (SELECT ID FROM " + SCHEMA + ".CAPABILITY WHERE NAME='chat-completion'))",
                "INSERT INTO " + SCHEMA + ".MODEL_CAPABILITY VALUES ((SELECT ID FROM " + SCHEMA + ".MODEL WHERE NAME='GPT-4'), (SELECT ID FROM " + SCHEMA + ".CAPABILITY WHERE NAME='code-generation'))",
                "INSERT INTO " + SCHEMA + ".MODEL_CAPABILITY VALUES ((SELECT ID FROM " + SCHEMA + ".MODEL WHERE NAME='Mistral-Base'), (SELECT ID FROM " + SCHEMA + ".CAPABILITY WHERE NAME='chat-completion'))",
                "INSERT INTO " + SCHEMA + ".MODEL_CAPABILITY VALUES ((SELECT ID FROM " + SCHEMA + ".MODEL WHERE NAME='Claude'), (SELECT ID FROM " + SCHEMA + ".CAPABILITY WHERE NAME='chat-completion'))",
                // Credentials
                "INSERT INTO " + SCHEMA + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob', 'sob')",
                "INSERT INTO " + SCHEMA + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'demo', 'demo')",
                // Customers
                "INSERT INTO " + SCHEMA + ".CUSTOMER (ID, CREDENTIALS_ID, ULTIMO_MODELO_VISITADO_ID, TELEFONO) VALUES (NEXT VALUE FOR CUSTOMER_GEN, (SELECT ID FROM " + SCHEMA + ".CREDENTIALS WHERE USERNAME='sob'), (SELECT ID FROM " + SCHEMA + ".MODEL WHERE NAME='GPT-4'), '+34612345678')",
                "INSERT INTO " + SCHEMA + ".CUSTOMER (ID, CREDENTIALS_ID, TELEFONO) VALUES (NEXT VALUE FOR CUSTOMER_GEN, (SELECT ID FROM " + SCHEMA + ".CREDENTIALS WHERE USERNAME='demo'), '+34698765432')"
            };
            
            // Ejecutar cada INSERT
            for (String sql : data) {
                try {
                    stmt.executeUpdate(sql);
                    System.out.println(" -> OK: " + sql.substring(0, Math.min(60, sql.length())) + "...");
                } catch (Exception e) {
                    System.err.println(" -> ERROR: " + sql);
                    System.err.println("    " + e.getMessage());
                }
            }
            
            stmt.close();
            con.close();
            
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            // No lanzamos excepción para no impedir el despliegue
        }
    }
}
