import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;

import java.util.List;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLList.list;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        LinkRepository linkRepository = new LinkRepository();


        DataFetcher linksDataFetcher = new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                return linkRepository.getAllLinks();
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
//        GraphQLObjectType queryType = newObject()
//                .name("Query")
//                .field(newFieldDefinition()
//                        .name("allLinks")
//                        .dataFetcher(linksDataFetcher)
//                        .type(new GraphQLList(link))
//                .build());

         GraphQLObjectType queryType = newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("allLinks")
                        .type(new GraphQLList(link))
                        .dataFetcher(linksDataFetcher))
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