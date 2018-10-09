/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tentakel.licensemanager.service.webconfiguration;

import java.io.File;
import java.text.DateFormat;
import org.springframework.stereotype.Service;

/**
 *
 * @author sebastian-daniel.herzog
 */
@Service
public interface ApplicationConfigurationService 
{
    public String getApplicationVersion();
    
    public String getLicenseFolderName();
    
    public String getLicenseName();
    
    public File getLicenseFile();
            
    public String getPrivateKeyName();
    
    public String getPublicKeyName();
    
    public String getPublicKeyHash();
    
    public boolean isPublicKeyAndLicenseValid();
}
 