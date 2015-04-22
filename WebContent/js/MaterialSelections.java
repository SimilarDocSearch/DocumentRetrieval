/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dataobjects;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hpandav
 */
@Entity
@Table(catalog = "ExNetContacts", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaterialSelections.findAll", query = "SELECT m FROM MaterialSelections m"),
    @NamedQuery(name = "MaterialSelections.findById", query = "SELECT m FROM MaterialSelections m WHERE m.id = :id"),
    @NamedQuery(name = "MaterialSelections.findByMaterialSelection", query = "SELECT m FROM MaterialSelections m WHERE m.materialSelection = :materialSelection")})
public class MaterialSelections implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 255)
    private String materialSelection;

    public MaterialSelections() {
    }

    public MaterialSelections(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialSelection() {
        return materialSelection;
    }

    public void setMaterialSelection(String materialSelection) {
        this.materialSelection = materialSelection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaterialSelections)) {
            return false;
        }
        MaterialSelections other = (MaterialSelections) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dataobjects.MaterialSelections[ id=" + id + " ]";
    }
    
}
