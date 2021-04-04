package com.addressbook;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddressBookDataJSONTest {

    @Before
    public void set() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4000;
    }

    public AddressBookData[] getContactList(){
        Response response = RestAssured.get("/contacts");
        System.out.println("Contacts in JSON are : " + response.asString());
        AddressBookData[] restAssuredBookData = new Gson().fromJson(response.asString(),AddressBookData[].class);
        return restAssuredBookData;
    }

    public Response addContactstoJSONServer(AddressBookData restAssuredBookData){
        String contact = new Gson().toJson(restAssuredBookData);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(contact);
        return requestSpecification.post("/contacts");
    }

    @Test
    public void givenContactsShouldReturnContactList() {
        AddressBookData[] restAssuredBookData = getContactList();
        System.out.println(restAssuredBookData);
        Assert.assertEquals(6, restAssuredBookData.length);
    }

    @Test
    public void whenNewContactisInsertedShouldReturnResponseCode201() {
        AddressBookData[] jsonServerBookData = getContactList();
        AddressBookData jsonServerBookData1 = new AddressBookData("7", "Wanda", "Maximoff", "Vision House", "Mumbai", "Maharashtra", "400265", "55665556622", "wanda@avenger.com", "2019-06-30");
        Response response = addContactstoJSONServer(jsonServerBookData1);
        int statusCode = response.statusCode();
        Assert.assertEquals(201, statusCode);
    }

    @Test
    public void givenContacttoUpdateShouldReturnResponseCode200() {
        AddressBookData[] serverContactData = getContactList();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body("{\"firstname\":\"Tony\",\"lastname\":\"Stark\",\"address\":\"Marine Lines\",\"city\":\"Mumbai\",\"state\":\"Maharashtra\",\"zip\":\"400009\",\"phonenumber\":\"8872661655\",\"email\":\"tony@avenger.com\",\"date\":\"2019-03-07\"}");
        Response response = requestSpecification.put("/contacts/update/3");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);
    }
}
