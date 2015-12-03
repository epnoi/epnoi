namespace java org.epnoi.model.services.thrift  // defines the namespace

struct AnnotatedDocument {
  1: string contentType,
  2: binary doc,
}

service AnnotatedContentService {  // defines the service to retrieve an annotated document
       AnnotatedDocument getAnnotatedContent(1:string uri, 2:string type), //defines a method
}
