package co.com.heinsohn.dnda.documents;

import java.io.Serializable;


public class Elemento implements Serializable, Comparable<Elemento> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2219498955442074563L;

	String path;
	String name;
	boolean isDir;
	
	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public Elemento() {
		
	}
	
	public Elemento(String path, String name, boolean isDir) {
		this.path = path;
		this.name = name;
		this.isDir = isDir;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Elemento other = (Elemento) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path) || isDir != other.isDir)
            return false;
        
        return true;
    }
	
	@Override
    public String toString() {
        return name;
    }
	
	@Override
	public int compareTo(Elemento o) {
		return this.getPath().compareTo(o.getPath());
	}
}
