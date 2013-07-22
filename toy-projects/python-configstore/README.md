# Python Configstore

The object providing nice API for storing and retrieving in-memory elements. The elements
can be hierarchical, but not recursive.

## Facts:

- Elements can have attributes (key-value pairs)
- Elements can have values
- Elements can have children (which are elements)
- Each child has a name stored in its parent (but not in the child)
- Parents can have multiple children with the same name

## Usage (API)

### Create new empty `Configstore` object

```python
   r = Configstore()
```

### Add new elements

- Without attributes or values

```python
    r = Configstore().servers
    s = r.server

    assertDictEqual(r._children, { 'server': [s] })
```

- With attributes

```python
    s = Configstore().servers.server(name='core', port=30400)

    assertDictEqual(s._attrs, { 'name': 'core', 'port': 30400 })
```

- Multiple elements into the same parent

```python
    r = Configstore().servers
    s1 = r.server(name='core', port=30400)
    s2 = r.server(name='core', port=40000)

    assertNotEquals(s1, s2)
    assertDictEqual(r._children, { 'server': [s1,s2] })
```

- With element value

```python
    r = Configstore().servers
    s1 = r.server('10.17.1.23', name='core', port=30400)

    assertDictEqual(r._value, ('10.17.1.23',))
```

- With multiple element values

```python
    r = Configstore.servers
    s1 = r.server('10.17.1.23', '10.17.1.24', name='core', port=30400)

    assertDictEqual(r._value, ('10.17.1.23','10.17.1.24'))
```

### Find/get element(s)

- Without specifying criteria

```python
    r = Configstore().servers
    s = r.server(name='core')

    s1 = r.server()   # find *any* server

    assertTrue(s is s1)
    assertDictEqual(r._children, { 'server': [s] })
```

- With attribute criteria

```python
    r = Configstore().servers
    s = r.server(name='core')

    s1 = r.server(name='core')

    assertTrue(s is s1)
    assertDictEqual(r._children, { 'server': [s] })
```

- Find multiple elements by attributes

```python
    r = Configstore().servers
    s1 = r.server(name='core', port=30400)
    s2 = r.server(name='core', port=40000)

    s3 = r.server(name='core') # finds multiple elements

    assertTrue(type(s3) == list)
    assertTrue(s1 in s3)
    assertTrue(s2 in s3)
```

- Find element by value

```python
    r = Configstore().servers
    s1 = r.server('10.17.1.23', name='core', port=30400)

    s2 = r.server('10.17.1.23') # find by value

    assertTrue(s1 is s2)
```

### Remove elements

- Remove by name and attributes

```python
    r = Configstore().servers
    
    s1 = r.server(name='core', port=30400)
    s2 = r.server(name='other', port=30400, somevalue=False)
    s3 = r.server(name='another', port=30400, somevalue=True)
    s4 = r.server(name='againother', port=30400, somevalue=True)
    s5 = r.server(name='abc', port=40000)

    r._remove_element('server', port=30400, somevalue=True)
    
    assertDictEqual(r._children, {'server': [s1, s2, s5]})
```
  - NOTE: If there are multiple matches, it removes all found

- Remove by name and value

```python
    r = Configstore().servers
    
    s1 = r.server(name='core')
    s2 = r.server('10.17.1.23', name='core')
    
    r._remove_element('server', '10.17.1.23')
```

- NOTE: It's ofcourse possible to combine attributes, values, or do not specify anything but name

### Get attribute value

```python
    r = Configstore().servers
    s = r.server(name='core', port=30400)

    assertEquals('core', s['name'])
```

### Add/modify/remove attributes in element

- Add/modify by "indexing"

```python
   r = Configstore().servers
   s = r.server(port=30400)
   s['name'] = 'core'  # add new
   s['port'] = 40000   # modify

   assertEquals('core', s['name'])
   assertEquals(40000, s['port'])
```

- Add new by calling

```python
    r = Configstore().servers
    s = r.server(name='core', port=30400)

    s(ipaddress='10.17.1.2')

    assertEquals('10.17.1.2', r.server(name='core')['ipaddress'])
```

- Add new by chaining calls:

```python
    r = Configstore().servers
    
    s = r.server(name='core')(port=30400)(ipaddress='10.17.1.2')
    
    assertEquals('10.17.1.2', s['ipaddress'])    
```

- Modify by call

```python
    r = Configstore().servers
    s = r.server(name='core', port=30400)

    s(name='other')              # modify name
    s2 = r.server(name='other')  # find element with new name

    assertTrue(s is s2)
```

- Remove attributes 

```python
    r = Configstore().servers
    s1 = r.server(name='core', port=30440)

    s1._remove_attribute('name')
    self.assertDictEqual(s1._attrs, {'port': 30440})
```

- NOTE: After removal there can exist two or more same elements, they will not be "merged"
  
### Removing a value

```python
    r = Configstore('some_value')
    
    assertTupleEqual(r._value, ('some_value',))
    
    r()  # remove value!
    
    assertTupleEqual(r._value, ())
```

- Always finish finding of element before removing value!

Sometimes is *required* to perform a "call" to finish the element finding process. Usually, the process is finished
automatically, but not when the intention is to modify or remove the value. In this case, the "call" preformed later
binds to the find operation:

```python
    r = Configstore()
    s = r.server('value')
    
    s1 = r.server   # find *any* server
    
    s2 = s1()       # This does *NOT* remove the value. This call binds to the previous `find` operation.
    
    # Multiple calls of s1() still does *NOT* remove the value:
    s1()            # does not remove
    s1()            # does not remove
    
    # But since the find operation is finished and stored to `s2`, doing this:
    s2()            # *will* remove the value.
    s1()()          # also this
```

### Multiple config objects in a set

```python
    r1 = Configstore('value')
    r2 = Configstore('value')
    
    self.assertEquals(1, len(set([r1,r2])))  # they are the same, Configstores support hashing
```

## Reserved element names

Elements in the `Configstore` object are not allowed to have the following names:

- `_matches`
- `_remove_element`
- `_remove_attribute`
- `_attrs`
- `_children`
- `_value`
- special python methods (`__eq__`, `__str__`, `__repr__`, ...)
- methods inherited from metaclass
