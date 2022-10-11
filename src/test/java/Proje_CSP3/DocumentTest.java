package Proje_CSP3;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import Campus.Model.Country;
import Proje_CSP3.Model.Document;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DocumentTest {
    Cookies cookies;

    @BeforeClass
    public void LoginCampus()
    {
        baseURI="https://demo.mersys.io/";
        Map<String,String> Identidad=new HashMap<>();
        Identidad.put("username","richfield.edu");
        Identidad.put("password","Richfield2020!");
        Identidad.put("rememberMe","true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(Identidad)
                        .when()
                        .post("auth/login")
                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
    }
    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }


    String documentName;
    String documentID;
    String documentStages="STUDENT_REGISTRATION";
    String documentSchoolID="5fe07e4fb064ca29931236a5";



    @Test
    public void createDocument()
    {
        documentName=getRandomName();

        Document document=new Document();
        document.setName(documentName);
        document.setAttachmentStages(documentStages);
        document.setSchoolId(documentSchoolID);
        document.setId(documentID);

        documentID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(document)

                        .when()
                        .post("school-service/api/attachments")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");
    }



}
