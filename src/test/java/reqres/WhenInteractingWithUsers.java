package reqres;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reqres.business.objects.User;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SerenityJUnit5Extension.class)
class WhenInteractingWithUsers {
    private static final int USER_ID = 1;
    private static final String ENDPOINT = "/users";
    EnvironmentVariables environmentVariables;
    Actor sam;

    @BeforeEach
    void setUp() {
        final String theRestApiBaseUrl = environmentVariables
                .optionalProperty("restapi.baseurl")
                .orElseThrow();

        sam = Actor.named("Sam the supervisor")
                .whoCan(CallAnApi.at(theRestApiBaseUrl));
    }

    @Test
    void find_user_by_id() {

        sam.attemptsTo(
                Get.resource(ENDPOINT + "/" + USER_ID)
        );

        sam.should(
                seeThatResponse("User details should be correct",
                        response -> response.statusCode(200)
                                .body("data.first_name",
                                        equalTo("George"))
                                .body("data.last_name",
                                        equalTo("Bluth"))
                )
        );
    }

    @Test
    void find_user_by_id_and_parse_object() {

        sam.attemptsTo(
                Get.resource(ENDPOINT + "/{id}")
                        .with(request -> request.pathParam("id", USER_ID))
        );

        User user = SerenityRest.lastResponse()
                .jsonPath()
                .getObject("data", User.class);

        assertThat(user.first_name()).isEqualTo("George");
        assertThat(user.last_name()).isEqualTo("Bluth");
    }

    @Test
    void list_users() {

        sam.attemptsTo(
                Get.resource(ENDPOINT).with(request ->
                        request.queryParam("page", 1))
        );

        sam.should(
                seeThatResponse("All users on page 1 should be returned",
                        response ->
                                response.body("data.first_name",
                                        hasItems("George", "Janet", "Emma")))
        );
    }

    @Test
    void create_user() {
        User newUser = new User(
                null,
                "Sarah",
                "Smith",
                "sarah.smith@example.com",
                "foo");

        sam.attemptsTo(
                Post.to(ENDPOINT)
                        .with(request ->
                                request.header(HttpHeaders.CONTENT_TYPE, "application/json")
                                        .body(newUser))
        );

        sam.should(
                seeThatResponse("The user should have been successfully added",
                        response -> response.statusCode(201))
        );
    }

    @Test
    void modify_user() {
        User newUser = new User(
                USER_ID,
                "Sarah",
                "Smith",
                "sarah.smith@example.com",
                "foo");

        sam.attemptsTo(
                Put.to(ENDPOINT + "/{id}")
                        .with(request ->
                                request.pathParam("id", USER_ID)
                                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                                        .body(newUser))
        );

        sam.should(
                seeThatResponse("The user should have been successfully updated",
                        response -> response.statusCode(200)
                                .body("updatedAt", not(empty())))
        );
    }

    @Test
    void delete_user() {

        sam.attemptsTo(
                Delete.from(ENDPOINT + "/{id}")
                        .with(request -> request.pathParam("id", USER_ID))
        );

        sam.should(
                seeThatResponse("The user should have been successfully deleted",
                        response -> response.statusCode(204))
        );
    }
}
