import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class Logger implements BurpExtension{
    @Override
    public void initialize(MontoyaApi api){
        api.extension().setName("my Logger");

        //String[] connectionDetails = {"mongodb://localhost:27017", "test", "newcollection"};
        // {< url >, < database >, < collection name >}
        String[] connectionDetails = {"mongodb://user:password@localhost:27017", "github", "burpLogs"};
        MyMongoClient myMongoClient = new MyMongoClient(connectionDetails, api);
        MyExperimentalHandler experimentalHandler = new MyExperimentalHandler(myMongoClient, api);
        api.proxy().registerRequestHandler( experimentalHandler );
        api.http().registerHttpHandler( experimentalHandler );
    }
}
