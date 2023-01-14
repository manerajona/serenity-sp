package reqres.business.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.thucydides.core.annotations.Step;
import org.apache.http.HttpHeaders;
import reqres.business.objects.User;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateUser implements Task {

    private final User newUser;

    public CreateUser(User newUser) {
        this.newUser = newUser;
    }

    public static CreateUser of(User newUser) {
        return instrumented(CreateUser.class, newUser);
    }

    @Override
    @Step("{0} creates a new user with params #newUser")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/users")
                        .with(request ->
                                request.header(HttpHeaders.CONTENT_TYPE, "application/json")
                                        .body(newUser))
        );
    }
}