package ai.verta.modeldb.telemetry;

import ai.verta.modeldb.ModelDBConstants;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import com.google.api.client.http.HttpMethods;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class TelemetryUtils extends TimerTask {
  private static final Logger LOGGER = LogManager.getLogger(TelemetryUtils.class);
  private static boolean telemetryInitialized = false;
  private static String telemetryUniqueIdentifier = null;
  private String consumer = "https://app.verta.ai/api/v1/uac-proxy/telemetry/collectTelemetry";

  public TelemetryUtils(String consumer) {
    if (consumer != null && !consumer.isEmpty()) {
      this.consumer = consumer;
    }
    initializeTelemetry();
  }

  /** The action to be performed by this timer task. */
  @Override
  public void run() {
    LOGGER.info("TelemetryUtils wakeup");

    // Delete existing telemetry information from DB
    deleteTelemetryInformation();

    // Get new telemetry data from database
    Map<String, Long> telemetryDataMap = collectTelemetryDataFromDB();

    // Insert collected data into telemetry table
    JsonArray telemetryMetricsJsonArray = new JsonArray();
    for (Map.Entry<String, Long> entry : telemetryDataMap.entrySet()) {
      if (entry.getValue() > 0) {
        insertTelemetryInformation(entry.getKey(), entry.getValue());
        JsonObject entityTelemetryDataJson = new JsonObject();
        entityTelemetryDataJson.addProperty(entry.getKey(), entry.getValue());
      }
    }

    if (telemetryMetricsJsonArray.size() > 0) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty(ModelDBConstants.ID, telemetryUniqueIdentifier);
      jsonObject.add(ModelDBConstants.METRICS, telemetryMetricsJsonArray);

      try {

        HttpURLConnection httpClient = (HttpURLConnection) new URL(consumer).openConnection();
        httpClient.setRequestMethod(HttpMethods.POST);
        httpClient.setDoOutput(true);
        httpClient.setRequestProperty("Content-Type", "application/json");
        httpClient.setRequestProperty("Accept", "application/json");
        httpClient.setRequestProperty("grpc-metadata-source", "PythonClient");

        try (OutputStream os = httpClient.getOutputStream()) {
          byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
          os.write(input, 0, input.length);
        }

        int responseCode = httpClient.getResponseCode();
        LOGGER.info("POST Response Code :: {}", responseCode);

        try (BufferedReader br =
            new BufferedReader(
                new InputStreamReader(httpClient.getInputStream(), StandardCharsets.UTF_8))) {
          StringBuilder response = new StringBuilder();
          String responseLine;
          while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
          }
          LOGGER.info(" Telemetry Response : {}", response.toString());
        }
      } catch (Exception e) {
        LOGGER.error("Error while uploading telemetry data : {}", e.getMessage(), e);
      }
    }

    LOGGER.info("TelemetryUtils finish tasks and reschedule");
  }

  private Map<String, Long> collectTelemetryDataFromDB() {
    // Get new telemetry data from database
    Map<String, Long> telemetryDataMap = new HashMap<>();
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      Query query = session.createQuery("select count(*) from ProjectEntity");
      Long projectCount = (Long) query.uniqueResult();
      telemetryDataMap.put(ModelDBConstants.PROJECTS, projectCount);

      query = session.createQuery("select count(*) from ExperimentEntity");
      Long experimentCount = (Long) query.uniqueResult();
      telemetryDataMap.put(ModelDBConstants.EXPERIMENTS, experimentCount);

      query = session.createQuery("select count(*) from ExperimentRunEntity");
      Long experimentRunCount = (Long) query.uniqueResult();
      telemetryDataMap.put(ModelDBConstants.EXPERIMENT_RUNS, experimentRunCount);
    } catch (Exception e) {
      LOGGER.error("Error on reading data from DB : {}", e.getMessage(), e);
    }
    return telemetryDataMap;
  }

  public void initializeTelemetry() {
    if (!telemetryInitialized) {
      LOGGER.info("Found value for telemetryInitialized : {}", telemetryInitialized);
      try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
        session.doWork(
            connection -> {
              try (Statement stmt = connection.createStatement()) {
                String existsSql =
                    "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'modeldb_deployment_info');";
                ResultSet existTable = stmt.executeQuery(existsSql);
                if (existTable.next()) {
                  if (!existTable.getBoolean(1)) {
                    LOGGER.warn("modeldb_deployment_info table not found");
                    LOGGER.info("Table modeldb_deployment_info creating");
                    JdbcConnection jdbcCon = new JdbcConnection(connection);
                    Database database =
                        DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcCon);
                    String rootPath = System.getProperty(ModelDBConstants.userDir);
                    rootPath =
                        rootPath
                            + "\\src\\main\\resources\\liquibase\\create-telemetry-tables-changelog-1.0.xml";
                    Liquibase liquibase =
                        new Liquibase(rootPath, new FileSystemResourceAccessor(), database);
                    liquibase.update(new Contexts(), new LabelExpression());

                    LOGGER.info("Table modeldb_deployment_info created successfully");
                  } else {
                    String selectQuery =
                        "Select * from modeldb_deployment_info where mdi_key = 'id'";
                    ResultSet rs = stmt.executeQuery(selectQuery);
                    if (rs.next()) {
                      telemetryUniqueIdentifier = rs.getString(2);
                    }
                  }
                  telemetryInitialized = true;
                  LOGGER.info("Set value for telemetryInitialized : {}", telemetryInitialized);
                }
              } catch (LiquibaseException e) {
                LOGGER.error("Error while creating telemetry tables : {}", e.getMessage(), e);
              }
            });
      }
    }
  }

  public static void insertModelDBDeploymentInfo() {
    if (telemetryUniqueIdentifier != null) {
      return;
    }
    LOGGER.info("Telemetry unique identifier not found");
    telemetryUniqueIdentifier = UUID.randomUUID().toString();
    try (Connection connection = ModelDBHibernateUtil.getConnection()) {
      String sql =
          "INSERT INTO modeldb_deployment_info (mdi_key, mdi_value, creation_timestamp) VALUES(?,?,?)";
      try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, ModelDBConstants.ID);
        pstmt.setString(2, telemetryUniqueIdentifier);
        pstmt.setLong(3, Calendar.getInstance().getTimeInMillis());
        pstmt.executeUpdate();
        LOGGER.info("Record inserted successfully");
      } catch (Exception e) {
        LOGGER.error(
            "Error while insertion entry on ModelDB deployment info : {}", e.getMessage(), e);
      } finally {
        if (connection != null) {
          connection.commit();
          connection.close();
        }
      }
    } catch (SQLException e) {
      LOGGER.error("Error while getting DB connection : {}", e.getMessage(), e);
    }
  }

  private void deleteTelemetryInformation() {
    try (Connection connection = ModelDBHibernateUtil.getConnection()) {
      Statement stmt = connection.createStatement();
      String query = "DELETE FROM telemetry_information";
      int deletedRows = stmt.executeUpdate(query);
      LOGGER.info("Record deleted successfully : {}", deletedRows);
      connection.commit();
    } catch (SQLException e) {
      LOGGER.error("Error while getting DB connection : {}", e.getMessage(), e);
    }
  }

  private void insertTelemetryInformation(String key, Long value) {
    try (Connection connection = ModelDBHibernateUtil.getConnection()) {
      String sql =
          "INSERT INTO telemetry_information (ti_key, ti_value, collection_timestamp, telemetry_consumer) VALUES(?,?,?,?)";
      try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, key);
        pstmt.setString(2, value.toString());
        pstmt.setLong(3, Calendar.getInstance().getTimeInMillis());
        pstmt.setString(4, consumer);
        pstmt.executeUpdate();
        LOGGER.info("Record inserted successfully");
      } catch (Exception e) {
        LOGGER.error(
            "Error while insertion entry on ModelDB deployment info : {}", e.getMessage(), e);
      } finally {
        connection.commit();
        connection.close();
      }
    } catch (SQLException e) {
      LOGGER.error("Error while getting DB connection : {}", e.getMessage(), e);
    }
  }
}
