/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tentakel.licensemanager.service.licensing.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import net.tentakel.licensemanager.service.licensing.PublicKeyService;
import net.tentakel.licensemanager.service.licensing.exception.KeyInvalidException;
import net.tentakel.licensemanager.service.licensing.exception.LicenseException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author sebastian-daniel.herzog
 */
@Service
public class PublicKeyServiceImpl implements PublicKeyService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicKeyServiceImpl.class);
    
    @Override
    public boolean checkPublicKeySha512Hash(File publicKeyFile, String publicKeyHash) throws LicenseException 
    {
        boolean isPublicKeyValid = false;
                
        InputStream pubKeyInStream = null;
        try { pubKeyInStream = new FileInputStream(publicKeyFile); } 
        catch (FileNotFoundException fnfex) { LOGGER.error(fnfex.getMessage()); } 
        
        String currentPublicKeyHash = "";
        try 
        {
            // 2018-04-27, sebastian-daniel.herzog:
            // check if public key is the original key has to be checked after creating the license,
            // otherwise an IOException ("Short read of DER length") will be thrown
            // because the inputstream of public key is changed by method md5Hex()
            currentPublicKeyHash = DigestUtils.sha512Hex(pubKeyInStream);
            LOGGER.debug("hash of available public key " + 
                    publicKeyFile.getAbsolutePath() + " is " + currentPublicKeyHash);
        } 
        catch (IOException ioex) 
        {
            LOGGER.error(ioex.getMessage());
        }
        
        if (!currentPublicKeyHash.equals(publicKeyHash))
        {
            LOGGER.error("Invalid public key. Public key is not the original key");
            throw new KeyInvalidException();            
        }
        
        isPublicKeyValid = true; 
        return isPublicKeyValid;
    }
}
