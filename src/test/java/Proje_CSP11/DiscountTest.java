package Proje_CSP11;

import Proje_CSP11.Model.Discount;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class DiscountTest {
    Cookies cookies;

    @BeforeClass
    public void LoginCampus()
    {
        baseURI="https://demo.mersys.io/";

//        {"username": "richfield.edu","password": "Richfield2020!","rememberMe": true}

        Map<String,String> credential=new HashMap<>();

        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)


                        .when()
                        .post("auth/login")

                        .then()
//                .log().cookies()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
    }
    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    public String getRandomCode() {
        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }


    String discountID;
    String discountDescription;
    String discountCode;
    int discountPriority=780;

    @Test
    public void CreateDiscount()
    {
        discountDescription=getRandomName();
        discountCode=getRandomCode();


        Discount discount=new Discount();
        discount.setCode(discountCode);
        discount.setPriority(discountPriority);
        discount.setDescription(discountDescription);

        discountID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(discount)

                        .when()
                        .post("school-service/api/discounts")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");
    }
    @Test(dependsOnMethods = "CreateDiscount")
    public void updateDiscount()
    {
        discountDescription=getRandomName();
        discountCode=getRandomCode();

        Discount discount=new Discount();
        discount.setId(discountID);
        discount.setCode(discountCode);
        discount.setPriority(discountPriority);
        discount.setDescription(discountDescription);

                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(discount)

                        .when()
                        .put("school-service/api/discounts")

                        .then()
                        .log().body()
                        .statusCode(200)
                        ;
    }
    @Test(dependsOnMethods = "updateDiscount")
    public void deleteDiscount()
    {

        given()
                .cookies(cookies)
                .pathParam("discountID",discountID)

                .when()
                .delete("school-service/api/discounts/{discountID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }
}
