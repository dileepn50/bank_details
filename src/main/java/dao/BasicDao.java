package dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.asyncsql.PostgreSQLClient;
import io.vertx.rxjava.ext.sql.SQLClient;
import io.vertx.rxjava.ext.sql.SQLConnection;
import rx.Single;

import java.util.List;

public class BasicDao {
    private static SQLClient sqlClient;
    public static void createJDBCClient(JsonObject config) {
        String pSqlHost = config.getString("psql_host");
        int pSqlPort = config.getInteger("psql_port");
        String pSqlUser = config.getString("psql_user");
        String pSqlPassword = config.getString("psql_password");
        String pSqlDatabase = config.getString("psql_database");


        sqlClient = PostgreSQLClient.createShared(Vertx.currentContext().owner(), new JsonObject()
                .put("host", pSqlHost)
                .put("port", pSqlPort)
                .put("username", pSqlUser)
                .put("password", pSqlPassword)
                .put("database", pSqlDatabase)
        );
    }

    private static Single<ResultSet> rxQuery(String query, JsonArray params) {
        return sqlClient.rxQueryWithParams(query, params);
    }

    static Single<JsonObject> findOne(String query, JsonArray params) {
        return rxQuery(query, params)
                .map((resultSet) -> {
                    if (resultSet.getRows().size() > 0) {
                        return resultSet.getRows().get(0);
                    } else {
                        return new JsonObject();
                    }
                });
    }

    static Single<List<JsonObject>> findAll(String query, JsonArray params) {
        return rxQuery(query, params)
                .map(ResultSet::getRows);
    }

    private static void updateWithParams(String sql, JsonArray params, Handler<AsyncResult<UpdateResult>> handler) {
        sqlClient.getConnection(conn -> {
            if (conn.failed()) {
                handler.handle(Future.failedFuture(conn.cause()));
            } else {

                final SQLConnection sqlConn = conn.result();

                //sqlConn.setOptions(sqlOptions);

                sqlConn.updateWithParams(sql, params, query -> {
                    if (query.failed()) {
                        sqlConn.close(close -> {
                            if (close.failed()) {
                                handler.handle(Future.failedFuture(close.cause()));
                            } else {
                                handler.handle(Future.failedFuture(query.cause()));
                            }
                        });
                    } else {
                        sqlConn.close(close -> {
                            if (close.failed()) {
                                handler.handle(Future.failedFuture(close.cause()));
                            } else {
                                handler.handle(Future.succeededFuture(query.result()));
                            }
                        });
                    }
                });
            }
        });
    }
} 