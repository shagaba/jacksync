# Jacksync

[![Build Status](https://travis-ci.org/shagaba/jacksync.svg?branch=master)](https://travis-ci.org/shagaba/jacksync)

## Creating and applying sync patches

Jacksync provides a library for synchronization by producing and applying a JSON patches to Java objects. 
Inspired by [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) and [RFC 7386 (JSON Merge Patch)](http://tools.ietf.org/html/rfc7386) written in Java, which uses Jackson at its core.

### Features overview
* enables an efficient data synchronization between clients and servers (or any set of nodes that share a resource).
* allows you to update a JSON document by sending the changes rather than the whole document.
* enables simple commit and audit all JSON Patch changes in your data, and later on browse the detailed change history.

### Jacksync Data Structure
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

## Operations Inspired by RFC 6902 (JSON Patch) :

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

The "merge" operation merge a JSON merge patch document value at the target location value.
The "merge" operation is unique to Jacksync project inspired by both RFC 6902 (JSON Patch) and RFC 7386 (JSON Merge Patch).
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
