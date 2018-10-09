/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tentakel.licensemanager.service.webconfiguration.implementation;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import net.tentakel.licensemanager.service.licensing.LicenseManager;
import net.tentakel.licensemanager.service.licensing.PublicKeyService;
import net.tentakel.licensemanager.service.licensing.exception.LicenseException;
import net.tentakel.licensemanager.service.webconfiguration.ApplicationConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author sebastian-daniel.herzog
 */
@Service
public class ApplicationConfigurationServiceImpl implements ApplicationConfigurationService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigurationServiceImpl.class);
    
    @Autowired
    LicenseManager licenseManager;
    
    @Autowired
    PublicKeyService publicKeyService;
    
    private static final String APP_VERSION = "0.2"; 
    
    private static final String LICENSE_FOLDERNAME = "lic";
    private static final String LICENSE_FILENAME = "client.lic";
    private File licenseFile;
    
    private static final String PRIVATE_KEY_FILENAME = "my_priv_key.der";
    private static final String PUBLIC_KEY_FILENAME = "my_pub_key.der";    
    private static final String PUBLIC_KEY_FILEHASH = "0f9c8f67cb848ead0f4de438a4950475055298688ce1573b2a3382eb5e4725bb59740b12929d71aac0e741777b536e000f9bf866a54deddbee94495fdfb63e4a"; 
    private File publicKeyFile;
        
    private static final DateFormat DATABASE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    @Override
    public String getApplicationVersion() { return APP_VERSION; }    
    
    @Override
    public String getLicenseFolderName() { return LICENSE_FOLDERNAME; }
    
    @Override
    public String getLicenseName() { return LICENSE_FILENAME; }
    
    @Override
    public File getLicenseFile() { return licenseFile; }
    private void setLicenseFile(File license) { this.licenseFile = license; }
    
    private boolean isLicenseValid()
    {
        boolean resultLicenseIsValid = false;
        String licenseFolder = ApplicationConfigurationServiceImpl.class.getClassLoader()
                .getResource("").getPath() + LICENSE_FOLDERNAME + File.separator ;
        publicKeyFile = new File(licenseFolder + PUBLIC_KEY_FILENAME);
        licenseFile = new File(licenseFolder + LICENSE_FILENAME);
        
        try 
        {
            licenseManager.validateLicenseFile(APP_VERSION, publicKeyFile, licenseFile);            
            resultLicenseIsValid = true;
            LOGGER.info("License " + licenseFile.getAbsolutePath() + " is valid");
        } 
        catch (LicenseException licex) 
        {
            LOGGER.error(licex.getMessage());
            resultLicenseIsValid = false;
        }
        
        return resultLicenseIsValid;
    }
    
    @Override
    public String getPrivateKeyName() { return PRIVATE_KEY_FILENAME; }
    
    @Override
    public String getPublicKeyName() { return PUBLIC_KEY_FILENAME; }
    
    @Override
    public String getPublicKeyHash() { return PUBLIC_KEY_FILEHASH; }
    
    private boolean isPublicKeyValid(String publicKeyHash)
    {
        String licenseFolder = ApplicationConfigurationServiceImpl.class.getClassLoader()
                .getResource("").getPath() + LICENSE_FOLDERNAME + File.separator ;
        publicKeyFile = new File(licenseFolder + PUBLIC_KEY_FILENAME);
        
        boolean resultPublicKeyIsValid = false;
        try 
        {
            resultPublicKeyIsValid = publicKeyService.checkPublicKeySha512Hash(publicKeyFile, publicKeyHash);
        } 
        catch (LicenseException licex) 
        {
            LOGGER.error(licex.getMessage());
            resultPublicKeyIsValid = false;
        }
        
        return resultPublicKeyIsValid;
    }
    
    @Override
    public boolean isPublicKeyAndLicenseValid()
    {
         boolean resultPublicKeyIsValid = isPublicKeyValid(PUBLIC_KEY_FILEHASH);
         boolean resultLicenseIsValid = isLicenseValid();
         
         return (resultPublicKeyIsValid && resultLicenseIsValid);
    }  
}
