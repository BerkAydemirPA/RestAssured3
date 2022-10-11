package Proje_CSP4;

import Proje_CSP4.Model.Fields;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FieldsTest {
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
    public String getRandomName()
    {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }
    public String getRandomCode()
    {
        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }

    String fieldsID;
    String fieldsName;
    String fieldsCode;
    String fieldsType="STRING";
    String fieldsSchoolID="5fe07e4fb064ca29931236a5";
    @Test
    public void createFields()
    {
        fieldsName=getRandomName();
        fieldsCode=getRandomCode();

        Fields fields=new Fields();
        fields.setName(fieldsName);
        fields.setCode(fieldsCode);
        fields.setType(fieldsType);
        fields.setSchoolId(fieldsSchoolID);



        fieldsID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(fields)

                        .when()
                        .post("school-service/api/entity-field")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");

    }

    @Test(dependsOnMethods = "createFields")
    public void updateFields()
    {
        fieldsName=getRandomName();
        fieldsCode=getRandomCode();

        Fields fields=new Fields();
        fields.setId(fieldsID);
        fields.setName(fieldsName);
        fields.setCode(fieldsCode);
        fields.setType(fieldsType);
        fields.setSchoolId(fieldsSchoolID);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(fields)

                .when()
                .put("school-service/api/entity-field")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(fieldsName));
    }
    @Test(dependsOnMethods = "updateFields")
    public void deleteFields()
    {
        given()
                .cookies(cookies)
                .pathParam("fieldsID",fieldsID)


                .when()
                .delete("school-service/api/entity-field/{fieldsID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }




}
