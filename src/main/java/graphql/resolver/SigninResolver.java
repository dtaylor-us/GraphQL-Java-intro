package graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import data.SigninPayload;
import data.model.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

    public User user(SigninPayload payload) {
        return payload.getUser();
    }
}