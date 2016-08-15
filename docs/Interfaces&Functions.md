#API & Functions

Function | Description | URL | Parameters | Returns
---- | ---- | ---- | ---- | ----
semantify | Semantify.io > Upload > ‘Semantify’ button | POST /ontologies | xmlSchema<br/>xmlInstance | Success:`{success:{ontologyId: 3}}`<br/>Error: `{error:{errorCode: 100, message: blah}}`
validateXml | xsd, xml validation | | xmlSchema<br/>xmlInstance | if success: Return true <br/> Else: throw error
handleOntology | 1. convertOntology()<br/>2. validateOntology()<br/>3. storeOntology() | | xmlSchema<br/>xmlInstance | if success: Return ontologyId<br/>Else: throw error
convertOntology | Convert schema to ontology<br/>Convert instance to rdf triples | | xmlSchema<br/>xmlInstance | if success: Return [ontology, rdfTriples]<br/>Else: throw error
validateOntology | Ontology validation | |ontology, rdfTriples | if success: Return true <br/>Else: throw error
storeOntology | Store ontology and rdf triples into RDF STORE | | ontology, rdfTriples | if success: Return ontologyId<br/>Else: throw error
getIDOntologies | Request a list of IDs and the corresponding filename from all the ontologies in the RDF store. | GET /ontologies | | Success: `{ontologies: { 1: “name 1”, 2: “name 2”,... } } `<br/> Error: `{error:{errorCode: 100, message: blah }}`
getOntology | Request a specific instance, its name and its content | GET /ontologies/{id} | ID of the ontology | Success: `{ontology: { 1: “name 1”, 2: “name 2”,... } } `<br/> Error: `{error:{errorCode: 100, message: blah }}`
queryOntology | Query the Ontology and return triples | POST /ontologies/{id}/query | String (query) | Success: `{result: “OWL string with the result of the query”}`<br/>Error:`{error:{errorCode: 100, message: blah}}`


