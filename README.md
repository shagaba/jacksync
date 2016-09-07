# jackson-patch-sync

##Creating and applying patches

The project provides a library for producing and applying patches to Java objects. 
Inspired by [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) and [RFC 7386 (JSON Merge Patch)](http://tools.ietf.org/html/rfc7386) written in Java, which uses Jackson at its core.

The project can enable an efficient communication and data synchronization between clients and servers (or any set of nodes that share a resource). 

##Operations Inspired by RFC 6902 (JSON Patch) :

**Add**

The "add" operation performs one of the following functions, depending upon what the target location references:
* If the target location specifies an array index, a new value is inserted into the array at the specified index.
* If the target location specifies an object member that does not already exist, a new member is added to the object.
* If the target location specifies an object member that does exist, that member's value is replaced.
The operation object MUST contain a "value" member whose content specifies the value to be added.
```json
{ "op" : "add", "path" : "/author/firstName", "value" : "James" }
```

**Remove**

The "remove" operation removes the value at the target location.
The target location MUST exist for the operation to be successful.
If removing an element from an array, any elements above the specified index are shifted one position to the left.
```json
{ "op" : "remove", "path" : "/author/email" }
```

**Replace**

The "replace" operation replaces the value at the target location with a new value.  
The operation object MUST contain a "value" member whose content specifies the replacement value.
The target location MUST exist for the operation to be successful.
This operation is functionally identical to a "remove" operation for a value, followed immediately by an "add" operation at the same location with the replacement value.
```json
{ "op" : "replace", "path" : "/sections/3/paragraphs/2", "value" : { "title" :  "Paragraph Title", "content" : "paragraph content" } }
```

**Move**

The "move" operation removes the value at a specified location and adds it to the target location.
The operation object MUST contain a "from" member, which is a string containing a JSON Pointer value that references the location in the target document to move the value from.
The "from" location MUST exist for the operation to be successful.
This operation is functionally identical to a "remove" operation on the "from" location, followed immediately by an "add" operation at the target location with the value that was just removed.
The "from" location MUST NOT be a proper prefix of the "path" location, a location cannot be moved into one of its children.
```json
{ "op" : "move", "from" : "/sections/3/paragraphs/2", "path" : "/sections/3/paragraphs/4" }
```

**Copy**

The "copy" operation copies the value at a specified location to the target location.
The operation object MUST contain a "from" member, which is a string containing a JSON Pointer value that references the location in the target document to copy the value from.
The "from" location MUST exist for the operation to be successful.
This operation is functionally identical to an "add" operation at the target location using the value specified in the "from" member.
```json
{ "op" : "copy", "from" : "/sections/3/paragraphs/2", "path" : "/sections/3/paragraphs/6" }
```
**Test**

```json
{ "op" : "test", "path" : "/author/firstName", "value" : "James" }
```

##Operations Inspired by RFC 7386 (JSON Merge Patch) :

**Merge**

```json
{ "op": "merge", "value" : { "author" : { "firstName" : "James" , "lastName" : "Bond" } }
```
