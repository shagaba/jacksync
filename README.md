# jackson-patch-sync

Enables efficient communication and data synchronization between clients and and a server (or any set of nodes that share a resource). 

**Creating and applying patches**

This project provides a library for producing and applying patches to Java objects. 
Inspired by RFC 6902 (JSON Patch) and RFC 7386 (JSON Merge Patch) written in Java, which uses Jackson at its core.

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

