namespace java org.epnoi.model.services.thrift  // defines the namespace



service KnowledgeBaseService {  // defines the service to retrieve an annotated document
       map<string, list<string>> getRelated(1:list<string> sources, 2:string type), //returns the target of the relations from a source

       map<string, list<string>> stem(1:list<string> terms),
}
