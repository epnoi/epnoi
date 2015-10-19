[*home*](https://github.com/epnoi/epnoi/wiki)
->[*technical-doc*](https://github.com/epnoi/epnoi/wiki/Technical-Documentation)
->[*event-bus*](https://github.com/epnoi/epnoi/wiki/Event-Bus)
->[*event-bus-msgs*](https://github.com/epnoi/epnoi/wiki/Event-Bus-Messages)
        
*** 

Usually, the content of a message is a string of characters in a [JSON](http://www.json.org/) format. Others, it may be an array of bytes 
to be serialized/deserialized in a specific way. Our [*event-bus*](https://github.com/epnoi/epnoi/wiki/Event-Bus) allows use both formats.

The type of the event, i.e. the *routing_key* of the message, defines the format of the message to be understood by both producer and consumer.

Except special cases, the message is a JSON text string with only one field: `uri`, that identifies the *resource* implied:

``` json
{ 
  "uri": "...",
}
```

So, for example, the message associated to the creation of a new *source*, i.e. *routing_key=*`source.new`, may be: 

``` json
{ 
  "uri": "http://epnoi.org/sources/2233-23238-212-23",
}
```

#### Special Cases

At present, no special cases exist.
