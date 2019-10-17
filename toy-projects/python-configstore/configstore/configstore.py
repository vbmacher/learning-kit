"""
Configuration storage
"""


class Configstore(object):

    def __init__(self, *args, **kwargs):
        self._children = {}
        self._attrs = kwargs
        self._value = args

    def __call__(self, *args, **kwargs):
        self._value = args
        self._attrs.update(kwargs)
        return self

    def __getattr__(self, name):
        """ Called when the attribute does not exist """
        self._children.setdefault(name, [])
        return GetOrCreate(self, name)

    def __getitem__(self, key):
        """ Called for getting attribute by key"""
        return self._attrs[key]

    def __setitem__(self, key, value):
        """ Called for setting attribute by key """
        self._attrs[key] = value

    def _matches(self, *args, **kwargs):
        return all((k in self._attrs and self._attrs[k] == v) \
                   for k,v in kwargs.iteritems()) and\
               all(a in self._value for a in args)

    def _remove_element(self, name, *args, **kwargs):
        to_remove = [child for child in self._children[name] if child._matches(*args, **kwargs)]
        for child in to_remove:
            self._children[name].remove(child)
        if not self._children[name]:
            self._children.pop(name, None)

    def _remove_attribute(self, name):
        self._attrs.pop(name, None)

    def __eq__(self, other):
        if isinstance(other, self.__class__):
            return self._attrs == other._attrs\
                   and self._children == other._children\
                   and self._value == other._value
        return NotImplemented

    def __ne__(self, other):
        if isinstance(other, self.__class__):
            return not self.__eq__(other)
        return NotImplemented

    def __hash__(self):
        ts = lambda x: tuple(sorted(x))
        return hash(ts(self._children.items()) + ts(self._attrs.items()) + ts(self._value))

    def __str__(self):
        return '({},{}.{})'.format(self._value, self._attrs, self._children)

    def __repr__(self):
        return self.__str__()


class GetOrCreate:

    def __init__(self, parent, name):
        self._parent = parent
        self._name = name
        self._cached = None

    def __getattr__(self, name):
        if not self._cached:
            self()
        return getattr(self._cached, name)

    def __call__(self, *args, **kwargs):
        """
        Gets or creates new Configstore.

        creates when one of below holds:
        1. self.instances is empty
        2. kwargs are defined and are not a subset of any known instance's kwargs

        gets otherwise.

        If the kwargs is a proper subset of several instances, the list of
        the instances is returned.

        :param args: optional value of the instance (all values are taken as 1 tuple)
        :param kwargs: optional attributes of the instance
        :return: new or existing instance (see above)
        """

        if self._cached:
            return self._cached

        instances = self._parent._children[self._name]
        if not instances:
            self._cached = self._create_new(*args, **kwargs)
        elif not kwargs and not args:
            self._cached = GetOrCreate._get_singleton_or_all(instances)
        else:
            to_return = [instance for instance in instances if instance._matches(*args, **kwargs)]
            if to_return:
                self._cached = GetOrCreate._get_singleton_or_all(to_return)

        if not self._cached:
            self._cached = self._create_new(*args, **kwargs)

        return self._cached

    @staticmethod
    def _get_singleton_or_all(instances):
        size = len(instances)
        if size == 1:
            return instances[0]
        elif size > 1:
            return instances

    def _create_new(self, *args, **kwargs):
        child = Configstore(*args, **kwargs)
        self._parent._children[self._name].append(child)
        return child
