# Jacksync

[![Build Status](https://travis-ci.org/shagaba/jacksync.svg?branch=master)](https://travis-ci.org/shagaba/jacksync)

## Creating and applying sync patches (merge, patch, diff, sync)

Jacksync provides a library for synchronization by producing and applying a JSON patches to Java objects. 
Inspired by [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) and [RFC 7386 (JSON Merge Patch)](http://tools.ietf.org/html/rfc7386) written in Java, which uses Jackson at its core.

### Features overview
* synchronization - enables an efficient data synchronization between clients and masters (or any set of nodes that share a resource).
* allows you to update a JSON document by sending the changes rather than the whole document.
* [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) - add, remove, replace, move, copy, test.
* [RFC 7386 (JSON Merge Patch)](http://tools.ietf.org/html/rfc7386) - merge patch document.
* JSON Diff computes the different between two Objects with two optional strategies:
  * simple - (default) returns Json Patch operations based on [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902).
  * merge - returns Json Patch operations using both [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) operations with a unique **merge operation** based on [RFC 7386 (JSON Merge Patch)](http://tools.ietf.org/html/rfc7386) that can minimize the amount of the operations and JSON document length.
* enables simple commit and audit all JSON Patch changes in your data, and later on browse the detailed change history.
* enables "reverse" patches using diff processor.

### JSON Patch
A JSON Patch document is a JSON document that represents an array of objects. Each object represents a single operation to be applied to the target JSON document.
The following is an example JSON Patch document

```json
[
	{ "op": "test", "path": "/a/b/c", "value": "foo" },
	{ "op": "remove", "path": "/a/b/c" },
	{ "op": "add", "path": "/a/b/c", "value": [ "foo", "bar" ] },
	{ "op": "replace", "path": "/a/b/c", "value": 42 },
	{ "op": "move", "from": "/a/b/c", "path": "/a/b/d" },
	{ "op": "copy", "from": "/a/b/d", "path": "/a/b/e" }
]
```

For example:
* given the following Blog Post original JSON document:

```json
{
	"id": "007",
	"title": "Diamonds Are Forever",
	"sections": [ {"title": "section-1"}, {"title": "section-2"}, {"title": "section-3"} ],
	"author": {"firstName": "james", "lastName": "bond", "email": "???"}
}
```

* changing the Blog Post JSON document to be:

```json
{
	"id": "007",
	"title": "Diamonds Are Forever",
	"sections": [ {"title": "section-1"}, {"title": "section-3"}],
	"tags": [ "007", "Sean Connery", "action"],
	"author": {"firstName": "james", "lastName": "bond", "email": "james.bond@mi6.com"}
}
```

* can be achieved by sending:

```json
[
	{"op":"remove","path":"/sections/1"},
	{"op":"add","path":"/tags","value":["007","Sean Connery","action"]},
	{"op":"replace","path":"/author/email","value":"james.bond@mi6.com"}
]
```

* using the following Java code:

```java
PatchProcessor patchProcessor = new ObjectPatchProcessor(objectMapper);
Post postV2 = patchProcessor.patch(originalPostV1, operations);
```

### JSON Merge Patch
A JSON merge patch document describes changes to be made to a target JSON document using a syntax that closely mimics the document being modified.

For example:
* changing the Blog Post can be achieved by sending:

```json
{
	"sections": [ {"title": "section-1"}, {"title": "section-3"}],
	"tags": [ "007", "Sean Connery", "action"],
	"author": {"firstName": "james", "lastName": "bond", "email": "james.bond@mi6.com"}
}
```

* using the following Java code:

```java
MergeProcessor mergeProcessor = new ObjectMergeProcessor(objectMapper);
Post postV2 = mergeProcessor.merge(originalPostV1, value);
```

### JSON Diff
Computes the different between two Objects source to target, and returns Json Patch operations.
Two strategies to Compute the Json Patch operations:
  * simple - (default) [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) operations.
  * merge - using both [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) operations with a unique **merge operation** based on [RFC 7386 (JSON Merge Patch)](http://tools.ietf.org/html/rfc7386) that can minimize the amount of the operations and JSON document length.

```java
DiffMapper diffMapper = new ObjectDiffMapper(objectMapper);
List<PatchOperation> operations = diffMapper.diff(sourcePostV1, targetPostV2);
```

### Sync Data Structure
TBD
* version - client received version.
* masterVersion - master version after committing all patch operations.
* targetChecksum - target object after applying all patch operations.
* operations - all patch operations.

```json
{
	"version" : 5,
	"masterVersion" : 6,
	"targetChecksum" : "Checksum",
	"operations" : [
		{ "op" : "replace", "path" : "/title", "value" : "How To Use Jacksync Data" },
		{ "op" : "add", "path" : "/author", "value" : "shagaba" },
		{ "op" : "add", "path" : "/tags/3", "value" : "sync" },
		{ "op" : "replace", "path" : "/version", "value" : 6 },
		{ "op" : "test", "path" : "/version", "value" : 6 }
	]
}
```

## Operations implemented as described in RFC 6902 (JSON Patch) :

**Add**

The "add" operation performs one of the following functions, depending upon what the target location references:
* If the target location specifies an array index, a new value is inserted into the array at the specified index.
* The character "-" is a new array index referenced value of a nonexistent member after the last array element "/foo/-".
* If the target location specifies an object member that does not already exist, a new member is added to the object.
* If the target location specifies an object member that does exist, that member's value is replaced.
The operation object MUST contain a "value" member whose content specifies the value to be added.

```json
{
	"op" : "add", 
	"path" : "/author/firstName", 
	"value" : "James" 
}
```

**Remove**

The "remove" operation removes the value at the target location.
The target location MUST exist for the operation to be successful.
If removing an element from an array, any elements above the specified index are shifted one position to the left.

```json
{
	"op" : "remove",
	"path" : "/author/email" 
}
```

**Replace**

The "replace" operation replaces the value at the target location with a new value.  
The operation object MUST contain a "value" member whose content specifies the replacement value.
The target location MUST exist for the operation to be successful.
This operation is functionally identical to a "remove" operation for a value, followed immediately by an "add" operation at the same location with the replacement value.

```json
{ 
	"op" : "replace", 
	"path" : "/sections/3/paragraphs/2", 
	"value" : { 
		"title" :  "Paragraph Title", 
		"content" : "paragraph content" 
	} 
}
```

**Move**

The "move" operation removes the value at a specified location and adds it to the target location.
The operation object MUST contain a "from" member, which is a string containing a JSON Pointer value that references the location in the target document to move the value from.
The "from" location MUST exist for the operation to be successful.
This operation is functionally identical to a "remove" operation on the "from" location, followed immediately by an "add" operation at the target location with the value that was just removed.
The "from" location MUST NOT be a proper prefix of the "path" location, a location cannot be moved into one of its children.

```json
{
	"op" : "move", 
	"from" : "/sections/3/paragraphs/2", 
	"path" : "/sections/3/paragraphs/4" 
}
```

**Copy**

The "copy" operation copies the value at a specified location to the target location.
The operation object MUST contain a "from" member, which is a string containing a JSON Pointer value that references the location in the target document to copy the value from.
The "from" location MUST exist for the operation to be successful.
This operation is functionally identical to an "add" operation at the target location using the value specified in the "from" member.

```json
{
	"op" : "copy", 
	"from" : "/sections/3/paragraphs/2", 
	"path" : "/sections/3/paragraphs/6" 
}
```

**Test**

The "test" operation tests that a value at the target location is equal to a specified value.
The operation object MUST contain a "value" member that conveys the value to be compared to the target location's value.
The target location MUST be equal to the "value" value for the operation to be considered successful.
Here, "equal" means that the value at the target location and the value conveyed by "value" are of the same JSON type, and that they are considered equal by the following rules for that type:
* strings: are considered equal if they contain the same number of Unicode characters and their code points are byte-by-byte equal.
* numbers: are considered equal if their values are numerically equal.
* arrays: are considered equal if they contain the same number of values, and if each value can be considered equal to the value at the corresponding position in the other array, using this list of type-specific rules.
* objects: are considered equal if they contain the same number of members, and if each member can be considered equal to a member in the other object, by comparing their keys (as strings) and their values (using this list of type-specific rules).
* literals (false, true, and null): are considered equal if they are the same.

```json
{
	"op" : "test", 
	"path" : "/author/firstName", 
	"value" : "James" 
}
```

## Jacksync Operation Inspired by both RFC 6902 (JSON Patch) and RFC 7386 (JSON Merge Patch) :

**Merge**

The "merge" operation merge a JSON Merge Patch document value at the target location value.
The "merge" operation is unique to Jacksync project inspired by both [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) and [RFC 7386 (JSON Merge Patch)](http://tools.ietf.org/html/rfc7386).
The operation object MUST contain a "value" member which is a JSON merge patch document.
The target location MUST exist for the operation to be successful.
A JSON merge patch document describes changes to be made to a target JSON document using a syntax that closely mimics the document being modified.
* Recipients of a merge patch document determine the exact set of changes being requested by comparing the content of the provided patch against the current content of the target document.
* If the provided merge patch contains members that do not appear within the target, those members are added.
* If the target does contain the member, the value is replaced.
* Null values in the merge patch are given special meaning to indicate the removal of existing values in the target.

```json
{
	"op" : "merge", 
	"path" : "/", 
	"value" : { 
		"title" : "Jacksync unique operation",
		"version" : 6,
		"author" : { 
			"firstName" : "James" , 
			"lastName" : "Bond",
			"email" : null
		} 
	}
}
```
