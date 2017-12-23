package graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import data.model.Link;
import data.model.User;
import data.repository.UserRepository;

public class LinkResolver implements GraphQLResolver<Link> {

    private final UserRepository userRepository;

    public LinkResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User postedBy(Link link) {
        if (link.getUserId() == null) {
            return null;
        }
        return userRepository.findById(link.getUserId());
    }
}