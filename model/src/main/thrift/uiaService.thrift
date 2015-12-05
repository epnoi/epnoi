namespace java org.epnoi.model.services.thrift  // defines the namespace

struct Resource {
  1: string type,
  2: binary resource,
}

service UIAService {  // defines the service to retrieve an annotated document
       Resource getResource(1:string uri, 2:string type), //defines a method
}
