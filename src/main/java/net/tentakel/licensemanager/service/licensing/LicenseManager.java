/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tentakel.licensemanager.service.licensing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import net.tentakel.licensemanager.service.licensing.exception.LicenseException;
import org.springframework.stereotype.Component;

/**
 *
 * @author sebastian-daniel.herzog
 */
@Component
public interface LicenseManager 
{
    public LicenseService validateLicenseFile(String appVersion, File publicKeyFile, File licenseFile) throws IllegalArgumentException, LicenseException;
    
    public LicenseService validate(InputStream publicKey, File... files) throws IllegalArgumentException, LicenseException;
    
    public LicenseService readLicenseFile(File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, ClassNotFoundException, LicenseException;

    public void writeLicense(LicenseService license, File file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException;
}
