package la.netco.core.uilayer.beans.utils;



import java.util.AbstractMap;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "makeList")
@RequestScoped
public class ListFromCollection {

	
	
	public ListFromCollection()
	  {
	    _map = new MakeList();
	    _size = _DEFAULT_SIZE;
	  }

	@SuppressWarnings("rawtypes")
	public Map<Collection, List> getList()
	  {
	    return _map;
	  }

	  public int getSize()
	  {
	    return _size;
	  }
	  
	  public void setSize(int size)
	  {
	    _size = size;
	  }

	@SuppressWarnings("rawtypes")
	private class MakeList extends AbstractMap<Collection, List>
	  {
	    public List get(Object o)
	    {
	      if (!(o instanceof Collection))
	        return null;
	      
	      // Just send RandomAccess lists out;  wrap any other Collection
	      // into a List
	      if ((o instanceof List) &&
	          (o instanceof RandomAccess))
	        return (List) o;

	      Collection c = (Collection) o;
	      if (c.isEmpty())
	        return Collections.EMPTY_LIST;

	      return new ListImpl(c, getSize());
	    }

	    public Set<Map.Entry<Collection, List>> entrySet()
	    {
	      // Not worth implementing at the moment;  this Map is only
	      // accessed from 
	      return Collections.emptySet();
	    }
	  }

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	static private class ListImpl extends AbstractList
	  {
	    public ListImpl(Collection c, int size)
	    {
	      _c = c;
	      _cSize = c.size();
	      if (size == 0)
	        _bufferSize = _cSize;
	      else
	        _bufferSize = Math.min(size, _cSize);

	      _buffer = new ArrayList(_bufferSize);
	      _offset = -1;
	    }

	    public int size()
	    {
	      return _cSize;
	    }

	    public Object get(int index)
	    {
	      if ((index < 0) || (index >= _cSize))
	        throw new IndexOutOfBoundsException();

	      int offset = (index / _bufferSize) * _bufferSize;
	      if (offset != _offset)
	      {
	        _loadBuffer(offset);
	        _offset = offset;
	      }

	      return _buffer.get(index - _offset);
	    }

	    private void _loadBuffer(int offset)
	    {
	      Iterator iter = _c.iterator();
	      int i = 0;

	      while (i < offset)
	      {
	        assert iter.hasNext();
	        iter.next();
	        i++;
	      }

	      _buffer.clear();

	      int count = 0;
	      while ((count < _bufferSize) && (i < _cSize))
	      {
	        assert iter.hasNext();
	        _buffer.add(iter.next());
	        i++;
	        count++;
	      }
	    }

	    private final Collection _c;
	    private final int        _bufferSize;
	    private final int        _cSize;
	    private int       _offset;
	    private ArrayList _buffer;
	  }

	@SuppressWarnings("rawtypes")
	private Map<Collection, List> _map;
	  private int                   _size;

	  static private int _DEFAULT_SIZE = 50;
	
}
