package dao;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import rx.Single;

import java.util.List;

public class BankDetailsDao {
    public static Single<JsonObject> getBankDetails(String ifscCode) {
        String query = "select bank_id, branch, address, city, district, state, bank_name from bank_details where ifsc = ?";
        JsonArray params = new JsonArray();
        params.add(ifscCode);

        return BasicDao.findOne(query, params);
    }

    public static Single<List<JsonObject>> getBankList(String city, String bankName) {
        String query = "select bank_id, branch, address, city, district, state, bank_name from bank_details where city = ? and bank_name = ?";
        JsonArray params = new JsonArray();
        params.add(city)
                .add(bankName);

        return BasicDao.findAll(query, params);
    }
} 