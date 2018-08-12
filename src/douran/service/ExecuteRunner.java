package douran.service;

import oracle.jdbc.pool.OracleDataSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;

public class ExecuteRunner {
    private static Connection dbConnection = null;
    private static PreparedStatement preparedStatement = null;

    private static String vAttachment = "VATTACHMENT";
    private static String vAttachmentMain = "VATTACHMENTMAIN";

    private static String vAttachmentTemp = "VATTACHMENT_1";
    private static String vAttachmentMainTemp = "VATTACHMENTMAIN_1";

    public static void convertModel(OA_Model oa_model) {
        try {
            AttachmentCollectionModel attachmentCollectionModel = new AttachmentCollectionModel();
            AttachmentCollectionModel attachmentMainCollectionModel = new AttachmentCollectionModel();
            preparedStatement = dbConnection.prepareStatement("SELECT a.AD_ATTACHMENT_ID,b.file_size,b.name,b.AD_ATTACHMENT_FILE_ID,b.DMS_DOCUMENT_ID from AD_ATTACHMENT a INNER JOIN AD_ATTACHMENT_FILE b ON a.VALUE=b.AD_ATTACHMENT_FILE_ID and b.DMS_DOCUMENT_ID is not null and b.AD_ATTACHMENT_FILE_ID is not null and a.AD_ATTACHMENT_ID is not null and a.RECORD_ID=" + oa_model.oa_letter_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String fileName = resultSet.getString("name");
                int attachmentID = resultSet.getInt("AD_ATTACHMENT_ID");
                int attachment_File_ID = resultSet.getInt("AD_ATTACHMENT_FILE_ID");
                int DMS_DOCUMENT_ID = resultSet.getInt("DMS_DOCUMENT_ID");
                int fileSize = resultSet.getObject("file_size") != null ? resultSet.getInt("file_size") : 0;
                if (fileName != null && fileName.length() > 0) {
                    fileName = purifyFileName(fileName);
                    AttachmentFile at = new AttachmentFile(attachmentID, fileName, oa_model.oa_letter_ID, 1000024);
                    at.setSizeFile(fileSize);
                    at.setAttachment_File_Id(attachment_File_ID);
                    at.setDmsDocumentId(DMS_DOCUMENT_ID);
                    at.setGuid(UUID.uuid());
                    if (oa_model.vattchment != null && oa_model.vattchment.length() > 0 && oa_model.vattchment.indexOf(fileName) > -1) {
                        attachmentCollectionModel.getvAttachments().add(at);
                    } else if (oa_model.vattchmentMain != null && oa_model.vattchmentMain.length() > 0 && oa_model.vattchmentMain.indexOf(fileName) > -1) {
                        attachmentMainCollectionModel.getvAttachments().add(at);
                    }
                }
            }
            resultSet.close();
            if (attachmentCollectionModel.getvAttachments().size() > 0)
                oa_model.vattchment = XMLService.getValue(AttachmentCollectionModel.class, attachmentCollectionModel);
            else
                oa_model.vattchment = null;

            if (attachmentMainCollectionModel.getvAttachments().size() > 0)
                oa_model.vattchmentMain = XMLService.getValue(AttachmentCollectionModel.class, attachmentMainCollectionModel);
            else
                oa_model.vattchmentMain = null;

            updateSerialize(oa_model);
        } catch (SQLException e) {
            writeErrors("oa_letters_ID:" + oa_model.oa_letter_ID + " has an error: " + e.toString());
        } finally {

            closePrepareStatement(null, preparedStatement);
        }
    }

    private static void updateSerialize(OA_Model oa_model) throws SQLException {
        if (oa_model.vattchmentMain != null || oa_model.vattchment != null)
            try {
                String executeUpdate = "UPDATE OA_LETTERS SET  " + vAttachment + "= ? ," + vAttachmentMain + "=? WHERE OA_LETTERS_ID=" + oa_model.oa_letter_ID;
                PreparedStatement st = dbConnection.prepareStatement(executeUpdate);
                st.setNString(1, oa_model.vattchment);
                st.setNString(2, oa_model.vattchmentMain);
                st.executeUpdate();
                st.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
    }

    private static String purifyFileName(String fileName) {
        return FarsiTextAssistant.validate(fileName.replace("<<", "").replace(">>", "").replace("^", "").replace("[", "").replace("]", "").toLowerCase());
    }

    public static ArrayList<OA_Model> selectListOfOaLettersID(Connection dbConnection) {
        ArrayList<OA_Model> oa_models = new ArrayList<>();
        String selectSQL = "select OA_LETTERS_ID," + vAttachmentTemp + "," + vAttachmentMainTemp + " from OA_LETTERS where (" + vAttachmentTemp + " IS NOT NULL OR " + vAttachmentMainTemp + " is NOT NULL) and (" + vAttachment + " is NULL and " + vAttachmentMain + " is NULL) ORDER BY CREATED DESC";
        try {
            preparedStatement = dbConnection.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                oa_models.add(new OA_Model(rs.getInt(1), rs.getObject(2) != null ? purifyFileName(rs.getString(2)) : null, rs.getObject(3) != null ? purifyFileName(rs.getString(3)) : null));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closePrepareStatement(null, preparedStatement);
        }
        return oa_models;
    }

    public static void closePrepareStatement(Connection dbConnection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (Exception e) {
        }
    }

    public static Connection getDBConnection(String serverName, String serviceName, String driverName, int portNumber, String databaseName, String userName, String password) {
        try {
            OracleDataSource oracleDataSource = new OracleDataSource();
            oracleDataSource.setServerName(serverName);
            oracleDataSource.setServiceName(serviceName);
            oracleDataSource.setDriverType(driverName);
            oracleDataSource.setPortNumber(portNumber);
            oracleDataSource.setDatabaseName(databaseName);
            oracleDataSource.setUser(userName);
            oracleDataSource.setPassword(password);
            dbConnection = oracleDataSource.getConnection();
        } catch (Exception e) {
            return null;
        }
        return dbConnection;
    }

    public static boolean createBackupColumns() {
        boolean isCreated = false;
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            String[] insertQueries = new String[]{"ALTER TABLE OA_LETTERS RENAME COLUMN VATTACHMENT TO " + vAttachmentTemp, "ALTER TABLE OA_LETTERS RENAME COLUMN VATTACHMENTMAIN TO " + vAttachmentMainTemp, "ALTER TABLE OA_LETTERS ADD VATTACHMENT CLOB NULL", "ALTER TABLE OA_LETTERS ADD VATTACHMENTMAIN CLOB NULL"};
            for (String query : insertQueries) {
                statement.addBatch(query);
            }
            statement.executeBatch();
            isCreated = true;
        } catch (SQLException e) {
            writeErrors("can not create backup columns!!!");
            isCreated = false;
        } finally {
            closePrepareStatement(dbConnection, statement);
        }
        return isCreated;
    }

    public static boolean isColumnExist() {
        boolean isExist = false;
        try {
            preparedStatement = dbConnection.prepareStatement("Select count(1) a from user_tab_cols where table_name = 'OA_LETTERS' and column_name = 'VATTACHMENT_1'");
            ResultSet re = preparedStatement.executeQuery();
            re.next();
            int isExistInt = re.getInt("a");
            if (isExistInt > 0)
                isExist = true;
        } catch (Exception e) {
            writeErrors("can not check backup columns!!!");
        } finally {
            closePrepareStatement(null, preparedStatement);

        }
        return isExist;
    }

    public static void writeErrors(String fault) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("DouranLog.txt", true))) {
            bw.write(fault);
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
