import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;

import java.util.List;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
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
                .fields(newFieldDefinition()
                        .name("allLinks")
                        .dataFetcher(environment -> {

                }))
                .build();

        //ADD ALL LINKS QUERY
        return GraphQLSchema.newSchema()
                .query(queryType)
                .build();

//        LinkRepository linkRepository = new LinkRepository();
//        return SchemaParser.newParser()
//                .file("schema.graphqls")
//                .resolvers(new Query(linkRepository))
//                .build()
//                .makeExecutableSchema();
    }

}