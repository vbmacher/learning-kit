import unittest
from configstore.configstore import *


class TestConfigstore(unittest.TestCase):

    # NEW INSTANCE TESTS

    def test_empty_with_attrs_creates_new(self):
        r = Configstore().server(name="core", port=30400)
        self.assertDictEqual(r._attrs, {'name':'core','port':30400})

    def test_empty_no_attrs_creates_new(self):
        r = Configstore()
        s = r.server()
        self.assertDictEqual(r._children, {'server':[s]})

    def test_different_attrs_creates_new(self):
        r = Configstore()
        s1 = r.server(name="core", port=30400)
        s2 = r.server(name="another", port=30400)
        self.assertDictEqual(r._children, {'server':[s1, s2]})

    def test_nested_with_attrs_creates_new(self):
        r = Configstore().servers
        s1 = r.server(name="core", port=30400)
        s2 = r.server(name="another", port=30400)
        self.assertNotEquals(s1, s2)
        self.assertDictEqual(r._children, {'server':[s1, s2]})

    def test_create_with_value(self):
        r = Configstore('value')
        self.assertTupleEqual(r._value, ('value',))

    def test_create_with_value_and_attrs(self):
        r = Configstore().servers
        s = r.server('value', name='core', port=30400)
        self.assertTupleEqual(s._value, ('value',))
        self.assertDictEqual(r._children, {'server':[s]})
        self.assertDictEqual(s._attrs, {'name':'core', 'port': 30400})

    # GET EXISTING INSTANCE TESTS

    def test_single_existing_no_attrs_returns_singleton(self):
        r = Configstore()
        s1 = r.server
        s2 = r.server
        self.assertTrue(s1() is s2())
        self.assertDictEqual(r._children, {'server': [s1]})

    def test_single_existing_with_attrs_subset_returns_singleton(self):
        r = Configstore()
        s1 = r.server(name='core', port=30400)
        s2 = r.server(name='core')
        self.assertTrue(s1 is s2)
        self.assertDictEqual(r._children, {'server': [s1]})

    def test_single_existing_query_without_attrs_returns_singleton(self):
        r = Configstore()
        s1 = r.server(name='core', port=30400)
        s2 = r.server()
        self.assertTrue(s1 is s2)
        self.assertDictEqual(r._children, {'server': [s1]})

    def test_multiple_existing_query_without_attrs_returns_list(self):
        r = Configstore()
        s1 = r.server(name='core', port=30400)
        s2 = r.server(name='core', port=80000)
        s3 = r.server
        self.assertTrue(s1 in s3)
        self.assertTrue(s2 in s3)

    def test_multiple_existing_with_attrs_returns_list(self):
        r = Configstore().servers
        s1 = r.server(name='core', port=30400)
        s2 = r.server(name='core', port=80000)
        s3 = r.server(name='core')

        self.assertTrue(s1 in s3)
        self.assertTrue(s2 in s3)

    def test_multiple_existing_iterate(self):
        r = Configstore().servers
        s1 = r.server(name='core', port=30400)
        s2 = r.server(name='core', port=80000)

        i = 0
        vals = [s1,s2]
        for s in r.server(name='core'):
            self.assertEquals(vals[i], s)
            i += 1

    def test_get_attribute_by_indexing(self):
        r = Configstore().servers
        r.server(name='core', port=30400)
        self.assertEquals('core', r.server['name'])
        self.assertEquals(30400, r.server['port'])

    def test_get_attribute_multiple_results(self):
        r = Configstore().servers
        r.server(name='core', port=30400)
        r.server(name='core', port=80000)
        self.assertEquals(30400, r.server(name='core')[0]['port'])
        self.assertEquals(80000, r.server(name='core')[1]['port'])

    def test_get_by_value_does_not_create_new(self):
        r = Configstore().servers
        s = r.server('value')
        s1 = r.server('value')  # find by value
        self.assertTrue(s is s1)
        self.assertDictEqual(r._children, {'server': [s]})

    def test_get_by_existing_attrs_and_nonexistant_value_creates_new(self):
        r = Configstore().servers
        s = r.server(name='core', port=30400)
        s1 = r.server('value', name='core', port=30400)  # find by value and attrs
        self.assertNotEquals(s,s1)
        self.assertDictEqual(r._children, {'server': [s,s1]})

    def test_get_by_value_when_there_exist_multiple_values(self):
        r = Configstore().servers
        s = r.server('value1', 'value2', 'value3')
        s1 = r.server('value1')
        self.assertTrue(s is s1)
        self.assertDictEqual(r._children, {'server': [s]})

    # MODIFICATION OF ATTRIBUTES TESTS

    def test_add_attribute_by_indexing(self):
        r = Configstore().servers
        s = r.server(name='core')
        s['port']=30400
        self.assertDictEqual(s._attrs, {'name':'core', 'port': 30400})

    def test_modify_attribute_by_indexing(self):
        r = Configstore().servers
        s = r.server(name='core', port=30400)
        s['name'] = 'other'
        self.assertDictEqual(s._attrs, {'name':'other', 'port': 30400})

    def test_modify_attribute_by_calling_updates_parent(self):
        r = Configstore().servers
        s = r.server(name='core', port=30400)
        s(name='other')
        s2 = r.server(name='other') # find by new name
        self.assertTrue(s is s2)

    def test_add_attributes(self):
        r = Configstore().servers
        s = r.server(name='core', port=30400)
        s(ipaddress='10.17.1.2', text='Wow')
        s = r.server(name='core') # find it again, just to see if it works
        self.assertEquals('10.17.1.2', s['ipaddress'])
        self.assertEquals('Wow', s['text'])

    def test_set_attribute_by_indexing(self):
        r = Configstore().servers
        s = r.server(name='core', port=30400)
        s['name'] = 'other'
        s1 = r.server(name='other')
        self.assertTrue(s is s1)

    def test_set_attribute_by_creator_indexing(self):
        r = Configstore()
        s = r.servers
        s['name'] = 'abc'
        self.assertEquals('abc', r.servers['name'])

    def test_add_value_later(self):
        r = Configstore().servers
        s = r.server(name='core')
        s1 = s('value')
        self.assertTrue(s is s1)
        self.assertDictEqual(r._children, {'server': [s]})
        self.assertTrue(s._value, ('value',))

    def test_value_is_overwritten(self):
        r = Configstore()
        s = r.server('value', 'value2')
        s1 = r.server()
        s1('another')
        self.assertDictEqual(r._children, {'server':[s1]})
        self.assertTupleEqual(s._value, ('another',))

    def test_multiple_equal_values_are_not_merged(self):
        r = Configstore().servers
        s = r.server('value', 'value')
        self.assertTupleEqual(s._value, ('value','value'))

    # REMOVE TESTS

    def test_remove_multiple_elements(self):
        r = Configstore().servers
        s1 = r.server(name='core')
        s2 = r.server(name='other')

        r._remove_element('server')
        self.assertDictEqual(r._children, {})

    def test_remove_elements_by_attributes(self):
        r = Configstore().servers
        s1 = r.server(name='core', port=30400)
        s2 = r.server(name='other', port=30400, somevalue=False)
        s3 = r.server(name='another', port=30400, somevalue=True)
        s4 = r.server(name='againother', port=30400, somevalue=True)
        s5 = r.server(name='abc', port=40000)

        r._remove_element('server', port=30400, somevalue=True)
        self.assertDictEqual(r._children, {'server': [s1, s2, s5]})

    def test_remove_attribute(self):
        r = Configstore().servers
        s1 = r.server(name='core', port=30440)
        s2 = r.server(name='other')

        s1._remove_attribute('name')
        self.assertDictEqual(s1._attrs, {'port': 30440})
        self.assertDictEqual(s2._attrs, {'name': 'other'})

    def test_remove_attribute_causes_same_element_exist_twice(self):
        r = Configstore().servers
        s1 = r.server(name='core', port=4444)
        s2 = r.server(name='another', port=4444)

        s2._remove_attribute('name')
        s1._remove_attribute('name')

        self.assertFalse(s1 is s2)
        self.assertEquals(s1, s2)
        self.assertDictEqual(r._children, {'server': [s1,s2]})

    def test_remove_element_by_value(self):
        r = Configstore().servers
        s = r.server('10.17.1.23')
        r._remove_element('server', '10.17.1.23')
        self.assertDictEqual(r._children, {})

    def test_remove_value(self):
        s = Configstore().servers.server('value')
        s()
        self.assertTupleEqual(s._value, ())


    # OTHER TESTS

    def test_calling_creator_twice_with_singleton_match_is_allowed(self):
        r = Configstore()
        s1 = r.servers
        s1()
        s2 = s1()
        self.assertTrue(s1() is s2)

    def test_calling_creator_twice_more_instances_but_singleton_match_is_allowed(self):
        r = Configstore()
        s1 = r.servers
        s2 = s1()
        s2['name'] = 'core'
        s1()

    def test_calling_creator_twice_more_instances_multiple_match_is_allowed(self):
        r = Configstore().servers

        s1 = r.server
        s2 = r.server(name='core')
        s3 = r.server(name='another')

        s4 = s1()[0]
        s5 = s1()[0]

        self.assertTrue(s4 is s5)
        self.assertTrue(s2 is s4)
        self.assertNotEquals(s2, s3)
        self.assertDictEqual(r._children, {'server': [s2, s3]})

    def test_hashing_works(self):
        r1 = Configstore('value')
        r2 = Configstore('value')
        self.assertEquals(1, len(set([r1,r2])))  # they are the same

    def test_equals_works(self):
        r1 = Configstore().servers.server('abc',name='val',another='other')
        r2 = Configstore().servers.server('abc',name='val',another='other')
        self.assertEquals(r1, r2)

    def test_not_equals_work(self):
        r1 = Configstore().servers.server('abc',name='val',another='a')
        r2 = Configstore().servers.server('abc',name='val',another='b')
        self.assertNotEquals(r1, r2)

if __name__ == '__main__':
    unittest.main()
