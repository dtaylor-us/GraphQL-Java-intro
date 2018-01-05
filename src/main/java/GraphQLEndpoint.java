import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        LinkRepository linkRepository = new LinkRepository();

        // DATA FETCHER FOR QUERY
        DataFetcher linksDataFetcher = new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                return linkRepository.getAllLinks();
            }
        };

        // DATA FETCHER FOR MUTATION
        DataFetcher createLinkDataFetcher = new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                String url = environment.getArgument("url");
                String description = environment.getArgument("description");
                Link newLink = new Link(url, description);
                linkRepository.saveLink(newLink);
                return newLink;
            }
        };

        //CREATE LINK TYPE
        GraphQLObjectType link = newObject()
                .name("Link")
                .field(newFieldDefinition()
                        .name("url")
                        .type(GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("description")
                        .type(GraphQLString)
                        .build())
                .build();

        //CREATE QUERY TYPE
         GraphQLObjectType queryType = newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("allLinks")
                        .type(new GraphQLList(link))
                        .dataFetcher(linksDataFetcher))
                .build();

        //CREATE MUTATION TYPE
        GraphQLObjectType mutationType = newObject()
                .name("Mutation")
                .field(newFieldDefinition()
                        .name("createLink")
                        .type(link)
                        .argument(newArgument()
                                .name("url")
                                .type(GraphQLString))
                        .argument(newArgument()
                                .name("description")
                                .type(GraphQLString))
                        .dataFetcher(createLinkDataFetcher))
                .build();

        //CREATE SCHEMA FROM TYPES
        return GraphQLSchema.newSchema()
                .query(queryType)
                .mutation(mutationType)
                .build();
    }

}