# customizing Ontmalizer
## Issues
### XSD to Ontology

* `xs:sequence` - owl doesn't support sequence?
* `xs:choice` - ontmalizer doesn't handle `minOccurs=0` well - under `xs:choice`.
* `xs:element` when property name and type are same. property name should have prefix "has"


### rules

* maxOccurs, minOccurs: default = 1
*
* base => class
