package services;

import dao.BankDetailsDao;
import io.vertx.core.json.JsonObject;
import rx.Single;

import java.util.List;

public class BankDetailsService {
    public static Single<JsonObject> getBankDetails(String ifscCode) {
        return BankDetailsDao.getBankDetails(ifscCode);
    }

    public static Single<List<JsonObject>> getBankList(String city, String bankName) {
        return BankDetailsDao.getBankList(city, bankName);
    }
} 