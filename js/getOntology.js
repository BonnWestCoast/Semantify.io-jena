
/* Testing the getOntology endpoint */

var request = require('request');
var fs = require('fs');
var url = 'http://localhost:8080/rest/ontologies/abox'

req = request.get(
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
