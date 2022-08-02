package apoc.export.arrow;

import apoc.Pools;
import apoc.result.ByteArrayResult;
import apoc.result.ProgressInfo;
import org.neo4j.cypher.export.SubGraph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;
import org.neo4j.procedure.TerminationGuard;

import java.util.stream.Stream;

public class ExportArrowService {

    private final GraphDatabaseService db;
    private final Pools pools;
    private final TerminationGuard terminationGuard;
    private final Log logger;
    private final Transaction tx;

    public ExportArrowService(GraphDatabaseService db, Pools pools, TerminationGuard terminationGuard, Log logger, Transaction tx) {
        this.db = db;
        this.pools = pools;
        this.terminationGuard = terminationGuard;
        this.logger = logger;
        this.tx = tx;
    }

    public Stream<ByteArrayResult> stream(Object data, ArrowConfig config) {
        if (data instanceof Result) {
            return new ExportResultStreamStrategy(db, pools, terminationGuard, logger).export((Result) data, config, tx);
        } else {
            return new ExportGraphStreamStrategy(db, pools, terminationGuard, logger).export((SubGraph) data, config, tx);
        }
    }

    public Stream<ProgressInfo> file(String fileName, Object data, ArrowConfig config) {
        if (data instanceof Result) {
            return new ExportResultFileStrategy(fileName, db, pools, terminationGuard, logger).export((Result) data, config, tx);
        } else {
            return new ExportGraphFileStrategy(fileName, db, pools, terminationGuard, logger).export((SubGraph) data, config, tx);
        }
    }
}