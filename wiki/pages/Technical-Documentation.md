[*home*](https://github.com/epnoi/epnoi/wiki)->[*technical-doc*](https://github.com/epnoi/epnoi/wiki/Technical-Documentation)  
        
*** 

**epnoi** is a system composed by a loosely-coupled set of **modules** connected by 
an [event bus](https://github.com/epnoi/epnoi/wiki/Event-Bus) using standardized data protocols and formats.   

![architecture](https://dl.dropboxusercontent.com/u/299257/epnoi/images/epnoi-system900x700.png)

It follows a ***Staged Event-Driven Architecture*** ([SEDA](http://www.eecs.harvard.edu/~mdw/proj/seda/)) that decomposes the flow 
into a set of stages connected by queues. When a new research content is published or modified from a remote source, or when someone 
upload a new document using the API, a new event is considered in the system to update or to create the relationships between this new 
information and the existing one. 
 
Each of these modules has specific responsibilities:  
- **[api](https://github.com/epnoi/epnoi/wiki/ModuleApi)**: deploy a web interface to allow users to do operations on the system
- **[hoarder](https://github.com/epnoi/epnoi/wiki/ModuleHoarder)**: download documents to be added to the system
- **[harvester](https://github.com/epnoi/epnoi/wiki/ModuleHarvester)**: extract text and meta-information from them
- **[learner](https://github.com/epnoi/epnoi/wiki/ModuleLearner)**: identify relevant terms and relations as well as create ontologies from the text
- **[modeler](https://github.com/epnoi/epnoi/wiki/ModuleModeler)**: create internal models to represent and categorize them
- **[comparator](https://github.com/epnoi/epnoi/wiki/ModuleComparator)**: measure the similarity between them according to the model created
- **[watcher](https://github.com/epnoi/epnoi/wiki/ModuleWatcher)**: check that everything is correct
 
This content-based recommender system is updated with both gathered and submitted information handling the following 
**resources**:
 
![resources](https://dl.dropboxusercontent.com/u/299257/epnoi/images/epnoi-resources900x700.png) 
 

Each of them has several [states](https://github.com/epnoi/epnoi/wiki/ResourceStates) and represents the following information:  
- **[source](https://github.com/epnoi/epnoi/wiki/ResourceSource)**: a repository of documents 
- **[domain](https://github.com/epnoi/epnoi/wiki/ResourceDomain)**: a logical grouping of documents
- **[document](https://github.com/epnoi/epnoi/wiki/ResourceDocument)**: a logic entity that includes meta-information and content of a resource
- **[item](https://github.com/epnoi/epnoi/wiki/ResourceItem)**: a textual content of a document
- **[part](https://github.com/epnoi/epnoi/wiki/ResourcePart)**: a section of a item
- **[topic](https://github.com/epnoi/epnoi/wiki/ResourceTopic)**: a sorted list of the most representative words of a text
- **[relation](https://github.com/epnoi/epnoi/wiki/ResourceRelation)**: an associative or semantic link between two words
- **[word](https://github.com/epnoi/epnoi/wiki/ResourceWord)**: a term, entity or any other textual unit that composes a text



