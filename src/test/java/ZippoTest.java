import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test()
    {
        given()
                // hazırlık işlemleri yapacagız

                .when()
                // link ve aksiyon işlemleri yapacagız
                .then();
                // test ve extract işlemleri yapacagız
    }

    @Test
    public void statusCodeTest()
    {
        given()

        .when().
          get("http://api.zippopotam.us/us/90210").
        then().log().body() // log().all() bütün respons u gösterir
        .statusCode(200); // status kontrolu
    }

    @Test
    public void contentTypeTest()
    {
        given()

                .when()
        .get("http://api.zippopotam.us/us/90210").

                then()
                .log().body()
                .contentType(ContentType.JSON)
                .statusCode(200);
    }

    @Test
    public void checkStateInResponseBody()
    {
        given()
                .when().
                get("http://api.zippopotam.us/us/90210").

                then()
                .log().body()
                .body("country", equalTo("United States")) // body.country == United States ?
                .statusCode(200);
    }

    @Test
    public void BodyJsonPathTest2()
    {
        given()
                .when().
                get("http://api.zippopotam.us/us/90210").

                then()
                .log().body()
                .body("places[0].state", equalTo("California")) // body.places[0].state ==  California ?
                .statusCode(200);
    }

    @Test
    public void BodyJsonPathTest3()
    {
        given()
                .when().
                get("http://api.zippopotam.us/us/90210").

                then()
                .log().body()
                .body("places.'place name'", hasItem("Beverly Hills")) // body.places.'place name ==  Beverly Hills ?
                .statusCode(200);
    }

    @Test
    public void BodyArrayHasSizeTest()
    {
        given()
                .when().
                get("http://api.zippopotam.us/us/90210").

                then()
                .log().body()
                .body("places", hasSize(1)) // verilen path deki size kontrolü
                .statusCode(200);
    }

    @Test
    public void combiningTest()
    {
        given()
                .when().
                get("http://api.zippopotam.us/us/90210").

                then()
                .log().body()
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200);
    }

    @Test
    public void pathParamTest()
    {
        given()
                .pathParam("Country","us")
                .pathParam("Zipcode","90210")
                .log().uri()

                .when().
                get("http://api.zippopotam.us/{Country}/{Zipcode}").

                then()
                .log().body()
                .statusCode(200);
    }

    @Test
    public void pathParamTest2()
     //TODO 90210 dan 90213 kadar test sonuçlarında places in size nın hepsinde 1 gediğini test ediniz.
    {
        for (int i = 90210; i <=90213 ; i++) {
            given()
                    .pathParam("Country", "us")
                    .pathParam("Zipcode", i)
                    .log().uri()

                    .when().
                    get("http://api.zippopotam.us/{Country}/{Zipcode}").

                    then()
                    .log().body()
                    .body("places", hasSize(1))
                    .statusCode(200);
        }
    }

    @Test
    public void queryParamTest()
            //https://gorest.co.in/public/v1/users?page=1
    {
        given()
                .param("page",1)
                .log().uri()

                .when().
                get("https://gorest.co.in/public/v1/users").

                then()
                .log().body()
                .body("meta.pagination.pages", equalTo(335))
                .statusCode(200);
    }

    @Test
    public void queryParamTest2()
    //https://gorest.co.in/public/v1/users?page=1
    {
        for (int pageNo = 1; pageNo <= 10; pageNo++){
        given()
                .param("page", pageNo)
                .log().uri()

                .when().
                get("https://gorest.co.in/public/v1/users").

                then()
                .log().body()
                .body("meta.pagination.page", equalTo(pageNo))
                .statusCode(200);
        }
    }


    RequestSpecification requestSpecs;
    ResponseSpecification responseSpecs;

    @BeforeClass
    void  Setup()
    {
        // RestAssured kendi statik değişkeni tanımlı değer atanıyor
        baseURI="https://gorest.co.in/public/v1";

        requestSpecs = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecs = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }


    @Test
    public void requestResponseSpecification()
    //https://gorest.co.in/public/v1/users?page=1
    {

            given()
                    .param("page",1)
                    .spec(requestSpecs)

                    .when().
                    get("/users"). // url nin başında http yoksa baseUri deki değer otomatik geliyor

                    then()
                    .body("meta.pagination.page", equalTo(1))
                    .spec(responseSpecs);
    }


    //TODO Json extract

    @Test
    public void extractingJsonPath()
    {
        String placeName=
        given()

                .when().
                get("http://api.zippopotam.us/us/90210").

                then()
//                .log().body()
                .statusCode(200)
                .extract().path("places[0].'place name'");
        // extract metodu ile given ile başlayan satır, bir değer döndürür hale geldi, en sonra extract olmalı

        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractingJsonPathInt()
    {

        int limit=
                given()

                        .when().
                        get("https://gorest.co.in/public/v1/users").

                        then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit");


        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");

    }


    @Test
    public void extractingJsonPathInt2()
    {

        int id=
                given()

                        .when().
                        get("https://gorest.co.in/public/v1/users").

                        then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("data[2].id");


        System.out.println("id = " + id);

    }

    @Test
    public void extractingJsonPathIntList()
    {

        List<Integer> idler=
                given()

                        .when().
                        get("https://gorest.co.in/public/v1/users").

                        then()
//                        .log().body()
                        .statusCode(200)
                        .extract().path("data.id"); // data daki bütün id leri bir list şeklinde verir


        System.out.println("idler = " + idler);

    }

    @Test
    public void extractingJsonPathStringList()
    {

        List<String> names=
                given()

                        .when().
                        get("https://gorest.co.in/public/v1/users").

                        then()
//                        .log().body()
                        .statusCode(200)
                        .extract().path("data.name"); // data daki bütün id leri bir list şeklinde verir


        System.out.println("names = " + names);
        Assert.assertTrue(names.contains("Datta Achari"));

    }

    @Test
    public void extractingJsonPathResponseAll()
    {

        Response response=
                given()

                        .when().
                        get("https://gorest.co.in/public/v1/users").

                        then()
//                        .log().body()
                        .statusCode(200)
                        .extract().response(); // data daki bütün id leri bir list şeklinde verir

        List<Integer> idler=response.path("data.id");
        List<String> names=response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("names = " + names);
        System.out.println("idler = " + idler);
    }

    @Test
    public void extractingJsonPOJO() {  // POJO : JSon Object i  (Plain Old Java Object)

        Location yer=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().as(Location.class); // Location şablocnu
        ;

        System.out.println("yer. = " + yer);

        System.out.println("yer.getCountry() = " + yer.getCountry());
        System.out.println("yer.getPlaces().get(0).getPlacename() = " + yer.getPlaces().get(0).getPlacename());
    }
}
