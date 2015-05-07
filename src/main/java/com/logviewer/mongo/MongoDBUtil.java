package com.logviewer.mongo;

import com.logviewer.entity.ErrorClass;
import com.logviewer.entity.SirenaLog;
import com.logviewer.util.Constants;
import com.logviewer.util.Helper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ali.senturk
 */
public class MongoDBUtil {

    public static final String MONGODB_DBNAME = "atlasdb";
    public static final String ATLASJET_LOG_TABLE = "logs";
    public static final String SIRENA_LOG_TABLE = "sirenalogs";

    public static MongoClient getMongoClient() throws UnknownHostException {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.readPreference(ReadPreference.primary());
        builder.writeConcern(WriteConcern.JOURNALED);
        builder.minConnectionsPerHost(1);
        builder.connectionsPerHost(5);
        builder.maxConnectionIdleTime(500000);
        builder.maxConnectionLifeTime(500000);
        builder.connectTimeout(10000);
        builder.threadsAllowedToBlockForConnectionMultiplier(1);
        builder.socketKeepAlive(true);
        builder.cursorFinalizerEnabled(true);
        builder.alwaysUseMBeans(true);
        builder.heartbeatFrequency(51);
        builder.heartbeatConnectTimeout(53);
        builder.heartbeatSocketTimeout(54);

        MongoClientOptions opts = builder.build();

        return new MongoClient(Constants.MONGODB_HOST, opts);

    }

