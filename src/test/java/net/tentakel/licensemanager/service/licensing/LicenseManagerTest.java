/**
 * Copyright(C) 2013 Patrik Dufresne Service Logiciel <info@patrikdufresne.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tentakel.licensemanager.service.licensing;

import net.tentakel.licensemanager.service.licensing.implementation.LicenseManagerImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import net.tentakel.licensemanager.service.licensing.exception.LicenseException;
import net.tentakel.licensemanager.service.licensing.exception.LicenseExpiredException;
import net.tentakel.licensemanager.service.licensing.exception.LicenseVersionExpiredException;
import net.tentakel.licensemanager.service.webconfiguration.ApplicationConfigurationService;
import net.tentakel.licensemanager.service.webconfiguration.implementation.ApplicationConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import org.testng.annotations.Test;

/**
 * This class test all the functionnality provided by the {@link LicenseManagerImpl}
 * .
 * 
 * @author sebastian-daniel.herzog
 * 
 */
public class LicenseManagerTest 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseManagerTest.class);
    
    private ApplicationConfigurationService applicationConfigurationService;
    private LicenseManager licenseManager;
    
    private String licenseFolderName;
    private String privateKeyName;
    private String publicKeyName;
    private String publicKeyHash;
    private String licenseFolder;
    
    private LicenseManagerImpl manager;
    private File publicKeyFile;
    private File privateKeyFile;
    private final String NON_EXISTING_FILENAME = "NONEXISTINGLICENSE.lic";
    private final String TRIAL_EXPIRED_FILENAME = "test-trial-expired-license.lic";
    private final String TRIAL_VALID_FILENAME = "test-trial-valid-license.lic";
    private final String TRIAL_WITHOUT_EXPIRATION_FILENAME = "test-trial-without-expiration-license.lic";
    // TODO: trial license mit unterschiedlicher Versionsnummer?
    private final String SINGLE_LOWER_VERSION_FILENAME = "test-single-lower-version-license.lic";
    private final String SINGLE_LOWER_LICENSE_VERSION = "0.0";
    private final String SINGLE_VALID_FILENAME = "test-single-valid-license.lic";
    private final String SINGLE_WITHOUT_VERSION_FILENAME = "test-single-without-version-license.lic";
    private String licenseVersion;
    // single license mit abgelaufenem Datum?
    // TODO: lifetime license 
    
    @BeforeTest
    public void initLicenseManagerService() throws GeneralSecurityException, IOException 
    {   
        applicationConfigurationService = new ApplicationConfigurationServiceImpl();
        licenseManager = new LicenseManagerImpl();
        licenseFolderName = applicationConfigurationService.getLicenseFolderName();
        licenseVersion = applicationConfigurationService.getApplicationVersion();
        privateKeyName = applicationConfigurationService.getPrivateKeyName();
        publicKeyName = applicationConfigurationService.getPublicKeyName();
        publicKeyHash = applicationConfigurationService.getPublicKeyHash();
        
        licenseFolder = PublicKeyServiceImplTest.class.getClassLoader()
                .getResource("").getPath() + licenseFolderName + File.separator ;
        LOGGER.info("licenseFolder: " + licenseFolder);
        
        publicKeyFile = new File(licenseFolder + File.separator + publicKeyName);
        privateKeyFile = new File(licenseFolder + File.separator + privateKeyName);
        
        this.manager = new LicenseManagerImpl(publicKeyFile, privateKeyFile);
        
        Assert.assertNotNull(this.manager);
    }
    
    @Test
    public void writeTrialExpiredLicense() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, IOException, 
            ClassNotFoundException, LicenseException 
    {
        String licenseId = "Id123";
        String licenseName = "TrialExpiredLicense";
        Date licenseDate = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        String licenseEmail = "trial@license.com";
        String licenseNumber = "0123ABCD";
        String licenseType = "trial";  // licenseType {"trial", "single-version", "lifetime"}        
        
        LicenseService license = new LicenseService();
        license.setProperty(LicenseService.ID, licenseId);
        license.setProperty(LicenseService.NAME, licenseName);
        license.setExpiration(licenseDate);
        license.setProperty(LicenseService.EMAIL, licenseEmail);
        license.setProperty(LicenseService.LICENSE_NUMBER, licenseNumber);
        license.setProperty(LicenseService.LICENSE_TYPE, licenseType);
        license.setProperty(LicenseService.VERSION, licenseVersion);
        
        File file = new File(licenseFolder  + TRIAL_EXPIRED_FILENAME);
        manager.writeLicense(license, file);
        Assert.assertTrue(file.exists());        
    } 
    
    @Test
    public void writeTrialWithoutExpirationLicense() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, IOException, 
            ClassNotFoundException, LicenseException 
    {
        String licenseId = "Id123";
        String licenseName = "TrialWithoutExpirationLicense";
        String licenseEmail = "trial@license.com";
        String licenseNumber = "0123ABCD";
        String licenseType = "trial";  // licenseType  {"trial", "single-version", "lifetime"}        
        
        LicenseService license = new LicenseService();
        license.setProperty(LicenseService.ID, licenseId);
        license.setProperty(LicenseService.NAME, licenseName);
        license.setProperty(LicenseService.EMAIL, licenseEmail);
        license.setProperty(LicenseService.LICENSE_NUMBER, licenseNumber);
        license.setProperty(LicenseService.LICENSE_TYPE, licenseType);
        license.setProperty(LicenseService.VERSION, licenseVersion);
        
        File file = new File(licenseFolder  + TRIAL_WITHOUT_EXPIRATION_FILENAME);
        manager.writeLicense(license, file);
        Assert.assertTrue(file.exists());        
    }
    
    @Test
    public void writeTrialValidLicense() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, IOException, 
            ClassNotFoundException,LicenseException 
    {
        String licenseId = "Id123";
        String licenseName = "TrialValidLicense";
        
        Calendar calender = new GregorianCalendar();
        calender.add(Calendar.DATE, 365);
        Date licenseDate = calender.getTime();
        
        String licenseEmail = "trial@license.com";
        String licenseNumber = "0123ABCD";
        String licenseType = "trial";  // licenseType {"trial", "single-version", "lifetime"}        
        
        LicenseService license = new LicenseService();
        license.setProperty(LicenseService.ID, licenseId);
        license.setProperty(LicenseService.NAME, licenseName);
        license.setExpiration(licenseDate);
        license.setProperty(LicenseService.EMAIL, licenseEmail);
        license.setProperty(LicenseService.LICENSE_NUMBER, licenseNumber);
        license.setProperty(LicenseService.LICENSE_TYPE, licenseType);
        license.setProperty(LicenseService.VERSION, licenseVersion);
        
        File file = new File(licenseFolder  + TRIAL_VALID_FILENAME);
        manager.writeLicense(license, file);
        Assert.assertTrue(file.exists());        
    } 
    
    @Test
    public void writeSingleLowerVersionLicense() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, IOException, 
            ClassNotFoundException,LicenseException 
    {
        String licenseId = "Id123";
        String licenseName = "SingleLowerVersionLicense";
        
        Calendar calender = new GregorianCalendar();
        calender.add(Calendar.DATE, 30);
        Date licenseDate = calender.getTime();
        
        String licenseEmail = "single@license.com";
        String licenseNumber = "0123ABCD";
        String licenseType = "single-version";  // licenseType {"trial", "single-version", "lifetime"}        
        
        LicenseService license = new LicenseService();
        license.setProperty(LicenseService.ID, licenseId);
        license.setProperty(LicenseService.NAME, licenseName);
        license.setExpiration(licenseDate);
        license.setProperty(LicenseService.EMAIL, licenseEmail);
        license.setProperty(LicenseService.LICENSE_NUMBER, licenseNumber);
        license.setProperty(LicenseService.LICENSE_TYPE, licenseType);
        license.setProperty(LicenseService.VERSION, SINGLE_LOWER_LICENSE_VERSION);
        
        File file = new File(licenseFolder  + SINGLE_LOWER_VERSION_FILENAME);
        manager.writeLicense(license, file);
        Assert.assertTrue(file.exists());        
    } 
    
    @Test
    public void writeSingleWithoutVersionLicense() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, IOException, 
            ClassNotFoundException,LicenseException 
    {
        String licenseId = "Id123";
        String licenseName = "SingleWithoutVersionLicense";
        
        Calendar calender = new GregorianCalendar();
        calender.add(Calendar.DATE, 30);
        Date licenseDate = calender.getTime();
        
        String licenseEmail = "single@license.com";
        String licenseNumber = "0123ABCD";
        String licenseType = "single-version";  // licenseType {"trial", "single-version", "lifetime"}        
               
        LicenseService license = new LicenseService();
        license.setProperty(LicenseService.ID, licenseId);
        license.setProperty(LicenseService.NAME, licenseName);
        license.setExpiration(licenseDate);
        license.setProperty(LicenseService.EMAIL, licenseEmail);
        license.setProperty(LicenseService.LICENSE_NUMBER, licenseNumber);
        license.setProperty(LicenseService.LICENSE_TYPE, licenseType);
               
        File file = new File(licenseFolder  + SINGLE_WITHOUT_VERSION_FILENAME);
        manager.writeLicense(license, file);
        Assert.assertTrue(file.exists());        
    } 
    
    @Test
    public void writeSingleValidLicense() throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException, IOException, 
            ClassNotFoundException,LicenseException 
    {
        String licenseId = "Id123";
        String licenseName = "SingleValidLicense";
        
        Calendar calender = new GregorianCalendar();
        calender.add(Calendar.DATE, 30);
        Date licenseDate = calender.getTime();
        
        String licenseEmail = "single@license.com";
        String licenseNumber = "0123ABCD";
        String licenseType = "single-version";  // licenseType {"trial", "single-version", "lifetime"}        
        
        
        LicenseService license = new LicenseService();
        license.setProperty(LicenseService.ID, licenseId);
        license.setProperty(LicenseService.NAME, licenseName);
        license.setExpiration(licenseDate);
        license.setProperty(LicenseService.EMAIL, licenseEmail);
        license.setProperty(LicenseService.LICENSE_NUMBER, licenseNumber);
        license.setProperty(LicenseService.LICENSE_TYPE, licenseType);
        license.setProperty(LicenseService.VERSION, licenseVersion);
        
        File file = new File(licenseFolder  + SINGLE_VALID_FILENAME);
        manager.writeLicense(license, file);
        Assert.assertTrue(file.exists());        
    } 
    
    @Test(dependsOnMethods = { "writeTrialExpiredLicense" })
    public void readTrialExpiredLicense() throws IOException, 
            InvalidKeyException, NoSuchAlgorithmException, SignatureException, 
            ClassNotFoundException, LicenseException
    {
        File file = new File(licenseFolder  + TRIAL_EXPIRED_FILENAME);
        
        // Read the file
        LicenseService license = (LicenseService) manager.readLicenseFile(file);
        Assert.assertNotNull(license);                
    }

    @Test(dependsOnMethods = { "writeTrialWithoutExpirationLicense" })
    public void readTrialWithoutExpirationLicense() throws IOException, 
            InvalidKeyException, NoSuchAlgorithmException, SignatureException, 
            ClassNotFoundException, LicenseException
    {
        File file = new File(licenseFolder  + TRIAL_WITHOUT_EXPIRATION_FILENAME);
        
        // Read the file
        LicenseService license = (LicenseService) manager.readLicenseFile(file);
        Assert.assertNotNull(license);                
    }
            
    @Test(dependsOnMethods = { "writeTrialValidLicense" })
    public void readTrialValidLicense() throws IOException, 
            InvalidKeyException, NoSuchAlgorithmException, SignatureException, 
            ClassNotFoundException, LicenseException
    {
        File file = new File(licenseFolder  + TRIAL_VALID_FILENAME);
        
        // Read the file
        LicenseService license = (LicenseService) manager.readLicenseFile(file);
        Assert.assertNotNull(license);                
    }    
    
    @Test(dependsOnMethods = { "writeSingleLowerVersionLicense" })
    public void readSingleLowerVersionLicense() throws IOException, 
            InvalidKeyException, NoSuchAlgorithmException, SignatureException, 
            ClassNotFoundException, LicenseException
    {
        File file = new File(licenseFolder  + SINGLE_LOWER_VERSION_FILENAME);
        
        // Read the file
        LicenseService license = (LicenseService) manager.readLicenseFile(file);
        Assert.assertNotNull(license);                
    }
        
    @Test(dependsOnMethods = { "writeSingleWithoutVersionLicense" })
    public void readSingleWithoutVersionLicense() throws IOException, 
            InvalidKeyException, NoSuchAlgorithmException, SignatureException, 
            ClassNotFoundException, LicenseException
    {
        File file = new File(licenseFolder  + SINGLE_WITHOUT_VERSION_FILENAME);
        
        // Read the file
        LicenseService license = (LicenseService) manager.readLicenseFile(file);
        Assert.assertNotNull(license);                
    }
    
    @Test(dependsOnMethods = { "writeSingleValidLicense" })
    public void readSingleValidLicense() throws IOException, 
            InvalidKeyException, NoSuchAlgorithmException, SignatureException, 
            ClassNotFoundException, LicenseException
    {
        File file = new File(licenseFolder  + SINGLE_VALID_FILENAME);
        
        // Read the file
        LicenseService license = (LicenseService) manager.readLicenseFile(file);
        Assert.assertNotNull(license);                
    }
    
    @Test(expectedExceptions = NullPointerException.class)
    public void validateNonExistingLicense() throws Exception 
    {
        File licenseFile = new File(licenseFolder + NON_EXISTING_FILENAME);
        InputStream pubKeyInStream = new FileInputStream(publicKeyFile);
        
        licenseManager.validate(pubKeyInStream, licenseFile);
    }
    
    /**
     * Check if validate is working.
     * 
     * @throws java.lang.Exception
     */
    @Test(dependsOnMethods = { "writeTrialExpiredLicense" }, expectedExceptions = LicenseExpiredException.class)
    public void validateTrialExpiredLicense() throws Exception 
    {
        File licenseFile = new File(licenseFolder  + TRIAL_EXPIRED_FILENAME);
        InputStream pubKeyInStream = new FileInputStream(publicKeyFile);
        
        licenseManager.validate(pubKeyInStream, licenseFile);
    }
    
     /**
     * Check if validate is working.
     * 
     * @throws java.lang.Exception
     */
    @Test(dependsOnMethods = { "writeTrialWithoutExpirationLicense" }, expectedExceptions = LicenseExpiredException.class)        
    public void validateTrialWithoutExpirationLicense() throws Exception 
    {
        File licenseFile = new File(licenseFolder  + TRIAL_WITHOUT_EXPIRATION_FILENAME);
        InputStream pubKeyInStream = new FileInputStream(publicKeyFile);
        
        licenseManager.validate(pubKeyInStream, licenseFile);
    }
    
    /**
     * Check if validate is working.
     * 
     * @throws java.lang.Exception
     */
    @Test(dependsOnMethods = { "writeTrialValidLicense" })
    public void validateTrialValidLicense() throws Exception 
    {
        File licenseFile = new File(licenseFolder  + TRIAL_VALID_FILENAME);
        InputStream pubKeyInStream = new FileInputStream(publicKeyFile);
        
        licenseManager.validate(pubKeyInStream, licenseFile);
    }
    
    /**
     * Check if validate is working.
     * 
     * @throws java.lang.Exception
     */
    @Test(dependsOnMethods = { "writeSingleLowerVersionLicense" }, expectedExceptions = LicenseVersionExpiredException.class)
    public void validateSingleLowerVersionLicense() throws Exception 
    {
        File licenseFile = new File(licenseFolder  + SINGLE_LOWER_VERSION_FILENAME);
        
        licenseManager.validateLicenseFile(licenseVersion, publicKeyFile, licenseFile);
    }
    
    /**
     * Check if validate is working.
     * 
     * @throws java.lang.Exception
     */
    @Test(dependsOnMethods = { "writeSingleWithoutVersionLicense" }, expectedExceptions = LicenseVersionExpiredException.class)
    public void validateSingleWithoutVersionLicense() throws Exception 
    {
        File licenseFile = new File(licenseFolder  + SINGLE_WITHOUT_VERSION_FILENAME);
        InputStream pubKeyInStream = new FileInputStream(publicKeyFile);
        
        licenseManager.validate(pubKeyInStream, licenseFile);
    }
    
    /**
     * Check if validate is working.
     * 
     * @throws java.lang.Exception
     */
    @Test(dependsOnMethods = { "writeSingleValidLicense" })
    public void validateSingleValidLicense() throws Exception 
    {
        File licenseFile = new File(licenseFolder  + SINGLE_VALID_FILENAME);
                
        licenseManager.validateLicenseFile(licenseVersion, publicKeyFile, licenseFile);
    }
    
    // clean up all test licenses
    @AfterTest
    public void removeAllLicenseFiles() throws Exception 
    {
       ArrayList<String> fileList = new ArrayList<String>();
       fileList.add(TRIAL_EXPIRED_FILENAME);
       fileList.add(TRIAL_EXPIRED_FILENAME);
       fileList.add(TRIAL_WITHOUT_EXPIRATION_FILENAME);
       fileList.add(TRIAL_VALID_FILENAME);
       fileList.add(SINGLE_LOWER_VERSION_FILENAME);
       fileList.add(SINGLE_VALID_FILENAME);
       fileList.add(SINGLE_WITHOUT_VERSION_FILENAME);
       
       for (String file : fileList)
       {
            File licenseFile =  new File(licenseFolder  + file.toString());
            if (licenseFile.exists())
            {
                licenseFile.delete();
            } 
       }        
    }    
}
