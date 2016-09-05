# jackson-patch-sync

**Creating and applying patches**

The project provides a library for producing and applying patches to Java objects. 
Inspired by RFC 6902 (JSON Patch) and RFC 7386 (JSON Merge Patch) written in Java, which uses Jackson at its core.

The project can enable an efficient communication and data synchronization between clients and servers (or any set of nodes that share a resource). 

**Operations Inspired by RFC 6902 (JSON Patch) :**

**Add**
 { "op" : "add", "path" : "/author/firstName", "value" : "James" }
**Remove**
{ "op" : "remove", "path" : "/author/email" }
**Replace**
{ "op" : "replace", "path" : "/sections/3/paragraphs/2", "value" : { "title" :  "Paragraph Title", "content" : "paragraph content" } }
**Move**
{ "op" : "move", "from" : "/sections/3/paragraphs/2", "path" : "/sections/3/paragraphs/4" }
**Copy**
{ "op" : "copy", "from" : "/sections/3/paragraphs/2", "path" : "/sections/3/paragraphs/6" }
**Test**
{ "op" : "test", "path" : "/author/firstName", "value" : "James" }


**Operations Inspired by RFC 7386 (JSON Merge Patch) :**

**Merge**
{ "op": "merge", "value" : { "author" : { "firstName" : "james" , "lastName" : "bond" } }