    public static DBCollection sirenaLogTable(MongoClient mongoClient) {
        try {

            DBCollection logTable;

            DB mongoDB = mongoClient.getDB(MONGODB_DBNAME);

            if (mongoDB.collectionExists(SIRENA_LOG_TABLE)) {
                logTable = mongoDB.getCollection(SIRENA_LOG_TABLE);
                logTable.createIndex("{project: 1, officeId: 1,creationDate:1,creationTime:1}");
            } else {
                BasicDBObject log = new BasicDBObject();
                log.put("project", removeForbiddenChar("TEST"));
                log.put("officeId", "");
                log.put("agentId", "");
                log.put("methodName", "");
                log.put("webServerUrl", "");
                log.put("requestXml", "logs tablosu oluşturuldu");
                log.put("responseXml", "");
                log.put("creationDate", Helper.date2String(new Date(), "dd/MM/yyyy"));
                log.put("creationTime", "00:00");
                log.put("processTime", new Date());

                logTable = mongoDB.createCollection(SIRENA_LOG_TABLE, log);

            }

            return logTable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DBCollection atlasjetLogTable(MongoClient mongoClient) {
        try {

            DBCollection logTable;

            DB mongoDB = mongoClient.getDB(MONGODB_DBNAME);

            if (mongoDB.collectionExists(ATLASJET_LOG_TABLE)) {
                logTable = mongoDB.getCollection(ATLASJET_LOG_TABLE);
                logTable.createIndex("{project: 1, errorDate: 1,errorMessage:1}");
            } else {
                BasicDBObject log = new BasicDBObject();
                log.put("project", removeForbiddenChar("TEST"));
                log.put("className", "");
                log.put("fileName", "");
                log.put("lineNumber", "");
                log.put("methodName", "");
                log.put("errorMessage", "logs tablosu oluşturuldu");
                log.put("userName", "");
                log.put("extraInfo", "");
                log.put("errorDate", Helper.date2String(new Date(), "dd/MM/yyyy HH:mm:SS"));
                log.put("errorTime", "00:00");

                logTable = mongoDB.createCollection(ATLASJET_LOG_TABLE, log);

            }

            return logTable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ErrorClass> queryAtlasjetLogs(String project, String errorBeginDate, String errorEndDate, String errorMsg, String className, String fileName) {
        List<ErrorClass> list = new ArrayList<ErrorClass>();
        MongoClient client = null;
        try {
            client = getMongoClient();
            BasicDBObject log = new BasicDBObject();
            log.put("project", project);

            if (errorMsg != null && errorMsg.length() >= 3) {
                log.put("errorMessage", java.util.regex.Pattern.compile(errorMsg));
            }

            log.put("errorDate", new BasicDBObject("$gte", errorBeginDate).append("$lte", errorEndDate));

            if (className != null && className.length() >= 3) {
                log.put("className", java.util.regex.Pattern.compile(className));
            }
            if (fileName != null && fileName.length() >= 3) {
                log.put("fileName", java.util.regex.Pattern.compile(fileName));
            }

            BasicDBObject sort = new BasicDBObject();
            sort.put("errorDate", 1);

            DBCollection logTable = atlasjetLogTable(client);
            if (logTable != null) {
                DBCursor cur = logTable.find(log).sort(sort);

                try {
                    while (cur.hasNext()) {
                        DBObject temp = cur.next();
                        ErrorClass err = new ErrorClass();
                        System.out.println("temp.get(className)..:" + temp.get("className"));
                        err.setClassName(Helper.checkNulls(temp.get("className"), ""));
                        err.setErrorDate(Helper.checkNulls(temp.get("errorDate"), ""));
                        err.setErrorMessage(Helper.checkNulls(temp.get("errorMessage"), ""));
                        err.setErrorTime(Helper.checkNulls(temp.get("errorTime"), ""));
                        err.setExtraInfo(Helper.checkNulls(temp.get("extraInfo"), ""));
                        err.setFileName(Helper.checkNulls(temp.get("fileName"), ""));
                        err.setLineNumber(Helper.checkNulls(temp.get("lineNumber"), ""));
                        err.setMethodName(Helper.checkNulls(temp.get("methodName"), ""));
                        err.setProject(Helper.checkNulls(temp.get("project"), ""));
                        err.setUserName(Helper.checkNulls(temp.get("userName"), ""));

                        list.add(err);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cur.close();
                }
            } else {
                System.out.println("Hata..:" + log.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
            client = null;
        }
        return list;
    }

    public static List<SirenaLog> querySirenaLogs(String project, String logBeginDate, String logEndDate, String officeId,
                                                  String agentId, String methodName, String webServerUrl, String request, String response) {
        List<SirenaLog> list = new ArrayList<SirenaLog>();
        MongoClient client = null;
        try {
            client = getMongoClient();
            BasicDBObject log = new BasicDBObject();
            log.put("project", project);
            
            if (officeId != null && officeId.length() >= 3) {
                log.put("officeId", java.util.regex.Pattern.compile(officeId));
            }
            if (agentId != null && agentId.length() >= 3) {
                log.put("agentId", java.util.regex.Pattern.compile(agentId));
            }
            if (methodName != null && methodName.length() >= 3) {
                log.put("methodName", java.util.regex.Pattern.compile(methodName));
            }
            if (webServerUrl != null && webServerUrl.length() >= 3) {
                log.put("webServerUrl", java.util.regex.Pattern.compile(webServerUrl));
            }
            log.put("processTime", new BasicDBObject("$gte",logBeginDate).append("$lte",logEndDate));

            if (request != null && request.length() >= 3) {
                log.put("requestXml", java.util.regex.Pattern.compile(request));
            }
            if (response != null && response.length() >= 3) {
                log.put("responseXml", java.util.regex.Pattern.compile(response));
            }

            BasicDBObject sort = new BasicDBObject();
            sort.put("processTime",1);
            sort.put("transactionId", 1);

            DBCollection logTable = sirenaLogTable(client);
            if (logTable != null) {
                DBCursor cur = logTable.find(log).sort(sort);
                SirenaLog slog = null;
                try {
                
                    while (cur.hasNext()) {
                        DBObject temp = cur.next();
                        slog = new SirenaLog();
                        slog.setProject(Helper.checkNulls(temp.get("project"),""));
                        slog.setOfficeId(Helper.checkNulls(temp.get("officeId"),""));
                        slog.setAgentId(Helper.checkNulls(temp.get("agentId"),""));
                        slog.setMethodName(Helper.checkNulls(temp.get("methodName"), ""));
                        slog.setWebServerUrl(Helper.checkNulls(temp.get("webServerUrl"),""));
                        slog.setProcessTime(Helper.str2Time(String.valueOf(temp.get("processTime"))));
                        slog.setCreationDate(Helper.checkNulls(temp.get("creationDate"), ""));
                        slog.setCreationTime(Helper.checkNulls(temp.get("creationTime"), ""));
                        slog.setRequestXml(Helper.checkNulls(temp.get("requestXml"),""));
                        slog.setResponseXml(Helper.checkNulls(temp.get("responseXml"),""));
                        slog.setTransactionId((Long)temp.get("transactionId"));
                        list.add(slog);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cur.close();
                }
            } else {
                System.out.println("Hata..:" + log.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
            client = null;
        }
        return list;
    }

    public static void dropAtlasjetLogDB() {
        Mongo mongo = new Mongo(Constants.MONGODB_HOST);
        DB mongoDB = mongo.getDB(MongoDBUtil.MONGODB_DBNAME);
        mongoDB.dropDatabase();
    }

    public static String date2String(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd/MM/yyyy");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public static String removeForbiddenChar(String str) {
        if (str != null && str.length() > 0) {
            String EMAIl_PATTERN = "[()?:!,'+/;+&$#%€~|^<>\"]";
            str = str.replaceAll(EMAIl_PATTERN, "");
        }
        return str;
    }

}
