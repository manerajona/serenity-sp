package reqres;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.core.util.EnvironmentVariables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reqres.business.objects.User;
import reqres.business.tasks.CreateUser;
import reqres.business.tasks.FindAUser;
import reqres.business.tasks.ListUsers;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@ExtendWith(SerenityJUnit5Extension.class)
class WhenInteractingWithUsersUsingTasks {
    private static final int USER_ID = 1;
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

        sam.attemptsTo(FindAUser.byId(USER_ID));

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
    void list_users() {

        sam.attemptsTo(ListUsers.onPage(1));

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

        sam.attemptsTo(CreateUser.of(newUser));

        sam.should(
                seeThatResponse("The user should have been successfully added",
                        response -> response.statusCode(201))
        );
    }
}
