/* Little script to communicate Node.js with an API Rest */
var request = require('request');
var fs = require('fs');
var url = 'http://localhost:8080/rest/ontologies/query/tbox'

req = request.post(
        url,
        function (error, response, body) {

            if (!error && response.statusCode == 200) {
                console.log(response.body);
                console.log('ok')
            } else {
                console.log(response.body);
                console.log(response.statusCode);
                console.log('error')
            }
        }
)

var form = req.form();

query = "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
        "select * " +
        "where { ?a ?b ?c }";

form.append('id', 'tbox');
form.append('query', query);
