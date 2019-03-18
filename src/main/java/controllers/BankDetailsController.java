package controllers;

import io.vertx.rxjava.ext.web.RoutingContext;
import services.BankDetailsService;
import utils.UtilityFunctions;

public class BankDetailsController {
    public static void getBankDetails(RoutingContext context) {
        String ifscCode = context.request().getParam("ifsc_code");
        BankDetailsService.getBankDetails(ifscCode)
                .subscribe(data -> UtilityFunctions.sendSuccess(context.response(), data));
    }

    public static void getBankList(RoutingContext context) {
        String city = context.request().getParam("city");
        String bankName = context.request().getParam("bank_name");
        BankDetailsService.getBankList(city, bankName)
                .subscribe(data -> UtilityFunctions.sendSuccess(context.response(), data));


    }
} 