[*home*](https://github.com/epnoi/epnoi/wiki)
->[*technical-doc*](https://github.com/epnoi/epnoi/wiki/Technical-Documentation)
->[*event-bus*](https://github.com/epnoi/epnoi/wiki/Event-Bus)
        
*** 

Following a publisher/subscriber approach, all the modules in the system can publish and read **events** to notify and to be notified
about the system state. 

![event-bus](https://dl.dropboxusercontent.com/u/299257/epnoi/images/epnoi-event-bus900x700.png)

For example, when a new ***source*** is added, a new event is published by the **api** module. This event will be 
read by the **hoarder** module to begin to download *documents* from that new location. Also, the **watcher** module will 
read that event to create a new *domain* associated to that source. Thus, the system flow is not unique and is not 
directly implemented, instead parallel and emergent flows can appear according to particular actions from modules.       

![event-bus-sample](https://dl.dropboxusercontent.com/u/299257/epnoi/images/epnoi-event-bus-sample900x700.png)

We use ***Advanced Message Queuing Protocol*** ([AMQP](http://www.amqp.org)) as our messaging 
standard to avoid any cross-platform problem and any dependency to the selected message broker. This protocol handles the 
following elements: *exchange*, *queue* and *routing_key*. 

The *producer* module sends a message to the ***event-bus-exchange*** element, instead of a ***queue***, along with a ***routing_key***. 
That *exchange* is bound to *queues* through ***topic*** and ***group*** ***bindings*** for each module. 

A ***topic binding*** is a directive indicating what messages should be routed from an *exchange* to a *queue*. 
The ***group binding*** allow system delivers a message only to one consumer sharing the same *group* value, 
getting a dynamic distribution of load between them. 

Message consumers attach to a *queue* by these values and receive messages from the *queue* that is bound to an *exchange*.

### Routing Key

Messages sent to a event bus can't have an arbitrary *routing_key* - it must be a list of words, delimited by dots. 

Our ***routing_key*** consists of three words (two dots). The first two words are *mandatory* and the third one is *optional*. 

The first one describes the ***resource***, i.e. *source*, *domain*, *document*, *item*, *part*, *relation* or *word*. 
The second one is the ***action*** executed on the resource, i.e. *new*, *deleted* or *updated*. 
The third one is the ***status*** of the action, i.e. *clear*, *opened*, *closed*, *modified*, *stable* or *set. (More details 
about the states [here](https://github.com/epnoi/epnoi/wiki/ResourceStates)) 

![event-bus-message](https://dl.dropboxusercontent.com/u/299257/epnoi/images/epnoi-eventbus-message900x700.png)

### Binding Key

A *exchange* is bound to *queues* through *binding_keys* composed by a ***topic*** and a ***group*** ***key***.

The ***topic binding*** *key* must be in the same form of the *routing_key*. Thus, a message sent with a particular *routing_key* will be delivered 
to all the *queues* that are bound with a matching *binding_key*. 

There are two important special cases for *binding_keys* words:  
- \* : can substitute for exactly one word. (e.g. `*.new` listen for all the new resources without state)  
- **\#**: can substitute for zero or more words. (e.g `document.#` listen for all the actions and states of the *document* resource)

Considering only systems that offer persistence and replication as well as routing, delaying, and re-delivering implementing 
AMQP protocol, the message broker selected is ***[RabbitMQ](http://www.rabbitmq.com)***.

### Scenarios

In order to understand the behaviour of the event-bus consider the following scenarios:  

![event-bus-exchange](https://dl.dropboxusercontent.com/u/299257/epnoi/images/epnoi-event-bus-exchange900x700.png)

#### Scenario 1: Different 'topic' and ' group'

In this case, the *binding_keys* may be:
- *topic_A* = `document.new`
- *group_A* = `harvester`
- *topic_B* = `document.deleted`
- *group_B* = `modeler`

Then, when the Producer sends a message with the *routing_key* equals to `document.new`, only the Consumer1 will receive the message. 
 If the message has the *routing_key* equals to `document.deleted`, then only the Consumer2 will receive the message. 
 
The consumers here are listening for different messages.

#### Scenario 2: Same 'topic', different 'group'

In this case, the *binding_keys* may be:
- *topic_A* = `document.new`
- *group_A* = `harvester`
- *topic_B* = `document.new`
- *group_B* = `modeler`

Then, when the Producer sends a message with the *routing_key* equals to `document.new`, both consumers, Consumer1 and Consumer2, 
will receive the same message. 

Now the message has been ***duplicated*** to be read for the consumers. 

#### Scenario 3: Different 'topic', same 'group'

In this case, the *binding_keys* may be:
- *topic_A* = `document.new`
- *group_A* = `harvester`
- *topic_B* = `document.delete`
- *group_B* = `harvester`

Then, when the Producer sends a message with the *routing_key* equals to `document.new`, only the Consumer1 will receive the message. When
 the message has a *routing_key* equals to `document.delete` then the Consumer2 will receive it. 
 
The consumers here are listening for different messages. The common *group* has no effect because the *topics* are different.


#### Scenario 4: Same 'topic' and 'group'

In this case, the *binding_keys* may be:
- *topic_A* = `document.new`
- *group_A* = `harvester`
- *topic_B* = `document.new`
- *group_B* = `harvester`

Then, when the Producer sends a message with the *routing_key* equals to `document.new`, only one of the consumers will receive the message. 

By default, a *round-robin* approach is defined to dispatch this type of messages. 
