package opcua.ontmalizer;

import junit.framework.TestCase;

/**
 * Created by shinho on 8/15/16.
 */
public class XSD2OWLTest extends TestCase {

    public void testCreateOPCUAOntology() throws Exception {
        XSD2OWL xd = new XSD2OWL();
        xd.createOPCUAOntology();
    }
}