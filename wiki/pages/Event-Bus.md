We have decided to use ***Advanced Message Queuing Protocol*** ([AMQP](http://www.amqp.org)) as our messaging 
standard to avoid any cross-platform problem and any dependency to the selected message broker. 
Now, the producer sends a message to an *exchange* element, instead of a *queue*, along with a *routing key*. 
That *exchange* is bound to *queues* through *bindings*. A binding is a directive indicating what messages should be 
routed from an *exchange* to a *queue*. Message consumers attach to a *queue* and receive messages from the *queue* that 
is bound to an *exchange*. Considering only systems that offer persitence and replication as well as routing, delaying, 
and re-delivering implementing that protocol, the message broker selected is ***[RabbitMQ](http://www.rabbitmq.com)***.