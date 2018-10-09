/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * http://afewguyscoding.com/2012/02/licensing-module-java/
 * License.java: This is your license object.  It will be written to disk and checked with the program starts.  
 * It contains all the information the managers will need to check the license.
 */
package net.tentakel.licensemanager.domain;

import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 *
 * @author sherzog
 */
public class License implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    private String firstName;
    private String lastName;
    private String email;
    private String licenseNumber;
    private String licenseType;
    
    @DateTimeFormat(iso=ISO.DATE)
    private Date licenseExpiration;
    
    private String licenseVersion;
   
    //private ArrayList<String> licenseType = new ArrayList<String>();
    
    public License(){}
    
    public License(String firstName, String lastName, 
            String email, String licenseNumber, String licenseType,
            Date licenseExpiration, String licenseVersion)//ArrayList<String> licenseType
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.licenseNumber = licenseNumber;
        this.licenseType = licenseType;
        this.licenseExpiration = licenseExpiration;        
        this.licenseVersion = licenseVersion;
    }

    public String getFirstName() { return this.firstName; }
    public void setFirstName(String name) { this.firstName = name; }
    
    public String getLastName() { return this.lastName; }
    public void setLastName(String name) { this.lastName = name; }
    
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getLicenseNumber() { return this.licenseNumber; }
    public void setLicenseNumber(String number) { this.licenseNumber = number; }
    
    public String getLicenseType() { return this.licenseType; }
    public void setLicenseType(String type) { this.licenseType = type; }
   
    //public ArrayList<String> getLicenseType() { return this.licenseType; }
    //public void setLicenseType(ArrayList<String> type) { this.licenseType = type; }
    
    public Date getLicenseExpiration() { return this.licenseExpiration; }
    public void setLicenseExpiration(Date date) { this.licenseExpiration = date; }
    
    public String getLicenseVersion() { return this.licenseVersion; }
    public void setLicenseVersion(String version) { this.licenseVersion = version; }
}
