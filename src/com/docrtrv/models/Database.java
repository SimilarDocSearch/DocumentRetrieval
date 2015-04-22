/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.docrtrv.models;

import java.io.Serializable;

/**
 *
 * @author hpandav
 */

public class Database implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public String path;
	public int rank;
	public String representative;
	
	
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

	
	
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Database)) {
            return false;
        }
        Database other = (Database) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public String getRepresentative() {
		return representative;
	}


	public void setRepresentative(String representative) {
		this.representative = representative;
	}


	@Override
    public String toString() {
        return "[\""+this.name+"\",\""+
        		this.rank+"\",\""+
        		this.path+"\",\""+
        		this.representative+"\"]";
    }
    
}
